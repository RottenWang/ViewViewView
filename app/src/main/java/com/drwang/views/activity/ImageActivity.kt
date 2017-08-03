package com.drwang.views.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.drwang.views.R
import com.drwang.views.adapter.ExampleAdapter
import com.drwang.views.base.BasicActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.util.*

class ImageActivity : BasicActivity() {
    var mImageAdapter: ExampleAdapter? = null;
    var mImageList: ArrayList<String>? = null;
    override fun initializeView() {
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mImageList = ArrayList();
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageList?.add("haha1")
        mImageAdapter = ExampleAdapter(this, mImageList)
        recycler_view.adapter = mImageAdapter;
    }

    override fun initializeData() {
        tool_bar.setNavigationOnClickListener { finish() }
    }

    override fun setContentViewRes(): Int = R.layout.activity_image

}
