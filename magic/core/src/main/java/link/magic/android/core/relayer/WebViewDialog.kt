package link.magic.android.core.relayer

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.RelativeLayout

class WebViewDialog(context: Context, private val webView: WebView) : Dialog(context){
    init {
        setContentView(createContentView())
        setCancelable(true)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    private fun createContentView(): View {
        val layout = RelativeLayout(context)
        if (webView.parent != null) {
            (webView.parent as ViewGroup).removeView(webView)
        }
        layout.addView(webView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return layout
    }
}