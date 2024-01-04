package link.magic.demo

import android.os.Bundle
import android.util.Log
import link.magic.DemoApp
import link.magic.android.Magic
import link.magic.android.modules.user.response.IsLoggedInResponse


open class MainActivity : UtilActivity() {
    lateinit var magic: Magic

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        startMALoginActivity()

// Utlize this when using MA API Keys
//        magic = (applicationContext as DemoApp).magic
//        val completable = (magic as Magic).user.isLoggedIn(this)
//        completable.whenComplete { response: IsLoggedInResponse?, error: Throwable? ->
//            if (error != null) {
//                Log.d("error", error.localizedMessage)
//            }
//            if (response != null && response.result) {
//                startTabActivity()
//            } else {
//                startMALoginActivity()
//            }
//        }
    }
}
