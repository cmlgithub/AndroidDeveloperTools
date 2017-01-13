package com.cml.androidcmldevelopertools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * author：cml on 2016/11/30
 * github：https://github.com/cmlgithub
 *
 * gson Utils
 *
 * Gson version 2.2.4
 */
public class GsonUtils {

    private static Gson gson ;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtils() {}

    /**
     *  to json
     */
    public static String objToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * JsonToBean
     */
    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * JsonToList
     */
    public static <T> List<T> jsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * JsonToListMaps(Map in List)
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * jsonToMap
     */
    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
