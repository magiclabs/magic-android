package link.magic.demo.tabs

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import link.magic.DemoApp
import link.magic.demo.R
import link.magic.demo.UtilActivity

class MainTabActivity : UtilActivity(){

    lateinit var magic: Any

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)
        renderTab()
        magic = (applicationContext as DemoApp).magic
    }

    private fun renderTab () {
        // tabs
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        viewPager = findViewById<View>(R.id.view_pager) as ViewPager
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Magic Auth"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Magic Connect"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Eth"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = TabLayoutAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }


            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tabLayout!!.getTabAt(2)?.select()
    }
}
