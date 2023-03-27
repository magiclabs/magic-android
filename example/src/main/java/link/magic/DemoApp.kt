package link.magic

import android.app.Application
import link.magic.android.Magic
import link.magic.android.Magic.Companion.debugEnabled

class DemoApp : Application() {

    lateinit var magic: Any
    override fun onCreate() {
        debugEnabled = true
        magic = Magic(this, "pk_live_CB29F016115E1D30")
        super.onCreate()
    }
}
