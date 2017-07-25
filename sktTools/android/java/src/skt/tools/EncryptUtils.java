package skt.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * @description 
 * @author Fang Yucun
 * @created 2013年10月31日
 */
public class EncryptUtils {

    private EncryptUtils() {};
    
    public static String SHA1Encrypt(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("sha1");
            md.update(input.getBytes());
            String pwd = new BigInteger(1, md.digest()).toString(16);
            return pwd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    public static String MD5Encrypt(String input) {    	
    	
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(new String(input.getBytes("gbk"), "UTF-8").getBytes("UTF-8"));
            byte[] bytes = md.digest();
            int i;
            StringBuffer buffer = new StringBuffer("");
            for (int offset = 0; offset < bytes.length; offset++) {
                i = bytes[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(i));
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
    
//    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
//        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    
    
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    
    
    public static String Md5(String plainText) {
    	  String result = null;
    	  try {
    	   MessageDigest md = MessageDigest.getInstance("MD5");
    	   md.update(plainText.getBytes());
    	   byte b[] = md.digest();
    	   int i;
    	   StringBuffer buf = new StringBuffer("");
    	   for (int offset = 0; offset < b.length; offset++) {
    	    i = b[offset];
    	    if (i < 0)
    	     i += 256;
    	    if (i < 16)
    	     buf.append("0");
    	    buf.append(Integer.toHexString(i));
    	   }
    	   // result = buf.toString();  //md5 32bit
    	   // result = buf.toString().substring(8, 24))); //md5 16bit
    	   result = buf.toString().substring(8, 24);
    	   System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
    	   System.out.println("md5 32bit: " + buf.toString() );
    	  } catch (NoSuchAlgorithmException e) {
    	   e.printStackTrace();
    	  }
    	  return result;
    	}
}
