package link.magic.android.extension.oauth.customTab

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class CustomTabMainActivity: Activity() {

    companion object {
        val EXTRA_CHROME_PACKAGE = CustomTabMainActivity::class.java.simpleName + ".extra_chromePackage"
        val EXTRA_URL = CustomTabMainActivity::class.java.simpleName + ".extra_url"
        val REFRESH_ACTION = CustomTabMainActivity::class.java.simpleName + ".action_refresh"
        val NO_ACTIVITY_EXCEPTION = CustomTabMainActivity::class.java.simpleName + ".no_activity_exception"
        val URI = CustomTabMainActivity::class.java.simpleName + ".uri"

        private var shouldCloseCustomTab = true
        private var redirectReceiver: BroadcastReceiver? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Custom Tab Redirects should not be creating a new instance of this activity
        if (CustomTabActivity.CUSTOM_TAB_REDIRECT_ACTION == intent.action) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        if (savedInstanceState == null) {
            val chromePackage = intent.getStringExtra(EXTRA_CHROME_PACKAGE)
            val customTab = intent.getStringExtra(URI)?.let { CustomTab(it) }
            val couldOpenCustomTab = customTab?.openCustomTab(this, chromePackage)
            shouldCloseCustomTab = false
            if (!couldOpenCustomTab!!) {
                setResult(RESULT_CANCELED, intent.putExtra(NO_ACTIVITY_EXCEPTION, true))
                finish()
                return
            }

            // This activity will receive a broadcast if it can't be opened from the back stack
            redirectReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    // Remove the custom tab on top of this activity.
                    val newIntent = Intent(this@CustomTabMainActivity, CustomTabMainActivity::class.java)
                    newIntent.action = REFRESH_ACTION
                    newIntent.putExtra(EXTRA_URL, intent.getStringExtra(EXTRA_URL))
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(newIntent)
                }
            }
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(
                            redirectReceiver as BroadcastReceiver, IntentFilter(CustomTabActivity.CUSTOM_TAB_REDIRECT_ACTION))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (REFRESH_ACTION == intent.action) {
            // The custom tab is now destroyed so we can finish the redirect activity
            val broadcast = Intent(CustomTabActivity.DESTROY_ACTION)
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast)
            sendResult(RESULT_OK, intent)
        } else if (CustomTabActivity.CUSTOM_TAB_REDIRECT_ACTION == intent.action) {
            // We have successfully redirected back to this activity. Return the result and close.
            sendResult(RESULT_OK, intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (shouldCloseCustomTab) {
            // The custom tab was closed without getting a result.
            sendResult(RESULT_CANCELED, null)
        }
        shouldCloseCustomTab = true
    }

    private fun sendResult(resultCode: Int, resultIntent: Intent?) {
        redirectReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
        if (resultIntent != null) {
            setResult(resultCode, resultIntent)
        } else {
            setResult(resultCode, intent)
        }

        // Destroy the CTMainActivity
        finish()
    }
}
