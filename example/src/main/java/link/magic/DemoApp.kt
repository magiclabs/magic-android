package link.magic

import android.app.Application
import link.magic.android.EthNetwork
import link.magic.android.Magic
import link.magic.android.Magic.Companion.debugEnabled

class DemoApp : Application() {

    lateinit var magic: Any
    override fun onCreate() {

        magic = Magic(this, "YOUR_MAGIC_PUBLISHABLE_KEY", ethNetwork = EthNetwork.Goerli)
        debugEnabled = true
        super.onCreate()
    }
}
