package com.dfs.render;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by hx on 2017/6/14.
 */
public class ImageRender  {

    private byte[] imgbyte;
    public ImageRender(byte[] imgbyte){
        this.imgbyte=imgbyte;
    }

    public void render(HttpServletResponse response) {
        ServletOutputStream sos = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(this.imgbyte);
            BufferedImage image = ImageIO.read(in);

            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("image/jpeg");//设置响应的媒体类型，这样浏览器会识别出响应的是图片
            response.setDateHeader("Expires", 0);

            sos = response.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (sos != null)
                try {sos.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
}
