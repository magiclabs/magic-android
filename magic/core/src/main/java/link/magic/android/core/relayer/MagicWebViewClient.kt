package link.magic.android.core.relayer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

internal class MagicWebViewClient : WebViewClient() {

    // The starting activity call in each if-branch looks the same,
    // keeping this structure just in case, we need tweak according to different deeplink url format
    // in the future
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        // Opens privacy and terms page in the new browser
        if(URLUtil.isNetworkUrl(url) ) {
            view.context.startActivity(intent)
            return true
        }
        // If app is installed, open the app
        if (appInstalledOrNot(url, view.context)) {
            view.context.startActivity(intent)
        } else {
            // else try to open the url regardless but throws with Toast when error occurs
            try {
                // WalletConnect redirection goes here
                view.context.startActivity(intent)
            } catch (e: Exception) {
                val message = "Magic: Unable to find installed wallet. Please use email login"
                Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    // Try to find if any wallet app has been installed in the device
    private fun appInstalledOrNot(uri: String, ctx: Context): Boolean {
        val pm: PackageManager = ctx.packageManager
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(uri, PackageManager.PackageInfoFlags.of(0))
                true
            } else {
                pm.getPackageInfo(uri, 0)
                true
            }
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
}
