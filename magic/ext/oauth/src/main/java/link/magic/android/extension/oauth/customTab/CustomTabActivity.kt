package link.magic.android.extension.oauth.customTab

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



class CustomTabActivity: Activity() {

    companion object{
        private const val CUSTOM_TAB_REDIRECT_REQUEST_CODE = 2
        val CUSTOM_TAB_REDIRECT_ACTION = CustomTabActivity::class.java.simpleName + ".action_customTabRedirect"
        val DESTROY_ACTION = CustomTabActivity::class.java.simpleName + ".action_destroy"
        private var closeReceiver: BroadcastReceiver? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, CustomTabMainActivity::class.java)
        intent.action = CUSTOM_TAB_REDIRECT_ACTION
        intent.putExtra(CustomTabMainActivity.EXTRA_URL, getIntent().dataString)

        // these flags will open CustomTabMainActivity from the back stack as well as closing this
        // activity and the custom tab opened by CustomTabMainActivity.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivityForResult(intent, CUSTOM_TAB_REDIRECT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            // We weren't able to open CustomTabMainActivity from the back stack. Send a broadcast
            // instead.
            val broadcast = Intent(CUSTOM_TAB_REDIRECT_ACTION)
            broadcast.putExtra(CustomTabMainActivity.EXTRA_URL, intent.dataString)
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast)

            // Wait for the custom tab to be removed from the back stack before finishing.
            closeReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    finish()
                }
            }
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(closeReceiver as BroadcastReceiver, IntentFilter(DESTROY_ACTION))
        }
    }

    override fun onDestroy() {
        closeReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
        super.onDestroy()
    }
}
