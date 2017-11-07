package com.dfs.render;

import com.dfs.util.NumberUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by hx on 2017/9/21.
 */
public class ExcelRender {
    private byte[] filebyte;
    public ExcelRender(byte[] filebyte){
        this.filebyte=filebyte;
    }

    public void render(HttpServletResponse response) {
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + NumberUtil.generateLowerAllString(8)+ ".xls\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");

        OutputStream outputStream=null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(filebyte);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null)
                try {outputStream.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
}
