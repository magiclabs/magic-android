package link.magic.android.extension.oauth.customTab

import android.content.ComponentName
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession


class CustomTabPrefetchHelper: CustomTabsServiceConnection() {

    companion object {
        private var client: CustomTabsClient? = null
        private var session: CustomTabsSession? = null

        @JvmStatic
        fun getPreparedSessionOnce(): CustomTabsSession? {
            val result = session
            session = null
            return result
        }
    }

    private fun prepareSession() {
        if (session == null) {
            if (client != null) {
                session = client!!.newSession(null)
            }
        }
    }

    fun mayLaunchUrl(url: Uri) {
        if (session == null) {
            prepareSession()
        }
        if (session != null) {
            session!!.mayLaunchUrl(url, null, null)
        }
    }

    override fun onCustomTabsServiceConnected(name: ComponentName, newClient: CustomTabsClient) {
        client = newClient
        client!!.warmup(0)
        prepareSession()
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {}

}
