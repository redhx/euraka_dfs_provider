package com.dfs.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class NumberUtil {

	public static final String ALLCHAR = "0123456789abcdefghijkmnpqrstuvwxyABCDEFGHIJKMNPQRSTUVWXY";


	public static String handleSeqOfTransNo(long seqNo) {
		String transNo = "" + seqNo;
		int length = transNo.length();
		int maxLength = 10;
		if (length < maxLength) {
			StringBuffer delta = new StringBuffer();
			for (int i = 0; i < (maxLength - length); i++) {
				delta.append("0");
			}
			transNo = delta.toString() + transNo;
		} else if (length > maxLength) {
			transNo = transNo.substring(0, maxLength);
		}
		return transNo;
	}

	//生成流水号 19位流水号
	public static String getTransNo() {
		String transNo = ObjectUtils.toString(System.currentTimeMillis());
		transNo = transNo + RandomStringUtils.randomNumeric(6);
		return transNo;
	}
	
	//生成流水号2 18位流水号
	public static String getTransNo2() {
			String transNo = ObjectUtils.toString(System.currentTimeMillis());
			transNo = transNo + RandomStringUtils.randomNumeric(5);
			return transNo;
	}

	// 把字符窜转成24位3des密码
	public static String to3despwd(String str) {
		String resultstr="111111111111111111111111";
		int len = str.length();
		if (len < 24) {
			int tmplen = 24 - len;
			String tmpstr = str.substring(0, tmplen);
			resultstr = str + tmpstr;
		}else if(len==24){
			resultstr=str;
		}else{
			String tmpstr = str.substring(0, 24);
			resultstr=tmpstr;
		}
		return resultstr;
	}

	public static void main(String[] args) {
		String[] serverip={"aaa","bbb"};
		String str = getTransNo();
		long i=Long.parseLong(str)%2;
		System.out.print(serverip[(int)i]);
	}

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerAllString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString().toLowerCase();
	}

	

	
}
