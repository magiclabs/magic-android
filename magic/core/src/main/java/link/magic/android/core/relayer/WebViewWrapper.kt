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
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebStorage
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import androidx.webkit.WebViewFeature.POST_WEB_MESSAGE
import androidx.webkit.WebViewFeature.WEB_MESSAGE_PORT_POST_MESSAGE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import link.magic.android.Magic
import link.magic.android.core.relayer.message.AnnoucementResult
import link.magic.android.core.relayer.message.Event
import link.magic.android.core.relayer.message.InboundMessageType
import link.magic.android.core.relayer.message.ResponseData
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import link.magic.android.utils.Debouncer
import org.web3j.protocol.core.Response
import java.util.*


/**
 * This class is designed to be instantiate only once. As for the webview
 */
class WebViewWrapper internal constructor(context: Context, private val urlBuilder: URLBuilder) {

    internal val mMutableContext = MutableContextWrapper(context)
    private val mImmutableContext = context
    private var webView: WebView = WebView(mMutableContext)
    private var webViewDialog: WebViewDialog? = null

    private var overlayReady = false
    private val queue: MutableList<String> = ArrayList()

    private var magicEventListener: MagicEventListener? = null;

    private var messageHandlers: HashMap<Long, (responseString: String) -> Unit> = HashMap()

    private var missedMessage: String? = null
    private val FIVE_SECONDS = 5_000L
    private val debouncer: Debouncer = Debouncer()

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
                    if (Magic.debugEnabled) {
                        Log.d("Magic", "Send Message $message")
                    }
                    WebViewCompat.postWebMessage(this.webView, WebMessageCompat(message), Uri.parse(urlBuilder.url))

                    debouncer.debounce(FIVE_SECONDS) {
                        missedMessage = message
                        rebuildWebView(mImmutableContext)
                    }
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
        if (Magic.debugEnabled) {
            Log.d("Magic", "Received Message: $message")
        }

        /* Deserialize without extended typing first to get msgType and id */
        val response = Gson().fromJson<ResponseData<Response<*>>>(message, object: TypeToken<ResponseData<Response<*>>>(){}.type)
        when {
            InboundMessageType.MAGIC_OVERLAY_READY.toString() in response.msgType -> {
                // In the case there's a missed message, re-queue it at the top of the stack
                missedMessage?.let {
                    queue.add(0, it)
                    missedMessage = null
                }
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
            InboundMessageType.MAGIC_MG_BOX_SEND_RECEIPT.toString() in response.msgType -> {
                // When a receipt is received, cancel previously invoked debounce call
                debouncer.cancel()
            }
            InboundMessageType.MAGIC_SEND_PRODUCT_ANNOUNCEMENT.toString() in response.msgType -> {
                val TAG: String = "Magic SDK ${WebViewWrapper::class.java.name}"
                val type = object : TypeToken<ResponseData<Response<AnnoucementResult>>>() {}.type
                val announcement = Gson().fromJson<ResponseData<Response<AnnoucementResult>>>(message, type)
                Log.w(TAG, announcement.response.result.product_announcement)
            }

            ("MAGIC_HANDLE_EVENT" in response.msgType) -> {
                // Notify the developer's listener
                val type = object : TypeToken<ResponseData<Response<Event>>>() {}.type
                val event = Gson().fromJson<ResponseData<Response<Event>>>(message, type)
                magicEventListener?.onMagicEvent(event.response.result.event, message)
            }
        }
    }

    /**
     * Attempt to clear all webview storage/cache, do nothing if exception is thrown.
     */
    fun clearWebviewStorage() {
        runOnUiThread {
            if (it is Activity && !it.isFinishing && !it.isDestroyed) {
                try {
                    // Clear all the Application Cache, Web SQL Database, and the HTML5 Web Storage
                    WebStorage.getInstance().deleteAllData()

                    // Clear all the cookies
                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()

                    // Clear WebView data
                    webView.clearCache(true);
                    webView.clearFormData();
                    webView.clearHistory();
                } catch (e: Exception) {
                    if (Magic.debugEnabled) {
                        Log.d("Magic", "clearWebviewStorage failed")
                    }
                }
            } else {
                if (Magic.debugEnabled) {
                    Log.d("Magic", "showOverlay failed, Please pass Activity Context to API Call")
                }
            }
        }
    }

    /**
     * Webview display related
     */
    private fun showOverlay() {
        runOnUiThread {
            if (it is Activity && !it.isFinishing && !it.isDestroyed) {
                webView.visibility = View.VISIBLE
                webViewDialog = WebViewDialog(it, webView)
                try {
                    webViewDialog?.show()
                } catch (e: WindowManager.BadTokenException) {
                    // Handle the exception gracefully
                    if (Magic.debugEnabled) {
                        Log.d("Magic", "showOverlay failed due to BadTokenException: ${e.message}")
                    }
                }
            } else {
                if (Magic.debugEnabled) {
                    Log.d("Magic", "showOverlay failed, Please pass Activity Context to API Call")
                }
            }
        }
    }

    private fun hideOverlay() {
        runOnUiThread {
            if (it is Activity && !it.isFinishing && !it.isDestroyed) {
                if (webView.parent != null) {
                    val vg = (webView.parent as ViewGroup)
                    vg.removeView(webView)
                }
                webView.visibility = View.INVISIBLE
                try {
                    webViewDialog?.dismiss()
                } catch (e: Exception) {
                    // Handle the exception gracefully
                    if (Magic.debugEnabled) {
                        Log.d("Magic", "hideOverlay failed due to Exception: ${e.message}")
                    }
                }
            }
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

    private fun rebuildWebView(context: Context) {
        if (Magic.debugEnabled) {
            Log.i("Magic", "The Webview has hung. Please wait a few seconds as it rebuilds")
        }
        runOnUiThread {
            webView = WebView(mMutableContext)
            if (context is Application) {
                initializeWebView()
            } else {
                throw IllegalArgumentException("Initializing context should be application context ")
            }
        }
    }

    fun setMagicEventListener(listener: MagicEventListener) {
        this.magicEventListener = listener
    }
}
