package com.tzh.mylibrary.util.voice

import android.content.Context
import android.media.MediaPlayer
import com.tzh.mylibrary.util.sound.VoiceFileDownloadHelper
import com.tzh.mylibrary.util.toDefault
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MusicPlayer(context: Context) {

    private val helper by lazy {
        VoiceFileDownloadHelper(context)
    }

    /**
     * 是否暂停
     */
    private var isPause = false

    private var mUrl = ""

    private var mediaPlayer : MediaPlayer ?= null

    fun init(){

    }


    /**
     * 播放音乐
     * @param url 音频地址
     * @param isLoop 是否循环播放
     */
    fun play(url : String,isLoop : Boolean){
        if(helper.isHaveFile(url)){
            playVoice(url,isLoop)
        }else{
            helper.onDownloadFile(url,url,object : VoiceFileDownloadHelper.OnDownloadListener() {
                override fun onStart() {
                    //"下载中(0%)"
                }

                override fun onProgress(percent: String) {
                    //"下载中($percent%)"
                }

                override fun onSuccess(file: File) {
                    playVoice(url,isLoop)
                }

                override fun onError(throwable: Throwable) {
                    when(throwable){
                        is ConnectException, is NullPointerException, is SocketTimeoutException, is UnknownHostException -> {
                            //"网络错误"
                        }
                        else ->{
                            //"下载错误"
                        }

                    }
                }
            })
        }
    }

    private fun playVoice(url : String,isLoop : Boolean){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }

        if(mUrl == helper.getPath(url) && mediaPlayer!!.isPlaying){
            mediaPlayer?.pause()
            return
        }

        if(mediaPlayer!!.isPlaying){
            mediaPlayer!!.stop()
        }


        if(mUrl == helper.getPath(url)){
            mediaPlayer?.start()
        }else{
            mUrl = helper.getPath(url)
            mediaPlayer?.isLooping = isLoop
            mediaPlayer?.setDataSource(helper.getPath(url))

            mediaPlayer?.setOnPreparedListener {
                mediaPlayer?.start()
            }
            mediaPlayer?.setOnCompletionListener {
                stop()
            }
            mediaPlayer?.setOnErrorListener { p0, p1, p2 ->
                stop()
                playVoice(url,isLoop)
                false
            }
            mediaPlayer?.prepareAsync()
        }
    }

    fun stop(){
        mUrl= ""
        isPause = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * 暂停
     */
    fun pause(){
        if(mediaPlayer?.isPlaying.toDefault(false)){
            isPause = true
            mediaPlayer?.pause()
        }
    }

    /**
     * 播放
     */
    fun start(){
        if(isPause){
            isPause = false
            mediaPlayer?.start()
        }
    }

    fun isPause() : Boolean{
        return isPause
    }
}