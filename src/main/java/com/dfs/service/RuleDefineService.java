package com.dfs.service;

import com.dfs.bean.ConfigBean;
import com.dfs.util.DateUtil;
import com.dfs.util.NumberUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RuleDefineService {


	// 生成文件名
	public static String filenameCalculate() {
		String filename = NumberUtil.getTransNo();
		return filename;
	}

	// 生成文件目录
	public static String filepathCalculate(ConfigBean configBean) {
		String rule = configBean.getRule();
		String fileroot = configBean.getFileroot();
		String filepath = "";
		String time = DateUtil.getDayTime("yyyy|MM|dd|HH|mm", 0);
		String[] times = time.split("\\|");
		String year = times[0];
		String month = times[1];
		String day = times[2];
		String hour = times[3];
		String minute = times[4];
		switch (rule) {
		case "y":
			filepath =  year + "/";
			break;
		case "m":
			filepath =  year + "/" + month + "/";
			break;
		case "d":
			filepath =  year + "/" + month + "/" + day + "/";
			break;
		case "h":
			filepath =  year + "/" + month + "/" + day + "/" + hour
					+ "/";
			break;
		case "i":
			filepath =  year + "/" + month + "/" + day + "/" + hour
					+ "/" + minute + "/";
			break;
		}
		return filepath;
	}

	// 生成服务器IP
	public static Map<String,String> serveripCalculate(String filename,ConfigBean configBean) {
		String[] serverip = configBean.getServerip();
		String[] savepoint = configBean.getSavepoint();

		int servernum = serverip.length;
		// 有指定savepoint就用指定位置的serverip,没有就直接技术使用那个serverip
		Map<String, String> tmpmap = new HashMap<String,String>();
		if (!StringUtils.isEmpty(savepoint[0])) {
			int savepointnum=savepoint.length;
			if(savepointnum==1){
				tmpmap.put("serverip", serverip[Integer.parseInt(savepoint[0])]);
				tmpmap.put("serverposition", savepoint[0]);
			}else{
				long i = Long.parseLong(filename) % savepointnum;
				tmpmap.put("serverip", serverip[Integer.parseInt(savepoint[(int) i])]);
				tmpmap.put("serverposition", savepoint[(int) i]);
			}
		} else {			
			if (servernum == 1) {
				tmpmap.put("serverip", serverip[0]);
				tmpmap.put("serverposition", "0");
			} else {
				long i = Long.parseLong(filename) % servernum;
				tmpmap.put("serverip", serverip[(int) i]);
				tmpmap.put("serverposition", Long.toString(i));
			}
		}
		return tmpmap;
	}

}
