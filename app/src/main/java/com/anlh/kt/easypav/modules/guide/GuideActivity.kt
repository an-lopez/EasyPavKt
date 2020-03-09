package com.anlh.kt.easypav.modules.guide

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.anlh.kt.easypav.BR
import com.anlh.kt.easypav.R
import com.anlh.kt.easypav.data.model.Guide
import com.anlh.kt.easypav.databinding.ActivityGuideBinding
import com.anlh.kt.easypav.modules.guide.view.GuideAdapter
import com.anlh.kt.easypav.modules.guide.viewModel.GuideVM
import com.anlh.kt.easypav.modules.signIn.SignInActivity
import com.anlh.kt.easypav.util.AppConstants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.highflyers.commonresources.AppBaseActivity
import com.highflyers.commonresources.AppBaseItem
import java.util.*

class GuideActivity : AppBaseActivity<ActivityGuideBinding, GuideVM>() {

    private lateinit var guideBinding : ActivityGuideBinding
    private lateinit var viewModel : GuideVM

    override fun getBindingVariable(): Int {
        return BR.guideVM
    }

    override fun fullScreenConfiguration(): Boolean {
        return true
    }

    override fun getViewModel(): GuideVM {
        viewModel = ViewModelProviders.of(this).get(GuideVM::class.java)
        return viewModel
    }

    override fun onFragmentAttached() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }

    override fun onFragmentDetached(tag: String?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guideBinding = viewDataBinding
        setupView()
    }

    private fun setupView(){
        guideBinding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        guideBinding.viewPager.adapter = GuideAdapter(items = generateDummyData() as List<Guide>)
        TabLayoutMediator(guideBinding.tabLayout, guideBinding.viewPager){ _, _ -> }.attach()
        disableTabLayout(guideBinding.tabLayout)

        guideBinding.btnContinue.setOnClickListener { moveNext(guideBinding.viewPager.currentItem) }
        guideBinding.btnSkip.setOnClickListener{skip()}

        guideBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if( position >=  guideBinding.viewPager.adapter?.itemCount?.minus(1) ?: 0){
                    guideBinding.btnContinue.text = resources.getText(R.string.button_finish)
                    guideBinding.btnSkip.visibility = View.INVISIBLE
                }else{
                    guideBinding.btnSkip.visibility = View.VISIBLE
                    guideBinding.btnContinue.text = resources.getText(R.string.button_continue)
                }
            }
        })

    }

    private fun moveNext(current: Int){
        if(current < guideBinding.viewPager.adapter?.itemCount?.minus(1) ?: 0)
            guideBinding.viewPager.currentItem = current+1
        else if(guideBinding.btnContinue.text == resources.getText(R.string.button_finish)) {
            startActivity(Intent(this@GuideActivity, SignInActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
            this.finish()
        }
    }

    private fun skip(){
        guideBinding.viewPager.currentItem = guideBinding.viewPager.adapter?.itemCount?.minus(1) ?: 0
    }

    private fun generateDummyData(): List<AppBaseItem> {
        val data = LinkedList<AppBaseItem>()
        data.add(Guide("Texto de prueba 1", "https://dwdqz3611m4qq.cloudfront.net/wp_content/uploads/fearless-accounting-guide-795x450.png?mtime=20190724224646", AppConstants.GUIDE_VIEW_INIT))
        data.add(Guide("Texto de prueba 2", "https://dwdqz3611m4qq.cloudfront.net/wp_content/uploads/fearless-accounting-guide-795x450.png?mtime=20190724224646", AppConstants.GUIDE_VIEW_RESUME))
        //data.add(Guide("Texto de prueba 3", "https://dwdqz3611m4qq.cloudfront.net/wp_content/uploads/fearless-accounting-guide-795x450.png?mtime=20190724224646"))
        //data.add(Guide("Texto de prueba 4", "https://dwdqz3611m4qq.cloudfront.net/wp_content/uploads/fearless-accounting-guide-795x450.png?mtime=20190724224646"))
        return data
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun disableTabLayout(tab: TabLayout) {
        val tabStrip = tab.getChildAt(0) as LinearLayout
        for (i in 0 until tabStrip.childCount)
            tabStrip.getChildAt(i).setOnTouchListener { _, _ -> true}
    }
}