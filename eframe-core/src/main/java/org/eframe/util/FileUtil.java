package org.eframe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileUtil {
	
	public static List<String> readLine(File source, String encoding) throws Exception{
		List<String> list = new ArrayList<String>();
		FileInputStream in = null;
		BufferedReader br = null;
		try {
			in = new FileInputStream(source);
			br = new BufferedReader(new InputStreamReader(in, encoding));
			String strLine;
			while ((strLine = br.readLine()) != null)   {
				list.add(strLine);
			}
			
		} finally {
			if(br!=null){
				br.close();
			}
			if(in!=null){
				IOUtils.closeQuietly(in);				
			}
		}
		return list;
	}
}
