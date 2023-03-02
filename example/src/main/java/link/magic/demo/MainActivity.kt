package link.magic.demo

import android.os.Bundle


open class MainActivity : UtilActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        startMALoginActivity()
    }
}
