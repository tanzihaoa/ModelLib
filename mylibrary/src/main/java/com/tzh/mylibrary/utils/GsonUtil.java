package com.tzh.mylibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static final Gson GSON = new Gson();

    private static final JsonParser PARSER = new JsonParser();


    private GsonUtil() {
    }


    public static Gson getGSON() {
        return GSON;
    }


    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (GSON != null) {
            gsonString = GSON.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        t = GSON.fromJson(gsonString, cls);
        return t;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> GsonToList(String json, Class<T> cls) {
        Gson GSON = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(GSON.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (GSON != null) {
            list = GSON.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (GSON != null) {
            map = GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static LinkedHashMap<String, String> GsonToLinkedHashMap(String gsonString) {
        LinkedHashMap<String, String> map = null;
        if (GSON != null) {
            map = GSON.fromJson(gsonString, new TypeToken<LinkedHashMap<String, String>>() {
            }.getType());
        }
        return map;
    }


    /**
     * 读取Json文件
     *
     * @author: 小嵩
     * @date: 2017/3/16 16:22
     */

    public static String getJsonFromFile(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static <T> List<T> jsonArrayStringToList(String jsonArrayStr, Class<T> elementCls) {
        List<T> result = new ArrayList<T>();
        try {
            JsonArray array = stringToJsonArray(jsonArrayStr);
            for (JsonElement element : array) {
                result.add(GSON.fromJson(element, elementCls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String objectToJsonString(Object obj) {
        try {
            return GSON.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List jsonArrayStringToList(String jsonArrayStr, Type type) {
        List result = new ArrayList();
        try {
            JsonArray array = stringToJsonArray(jsonArrayStr);
            for (JsonElement element : array) {
                result.add(GSON.fromJson(element, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JsonArray stringToJsonArray(String str) {
        try {
            return PARSER.parse(str).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonStringToObject(String jsonStr, TypeToken<T> token) {
        try {
            return GSON.fromJson(jsonStr, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonStringToObject(String jsonStr, Class<T> objCls) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        try {
            return GSON.fromJson(jsonStr, objCls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T jsonStringToObject(String jsonStr, Type type) {
        try {
            return GSON.fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
