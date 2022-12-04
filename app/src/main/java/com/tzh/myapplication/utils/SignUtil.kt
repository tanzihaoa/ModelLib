package com.tzh.myapplication.utils

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

object SignUtil {
    fun byteBufferToString(buffer: ByteBuffer): String? {
        var charBuffer: CharBuffer? = null
        return try {
            val charset = Charset.forName("UTF-8")
            val decoder = charset.newDecoder()
            charBuffer = decoder.decode(buffer)
            buffer.flip()
            charBuffer.toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun onSign(requestParams: LinkedHashMap<String, Any?>): String {
        val requestMap = requestParams.clone() as LinkedHashMap<String, Any>

//        Log.e("onSignByString==",new Gson().toJson(requestParams));
        requestMap["_key"] = "1234a0e116225678"
        val paramsSort = sortMapByKey(requestMap)
        val paramsSign = sign(paramsSort)

//        Log.e("paramsSort==",new Gson().toJson(paramsSort));
        //md5加密
        val md5 = encryption(paramsSign).uppercase(Locale.getDefault())
        return md5
    }

    fun onSignByString(requestParams: LinkedHashMap<String, String?>): String {
//        Log.e("onSignByString==",new Gson().toJson(requestParams));
        val requestMap = requestParams.clone() as LinkedHashMap<String, String>
        requestMap["_key"] = "1234a0e116225678"
        val paramsSort = sortMapByKeyByString(requestMap)
        val paramsSign = signMap(paramsSort)

//        Log.e("paramsSort==",new Gson().toJson(paramsSort));
        //md5加密
        val md5 = encryption(paramsSign).uppercase(Locale.getDefault())
        return md5
    }

    /**
     * @param plainText 明文
     * @return 32位密文
     */
    fun encryption(plainText: String): String {
//        Log.e("加密前===",plainText);
        var md5 = String()
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(plainText.toByteArray())
            val b = md.digest()
            var i: Int
            val buf = StringBuffer("")
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            md5 = buf.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //        Log.e("加密后===",md5);
//        Log.e("转大写===",md5.toUpperCase());
        return md5
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    fun sortMapByKey(map: Map<String, Any>?): Map<String, Any>? {
        if (map == null || map.isEmpty()) {
            return null
        }
        val sortMap: MutableMap<String, Any> = TreeMap { obj: String, anotherString: String? ->
            obj.compareTo(
                anotherString!!
            )
        }
        sortMap.putAll(map)
        return sortMap
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    fun sortMapByKeyByString(map: Map<String, String>?): Map<String, String>? {
        if (map == null || map.isEmpty()) {
            return null
        }
        val sortMap: MutableMap<String, String> = TreeMap { obj: String, anotherString: String? ->
            obj.compareTo(
                anotherString!!
            )
        }
        sortMap.putAll(map)
        return sortMap
    }

    /**
     * 签名
     */
    fun sign(map: Map<String, Any>?): String {
        //System.out.println("排序前：" + map);
        var map = map
        map = sortMapByKey(map)
        //System.out.println("排序后：" + map);
        val list: MutableList<Any> = ArrayList()
        var str = ""
        val iter = map!!.keys.iterator()
        while (iter.hasNext()) {
            val key = iter.next()
            val value = map[key]
            list.add("$key=$value")
        }
        val leng = list.size - 1
        for (i in 0 until leng) {
            str += list[i].toString() + "&" //拼索要加密的字符串格式
        }
        str += list[leng]
        //System.out.println("要加密的字符串:" + str);//所要加密的字符串
        return str
    }

    /**
     * 签名
     */
    fun signMap(map: Map<String, String>?): String {
        //System.out.println("排序前：" + map);
        var map = map
        map = sortMapByKeyByString(map)
        //System.out.println("排序后：" + map);
        val list: MutableList<Any> = ArrayList()
        var str = ""
        val iter = map!!.keys.iterator()
        while (iter.hasNext()) {
            val key = iter.next()
            val value: Any? = map[key]
            list.add("$key=$value")
        }
        val leng = list.size - 1
        for (i in 0 until leng) {
            str += list[i].toString() + "&" //拼索要加密的字符串格式
        }
        str += list[leng]
        //System.out.println("要加密的字符串:" + str);//所要加密的字符串
        return str
    }
}
