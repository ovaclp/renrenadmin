package io.renren.common.utils;


import io.renren.modules.device.entity.DevicedyninfoEntity;
import io.renren.modules.device.entity.DevicedyninfonewEntity;

import java.net.HttpURLConnection;
import java.net.URL;

public class CodeUtils {


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 十六进制转换字符串
     *
     * @param s
     *            str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String removeTail0(String str){
		//如果字符串尾部不为0，返回字符串
        if(!str.substring(str.length() -1).equals("0")){
            return str;
        }else{
			//否则将字符串尾部删除一位再进行递归
            return removeTail0(str.substring(0, str.length() -1 ));
        }
    }

    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }




    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }



    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    public  static  byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static  DevicedyninfoEntity dynnewTransferDyn(DevicedyninfonewEntity devicedyninfonewEntity){

        DevicedyninfoEntity devicedyninfoEntity=new DevicedyninfoEntity();
        devicedyninfoEntity.setDevid(devicedyninfonewEntity.getDevid());
        devicedyninfoEntity.setEncservicestate(devicedyninfonewEntity.getEncservicestate());
        devicedyninfoEntity.setKeyversion(devicedyninfonewEntity.getKeyversion());
        devicedyninfoEntity.setKeyupdatestate(devicedyninfonewEntity.getKeyupdatestate());
        devicedyninfoEntity.setTunnelconnectivity(devicedyninfonewEntity.getTunnelconnectivity());
        devicedyninfoEntity.setDevipstate(devicedyninfonewEntity.getDevipstate());
        devicedyninfoEntity.setAlgrescontion(devicedyninfonewEntity.getAlgrescontion());
        devicedyninfoEntity.setKeyrescondition(devicedyninfonewEntity.getKeyrescondition());
        devicedyninfoEntity.setDevstate(devicedyninfonewEntity.getDevstate());
        devicedyninfoEntity.setRngCheck(devicedyninfonewEntity.getRngCheck());
        devicedyninfoEntity.setKernelprogcheck(devicedyninfonewEntity.getKernelprogcheck());
        devicedyninfoEntity.setNotice(devicedyninfonewEntity.getNotice());
        devicedyninfoEntity.setLastmodifytime(devicedyninfonewEntity.getLastmodifytime());
        return devicedyninfoEntity;
    }

    public static boolean urlOnline(String urlParam){
        try{
            URL url=new URL(urlParam);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            return true;

        }catch(Exception e){
            return false;
        }
    }







}
