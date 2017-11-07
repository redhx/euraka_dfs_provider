package com.dfs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dfs.util.HttpUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FileMoniterService {
	private final static Logger log = LoggerFactory
			.getLogger(FileMoniterService.class);
	
	public static final FileMoniterService service = new FileMoniterService();

	/**
	 * 请求执行服务器进行文件保存
	 * @param serverip
	 * @param filepath
	 * @param filename
	 * @return
	 */
	public String SaveFile(String serverip,String localip, String filepath, String filename,String filecontent) {
		NameValuePair[] params = { 
				new NameValuePair("filepath", filepath),
				new NameValuePair("filename", filename),
				new NameValuePair("filecontent", filecontent)
				};
		//存储地址
		String url = "http://" + serverip + "/action/savefile";
		
		String localurl = "http://" + localip + "/action/savefile";
		
		
		try {
			//指定负载进行存储
			String responResult = HttpUtils.sendPostRequest(url, params);
			if("success".equals(responResult)){
				return "successlvs";
			}else{//失败就本地存储
				try {
					String responResult1 = HttpUtils.sendPostRequest(localurl, params);
					if("success".equals(responResult1)){
						return "successlocal";
					}else{
						return "fail";
					}	
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					log.error("本地节点调用失败:"+e1.getMessage());
					return "fail";
				}
			}			
		} catch (Exception e) {
			log.error("moniter调用savefile失败:"+e.getMessage());
			log.error("采用本地存储节点进行存储");
			//负载存储节点异常，本地存储
			try {
				String responResult1 = HttpUtils.sendPostRequest(localurl, params);
				if("success".equals(responResult1)){
					return "successlocal";
				}else{
					return "fail";
				}	
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.error("本地节点调用失败:"+e1.getMessage());
				return "fail";
			}
					
		}
		
	}

	/**
	 * 请求执行服务器进行文件保存
	 * @param serverip
	 * @param filepath
	 * @param filename
	 * @return
	 */
	public String SaveFileNew(String serverip,String localip, String filepath, String filename,String filecontent,String rootpath) {
		NameValuePair[] params = {
				new NameValuePair("filepath", filepath),
				new NameValuePair("rootpath", rootpath),
				new NameValuePair("filename", filename),
				new NameValuePair("filecontent", filecontent)
		};
		//存储地址
		String url = "http://" + serverip + "/action/savefilenew";

		String localurl = "http://" + localip + "/action/savefilenew";


		try {
			//指定负载进行存储
			String responResult = HttpUtils.sendPostRequest(url, params);
			if("success".equals(responResult)){
				return "successlvs";
			}else{//失败就本地存储
				try {
					String responResult1 = HttpUtils.sendPostRequest(localurl, params);
					if("success".equals(responResult1)){
						return "successlocal";
					}else{
						return "fail";
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					log.error("本地节点调用失败:"+e1.getMessage());
					return "fail";
				}
			}
		} catch (Exception e) {
			log.error("moniter调用savefile失败:"+e.getMessage());
			log.error("采用本地存储节点进行存储");
			//负载存储节点异常，本地存储
			try {
				String responResult1 = HttpUtils.sendPostRequest(localurl, params);
				if("success".equals(responResult1)){
					return "successlocal";
				}else{
					return "fail";
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.error("本地节点调用失败:"+e1.getMessage());
				return "fail";
			}

		}

	}

	public Map ReadFile(String serverip, String filepath) {
		NameValuePair[] params = { 
				new NameValuePair("filepath", filepath)
				};
		String url = "http://" + serverip + "/action/readfile";
		Map<String,String> resultmap=new HashMap();
		
		try {
			String responResult = HttpUtils.sendPostRequest(url, params);
			
			JSONObject json = JSONObject.parseObject(responResult);
			Map<String,String> map= JSON.toJavaObject(json, Map.class);
			
			if("success".equals(map.get("status"))){
				resultmap.put("status", "success");
				resultmap.put("filecontent", map.get("filecontent"));
				
			}else{
				resultmap.put("status", "fail");
				resultmap.put("filecontent", "");
			}			
		} catch (Exception e) {
			log.error("moniter调用readfile失败:"+e.getMessage());
			resultmap.put("status", "fail");
			resultmap.put("filecontent", "");
		}
		return resultmap;
	}

	public Map ReadFileNew(String serverip, String filepath,String rootpath) {
		NameValuePair[] params = {
				new NameValuePair("filepath", filepath),
				new NameValuePair("rootpath", rootpath)
		};
		String url = "http://" + serverip + "/action/readfilenew";
		Map<String,String> resultmap=new HashMap();

		try {
			String responResult = HttpUtils.sendPostRequest(url, params);

			JSONObject json = JSONObject.parseObject(responResult);
			Map<String,String> map= JSON.toJavaObject(json, Map.class);

			if("success".equals(map.get("status"))){
				resultmap.put("status", "success");
				resultmap.put("filecontent", map.get("filecontent"));

			}else{
				resultmap.put("status", "fail");
				resultmap.put("filecontent", "");
			}
		} catch (Exception e) {
			log.error("moniter调用readfile失败:"+e.getMessage());
			resultmap.put("status", "fail");
			resultmap.put("filecontent", "");
		}
		return resultmap;
	}
}
