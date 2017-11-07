package com.dfs.util;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Base64算法
 *
 * @author huangxi
 * @version 2008-6-18 11:48:08
 */
public class Base64
{
	/**
	 * 图片base64
	 * @param imgurl
	 * @return
	 */
	public static String GetImageStr(String imgurl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = imgurl;// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encode(data);// 返回Base64编码过的字节数组字符串
	}
	
    /**
     * Base64编码加密
     *
     * @param data 待加密字节数组
     * @return 加密后字符串
     */
    public static String encode(byte[] data)
    {
        if (data == null) return null;
        return new String(org.bouncycastle.util.encoders.Base64.encode(data));
    }

    /**
     * Base64编码解密
     *
     * @param data 待解密字符串
     * @return 解密后字节数组
     * @throws CodecException 异常
     */
    public static byte[] decode(String data) throws CodecException
    {
        if (data == null) return null;
        try
        {
            return org.bouncycastle.util.encoders.Base64.decode(data.getBytes());
        } catch (RuntimeException e)
        {
            throw new CodecException(e.getMessage(), e);
        }
    }

	public static String encodeBase64(BufferedImage image) throws IOException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] bytes = baos.toByteArray();
			return new String(Base64.encode(bytes));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
	}

	public static boolean GenerateImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b =decode(imgStr);
//			byte[] b = decoder.decodeBuffer(imgStr);
//			for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//					b[i] += 256;
//				}
//			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}