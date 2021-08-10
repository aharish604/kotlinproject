package com.uniimarket.app

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.uniimarket.app.Signin.view.SignInActivity
import kotlinx.android.synthetic.main.activity_intro_screens.view.*
import kotlinx.android.synthetic.main.intro_screen_1.view.*
import java.util.*

class IntroScreensActivity : AppCompatActivity(), ScreenPresenter.ScreenListener,
    DummyAdapter.screenClickListener {

    private var introViewPager: ViewPager? = null
    private var introViewPagerAdapter: IntroScreenViewPagerAdapter? = null
    private var introBullets: Array<TextView>? = null
    private var introBulletsLayout: LinearLayout? = null
    private var introSliderLayouts: IntArray? = null
    private var btnSkip: Button? = null
    var btnNext: Button? = null
    private var runOnce: RunOnce? = null


    var rv_Dummy: RecyclerView? = null

    var image_list =
        arrayListOf<Int>(R.drawable.introshopping, R.drawable.intro_one, R.drawable.intro_two)
    var string_text = arrayListOf<ScreenDatum>()

    var screenPresenter: ScreenPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val introScreensLayout = layoutInflater.inflate(R.layout.activity_intro_screens, null)
        setContentView(introScreensLayout)


        runOnce = RunOnce(this)
//        if (!runOnce!!.isFirstTimeLaunch) {
//            applicationStartup()
//            finish()
//        }
        introViewPager = introScreensLayout.intro_view_pager
        introBulletsLayout = introScreensLayout.intro_bullets
        btnSkip = introScreensLayout.btn_skip
        btnSkip!!.visibility = View.GONE
        btnNext = introScreensLayout.btn_next
        btnNext!!.visibility = View.GONE
        //Get the intro slides
        introSliderLayouts = intArrayOf(
            R.layout.intro_screen_1,
            R.layout.intro_screen_2,
            R.layout.intro_screen_3
        )
        // adding bottom introBullets
        makeIIntroBullets(0)
        introViewPagerAdapter = IntroScreenViewPagerAdapter()
        introViewPager!!.adapter = introViewPagerAdapter

        introViewPager!!.addOnPageChangeListener(introViewPagerListener)


        val handler = Handler()
        val Update = Runnable {
            var currentPage = getItem(+1)
//            if (currentPage == 3) {
//                currentPage = 0
//            }
            introViewPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 5000, 3000)

        (btnSkip as View?)!!.setOnClickListener { applicationStartup() }
        (btnNext as View?)!!.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
//            val current = getItem(+1)
//            if (current < introSliderLayouts!!.size) {
//                // move to next screen
//                introViewPager!!.currentItem = current
//            } else {
            applicationStartup()
//            }
        }
        // making notification bar transparent
        SetTransperantStatusBar()
        screenPresenter = ScreenPresenter(this)

        rv_Dummy = findViewById(R.id.rvDummy)


        screenPresenter?.getIntroData()
    }

    override fun onClickcreen(id: Int?) {
        applicationStartup()
    }


    override fun screenResponseSuccess(
        status: String,
        message: String?,
        signInList: ArrayList<ScreenDatum>
    ) {
        string_text.addAll(signInList)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_Dummy)
        rv_Dummy?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_Dummy?.addItemDecoration(CirclePagerIndicatorDecoration())
        rv_Dummy?.adapter =
            image_list?.let { DummyAdapter(it, string_text, this, this) }
    }

    override fun screenResponseFailure(status: String, message: String?) {

    }

    private fun makeIIntroBullets(currentPage: Int) {
        var arraySize = introSliderLayouts!!.size
        introBullets = Array<TextView>(arraySize) { textboxInit() }
        val colorsActive = resources.getIntArray(R.array.array_intro_bullet_active)
        val colorsInactive = resources.getIntArray(R.array.array_intro_bullet_inactive)
        introBulletsLayout!!.removeAllViews()
        for (i in 0 until introBullets!!.size) {
            introBullets!![i] = TextView(this)
            introBullets!![i].text = Html.fromHtml("&#8226;")
            introBullets!![i].textSize = 30F
            introBullets!![i].setTextColor(colorsInactive[currentPage])
            introBulletsLayout!!.addView(introBullets!![i])
        }
        if (introBullets!!.isNotEmpty())
            introBullets!![currentPage].setTextColor(colorsActive[currentPage])
    }

    private fun textboxInit(): TextView {
        return TextView(applicationContext)
    }

    private fun getItem(i: Int): Int {
        return introViewPager!!.getCurrentItem() + i
    }

    private fun applicationStartup() {
        runOnce!!.isFirstTimeLaunch = false
        startActivity(Intent(this@IntroScreensActivity, SignInActivity::class.java))
        finish()
    }

    private var introViewPagerListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                makeIIntroBullets(position)
                /*Based on the page position change the button text*/
                if (position == introSliderLayouts!!.size - 1) {
                    btnNext!!.text = getString(R.string.done_button_title)
                    btnNext!!.visibility = View.GONE
                    btnSkip!!.setVisibility(View.GONE)
                } else {
                    btnNext!!.text = getString(R.string.next_button_title)
                    btnSkip!!.visibility = View.GONE
                    btnNext!!.visibility = View.GONE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
                //Do nothing for now
            }

            override fun onPageScrollStateChanged(arg0: Int) {
                //Do nothing for now
            }
        }

    private fun SetTransperantStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    inner class IntroScreenViewPagerAdapter : PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val layoutInflater: LayoutInflater = LayoutInflater.from(applicationContext)
            val view = layoutInflater.inflate(introSliderLayouts!![position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return introSliderLayouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
