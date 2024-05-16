package com.tzh.mylibrary.util.voice

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.tzh.mylibrary.util.LogUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

object VoiceAudioPlayCore {
    private var TAG = "VoiceAudioPlayCore"

    /**
     * 播放中
     */
    const val STATUS_PLAYING = 1

    /**
     * 播放暂停
     */
    const val STATUS_PAUSE = 2

    /**
     * 播放停止
     */
    const val STATUS_STOP = 3

    /**
     * 加载中
     */
    const val STATUS_LOADING = 4

    /**
     * 音乐播放状态
     */
    private var mPlayStatus = STATUS_STOP

    /**
     * 进度计时
     */
    private var mTimeDisposable: Disposable? = null


    var mListener: AudioPlayListener?= null

    /**
     * 是否需要暂停其他地方(其他app、播放器)播放音乐
     */
    private var isPauseMusic = false

    /**
     * audiomanager
     */
    private var mAudioManager: AudioManager? = null

    var mUrl : String?=null

    /**
     * 进度时间处理
     */
    private fun onSeekTime() {
        mTimeDisposable?.dispose()
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable) {
                        mTimeDisposable = d
                    }

                    override fun onNext(i: Long) {
                        mMediaPlayer.let {
                            if (mPlayStatus == STATUS_PLAYING) {
                                mListener?.onUpdate(OperationUtil.div((getPlayProgress()).toString(),"1000",0).toInt())
                            }
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }

    private val mMediaPlayer by lazy {
        MediaPlayer().apply {
            isLooping = true
            setOnPreparedListener {
                LogUtils.d(TAG, "OnPrepared")
                //在异步加载的时候，就被停止了播放
                if (mPlayStatus == STATUS_STOP) {
                    return@setOnPreparedListener
                }
                onMediaStart()
                onSeekTime()
                mListener?.onStart(it.duration)
            }
            setOnCompletionListener {
                onStopPlay()
                mListener?.onEnd()
            }

            setOnErrorListener { mp, what, extra ->
                LogUtils.d(TAG, "OnError")
                onStopPlay()
                mp.reset()
                mListener?.onError()
                false
            }
        }
    }

    /**
     * 在onStart监听里面使用
     * 必须要是 预览成功 后才能使用
     */
    fun onMediaStart() {
        mMediaPlayer.start()
        mPlayStatus = STATUS_PLAYING
        //确定播放，状态应该是 继续播放
        mListener?.onResume()
    }

    /**
     * 获取当前进度
     */
    fun getPlayProgress(): Int {
        return if (mPlayStatus != STATUS_STOP) {
            mMediaPlayer.currentPosition
        } else {
            0
        }
    }

    /**
     * 获取总进度
     */
    fun getPlaySumProgress(): Int {
        return if (mPlayStatus != STATUS_STOP) {
            mMediaPlayer.duration
        } else {
            0
        }
    }


    /**
     * 获取播放状态
     */
    fun getPlayStatus(): Int {
        return mPlayStatus
    }

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean {
        return mPlayStatus == STATUS_PLAYING
    }

    /**
     * 是否正在播放
     */
    fun isPlaying(url: String): Boolean {
        return mPlayStatus == STATUS_PLAYING && mUrl ==url
    }

    /**
     * 开始播放
     * todo 退出发生错误时，再次播放问题
     * @param isNewAudio 是否播放新的audio
     */
    fun onStartPlay(context: Context?,url : String,listener: AudioPlayListener) {
        if (context == null || url.isEmpty()) {
            return
        }
        val isNewAudio = url != mUrl
        mUrl = url
        if (isNewAudio) {
            onStopPlay()
//            mMediaPlayer.release()
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(mUrl)
        }
        mListener = listener
        if (mPlayStatus == STATUS_PAUSE) {
            mPlayStatus = STATUS_PLAYING
            mMediaPlayer.start()
            LogUtils.d(TAG, "onResume")
            mListener?.onResume()
        }else if(!isNewAudio && mPlayStatus == STATUS_PLAYING){
            onPausePlay()
        } else {
            LogUtils.d(TAG, "onStart")

            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioManager?.let {
                //先判断后台是否再播放音乐
                if (it.isMusicActive) {
                    it.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    isPauseMusic = true
                }
            }
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(mUrl)
            mMediaPlayer.prepareAsync()
            mPlayStatus = STATUS_LOADING
            mListener?.onPreview()
        }
    }


    /**
     * 暂停播放
     */
    fun onPausePlay() {
        LogUtils.d(TAG, "onPause")
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.pause()
        }
        mPlayStatus = STATUS_PAUSE
        mListener?.onPause()
    }

    /**
     * 停止播放
     */
    fun onStopPlay() {
        LogUtils.d(TAG, "onStop")
        mMediaPlayer.let {
            if (mPlayStatus == STATUS_PLAYING) {
                it.pause()
                it.seekTo(0)
            }
            if (mPlayStatus != STATUS_STOP) {
                it.stop()
            }
            mPlayStatus = STATUS_STOP
            mListener?.onStop()
        }
        mAudioManager?.let {
            //不需要时释放焦点，音乐播放就会继续
            if (isPauseMusic) {
                it.abandonAudioFocus(null)
                isPauseMusic = false
            }
        }
    }

    /**
     * 关闭会话界面时，调用此方法
     */
    fun reset(){
        try {
            onStopPlay()
            mMediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 不要轻易使用
     * release 后  ， mediaplay 在 native 层是为null的，  需要重新生成mediaplay 才行
     */
    fun release() {
        try {
            onStopPlay()
            mMediaPlayer.reset()
            mMediaPlayer.release()
            mAudioManager?.let {
                //不需要时释放焦点，音乐播放就会继续
                if (isPauseMusic) {
                    it.abandonAudioFocus(null)
                    isPauseMusic = false
                }
            }
            mAudioManager = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract class AudioPlayListener {


        open fun onPreview() {
        }

        /**
         * 音频时长
         */
        open fun onStart(duration: Int) {

        }

        open fun onResume() {

        }

        open fun onPause() {

        }

        open fun onStop() {

        }

        open fun onUpdate(time : Int) {

        }

        open fun onError() {

        }

        open fun onEnd() {}
    }
}