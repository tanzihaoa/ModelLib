package com.tzh.mylibrary.util.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SoundPlayer(mContext: Context) {

    private val helper by lazy {
        VoiceFileDownloadHelper(mContext)
    }

    private val soundPool: SoundPool by lazy {
        SoundPool.Builder().apply {
            this.setMaxStreams(1)
            this.setAudioAttributes(AudioAttributes.Builder().apply {
                setLegacyStreamType(AudioManager.STREAM_MUSIC)
            }.build())
        }.build()
    }

    fun init(){

    }

    private var voiceId = 0

    /**
     * 播放音乐
     * @param url 音频地址
     * @param isLoop 是否循环播放
     * @param volume 声音
     */
    fun play(url : String,isLoop : Boolean = false,volume : Float = 1f){
        if(helper.isHaveFile(url)){
            playVoice(url,isLoop,volume)
        }else{
            helper.onDownloadFile(url,url,object : VoiceFileDownloadHelper.OnDownloadListener() {
                override fun onStart() {
                    //"下载中(0%)"
                }

                override fun onProgress(percent: String) {
                    //"下载中($percent%)"
                }

                override fun onSuccess(file: File) {
                    playVoice(url,isLoop,volume)
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

    private fun playVoice(url : String,isLoop : Boolean,volume : Float = 1f){
        voiceId = soundPool.load( helper.getPath(url),1)
        val loop = if(isLoop) -1 else 0

        soundPool.setOnLoadCompleteListener { soundPool, i, i2 ->
            run {
                soundPool.play(voiceId, volume, volume, 1, loop, 1f)
            }
        }
    }

    fun pause(){
        soundPool.autoPause()
    }

    fun stop(){
        soundPool.stop(voiceId)
    }

    fun reStar(){
        soundPool.resume(voiceId)
    }
}