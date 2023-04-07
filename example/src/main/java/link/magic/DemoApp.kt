package link.magic

import android.app.Application
import link.magic.android.Magic
import link.magic.android.Magic.Companion.debugEnabled

class DemoApp : Application() {

    lateinit var magic: Any
    override fun onCreate() {
        debugEnabled = true
        magic = Magic(this, "pk_live_5C316374413D28A3")
        super.onCreate()
    }
}
