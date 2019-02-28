package com.base.util;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class GUIDGenerator {
    private static SecureRandom _secureRand;
    private static Random _rand;
    private static MessageDigest messagedigest;
    
    static {
        _secureRand = new SecureRandom();
        long l = _secureRand.nextLong();
        _rand = new Random(l);
        try {
        	messagedigest = MessageDigest.getInstance("MD5");
        } catch(NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new RuntimeException(nosuchalgorithmexception);
        }

    }

    public static String generate() {
        try {
            return generate(true, false);
        } catch (UnknownHostException e) {
            return "00000000000000000000000000000000";
        }
    }
    
    public static String generate(boolean secure, boolean sep) throws UnknownHostException {
    	String uuid = UUID.randomUUID().toString();
    	if (!sep) {
    		uuid = uuid.replaceAll("-", "");
    	}
    	return uuid;
    }
    
    public static void main(String args[]) throws Exception {
        for (int i=0; i<10; i++) {
            System.out.println("RandomGUID = " + generate().toUpperCase());
        }

        System.out.println(generate().length());
    }
}
