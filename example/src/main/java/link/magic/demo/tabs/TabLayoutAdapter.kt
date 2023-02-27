package link.magic.demo.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import link.magic.DemoApp
import link.magic.demo.eth.EthFragment
import link.magic.demo.magic.MCFragment
import link.magic.demo.magic.MainFragment

class TabLayoutAdapter(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int) :
    FragmentPagerAdapter(fm!!) {
    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        val magic = (myContext.applicationContext as DemoApp).magic;

        return when (position) {
            0 -> {
                MainFragment()
            }
            1 -> {
                MCFragment()
            }
            2 -> {
                EthFragment()
            }
            else -> MainFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
