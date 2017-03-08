package com.cml.androidcmldevelopertools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author：cml on 2017/1/17
 * github：https://github.com/cmlgithub
 */

public class EncryptionUtils {

    /*******************************bitmap url -- > key********************************************/
    public static String hashKeyFormUrl(String url){
        String cacheKey ;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] digest) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if(hex.length() == 1){
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }
    /*******************************bitmap url -- > key********************************************/
}
