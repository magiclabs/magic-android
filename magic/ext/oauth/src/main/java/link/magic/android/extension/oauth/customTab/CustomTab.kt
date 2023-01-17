package link.magic.android.extension.oauth.customTab

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession

class CustomTab(private val uri: String) {

    fun openCustomTab(activity: Activity?, packageName: String?): Boolean {
        val session: CustomTabsSession? = CustomTabPrefetchHelper.getPreparedSessionOnce()
        val customTabsIntent = CustomTabsIntent.Builder(session).build()
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        try {
            customTabsIntent.launchUrl(activity!!, Uri.parse(uri))
        } catch (e: ActivityNotFoundException) {
            return false
        }
        return true
    }
}
