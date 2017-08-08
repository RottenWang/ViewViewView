package com.drwang.views.activity

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.graphics.Palette
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.ViewTreeObserver
import com.drwang.views.R
import com.drwang.views.adapter.ImageAdapter
import com.drwang.views.base.BasicActivity
import com.drwang.views.bean.ImageEntityBean
import com.drwang.views.support.LocalThreadPoolManager
import com.drwang.views.support.fresco.FrescoScheme
import com.drwang.views.support.fresco.FrescoUtils
import com.drwang.views.util.DensityUtil
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.activity_image.view.*
import kotlinx.android.synthetic.main.activity_main.*

class ImageActivity : BasicActivity() {
    var titleRes: Int = R.drawable.default_title_bg2;
    var mImageList: ArrayList<ImageEntityBean>? = null;
    var mImageAdapter: ImageAdapter? = null;
    val LOAD_ALL = 0;
    override fun initializeView() {
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recycler_view.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mImageList = ArrayList()
        mImageAdapter = ImageAdapter(this, mImageList);
        recycler_view.adapter = mImageAdapter;
        setTitleImage();
    }

    private fun setTitleImage() {
        val decodeResource = BitmapFactory.decodeResource(resources, titleRes);
        val width = decodeResource.width
        val height = decodeResource.height;
        val newWidth = DensityUtil.getInstance().getScreenWidth(this).toInt();
        val newHeight = (newWidth / width.toFloat()) * height;
        var uri = FrescoScheme.SCHEME_RES + packageName + "/" + titleRes;
        simpleDraweeView_title.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                simpleDraweeView_title.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                val layoutParams = simpleDraweeView_title.layoutParams;
                layoutParams.width = newWidth
                layoutParams.height = newHeight.toInt()
                simpleDraweeView_title.layoutParams = layoutParams
                simpleDraweeView_title.controller = FrescoUtils.getController(simpleDraweeView_title, newWidth, newHeight.toInt(), uri, object : BaseControllerListener<ImageInfo>() {
                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        super.onFinalImageSet(id, imageInfo, animatable)
                        Palette.from(BitmapFactory.decodeResource(resources, titleRes)).generate(object : Palette.PaletteAsyncListener {
                            override fun onGenerated(palette: Palette?) {
                                val lightMutedColor = palette?.getLightMutedColor(0xffffff);
                                val toHexString = Integer.toHexString(lightMutedColor!!);
                                val substring = toHexString.substring(startIndex = 0, endIndex = 2);
                                val toInt = Integer.parseInt(substring, 16)
                                if (toInt <= 127) {
                                    //半透明
                                    setStatusBarDarkMode(true, this@ImageActivity);
                                } else {
                                    setStatusBarDarkMode(false, this@ImageActivity);
                                }
                                if (toHexString.length == 8) {

                                } else {

                                }
                            }
                        });
                    }
                })


            }
        })

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
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
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
                            val width = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                            val height = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
                                continue
                            }
                            if (width == 0L || height == 0L) {
                                continue
                            }
                            imageBean = ImageEntityBean(path, name, dateTime)
                            imageList.add(imageBean)

                        } while (data.moveToNext())
                        data.close()
                        runOnUiThread {
                            mImageList?.addAll(imageList);
                            mImageAdapter?.notifyDataSetChanged()
                            this@ImageActivity.supportLoaderManager.destroyLoader(LOAD_ALL)
                        }
                    }
                })
            }
        }
    }


}
