package link.magic.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import link.magic.DemoApp
import link.magic.android.Magic
import link.magic.android.MagicConnect
import link.magic.android.modules.user.response.IsLoggedInResponse


open class MainActivity : UtilActivity() {

    lateinit var magic: Any

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        magic = (applicationContext as DemoApp).magic
        if (magic is Magic) {
            val completable = (magic as Magic).user.isLoggedIn()
            completable.whenComplete { response: IsLoggedInResponse?, error: Throwable? ->
                if (error != null) {
                    Log.d("error", error.localizedMessage)
                    // Navigate to MA login
                    startMALoginActivity()
                } else if (response != null && response.result) {
                    // Display tab layout
                    startTabActivity()
                    toastAsync("You're Logged In")
                } else {
                    startMALoginActivity()
                }
            }
        }
        if (magic is MagicConnect) {
            startTabActivity()
        }
    }


}