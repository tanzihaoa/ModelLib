package com.tzh.mylibrary.util.voice;

import android.media.AudioFormat;

import com.czt.mp3recorder.util.LameUtil;
import com.tzh.mylibrary.util.LogUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class PcmToMp3Util {
    private static final String TAG = "PcmToMp3Util";

    /**
     * 大小端字节转换
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String bigtolittle(String fileName) throws IOException {

        File file = new File(fileName);    //filename为pcm文件，请自行设置

        InputStream in = null;
        byte[] bytes = null;
        in = new FileInputStream(file);
        bytes = new byte[in.available()];//in.available()是得到文件的字节数
        int length = bytes.length;
        while (length != 1) {
            long i = in.read(bytes, 0, bytes.length);
            if (i == -1) {
                break;
            }
            length -= i;
        }

        int dataLength = bytes.length;
        int shortlength = dataLength / 2;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, dataLength);
        ShortBuffer shortBuffer = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();//此处设置大小端
        short[] shorts = new short[shortlength];
        shortBuffer.get(shorts, 0, shortlength);
        File file1 = File.createTempFile("pcm", null);//输出为临时文件
        String pcmtem = file1.getPath();
        FileOutputStream fos1 = new FileOutputStream(file1);
        BufferedOutputStream bos1 = new BufferedOutputStream(fos1);
        DataOutputStream dos1 = new DataOutputStream(bos1);
        for (int i = 0; i < shorts.length; i++) {
            dos1.writeShort(shorts[i]);

        }
        dos1.close();
        LogUtils.d("gg", "bigtolittle: " + "=" + shorts.length);
        return pcmtem;
    }

    public static String path;


    /**
     * pcm 转 MP3
     * 因为安卓字节是小端排序，lame是大端排序，所以需要转换
     * 自带转换
     */
    public static File pcmToMp3(File file) throws Exception {
        File outFile = new File(file.getParentFile().getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
        FileInputStream ins = new FileInputStream(file);
        byte[] bbytes = new byte[(int) file.length()];
        int readBytes = ins.read(bbytes);
        ins.close();

        LogUtils.i(TAG, "读取pcm数据流，大小为：" + readBytes);

        //录音用16K采用，单声道，16位
        //读取PCM文件到bbytes[]
        int count = bbytes.length / 2;        //16位数组的长度
        short[] data = new short[count];
        for (int i = 0; i < count; i++) {
            data[i] = (short) (bbytes[i * 2] & 0xff | (bbytes[2 * i + 1] & 0xff) << 8);            //需要确认是否是小端模式
        }

        int num = 0;
        float allNum = 0;
        for (int i = 0; i < count; i++) {
            if (data[i] > 1800) {
                allNum = allNum + data[i];
                num = num + 1;
            }
        }

        float multiple;
        if (allNum == 0 || num == 0) {
            multiple = 6;
        } else {
            if (allNum / num < 2500) {
                multiple = (float) (32000 / (allNum / num) / 2.5);
            } else {
                multiple = (float) (32000 / (allNum / num) / 6.5);
            }
            if (multiple < 1) {
                multiple = 1;
            }
        }
        for (int i = 0; i < count; i++) {
            if (Math.abs(data[i]) < ((short) 32000 / multiple)) {
                data[i] = (short) (data[i] * multiple);
            }
        }

        for (int i = 0; i < count; i++) {
            bbytes[i * 2] = (byte) (data[i]);
            bbytes[i * 2 + 1] = (byte) (data[i] >> 8);            //需要确认是否是小端模式
        }

        byte[] bbytes8000 = new byte[count];
        int j = 0;
        for (int i = 0; i < bbytes.length; i++) {
            if ((i % 4) == 0) {
                bbytes8000[j] = bbytes[i];
                bbytes8000[j + 1] = bbytes[i + 1];
                j += 2;
            }
        }

        // 保存bbytes[]到PCM文件
        try {
            // 初始化lame库，配置相关信息
            //  * @param inSampleRate pcm格式音频采样率
            //  * @param outChannel pcm格式音频通道数量
            //  * @param outSampleRate mp3格式音频采样率
            //  * @param outBitRate mp3格式音频比特率
            //  * @param quality mp3格式音频质量，0~9，最慢最差~最快最好
            //
            //这里需要 与 录制的配置 一致
            LameUtil.init(44100, AudioFormat.CHANNEL_IN_MONO, 44100, 32, 5);
            byte[] mp3Bytes = new byte[bbytes.length];

            int lameFlush = LameUtil.encode(data, data, data.length, mp3Bytes);

            int flush = LameUtil.flush(mp3Bytes);


            OutputStream mp3Out;

            try {
                mp3Out = new FileOutputStream(outFile);
                mp3Out.write(mp3Bytes, 0, lameFlush);
                mp3Out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LameUtil.close();
        }

        return outFile;
    }


    private short[] transferByte2Short(byte[] data, int readBytes) {
        // byte[] 转 short[]，数组长度缩减一半
        int shortLen = readBytes / 2;
        // 将byte[]数组装如ByteBuffer缓冲区
        ByteBuffer byteBuffer = ByteBuffer.wrap(data, 0, readBytes);
        // 将ByteBuffer转成小端并获取shortBuffer
        ShortBuffer shortBuffer = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] shortData = new short[shortLen];
        shortBuffer.get(shortData, 0, shortLen);
        return shortData;
    }


    /**
     * @param src    待转换文件路径
     * @param target 目标文件路径
     * @throws IOException 抛出异常
     *                     <p>
     *                     这个转换 后的大小 跟 源文件一致，暂不使用
     */
    public static File convertAudioFiles(File src, File target) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        //计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.setFileLength(PCMSize + (44 - 8));
        header.setFmtHdrLeth(16);
        header.setBitsPerSample((short) 16) ;
        header.setChannels((short) 1);
        header.setFormatTag((short) 0x0001);
        header.setSamplesPerSec(44100);//正常速度是8000，这里写成了16000，速度加快一倍
        header.setBlockAlign((short) (header.getChannels() * header.getBitsPerSample() / 8)) ;
        header.setAvgBytesPerSec(header.getBlockAlign() * header.getSamplesPerSec());
        header.setDataHdrLeth(PCMSize);

        byte[] h = header.getHeader();

        assert h.length == 44; //WAV标准，头部应该是44字节
        //write header
        fos.write(h, 0, h.length);
        //write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        System.out.println("PCM Convert to MP3 OK!");
        return target;
    }
}
