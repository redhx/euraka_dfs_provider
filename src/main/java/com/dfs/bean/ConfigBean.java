package com.dfs.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件been
 * @author huangxi
 *
 */
@ConfigurationProperties(prefix="my")
public class ConfigBean {

	private  String rule;
	private  String serverip;
	private  String fileroot;
	private  String savepoint;
	private  String localpoint;
	private  String ipwhitelist;
	private  String crossorgin;

	public  String getCrossorgin() {
		return crossorgin;
	}
	public  void setCrossorgin(String crossorgin) {
		this.crossorgin = crossorgin;
	}

	public  String getRule() {
		return rule;
	}
	public  void setRule(String rule) {
		this.rule = rule;
	}

	public  String[] getServerip() {
		String[] tmp=serverip.split(",");
		return tmp;
	}
	public  String getServerips() {
		return serverip;
	}
	public  void setServerip(String serverip) {
		this.serverip = serverip;
	}

	public  String getFileroot() {
		return fileroot;
	}
	public  void setFileroot(String fileroot) {
		this.fileroot = fileroot;
	}

	public  String[] getSavepoint() {
		String[] tmp=savepoint.split(",");
		return tmp;
	}
	public  String getSavepoints() {
		return savepoint;
	}
	public  void setSavepoint(String savepoint) {
		this.savepoint = savepoint;
	}

	public  String getLocalpoint() {
		return localpoint;
	}
	public  void setLocalpoint(String localpoint) {
		this.localpoint = localpoint;
	}

	public  String getIpwhitelist() {
		return ipwhitelist;
	}
	public  void setIpwhitelist(String ipwhitelist) {
		this.ipwhitelist = ipwhitelist;
	}
	
}
