package com.uniimarket.app.Dashboard.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.uniimarket.app.R
import com.uniimarket.app.home.view.ProductsFragment

class SearchTabsFragment : Fragment() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =
            inflater?.inflate(com.uniimarket.app.R.layout.search_tabs_fragment, container, false)
        Log.e("search fra", "called")

        initialVariables(view)
        (context as DashboardActivity).ll_back?.setOnClickListener {
            (context as DashboardActivity).replaceFragment(
                com.uniimarket.app.R.id.frame_layout,
                PathWaysFragment(), true, false
            )
        }

        val adapter = context?.let {
            childFragmentManager?.let { it1 ->
                MyAdapter(
                    it,
                    it1, tabLayout!!.tabCount
                )
            }
        }

        viewPager!!.adapter = adapter



        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position



//                if (tab.position == 1){
//                    viewPager?.currentItem = tab.position
//                } else{
//                    viewPager?.setCurrentItem(tab.position)
//                }

                viewPager?.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        adapter?.notifyDataSetChanged()

        return view
    }

    private fun initialVariables(view: View?) {

        tabLayout = view?.findViewById(R.id.tabLayout)
        viewPager = view?.findViewById(R.id.viewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Products"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Users"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


    }

    class MyAdapter(
        private val myContext: Context,
        fm: FragmentManager,
        internal var totalTabs: Int
    ) : FragmentPagerAdapter(fm) {

        // this is for fragment tabs
        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    //  val homeFragment: HomeFragment = HomeFragment()
                    return ProductsFragment()
                }
                1 -> {
                    return FriendsSearchList()
                }

                else -> return null
            }
        }

        // this counts total number of tabs
        override fun getCount(): Int {
            return totalTabs
        }
    }

}