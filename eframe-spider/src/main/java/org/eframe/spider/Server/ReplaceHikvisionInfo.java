package org.eframe.spider.Server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ReplaceHikvisionInfo {

	public static void main(String[] args) throws IOException {
		File dir = new File("E:/hikvision/cn");
		for(File f :dir.listFiles()){
			if(f.isFile() && Clawer.isPattern(f.getName())){
				String fileContent = FileUtils.readFileToString(f, "utf-8");
				
				fileContent = fileContent.replaceAll("src=\"/uploadfile/image/product/small", "src=\"../uploadfile/image/product/small");
				
				fileContent = fileContent.replaceAll("浙ICP备05007700号-1", "粤ICP备16021782号-2");
				fileContent = fileContent.replaceAll("杭州海康威视数字技术股份有限公司 版权所有", "");
				fileContent = fileContent.replaceAll("www.mountor.cn","www.hal123.cn");				
				fileContent = fileContent.replaceAll("Powered by Mountor","Powered by Hal");				
				
				FileUtils.writeStringToFile(f, fileContent, "utf-8");
			}
		}
		
		
	}

}
