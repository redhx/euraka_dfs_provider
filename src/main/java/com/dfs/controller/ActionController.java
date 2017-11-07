package com.dfs.controller;

import com.alibaba.fastjson.JSON;
import com.dfs.bean.ConfigBean;
import com.dfs.service.FileActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 文件保存/读取 控制器
 * @author huangxi
 *
 */
@RestController
@RequestMapping("/action")
public class ActionController {

	@Autowired
	ConfigBean configBean;

	@RequestMapping(value="/savefile",method = RequestMethod.POST)
	public String savefile(@RequestParam String filecontent,@RequestParam String filepath,@RequestParam String filename){
		//System.out.println("startAction-save:"+DateUtil.getDayTime("yyyy-MM-dd HH:mm:ss SSS",0));
		String fileroot = configBean.getFileroot();
		filepath=fileroot+filepath;
		//调用保存方法
		Boolean bl=FileActionService.service.SaveFile(filepath, filename, filecontent);
		if(bl){
			return "success";
		}else{
			return "fail";
		}
		//System.out.println("endAction-save:"+DateUtil.getDayTime("yyyy-MM-dd HH:mm:ss SSS",0));
	}

	@RequestMapping(value="/savefilenew",method = RequestMethod.POST)
	public String savefilenew(@RequestParam String rootpath,@RequestParam String filecontent,@RequestParam String filepath,@RequestParam String filename){
		//System.out.println("startAction-save:"+DateUtil.getDayTime("yyyy-MM-dd HH:mm:ss SSS",0));
		String fileroot = configBean.getFileroot();
		filepath=fileroot+rootpath+filepath;

		//调用保存方法
		Boolean bl=FileActionService.service.SaveFile(filepath, filename, filecontent);
		if(bl){
			return "success";
		}else{
			return "fail";
		}
		//System.out.println("endAction-save:"+DateUtil.getDayTime("yyyy-MM-dd HH:mm:ss SSS",0));
	}

	@RequestMapping(value="/readfile",method = RequestMethod.POST)
	public String readfile(@RequestParam String filepath){
		String fileroot = configBean.getFileroot();
		filepath=fileroot+filepath;
		//调用保存方法
		Map map=FileActionService.service.readTxtFile(filepath);
		String result= JSON.toJSONString(map);
		return result;
	}

	@RequestMapping(value="/readfilenew",method = RequestMethod.POST)
	public String readfilenew(@RequestParam String rootpath,@RequestParam String filepath){
		String fileroot = configBean.getFileroot();
		filepath=fileroot+rootpath+filepath;
		//调用保存方法
		Map map=FileActionService.service.readTxtFile(filepath);
		String result= JSON.toJSONString(map);
		return result;
	}
}
