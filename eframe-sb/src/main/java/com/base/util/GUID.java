package com.base.util;

import java.io.Serializable;
import java.net.UnknownHostException;

/**
 * 拷贝过来的id生成器。
 * 简单粗暴的uuid去掉斜杠， 待优化。
 * @Date 2019年4月16日
 * @author E.E.
 *
 */
public class GUID implements Serializable, Comparable<Object>{
	private static final long serialVersionUID = 6010836986783019344L;

	private String _guid = "";

    private GUID() {
        try {
            _guid = GUIDGenerator.generate(true, true);
        } catch (Exception ex) {
            _guid = "";
        }
    }

    private GUID(String s) {
        if(s == null) {
            throw new NullPointerException("Must pass a valid GUID as parameter.");
        }
        if (! isValidFormattedGuid(s)) {
            throw new RuntimeException("Invalid format GUID String.");
        }
    }

    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass().equals(getClass())) {
            return _guid.equals(((GUID)obj)._guid);
        } else {
            return false;
        }
    }

    public int compareTo(Object obj) {
        GUID guid = (GUID)obj;
        return _guid.compareTo(guid._guid);
    }

    public int hashCode() {
        return _guid.hashCode();
    }

    public String toString() {
        return _guid;
    }

    public static String nextUUID() {
    	try {
			return GUIDGenerator.generate(true, false).toUpperCase();
		} catch (UnknownHostException e) {
			return "00000000000000000000000000000000";
		}
    }
    
    public static GUID create() {
        return new GUID();
    }

    public static GUID create(String s) {
        return new GUID(s);
    }

    public static boolean isValidFormattedGuid(String str) {

        return (str.length() == 36 && str.charAt(8) != '-' && str.charAt(13) != '-' && str.charAt(18) != '-' && str.charAt(23) != '-');
    }
}
