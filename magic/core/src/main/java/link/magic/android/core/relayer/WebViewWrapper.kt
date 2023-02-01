package link.magic.android.core.relayer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.MutableContextWrapper
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.POST_WEB_MESSAGE
import androidx.webkit.WebViewFeature.WEB_MESSAGE_PORT_POST_MESSAGE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import link.magic.android.MagicCore
import link.magic.android.core.relayer.message.InboundMessageType
import link.magic.android.core.relayer.message.ResponseData
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import org.web3j.protocol.core.Response
import java.util.*


/**
 * This class is designed to be instantiate only once. As for the webview
 */

class WebViewWrapper internal constructor(context: Context, private val urlBuilder: URLBuilder) {

    internal val mMutableContext = MutableContextWrapper(context)
    private val webView: WebView = WebView(mMutableContext)
    private var webViewDialog: WebViewDialog? = null

    private var overlayReady = false
    private val queue: MutableList<String> = ArrayList()

    private var messageHandlers: HashMap<Long, (responseString: String) -> Unit> = HashMap()

     init {
         WebView.setWebContentsDebuggingEnabled(true)

         // if updated context is an activity context, attach webview to the current activity
         if (context is Application) {
             initializeWebView()
         } else {
             throw IllegalArgumentException("Initializing context should be application context ")
         }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true // Enable JS post messages
        webSettings.domStorageEnabled = true
        webView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        webView.webViewClient = MagicWebViewClient()
        webView.addJavascriptInterface(this, "FortmaticAndroid")
        webView.loadUrl(urlBuilder.url)
    }

    // set Webview context whenever there's activity being changed
    fun setContext(newContext: Context) {
        mMutableContext.baseContext = newContext
    }

    /**
     * Message Queue
     */
    fun enqueue(message: String, id: Long, callback: (String) -> Unit) {
        queue.add(message)
        messageHandlers[id] = callback
        dequeue()
    }

    private fun dequeue() {
        if (queue.isNotEmpty() && overlayReady) {
            val message = queue.removeAt(0)
            postMessageToMgbox(message)
            dequeue()
        }
    }

    private fun postMessageToMgbox(message: String) {

        if (WebViewFeature.isFeatureSupported(WEB_MESSAGE_PORT_POST_MESSAGE)) {
            runOnUiThread {
                if (WebViewFeature.isFeatureSupported(POST_WEB_MESSAGE)) {
                    if (MagicCore.debugEnabled) {
                        Log.d("Magic", "Send Message $message")
                    }
                    WebViewCompat.postWebMessage(this.webView, WebMessageCompat(message), Uri.parse(urlBuilder.url))
                }
            }
        } else {
            throw Error("SDK ERROR: Post message failed. API version ${Build.VERSION.SDK_INT} is not supported")
        }
    }

    /**
     * Receive message in JS
     * This function name will be called in JS message box relayer
     */
    @JavascriptInterface
    fun postMessage(message: String) {

        if (MagicCore.debugEnabled) {
            Log.d("Magic", "Received Message: $message")
        }

        /* Deserialize without extended typing first to get msgType and id */
        val response = Gson().fromJson<ResponseData<Response<*>>>(message, object: TypeToken<ResponseData<Response<*>>>(){}.type)
        when {
            InboundMessageType.MAGIC_OVERLAY_READY.toString() in response.msgType -> {
                overlayReady = true
                dequeue()
            }
            InboundMessageType.MAGIC_SHOW_OVERLAY.toString() in response.msgType -> showOverlay()
            InboundMessageType.MAGIC_HIDE_OVERLAY.toString() in response.msgType -> hideOverlay()
            InboundMessageType.MAGIC_HANDLE_RESPONSE.toString() in response.msgType -> {
                val json = Gson().toJson(response.response)
                messageHandlers[response.response.id]?.let { it(json) }
                messageHandlers.remove(response.response.id)
            }
        }
    }

    /**
     * Webview display related
     */
    private fun showOverlay() {
        runOnUiThread {
            webView.visibility = View.VISIBLE
            webViewDialog = WebViewDialog(it, webView)

            webViewDialog?.show()
        }
    }

    private fun hideOverlay() {
        runOnUiThread {
            if (webView.parent != null) {
                val vg = (webView.parent as ViewGroup)
                vg.removeView(webView)
            }
            webView.visibility = View.INVISIBLE
            webViewDialog?.dismiss()
        }
    }


    /**
     * Util:
     * run callbacks onto the Ui Thread
     */
    private fun runOnUiThread(cb: (Context) -> Unit) {
        val ctx = mMutableContext.baseContext
        if (ctx is Application) {
            val handler = Handler(ctx.mainLooper)
            handler.post {
                cb(ctx)
            }
        }
        if (ctx is Activity) {
            /* Only the original thread that created a view hierarchy can touch its views. */
            ctx.runOnUiThread {
                run {
                    cb(ctx)
                }
            }
        }
    }
}
