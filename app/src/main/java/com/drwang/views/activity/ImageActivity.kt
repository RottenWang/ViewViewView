package com.drwang.views.activity

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.util.Log
import com.drwang.views.R
import com.drwang.views.adapter.ImageAdapter
import com.drwang.views.base.BasicActivity
import com.drwang.views.bean.ImageEntityBean
import com.drwang.views.support.LocalThreadPoolManager
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : BasicActivity() {
    var mImageList: ArrayList<ImageEntityBean>? = null;
    var mImageAdapter: ImageAdapter? = null;
    val LOAD_ALL = 0;
    override fun initializeView() {
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recycler_view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mImageList = ArrayList()
        mImageAdapter = ImageAdapter(this, mImageList);
        recycler_view.adapter = mImageAdapter;
    }

    override fun initializeData() {
        tool_bar.setNavigationOnClickListener { finish() }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100);
        } else {
            supportLoaderManager.initLoader(LOAD_ALL, null, imageLoader)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                supportLoaderManager.initLoader(LOAD_ALL, null, imageLoader)
            }
        }
    }

    override fun setContentViewRes(): Int = R.layout.activity_image

    val imageLoader = object : LoaderManager.LoaderCallbacks<Cursor> {
        private val IMAGE_PROJECTION = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID
        )

        override fun onLoaderReset(loader: Loader<Cursor>?) {

        }

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            val cursorLoader = CursorLoader(
                    this@ImageActivity,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null,
                    IMAGE_PROJECTION[2] + " DESC"
            )
            return cursorLoader
        }

        override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
            Log.i("tag", "thread = " + Thread.currentThread());
            if (data != null) {
                LocalThreadPoolManager.getInstance().execute({
                    if (data.moveToFirst()) {
                        var imageList = ArrayList<ImageEntityBean>();
                        var imageBean: ImageEntityBean
                        do {
                            val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                            val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                            val dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
                                continue
                            }
                            imageBean = ImageEntityBean(path, name, dateTime)
                            imageList.add(imageBean)

                        } while (data.moveToNext())
                        runOnUiThread {
                            mImageList?.addAll(imageList);
                            mImageAdapter?.notifyDataSetChanged()
                        }
                    }
                })
            }
        }
    }


}
