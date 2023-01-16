package link.magic

import android.app.Application
import link.magic.android.EthNetwork
import link.magic.android.Magic
import link.magic.android.MagicConnect
import link.magic.android.MagicCore.Companion.debugEnabled

class DemoApp : Application() {

    lateinit var magic: Any
    override fun onCreate() {

//        magic = Magic(this, "YOUR_MC_PUBLISHABLE_KEY", ethNetwork = EthNetwork.Goerli)
        magic = MagicConnect(this, "YOUR_MC_PUBLISHABLE_KEY", ethNetwork = EthNetwork.Goerli)
        debugEnabled = true
        super.onCreate()
    }
}
