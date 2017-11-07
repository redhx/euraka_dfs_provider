package com.dfs.controller;

import com.alibaba.fastjson.JSON;
import com.dfs.bean.ConfigBean;
import com.dfs.service.FileMoniterService;
import com.dfs.service.RuleDefineService;
import com.dfs.util.Base64;
import com.dfs.util.DataUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hx on 2017/9/21.
 */
@RestController
@RequestMapping("/moniterb")
public class MoniterbController {

    @Autowired
    ConfigBean configBean;

    @RequestMapping("/savefileb")
    public String savefileb(HttpServletRequest request) {
        String filecontentstr = "";
        Map<String, String> hm = new HashMap();
        byte[] buff = null;
        try {
            InputStream imgStream = request.getInputStream();
            if (imgStream != null) {
                buff = DataUtil.input2byte(imgStream);
                filecontentstr = Base64.encode(buff);
            } else {
                hm.put("status", "fail");
                hm.put("fileinfo", "");
                String info = JSON.toJSONString(hm);
                return info;
            }
        } catch (Exception ex) {
            hm.put("status", "fail");
            hm.put("fileinfo", "");
            String info = JSON.toJSONString(hm);
            return info;
        }

        if (!StringUtils.isBlank(filecontentstr)) {
            // 计算文件名
            String filename = RuleDefineService.filenameCalculate();

            // 计算存储目录
            String filepath = RuleDefineService.filepathCalculate(configBean);

            // 计算存储服务器信息(ip和配置文件位置)
            Map<String, String> serverinfo = RuleDefineService
                    .serveripCalculate(filename,configBean);

            // 本地存储地址
            String[] serverips = configBean.getServerip();
            String localip = serverips[Integer.parseInt(configBean
                    .getLocalpoint())];

            // 进行存储调用
            String result = FileMoniterService.service.SaveFile(
                    serverinfo.get("serverip"), localip, filepath, filename,
                    filecontentstr);

            // 根据存储状态返回结果
            String fileinfo = filepath + filename + "@"
                    + serverinfo.get("serverposition");
            String fileinfolocal = filepath + filename + "@"
                    + configBean.getLocalpoint();

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
            return (info);
        } else {
            hm.put("status", "fail");
            hm.put("fileinfo", "");
            String info = JSON.toJSONString(hm);
            return info;
        }
    }
}
