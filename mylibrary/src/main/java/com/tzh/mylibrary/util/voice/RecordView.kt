package com.tzh.mylibrary.util.voice

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tzh.mylibrary.R
import com.tzh.mylibrary.databinding.LayoutRecordViewBinding
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.OnPermissionCallBackListener
import com.tzh.mylibrary.util.PermissionXUtil
import com.tzh.mylibrary.util.bindingInflateLayout
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.util.setTextColorRes
import com.tzh.mylibrary.util.setTextSizeDip
import com.tzh.mylibrary.util.setTextStyle
import java.io.File

/**
 *
 */
@SuppressLint("SetTextI18n")
class RecordView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) , LifecycleObserver {
    init {
        attrs?.run {
            initView(this)
        }
    }
    var callback : RecordCallback?= null
    var mSecond : Long = 0
    var file : File ?= null
    var pcmFile : File ?= null
    var manager : AudioRecordManager?= null
    var mMaxSecond = 0
    var mMinSecond = 0
    private var mActivity : AppCompatActivity ?= null
    private var mFragment : Fragment ?= null
    fun init(fragment : Fragment){
        mFragment = fragment
        initView(fragment)
    }

    fun init(activity : AppCompatActivity){
        mActivity = activity
        initView(activity)
    }

    private fun initView(lifecycle: LifecycleOwner){
        manager = AudioRecordManager(lifecycle).apply { this.recordTimeMax = mMaxSecond.toLong() }
        manager?.callback = object : AudioRecordManager.RecordCallback{
            override fun onSuccess(outputFile: File?) {
                pcmFile = outputFile
                manager?.pcmToMp3(outputFile)
                endRecord()
                LogUtils.e("RecordView","onSuccess")
            }

            override fun onError(t: Throwable?) {

            }

            override fun onStart() {
                startRecord()
            }

            override fun onTime(second: Long) {
                binding?.tvTime?.text = second.toString()+"s"
                mSecond = second
                binding?.recordView?.progress = second.toInt()
                if(mMinSecond>0 && second.toInt() > mMinSecond){
                    binding?.tvVoiceSecond?.visibility = INVISIBLE
                }
            }

            override fun onStop() {
                LogUtils.e("RecordView","暂停2")
            }

            override fun onPcmToMp3(outputFile: File?) {
                pcmFile?.delete()
                file = outputFile
                if (outputFile != null) {
                    callback?.recordEnd(outputFile,mSecond)
                }
            }
        }
    }

    var binding: LayoutRecordViewBinding? = null

    private fun initView(attrs: AttributeSet) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RecordView)
        mMaxSecond = attributes.getInt(R.styleable.RecordView_rv_Max_Second,300)
        mMinSecond = attributes.getInt(R.styleable.RecordView_rv_Min_Second,0)
        attributes.recycle()
        binding = bindingInflateLayout<LayoutRecordViewBinding>(R.layout.layout_record_view).also {
            it.tvTips.text = "最长录制"+mMaxSecond.toString()+"s，点击开始录制"
            it.recordView.max = mMaxSecond
            //开始录音
            it.layoutRecord.setOnClickNoDouble { view->
                if(manager!=null){
                    if(mActivity!=null || mFragment != null){
                        mActivity?.let {act->
                            PermissionXUtil.requestRecordPermission(act,object : OnPermissionCallBackListener {
                                override fun onAgree() {
                                    start()
                                }

                                override fun onDisAgree() {

                                }
                            })
                        }

                        mFragment?.let {act->
                            PermissionXUtil.requestRecordPermission(act,object : OnPermissionCallBackListener {
                                override fun onAgree() {
                                    manager?.onStartRecord(PathUtil.getMasterQuestionVoice(view.context).absolutePath)
                                }

                                override fun onDisAgree() {

                                }
                            })
                        }
                    }
                }else{
                    Toast.makeText(context,"请先初始化",Toast.LENGTH_LONG).show()
                }
            }
            //结束录音
            it.layoutInRecord.setOnClickNoDouble {
                stop()
            }


            it.layoutInRecordPlay.setOnClickNoDouble {
                playVoice2()
            }
        }

    }

    fun start(){
        binding?.let {
            manager?.onStartRecord(PathUtil.getMasterQuestionVoice(it.layoutRecord.context).absolutePath)
        }
    }

    fun stop(){
        binding?.let {
            if(manager?.recordStatus == AudioRecordManager.STATUS_RECORD){
                manager?.onStopRecord()
            }
        }
    }

    /**
     * 开始录音
     */
    fun startRecord(){
        binding?.let {
            it.layoutRecord.visibility = GONE
            it.layoutInRecord.visibility = VISIBLE
            it.layoutInRecordPlay.visibility = GONE
            it.tvTips.text = "点击停止"
            it.tvTips.setTextColorRes(R.color.color_333)
            if(mMinSecond>0){
                it.tvVoiceSecond.visibility = VISIBLE
                it.tvVoiceSecond.text = "最少录制"+mMinSecond+"s"
                it.tvVoiceSecond.setTextStyle(false)
                it.tvVoiceSecond.setTextSizeDip(12f)
            }else{
                it.tvVoiceSecond.visibility = INVISIBLE
            }
        }
    }

    /**
     * 完成录音
     */
    fun endRecord(){
        binding?.let {
            it.layoutRecord.visibility = GONE
            it.layoutInRecord.visibility = GONE
            it.layoutInRecordPlay.visibility = VISIBLE
            it.view.visibility = VISIBLE
            it.recordViewPlay.visibility = GONE
            it.ivIcon.setImageResource(R.drawable.icon_sjx)
            it.tvTips.text = "录制成功，点击试听"
            it.tvTips.setTextColorRes(R.color.color_333)
            it.tvVoiceSecond.visibility = VISIBLE
            it.tvVoiceSecond.text = mSecond.toString()+"s"
            it.tvVoiceSecond.setTextStyle(true)
            it.tvVoiceSecond.setTextSizeDip(16f)
        }
    }

    /**
     * 播放录音
     */
    fun playVoice(){
        binding?.let {
            it.layoutRecord.visibility = GONE
            it.layoutInRecord.visibility = GONE
            it.layoutInRecordPlay.visibility = VISIBLE
            it.view.visibility = GONE
            it.recordViewPlay.visibility = VISIBLE
            it.ivIcon.setImageResource(R.drawable.icon_bfz1)
            it.tvTips.text = "试听中..."
            it.tvTips.setTextColorRes(R.color.color_00f3b8)
            it.tvVoiceSecond.visibility = VISIBLE
        }
    }

    fun againRecord(){
        VoiceAudioPlayCore.onStopPlay()
        file?.delete()
        file = null
        mSecond = 0
        start()
    }

    fun toInit(){
        binding?.let {
            it.tvTips.text = "最长录制"+mMaxSecond.toString()+"s，点击开始录制"
            it.layoutRecord.visibility = VISIBLE
            it.layoutInRecord.visibility = GONE
            it.layoutInRecordPlay.visibility = GONE
            it.tvTips.setTextColorRes(R.color.color_333)
            it.tvVoiceSecond.visibility = INVISIBLE
        }
    }

    private fun playVoice2(){
        binding?.let { b->
            file?.let {
                b.recordViewPlay.max = mSecond.toInt()
                VoiceAudioPlayCore.onStartPlay(context, it.path, object : VoiceAudioPlayCore.AudioPlayListener() {
                    override fun onStart(duration: Int) {
                        playVoice()
                    }

                    override fun onResume() {
                        playVoice()
                    }

                    override fun onUpdate(time: Int) {
                        context.run {
                            b.tvVoiceSecond.text = time.toString()+"s"
                            b.recordViewPlay.progress = time
                        }
                    }

                    override fun onPause() {
                        playVoice()
                        b.ivIcon.setImageResource(R.drawable.icon_sjx)
                    }

                    override fun onEnd() {
                        endRecord()
                    }

                    override fun onStop() {
                        endRecord()
                    }

                    override fun onError() {

                    }

                    override fun onPreview() {

                    }
                })
            }
        }

    }

    /**
     * 销毁
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onRecycle() {
        VoiceAudioPlayCore.onStopPlay()
        file?.delete()
        file = null
        mSecond = 0
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if(manager?.recordStatus == AudioRecordManager.STATUS_RECORD){
            stop()
        }
    }


    interface RecordCallback{
        fun recordEnd(outputFile: File,second : Long)
    }
}