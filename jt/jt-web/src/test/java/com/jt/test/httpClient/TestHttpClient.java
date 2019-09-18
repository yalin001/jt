package com.jt.test.httpClient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	/**
	 * 1.定位url地址
	 * 2.创建http工具API
	 * 3.定义请求方式  get/post
	 * 4.发起请求,获得响应 response对象
	 * 5.判断服务器返回状态是否正确(200)
	 * 6.获取服务器响应数据 String类型
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void doGet() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com/";
		CloseableHttpClient client = 
						HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = 
						client.execute(get);
		if(200 == response.getStatusLine().getStatusCode()) {
			System.out.println("请求调用成功!!!");
			String result = 
			EntityUtils.toString(response.getEntity(),"UTF-8");
			System.out.println(result);
		}
	}
}
