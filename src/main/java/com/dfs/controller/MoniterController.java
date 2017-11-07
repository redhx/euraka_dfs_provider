package com.dfs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dfs.bean.ConfigBean;
import com.dfs.render.ExcelRender;
import com.dfs.render.ImageRender;
import com.dfs.service.FileMoniterService;
import com.dfs.service.RuleDefineService;
import com.dfs.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx on 2017/9/18.
 */
@RestController
@RequestMapping("/moniter")
public class MoniterController {

    @Autowired
    ConfigBean configBean;
    /**
     * 文件保存请求 http://ip:port/moniter/savefile 请求协议 http post json 请求参数
     * info={"filecontent":"文件内容"} 返回参数
     * {"status":"success/fail","fileinfo":"文件存储信息"}
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
        String result=savefileFun(filecontent,configBean);
        return result;
    }

    /**
     * 非跨域请求
     * 文件读取请求 http://ip:port/moniter/readfile 请求协议 http post json 请求参数
     * info={"fileinfo":"文件存储信息"} 返回参数
     * {"status":"success/fail","filecontent":"文件内容"}
     */
    @RequestMapping(value="/readfile")
    public String readfile(@RequestBody String info) {
        JSONObject json = JSONObject.parseObject(info);
        Map<String, String> tmpmap = JSON.toJavaObject(json, Map.class);
        String fileinfo = tmpmap.get("fileinfo");
        Map map=readfileFun(fileinfo,configBean);
        String result = JSON.toJSONString(map);
        return result;
    }

    /**
     * 文件读取请求 http://ip:port/moniter/readfileNew 请求协议 http post
     * 请求参数info="请求地址"
     * 返回参数
     * 直接返回图片流
     */
    @RequestMapping(value="/readfileNew")
    public void readfileNew(@RequestParam String info,HttpServletResponse response) {
        String[] serverip = configBean.getServerip();
        String[] tmp = info.split("@");
        String filepath = tmp[0];
        String actionip = serverip[Integer.parseInt(tmp[1])];
        // 进行读取调用
        Map<String, String> map = FileMoniterService.service.ReadFile(actionip,
                filepath);
        String imgbase64=map.get("filecontent");
        byte[] imgbyte=null;
        try {
            imgbyte= Base64.decode(imgbase64);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ImageRender imageRender=new ImageRender(imgbyte);
        imageRender.render(response);
    }

    /**
     *
     * @param info 文件服务器地址
     * @param response
     */
    @RequestMapping("/downloadExcelFile")
    public void downloadExcelFile(@RequestParam String info,HttpServletResponse response){
        String[] serverip = configBean.getServerip();
        String[] tmp = info.split("@");
        String filepath = tmp[0];
        String actionip = serverip[Integer.parseInt(tmp[1])];
        // 进行读取调用
        Map<String, String> map = FileMoniterService.service.ReadFile(actionip,
                filepath);
        String filebase64=map.get("filecontent");
        byte[] filebyte=null;
        try {
            filebyte= Base64.decode(filebase64);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ExcelRender excelRender=new ExcelRender(filebyte);
        excelRender.render(response);
    }



    //---------------------------------------------------------------------------//

    /**
     * 读文件方法
     * @param fileinfo 文件路径
     * @return
     */
    public static Map<String,String> readfileFun(String fileinfo,ConfigBean configBean){
        String[] serverip = configBean.getServerip();
        String[] tmp = fileinfo.split("@");
        String filepath = tmp[0];
        String actionip = serverip[Integer.parseInt(tmp[1])];
        // 进行读取调用
        Map<String, String> map = FileMoniterService.service.ReadFile(actionip,
                filepath);
        return map;
    }

    /**
     * 保存文件方法,使用配置文件的根目录
     * @param filecontent base64文件内容
     * @return
     */
    public static String savefileFun(String filecontent,ConfigBean configBean){
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
        String result = FileMoniterService.service.SaveFile(
                serverinfo.get("serverip"), localip, filepath, filename,
                filecontent);

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
}
