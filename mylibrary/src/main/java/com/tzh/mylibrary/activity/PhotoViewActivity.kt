package com.tzh.mylibrary.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.tzh.mylibrary.R
import com.tzh.mylibrary.adapter.BannerImageAdapter
import com.tzh.mylibrary.base.XBaseBindingActivity
import com.tzh.mylibrary.databinding.ActivityPhotoViewBinding
import com.tzh.mylibrary.databinding.ActivityScanBinding
import com.tzh.mylibrary.livedata.ActivityCloseLiveData
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.util.general.FileUtil
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.toDefault
import com.youth.banner.listener.OnPageChangeListener
import java.io.File

class PhotoViewActivity : XBaseBindingActivity<ActivityPhotoViewBinding>(R.layout.activity_photo_view) {
    companion object {
        @JvmStatic
        fun start(context: Context, url : String) {
            start(context,ArrayList<String>().apply {
                add(url)
            },0)
        }

        @JvmStatic
        fun start(context: Context, imageList : ArrayList<String>, position : Int ?= null) {
            context.startActivity(Intent(context, PhotoViewActivity::class.java).apply {
                putExtra("imgUrlList",imageList)
                putExtra("imgPosition",position)
            })
        }
    }

    private var currentPosition = 0

    val mList by lazy {
        intent.getStringArrayListExtra("imgUrlList").toDefault(mutableListOf<String>())
    }

    private val mPosition by lazy {
        intent.getIntExtra("imgPosition",0)
    }

    val mAdapter by lazy {
        BannerImageAdapter(mList) {
            finish()
        }
    }

    override fun initView() {
        binding.tvNum.text = (mPosition + 1).toString() +"/" + mList.size.toString()
        binding.banner.setAdapter(mAdapter)
        binding.banner.setCurrentItem(mPosition,false)
        binding.banner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                binding.tvNum.text = (position + 1).toString() +"/" + mList.size.toString()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        binding.ivSaveImagePhoto.setOnClickNoDouble {
            save()
        }
    }

    override fun initData() {

    }

    override fun onCloseActivity() {

    }

    private fun save(){
        PermissionXUtil.requestStoragePermission(this, object : OnPermissionCallBackListener {
            override fun onAgree() {
                //save image
                if (currentPosition != -1) {
                    download(mList[currentPosition])
                }
            }

            override fun onDisAgree() {

            }
        })
    }

    // 保存图片到手机
    @SuppressLint("StaticFieldLeak")
    fun download(url: String?) {
        object : AsyncTask<Void?, Int?, File?>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg params: Void?): File? {
                var file: File? = null
                try {
                    val future = Glide
                        .with(this@PhotoViewActivity)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    file = future.get()

                    // 首先保存图片
                    val pictureFolder =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile
                    val appDir = File(pictureFolder, "biubiu")
                    if (!appDir.exists()) {
                        appDir.mkdirs()
                    }
                    val fileName = System.currentTimeMillis().toString() + ".jpg"
                    val destFile = File(appDir, fileName)
                    FileUtil.copy(file, destFile)

                    // 最后通知图库更新
                    sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(File(destFile.path))
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@PhotoViewActivity,"保存失败",Toast.LENGTH_LONG).show()
                    Log.e("PhotoViewActivity", e.message!!)
                }
                return file
            }

            override fun onPostExecute(file: File?) {
                Toast.makeText(this@PhotoViewActivity,"图片已保存到" + file!!.absolutePath + "下",Toast.LENGTH_LONG).show()
                //Toast.makeText(PromotionPosterActivity.this, "已保存到Pictures/bbc下", Toast.LENGTH_SHORT).show();
            }

            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
            }
        }.execute()
    }
}