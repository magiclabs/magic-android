package link.magic.demo

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import link.magic.demo.login.MALoginActivity
import link.magic.demo.tabs.MainTabActivity

open class UtilActivity: AppCompatActivity() {
    fun toastAsync(message: String?) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
    }

    fun startTabActivity() {
        val intent = Intent(applicationContext, MainTabActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun startMALoginActivity() {
        val intent = Intent(applicationContext, MALoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
