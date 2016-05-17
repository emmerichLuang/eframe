package org.eframe.rpcAccess.encypt;

import java.util.HashMap;
import java.util.Map;

import org.eframe.rpcAccess.encypt.behavior.IEncoder;
import org.eframe.rpcAccess.encypt.behavior.impl.BASE64;
import org.eframe.rpcAccess.encypt.behavior.impl.MD5;
import org.eframe.rpcAccess.encypt.behavior.impl.RSA;
import org.eframe.rpcAccess.encypt.constant.EnCoderType;

/**
 * 根据参数构造不同的加解密方式
 * @author liangrl
 * @date   2016年5月17日
 *
 */
public class EncodeFactory {
	
	private static Map<EnCoderType, IEncoder> encoders = new HashMap<EnCoderType, IEncoder>();
	
	static{
		encoders.put(EnCoderType.BASE64, new BASE64());
		encoders.put(EnCoderType.MD5, new MD5());
		encoders.put(EnCoderType.RSA, new RSA());
	}

	
	public static IEncoder getEncoder(EnCoderType et){
		return encoders.get(et);
	}
}
