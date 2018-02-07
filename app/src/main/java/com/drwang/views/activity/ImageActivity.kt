package com.drwang.views.activity

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
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
import com.drwang.views.event.DeleteImageEvent
import com.drwang.views.event.GifChangeEvent
import com.drwang.views.event.GifImageInfoEvent
import com.drwang.views.support.LocalThreadPoolManager
import com.drwang.views.support.PriorityRunnable
import com.drwang.views.support.fresco.FrescoScheme
import com.drwang.views.support.fresco.FrescoUtils
import com.drwang.views.util.DensityUtil
import com.drwang.views.util.IntentUtil
import com.drwang.views.util.MathUtil
import com.drwang.views.util.SharedPreferencesUtils
import com.drwang.views.view.GridItemDecoration
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.activity_image.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

class ImageActivity : BasicActivity() {
    val TITLE_KEY = "title_key"
    val STRING_DEFAULT = "default";
    var mImageList: ArrayList<ImageEntityBean>? = null;
    var mGifList: ArrayList<ImageEntityBean>? = null;
    var mImageAdapter: ImageAdapter? = null;
    var isLoadFinished: Boolean = false;
    val LOAD_ALL = 0;
    var path: String? = null;
    override fun initializeView() {
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        recycler_view.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recycler_view.addItemDecoration(GridItemDecoration((10 * resources.displayMetrics.density).toInt(), Color.GREEN))
        mImageList = ArrayList()
        mGifList = ArrayList()
        mImageAdapter = ImageAdapter(this, mImageList, ImageAdapter.TYPE_NORMAL);
        recycler_view.adapter = mImageAdapter;
        path = SharedPreferencesUtils.getString(TITLE_KEY, STRING_DEFAULT);
        setTitleImage(true);
        simpleDraweeView_title.setOnClickListener {
            if (!isLoadFinished) {
                return@setOnClickListener
            }
            EventBus.getDefault().postSticky(GifImageInfoEvent(mGifList))
            IntentUtil.toSelectTitleImageActivity(ImageActivity@ this);
        }
    }

    private fun setTitleImage(isFirst: Boolean) {
        setImageToTitleBg(isFirst)
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
                LocalThreadPoolManager.execute(object : PriorityRunnable(10) {
                    override fun run() {
                        if (data.moveToFirst()) {
                            var imageList = ArrayList<ImageEntityBean>();
                            var gifList = ArrayList<ImageEntityBean>();
                            var imageBean: ImageEntityBean
                            do {
                                val path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
                                val name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
                                val dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
                                val width = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
                                val height = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
                                if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
                                    continue
                                }
                                if (width <= 0 || height <= 0) {
                                    continue
                                }

                                imageBean = ImageEntityBean(path, name, dateTime, width, height)
                                if (name.endsWith(".gif")) {
                                    gifList.add(imageBean)
                                } else {
                                    imageList.add(imageBean)
                                }

                            } while (data.moveToNext())
                            data.close()
                            runOnUiThread {
                                isLoadFinished = true
                                mImageList?.addAll(imageList)
                                mGifList?.addAll(gifList)
                                mImageAdapter?.notifyDataSetChanged()
                                this@ImageActivity.supportLoaderManager.destroyLoader(LOAD_ALL)
                            }
                        }
                    }
                })
            }
        }
    }

    @Subscribe
    fun onGifChangeEvent(gifChangeEvent: GifChangeEvent) {
        if (gifChangeEvent.imageEntityBean != null) {
            path = gifChangeEvent.imageEntityBean.path;
        } else {
            path = STRING_DEFAULT
        }
        setImageToTitleBg(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportLoaderManager.destroyLoader(LOAD_ALL)
    }

    private fun setImageToTitleBg(first: Boolean) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        val file = File(path)
        var decodeResource: Bitmap;
        var uri: String
        if (!file.exists()) {
            uri = FrescoScheme.SCHEME_RES + packageName + "/" + R.drawable.default_title_bg2;
            decodeResource = BitmapFactory.decodeResource(resources, R.drawable.default_title_bg2);
            SharedPreferencesUtils.putString(TITLE_KEY, STRING_DEFAULT);
        } else {
            uri = FrescoScheme.SCHEME_FILE + path!!;
            decodeResource = BitmapFactory.decodeFile(path)
            SharedPreferencesUtils.putString(TITLE_KEY, path);
        }
        Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(uri))
        val width = decodeResource.width
        val height = decodeResource.height;
        val newWidth = DensityUtil.getInstance().getScreenWidth(this).toInt();
        val newHeight = (newWidth / width.toFloat()) * height;
        if (first) {
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
                            setStatusBarMode(decodeResource)
                        }
                    })
                }
            })
        } else {
            val layoutParams = simpleDraweeView_title.layoutParams;
            layoutParams.width = newWidth
            layoutParams.height = newHeight.toInt()
            simpleDraweeView_title.layoutParams = layoutParams
            simpleDraweeView_title.controller = FrescoUtils.getController(simpleDraweeView_title, newWidth, newHeight.toInt(), uri, object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    setStatusBarMode(decodeResource)
                }
            })
        }
    }

    private fun setStatusBarMode(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val darkMutedColor = palette?.getDarkMutedColor(0xffffff);
            val red = MathUtil.getRed(darkMutedColor!!);
            val green = MathUtil.getGreen(darkMutedColor);
            val blue = MathUtil.getBlue(darkMutedColor);
//            val substring = toHexString.substring(startIndex = 0, endIndex = 2);
//            val toInt = Integer.parseInt(substring, 16)
            if (red <= 80 || green <= 80 || blue <= 80) {
                //半透明
                setStatusBarDarkMode(false, this@ImageActivity);
            } else {
                setStatusBarDarkMode(true, this@ImageActivity);
            }
        };
    }

    @Subscribe
    fun removeImage(deleteImageEvent: DeleteImageEvent) {
        mImageList?.remove(deleteImageEvent.imageEntityBean);
        mImageAdapter?.notifyDataSetChanged();
    }
}
