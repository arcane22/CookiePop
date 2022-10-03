package com.cookiefeeder.cookiepop;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto
{
    private final String CHARSET_NAME = "UTF-8";
    private final String HASH_SHA256 = "SHA-256";
    private final String HASH_SHA512 = "SHA-512";

    public String hashing(String data, String algorithm)
    {
        String result = "";
        MessageDigest messageDigest = null;
        try
        {
            if(algorithm.equals("sha256"))
            {
                messageDigest = MessageDigest.getInstance(HASH_SHA256);
                messageDigest.reset();
                messageDigest.update(data.getBytes(CHARSET_NAME));
                result = String.format("%064x", new java.math.BigInteger(1, messageDigest.digest()));
            }
            else if(algorithm.equals("sha512"))
            {
                messageDigest = MessageDigest.getInstance(HASH_SHA512);
                messageDigest.reset();
                messageDigest.update(data.getBytes(CHARSET_NAME));
                result = String.format("%0128x", new java.math.BigInteger(1, messageDigest.digest()));
            }
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}