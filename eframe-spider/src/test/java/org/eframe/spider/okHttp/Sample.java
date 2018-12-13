package org.eframe.spider.okHttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Sample {

	static String url = "https://github.com/b3log/solo/blob/master/README.md";

	public static void main(String[] args) throws Exception {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		
		String content = response.body().string();
		
		System.err.println(content);
		
		
	}

}
