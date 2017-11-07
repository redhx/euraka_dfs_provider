package com.dfs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileActionService {
	private final static Logger log = LoggerFactory
			.getLogger(FileActionService.class);

	public static final FileActionService service = new FileActionService();

	public Boolean SaveFile(String filepath, String filename, String filecontent) {
		try {
			//long lStartTime = System.currentTimeMillis();			
			File fp = new File(filepath);
			// 创建目录
			if (!fp.exists()) {
				fp.mkdirs();// 目录不存在的情况下，创建目录。
			}

			File fn = new File(filepath + filename);
			// 创建文件
			if (!fn.exists()) {
				fn.createNewFile();
			}
			//写入文件filewriter方式(经测试比下面方式快)
			FileWriter fw  = new FileWriter(fn);  
            fw.write(filecontent);   
            fw.flush();
            fw.close();
			
			//写文件缓冲区方式
			/*
			InputStream in = new ByteArrayInputStream(filecontent.getBytes()); 
			InputStream bis = new BufferedInputStream(in);  
            OutputStream out = new FileOutputStream(filepath + filename);  
            OutputStream bos = new BufferedOutputStream(out);  
            int iEOF = -1;  
            while ((iEOF = bis.read()) != -1) {  
                bos.write(iEOF);  
            } 
            if (bis != null) {  
                bis.close();  
            }  
            if (bos != null) {  
                bos.close();  
            } 
            */
           
            /*
            long lEndTime = System.currentTimeMillis();              
            System.out.println("readByFileStream() time: "  
                    + (lEndTime - lStartTime) + "ms");  
            */
            
			return true;
		} catch (IOException e) {
			log.error("文件存储位置:"+filepath);
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static Map readTxtFile(String filePath) {
		Map<String, String> map = new HashMap();
		String lineTxt = null;
		StringBuffer sbf = new StringBuffer();
		try {
			//long lStartTime = System.currentTimeMillis();
			
			String encoding = "UTF8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sbf.append(lineTxt);
				}
				map.put("filecontent", sbf.toString());
				map.put("status", "success");
				read.close();
			} else {
				System.out.println("找不到指定的文件");
				log.error("readTxtFile:找不到指定的文件");
				map.put("filecontent", "");
				map.put("status", "fail");
			}
			
			/*
			long lEndTime = System.currentTimeMillis();
			System.out.println("readByFileStream() time: "  
                    + (lEndTime - lStartTime) + "ms");  
            */
		} catch (Exception e) {
			System.out.println("读取文件内容出错" + e.getMessage());
			log.error("readTxtFile:读取文件内容出错");
			map.put("filecontent", "");
			map.put("status", "fail");
		}
		return map;
	}
}
