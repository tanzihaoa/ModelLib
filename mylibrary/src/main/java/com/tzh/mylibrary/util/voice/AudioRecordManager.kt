package com.tzh.mylibrary.util.voice

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tzh.mylibrary.util.LogUtils
import com.tzh.mylibrary.util.toDefault
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.TimeUnit

class AudioRecordManager(var lifecycle: LifecycleOwner) : LifecycleObserver {

    companion object {

        private const val TAG = "AudioRecordManager"

        /**
         * 录制音频自有格式
         */
        private const val SUFFIX_PCM = ".pcm"

        /**
         * 采样频率
         * 模拟器仅支持从麦克风输入8kHz采样率
         */
        private const val SAMPLE_RATE_IN_HZ_8000 = 8000

        /**
         * 声源用最高采样率
         */
        private const val SAMPLE_RATE_IN_HZ_44100 = 44100

        /**
         * 状态-录制中
         */
        const val STATUS_RECORD = 1

        /**
         * 状态-暂停中
         */
        const val STATUS_PAUSE = 2

        /**
         * 已停止
         */
        const val STATUS_STOP = 3

    }

    /**
     * 录制器
     */
    private var mAudioRecord: AudioRecord? = null

    /**
     * 录制写入线程
     */
    private var mRecordThread: Thread? = null

    /**
     * 文件夹
     */
    private var mDirectoryFile: File? = null

    /**
     * 录音文件
     */
    private var mOutPcmFile: File? = null

    /**
     * 计算 录制的时间
     */
    private var mRecordTimeDisposable: Disposable? = null

    /**
     * 已录制的时间
     */
    var recordProgressTime = 0L
        private set

    /**
     * 录制最长时间 (秒)
     */
    var recordTimeMax = 60 * 60L

    /**
     * 回调
     */
    var callback: RecordCallback? = null

    /**
     * 录制状态
     */
    var recordStatus = STATUS_STOP
        private set


    init {
        lifecycle.lifecycle.addObserver(this)
    }


    /**
     * 开始录音，会保存成一个pcm格式的文件，然后再转换成wav格式的文件
     *
     * @param fileDirectory 保存的wav文件的绝对路径，也可以不带后缀，有根目录和文件名即可
     */
    fun onStartRecord(fileDirectory: String) {
        if (recordStatus == STATUS_STOP) {
            val directory = File(fileDirectory)
            if (!directory.exists()) {
                try {
                    directory.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (!directory.isDirectory) {
                throw RuntimeException("请传入录音文件夹路径")
            }
            mDirectoryFile = directory
            initRecord()
        }
    }

    /**
     * 暂停
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onPause() {
        if (mAudioRecord != null && recordStatus == STATUS_RECORD) {
            recordStatus = STATUS_PAUSE
            mAudioRecord?.stop()
            callback?.onPause()
        }
    }

    /**
     * 继续
     */
    fun onResume() {
        if (recordStatus == STATUS_PAUSE) {
            recordStatus = STATUS_RECORD
            mAudioRecord?.startRecording()
            callback?.onResume()
        }
    }


    /**
     * 停止录制
     */
    fun onStopRecord() {
        recordStatus = STATUS_STOP
        mAudioRecord?.stop()
        mAudioRecord?.release()
        mAudioRecord = null
        callback?.onStop()
        mRecordTimeDisposable?.dispose()
        mRecordTimeDisposable = null
    }

    /**
     * 强制释放，会把线程interrupt掉
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onReleaseRecord() {
        mRecordThread?.interrupt()
        mRecordThread = null
        mAudioRecord?.stop()
        mAudioRecord?.release()
        mAudioRecord = null
        callback?.onStop()
        callback = null
        mRecordTimeDisposable?.dispose()
        mRecordTimeDisposable = null
    }


    /**
     * 初始化 录制器
     */
    private fun initRecord() {
        if (mDirectoryFile == null) {
            return
        }
        mOutPcmFile = File(mDirectoryFile!!.absolutePath + "/" + UUID.randomUUID().toString() + SUFFIX_PCM)

        //缓存size()  单声道
        val minBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ_44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        //创建(音频采集的来源、
        // 音频采样率、
        // 声道、
        // 音频采样精度，指定采样的数据的格式和每次采样的大小、
        // AudioRecord 采集到的音频数据所存放的缓冲区大小、
        // 获取最小的缓冲区大小，用于存放AudioRecord采集到的音频数据)
        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ_44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufSize)
        val buffer = ByteArray(minBufSize)
        LogUtils.i(TAG, "minBufSize = $minBufSize")
        if (AudioRecord.ERROR_BAD_VALUE == minBufSize || AudioRecord.ERROR == minBufSize) {
            callback?.onError(Throwable("Unable to getMinBufferSize"))
            onStopRecord()
            return
        }
        if (mAudioRecord == null) {
            callback?.onError(Throwable("录音尚未初始化,请检查是否禁止了录音权限~"))
            onStopRecord()
            return
        }
        mRecordThread = object : Thread() {
            override fun run() {
                val fileOutputStream = try {
                    FileOutputStream(mOutPcmFile)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    MainHandler.getInstance()?.postRunnable { callback?.onError(Throwable("创建文件失败!")) }
                    return
                }
                //设置线程权限
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
                try {
                    while (recordStatus == STATUS_RECORD || recordStatus == STATUS_PAUSE) {
                        val readSize = mAudioRecord?.read(buffer, 0, minBufSize).toDefault(0)
                        if (readSize > 0) {
                            //写入文件
                            fileOutputStream.write(buffer)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                MainHandler.getInstance()?.postRunnable { callback?.onSuccess(mOutPcmFile) }
            }
        }
        recordStatus = STATUS_RECORD
        mRecordThread?.start()
        mAudioRecord?.startRecording()
        timeCountDown()
        callback?.onStart()
    }


    /**
     * 对录制进行时间计算
     */
    private fun timeCountDown() {
        mRecordTimeDisposable?.dispose()
        recordProgressTime = 0
        mRecordTimeDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_DESTROY)))
                .subscribe {
                    if (recordStatus == STATUS_RECORD) {
                        callback?.onTime(recordProgressTime++)
                        if (recordProgressTime > recordTimeMax) {
                            onStopRecord()
                        }
                    }
                }
    }


    /**
     * 删除 音频文件
     * 每次上传完 音频文件后，应当删除，防止数量过多
     */
    fun deleteAudioFile(audioFile: File?) {
        audioFile?.run {
            if (this.exists()) {
                delete()
            }
        }
    }

    /**
     * pcm 格式转 MP3格式
     */
    fun pcmToMp3(pcmFile: File?) {
        pcmFile ?: return
        if (!pcmFile.exists()) {
            return
        }
        if (!pcmFile.isFile) {
            return
        }
        Observable.create(ObservableOnSubscribe<File> {
            if (pcmFile.parentFile == null) {
                it.onError(Throwable("文件错误"))
            } else {
                // val outFile = File((pcmFile.parentFile!!.absolutePath) + "/" + System.currentTimeMillis() + ".mp3")
                it.onNext(PcmToMp3Util.pcmToMp3(pcmFile))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_DESTROY)))
                .subscribe({
                    callback?.onPcmToMp3(it)
                }, {
                    callback?.onError(it)
                })

    }


    /**
     * 大小端字节转换
     * 因为安卓字节是小端排序，lame是大端排序，所以需要转换
     * @param fileName   //filename为pcm文件，请自行设置
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun bigtolittle(file: File): File {
        val inputStream = FileInputStream(file)
        val bytes = ByteArray(inputStream.available())
        //inputStream.available()是得到文件的字节数
        var length = bytes.size
        while (length != 1) {
            val i = inputStream.read(bytes, 0, bytes.size).toLong()
            if (i == -1L) {
                break
            }
            length -= i.toInt()
        }
        val byteBuffer = ByteBuffer.wrap(bytes, 0, bytes.size)
        val shortBuffer = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer() //此处设置大小端
        val shorts = ShortArray(bytes.size / 2)
        shortBuffer[shorts, 0, bytes.size / 2]
        val pcmtem = File.createTempFile("pcm", null) //输出为临时文件
        val fos1 = FileOutputStream(pcmtem)
        val bos1 = BufferedOutputStream(fos1)
        val dos1 = DataOutputStream(bos1)
        for (i in shorts.indices) {
            dos1.writeShort(shorts[i].toInt())
        }
        dos1.close()
        LogUtils.d("gg", "bigtolittle: " + "=" + shorts.size)
        return pcmtem
    }

    /**
     * @param src    待转换文件路径
     * @param target 目标文件路径
     * @throws IOException 抛出异常
     */
    @Throws(IOException::class)
    fun convertAudioFiles(src: File, target: File): File {
        var fis = FileInputStream(src)
        val fos = FileOutputStream(target)

        //计算长度
        val buf = ByteArray(1024 * 4)
        var size = fis.read(buf)
        var PCMSize = 0
        while (size != -1) {
            PCMSize += size
            size = fis.read(buf)
        }
        fis.close()

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        val header = WaveHeader()
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8)
        header.FmtHdrLeth = 16
        header.BitsPerSample = 16
        header.Channels = 1
        header.FormatTag = 0x0001
        header.SamplesPerSec = 16000 //正常速度是8000，这里写成了16000，速度加快一倍
        header.BlockAlign = (header.Channels * header.BitsPerSample / 8).toShort()
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec
        header.DataHdrLeth = PCMSize
        val h = header.getHeader()
        assert(
                h.size == 44 //WAV标准，头部应该是44字节
        )
        //write header
        fos.write(h, 0, h.size)
        //write data stream
        fis = FileInputStream(src)
        size = fis.read(buf)
        while (size != -1) {
            fos.write(buf, 0, size)
            size = fis.read(buf)
        }
        fis.close()
        fos.close()
        println("PCM Convert to MP3 OK!")
        return target
    }


    /**
     * pcm 格式转 MP3格式
     */
    fun pcmToMp3222(pcmFile: File?) {
        pcmFile ?: return
        if (!pcmFile.exists()) {
            return
        }
        if (!pcmFile.isFile) {
            return
        }
        Observable.create(ObservableOnSubscribe<File> {
            if (pcmFile.parentFile == null) {
                it.onError(Throwable("文件错误"))
            } else {
//                val outFile = File((pcmFile.parentFile!!.absolutePath) + "/" + System.currentTimeMillis() + ".mp3")
                it.onNext(PcmToMp3Util.pcmToMp3(pcmFile))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_DESTROY)))
                .subscribe({
                    callback?.onPcmToMp3(it)
                }, {
                    callback?.onError(it)
                })

    }


    /**
     * pcm 格式转 MP3格式
     */
    fun pcmToMp333(pcmFile: File?) {
        pcmFile ?: return
        if (!pcmFile.exists()) {
            return
        }
        if (!pcmFile.isFile) {
            return
        }
        Observable.create(ObservableOnSubscribe<File> {
            if (pcmFile.parentFile == null) {
                it.onError(Throwable("文件错误"))
            } else {
                val outFile = File((pcmFile.parentFile!!.absolutePath) + "/" + System.currentTimeMillis() + ".mp3")
                it.onNext(PcmToMp3Util.convertAudioFiles(pcmFile, outFile))
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle, Lifecycle.Event.ON_DESTROY)))
                .subscribe({
                    callback?.onPcmToMp3(it)
                }, {
                    callback?.onError(it)
                })

    }


    interface RecordCallback{
        /**
         * 录制开始
         */
        fun onStart() {}

        /**
         * 计时
         *
         * @param second 秒
         */
        fun onTime(second: Long) {}

        /**
         * 录制暂停
         */
        fun onPause() {}

        /**
         * 录制继续
         */
        fun onResume() {}

        /**
         * 录制停止
         */
        fun onStop() {}

        /**
         * 录制完成
         *
         * @param outputFile pcm 文件
         */
        fun onSuccess(outputFile: File?)

        /**
         * 录制错误
         *
         * @param t error
         */
        fun onError(t: Throwable?)

        /**
         * pcm 转化成 mp3 成
         */
        fun onPcmToMp3(outputFile: File?) {}
    }
}