package com.tzh.mylibrary.util.voice

import android.content.Context
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend
import com.tzh.mylibrary.util.AppPathManager
import com.tzh.mylibrary.util.sound.reName
import com.tzh.mylibrary.util.toDefault
import java.io.File

class VoiceFileDownloadHelper(var context : Context) {

    private var downloadTask: DownloadTask? = null

    /**
     * 下载中的文件后缀
     */
    private val FILE_TMP = ".tmp"

    /**
     * 下载完成的文件后缀
     */
    private val FILE_OK = ".mp3"

    /**
     * 下载
     */
    fun onDownloadFile(url: String?, saveFileName: String?,listener: OnDownloadListener?) {

        if (url.isNullOrEmpty()) return
        getVoiceCacheFolder()?.let {
            if (!it.exists().toDefault(false) ) {
                AppPathManager.ifFolderExit(it.absolutePath)
            }
            if (saveFileName.isNullOrEmpty()) return

            var saveName = saveFileName.substring(url.lastIndexOf("/")+1)
            saveName = if(saveName.indexOf(FILE_OK)>0)saveName.replace(FILE_OK,FILE_TMP) else saveName + FILE_TMP

            if (downloadTask != null && StatusUtil.getStatus(downloadTask!!) == StatusUtil.Status.RUNNING) {
                return
            }
            //如果这个url有存在的tmp预下载文件，则需要删除,避免文件冲突
            val tmpFile = File(it.absolutePath + "/" + saveName)
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            //下载文件时为了防止文件下到一半损坏，需要在下载时，给文件命名加".tmp"，下载完成后再改名去掉.tmp
            downloadTask = DownloadTask.Builder(url, it)
//            .setFilename(dto.version_name + ".apk") // the minimal interval millisecond for callback progress
                    .setFilename(saveName) // the minimal interval millisecond for callback progress
                    .setMinIntervalMillisCallbackProcess(30) // do re-download even if the task has already been completed in the past.
                    .setPassIfAlreadyCompleted(false)
                    .setMinIntervalMillisCallbackProcess(100)
                    //不能分块，在android 10以上会出问题
                    .setConnectionCount(1)
                    .build()

            downloadTask?.enqueue(object : DownloadListener4WithSpeed() {
                override fun taskStart(task: DownloadTask) {
                    listener?.onStart()
                }

                override fun connectStart(task: DownloadTask, blockIndex: Int, requestHeaderFields: MutableMap<String, MutableList<String>>) {}

                override fun connectEnd(task: DownloadTask, blockIndex: Int, responseCode: Int, responseHeaderFields: MutableMap<String, MutableList<String>>) {}

                override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, taskSpeed: SpeedCalculator) {
                    when (cause) {
                        EndCause.COMPLETED -> {
                            if (task.file != null && task.file?.exists().toDefault(false)) {
                                val newFile = task.file.reName(task.file?.name?.replace(FILE_TMP, FILE_OK))
                                newFile?.let { file ->
                                    listener?.onSuccess(file)
                                }
                            } else {
                                listener?.onError(Throwable("文件下载失败"))
                            }
                        }
                        EndCause.SAME_TASK_BUSY -> {
                            if (task.file != null && task.file?.exists().toDefault(false)) {
                                val newFile = task.file.reName(task.file?.name?.replace(FILE_TMP, FILE_OK))
                                newFile?.let { file ->
                                    listener?.onSuccess(file)
                                }
                            } else {
                                listener?.onError(Throwable("文件下载失败"))
                            }
                        }
                        EndCause.ERROR -> {
                            listener?.onError(realCause.toDefault(Throwable("未知异常")))
                        }
                        EndCause.CANCELED -> {

                        }
                        EndCause.FILE_BUSY -> {}
                        EndCause.PRE_ALLOCATE_FAILED -> {}
                    }
                }


                override fun infoReady(task: DownloadTask, info: BreakpointInfo, fromBreakpoint: Boolean, model: Listener4SpeedAssistExtend.Listener4SpeedModel) {}

                override fun progressBlock(task: DownloadTask, blockIndex: Int, currentBlockOffset: Long, blockSpeed: SpeedCalculator) {}


                override fun progress(task: DownloadTask, currentOffset: Long, taskSpeed: SpeedCalculator) {
                    val percent = (currentOffset.toFloat() / task.info?.totalLength.toDefault(0) * 100).toInt()
                    listener?.onProgress(percent.toString())
                }

                override fun blockEnd(task: DownloadTask, blockIndex: Int, info: BlockInfo?, blockSpeed: SpeedCalculator) {}
            })
        }
    }

    fun cancel() {
        downloadTask?.cancel()
    }

    abstract class OnDownloadListener {
        open fun onStart() {}
        abstract fun onSuccess(file: File)
        abstract fun onError(throwable: Throwable)

        /**
         * 进度 百分比
         */
        open fun onProgress(percent: String) {}
    }

    /**
     * 获取 语音文件下载目录
     *
     */
    fun getVoiceCacheFolder(): File? {
        val file = context.applicationContext.getExternalFilesDir("mVoice/")
        AppPathManager.ifFolderExit(file?.absolutePath)
        return file
    }

    fun isHaveFile(url: String) : Boolean{
        val saveName = url.substring(url.lastIndexOf("/")+1)
        val localFile = File(getVoiceCacheFolder()?.absolutePath + "/" +(if(saveName.indexOf(FILE_OK)>0)saveName else saveName + FILE_OK))
        return localFile.exists()
    }

    fun getPath(url: String):String{
        val mLocal = getVoiceCacheFolder()?.absolutePath
        val mUrl = url.substring(url.lastIndexOf("/")+1)
        var url2 = "$mLocal/$mUrl"
        url2 = if(url2.indexOf(FILE_OK)>0)url2 else url2 + FILE_OK
        return File(url2).absolutePath
    }
}