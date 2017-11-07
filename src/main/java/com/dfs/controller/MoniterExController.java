package com.dfs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dfs.bean.ConfigBean;
import com.dfs.service.FileMoniterService;
import com.dfs.service.RuleDefineService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx on 2017/10/13.
 */
@RestController
@RequestMapping("/moniterex")
public class MoniterExController {

    @Autowired
    ConfigBean configBean;

    /**
     * map(info,json)
     * json:{"filecontent":"","rootpath":""}
     * @param infomap
     * @return
     */
    @RequestMapping(value="/savefile")
    public String savefile(@RequestBody Map infomap) {
        String info= (String) infomap.get("info");
        if (StringUtils.isEmpty(info)) {
            return "";
        }
        JSONObject json = JSONObject.parseObject(info);
        Map<String, String> tmpmap = JSON.toJavaObject(json, Map.class);
        String filecontent = tmpmap.get("filecontent");
        String rootpath=tmpmap.get("rootpath");
        String result=savefileFunNew(filecontent,rootpath,configBean);
        return result;
    }

    /**
     *
     * @param info {"fileinfo":"存储地址", "rootpath":"指定的根目录"}
     * @return
     */
    @RequestMapping(value="/readfile")
    public String readfile(@RequestBody String info){
        JSONObject json = JSONObject.parseObject(info);
        Map<String, String> tmpmap = JSON.toJavaObject(json, Map.class);
        String fileinfo = tmpmap.get("fileinfo");
        String rootpath = tmpmap.get("rootpath");
        Map map=readfileFunNew(fileinfo,rootpath,configBean);
        String result = JSON.toJSONString(map);
        return result;
    }

    /*---------------------------------------------------------------------*/

    /**
     *
     * @param filecontent  文件base64内容
     * @param rootpath      用户指定根目录
     * @return
     */
    public static  String savefileFunNew(String filecontent,String rootpath,ConfigBean configBean){
        // 计算文件名
        String filename = RuleDefineService.filenameCalculate();

        // 计算存储目录
        String filepath = RuleDefineService.filepathCalculate(configBean);

        // 计算存储服务器信息(ip和配置文件位置)
        Map<String, String> serverinfo = RuleDefineService
                .serveripCalculate(filename,configBean);

        // 本地存储地址
        String[] serverips = configBean.getServerip();
        String localip = serverips[Integer.parseInt(configBean.getLocalpoint())];

        // 进行存储调用
        String result = FileMoniterService.service.SaveFileNew(
                serverinfo.get("serverip"), localip, filepath, filename,
                filecontent,rootpath);

        // 根据存储状态返回结果
        String fileinfo = filepath + filename + "@"
                + serverinfo.get("serverposition");
        String fileinfolocal = filepath + filename + "@"
                + configBean.getLocalpoint();
        Map<String, String> hm = new HashMap();
        if ("successlvs".equals(result)) {
            hm.put("status", "success");
            hm.put("fileinfo", fileinfo);
        } else if ("successlocal".equals(result)) {
            hm.put("status", "success");
            hm.put("fileinfo", fileinfolocal);
        } else {
            hm.put("status", "fail");
            hm.put("fileinfo", "");
        }

        String info = JSON.toJSONString(hm);
        return info;
    }

    public static Map<String,String> readfileFunNew(String fileinfo,String rootpath,ConfigBean configBean){
        String[] serverip = configBean.getServerip();
        String[] tmp = fileinfo.split("@");
        String filepath = tmp[0];
        String actionip = serverip[Integer.parseInt(tmp[1])];
        // 进行读取调用
        Map<String, String> map = FileMoniterService.service.ReadFileNew(actionip,
                filepath,rootpath);
        return map;
    }
}
