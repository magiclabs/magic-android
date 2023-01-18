package link.magic.demo.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import link.magic.DemoApp
import link.magic.android.Magic
import link.magic.android.MagicConnect
import link.magic.demo.eth.EthFragment
import link.magic.demo.magic.MAFragment
import link.magic.demo.magic.MCFragment

class TabLayoutAdapter(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int) :
    FragmentPagerAdapter(fm!!) {
    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        val magic = (myContext.applicationContext as DemoApp).magic;

        return when (position) {
            0 -> {
                pickMagicFragment(magic)
            }
            1 -> {
                EthFragment()
            }
            else -> MAFragment()
        }
    }

    private fun pickMagicFragment(magic: Any): Fragment {
        if (magic is Magic) {
            return MAFragment()
        }
        if (magic is MagicConnect) {
            return MCFragment()
        }
        return MAFragment()
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}