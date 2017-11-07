package com.dfs.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpUtils {
	public static String sendPostRequest(String url, NameValuePair[] params)
			throws Exception {
		HttpClient httpClient = new HttpClient();
		// 连接超时，毫秒
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(1000);
		// 读取数据超时，毫秒
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(4000);

		PostMethod postMethod = new Utf8PostMethod(url);
		// postMethod.setRequestHeader("Content-Type",
		// "text/xml; charset=utf-8");
		postMethod.setRequestHeader("Connection", "close");
		postMethod.setRequestBody(params);

		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								postMethod.getResponseBodyAsStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				return stringBuffer.toString();
			} else {
				throw new IOException("HttpClient response status is: "
						+ statusCode);
			}
		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(1000);
		}
	}

	/**
	 * 发送multipart文件
	 */
	public static String sendPostMultipartRequest(String url, Part[] parts)
			throws Exception {
		HttpClient httpClient = new HttpClient();
		// 连接超时，毫秒
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(1000);
		// 读取数据超时，毫秒
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);

		PostMethod postMethod = new Utf8PostMethod(url);

		postMethod.setRequestEntity(new MultipartRequestEntity(parts,
				postMethod.getParams()));
		try {
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								postMethod.getResponseBodyAsStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String str = "";
				while ((str = reader.readLine()) != null) {
					stringBuffer.append(str);
				}
				return stringBuffer.toString();
			}else{
				throw new IOException("HttpClient response status is: "
						+ statusCode);
			}
		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(1000);
		}
	}
}

/**
 * 重写PostMethod以解决UTF-8编码问题
 */
class Utf8PostMethod extends PostMethod {
	public Utf8PostMethod(String url) {
		super(url);
	}

	@Override
	public String getRequestCharSet() {
		return "UTF-8";
	}
}
