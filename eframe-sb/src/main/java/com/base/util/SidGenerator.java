package com.base.util;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>版本+mac地址+时间戳+原子自增</p>
 * 
 * <p>位数：2+12 + 8 + 3 一共25位长的字符串</p>
 * 
 * @author liangrl
 * @date 2015年11月19日
 *
 */
public class SidGenerator {
	private static String MAC_ADDRESS = null;
	private static AtomicInteger atomicId = new AtomicInteger(1);
	
	static {
		try {
			MAC_ADDRESS = getMacAddress();
			//System.out.println("初始化sid生成器。mac地址：" + MAC_ADDRESS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 原子自增数字
	 * @return
	 */
	private static int getIncreaseNum() {
		int num = atomicId.getAndIncrement();
		if (num >= 999) {
			atomicId.set(1);
			return atomicId.getAndIncrement();
		}
		return num;
	}
	
	/**
	 * 例如：B8975A625101 12位
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String getMacAddress() throws Exception {
		Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
		while (ni.hasMoreElements()) {
			NetworkInterface netI = ni.nextElement();
			if (null == netI) {
				continue;
			}
			byte[] macBytes = netI.getHardwareAddress();
			if (netI.isUp() && !netI.isLoopback() && null != macBytes && macBytes.length == 6) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0, nLen = macBytes.length; i < nLen; i++) {
					byte b = macBytes[i];
					sb.append(Integer.toHexString((b & 240) >> 4));
					sb.append(Integer.toHexString(b & 15));
					/*
					 * if (i < nLen - 1) { //看起来更好看一点 sb.append("-"); }
					 */
				}
				return sb.toString().toUpperCase();
			}
		}
		return null;
	}
	
	public static String getId() {
		Integer stamp = Integer.parseInt(System.currentTimeMillis()/1000+"");
		
		int increseNum = getIncreaseNum();
		String last = "";	//最后几个位可能填充0
		if(increseNum/100>0){		//百
			last = increseNum+"";
		}else if(increseNum/10>0 && increseNum/1000==0){	//十
			last = "0"+increseNum;
		}else{		//个
			last = "00"+increseNum;
		}
		
		return String.format("%s%s%s%s", "v1", MAC_ADDRESS, Integer.toHexString(stamp), last);
	}
	
	public static void main(String[] args) throws Exception {
		
		for(int i=0; i<10;++i){
			System.out.println(getId());
			Thread.sleep(1000);
		}
	}
	
}
