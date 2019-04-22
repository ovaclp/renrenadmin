package io.renren.common.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BytesUtil {

	/**
	 * 将bytes转换为16进制整数
	 * @param src
	 * @return
	 */
	public static int bytesToHexInt(byte[] src){  
		String sbyte = bytesToHexString(src, 0, src.length);

		return Integer.parseInt(sbyte,16);  
	}

	public static int bytesToHexInt(byte[] src,int start,int length){  
		String sbyte = bytesToHexString(src, start, length);

		return Integer.parseInt(sbyte,16);  
	}

	/**
	 * 截取byte
	 * @param src
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] getBytesByLength(byte[] src,int start,int length){
		byte[] result = new byte[length];
		if (src == null || src.length <= 0) {  
			return null;  
		} 
		for(int i=start,j=0;i<start+length;i++,j++){
			result[j] = src[i];
		}
		return result;
	}

	/**
	 * 将bytes转换为16进制字符串
	 * @param src
	 * @param start
	 * @param length
	 * @return
	 */
	public static String bytesToHexString(byte[] src,int start,int length){  
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		for (int i = start; i < start+length; i++) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append(0);  
			}  
			stringBuilder.append(hv);  
		}  
		return stringBuilder.toString();  
	}

	/**
	 * 将bytes转换为字符串,低位在前
	 * @param src
	 * @param start
	 * @param length
	 * @return
	 */
	public static String bytesToHexStringLow(byte[] src,int start,int length){  
		StringBuilder stringBuilder = new StringBuilder("");  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		for (int i = start+length-1; i >= start; i--) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append(0);  
			}  
			stringBuilder.append(hv);  
		}  
		return stringBuilder.toString();  
	}

	//整数转4byte数组
	public static byte[] intToByteArray(int i){
		byte[] result = new byte[4];
		result[3] = (byte)((i>>24)&0xff);
		result[2] = (byte)((i>>16)&0xff);
		result[1] = (byte)((i>>8)&0xff);
		result[0] = (byte)(i&0xff);
		return result;
	}
	public static byte[] intToByteArrayLow(int i){
		byte[] result = new byte[4];
		result[0] = (byte)((i>>24)&0xff);
		result[1] = (byte)((i>>16)&0xff);
		result[2] = (byte)((i>>8)&0xff);
		result[3] = (byte)(i&0xff);
		return result;
	}

	//整数转2byte数组
	public static byte[] intTo2ByteArray(int i){
		byte[] result = new byte[2];
		result[0] = (byte)((i>>8)&0xff);
		result[1] = (byte)(i&0xff);
		return result;
	}
	public static byte[] intTo2ByteArrayLow(int i){
		byte[] result = new byte[2];
		result[1] = (byte)((i>>8)&0xff);
		result[0] = (byte)(i&0xff);
		return result;
	}

	/**
	 * 高位在前整数转为低位在前
	 * @param hValue
	 * @return
	 */
	public static int highToLow(int hValue){
		int lValue = 0;
		ByteBuffer buteBuf = ByteBuffer.wrap(intToByteArray(hValue));
		buteBuf.order(ByteOrder.LITTLE_ENDIAN);
		lValue = bytesToInt(intToByteArray(hValue));
		return lValue;
	}

	/**
	 * 高位在前byte数组转为低位在前
	 * @param bArray
	 * @return
	 */
	public static byte[] highToLowByteArray(byte[] bArray){
//		ByteBuffer buteBuf = ByteBuffer.wrap(bArray);
//		buteBuf.order(ByteOrder.LITTLE_ENDIAN);
//		return bArray;
		byte[] newArray = new byte[bArray.length];
		for(int m=0;m<bArray.length;m++){
			newArray[m] = bArray[bArray.length-1-m];
		}
		return newArray;
	}

	//将bytes转换为Int
	public static int bytesToInt(byte[] bytes){  
		int num = bytes[3]&0xFF;
		num |= ((bytes[2]<<8)&0xFF00);
		num |= ((bytes[1]<<16)&0xFF0000);
		num |= ((bytes[0]<<24)&0xFF000000);
		return num;  
	}
	
	//将bytes转换为Long
	public static long bytesToLong(byte[] bytes){  
		long temp = 0;
		long res = 0;
		for(int i=0; i<8; i++){
			res<<=8;
			temp=bytes[i]&0xFF;
			res |= temp;
		}

		return res;  
	}
	
	//仅将中文转换为unicode
	public static String toUnicodeString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			}
			else {
				sb.append("&#x"+Integer.toHexString(c)).append(";");
			}
		}
		return sb.toString();
	}

	//将中文转换为unicode
	public static String toAllUnicodeString(String s) {
		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {

			// 取出每一个字符
			char c = s.charAt(i);

			// 转换为unicode
			unicode.append("\\u").append(Integer.toHexString(c));//.append(";");
		}
		return unicode.toString();
	}

	/**  
	 * 16进制字符串转换为byte数组  
	 * @param hexString the hex string  
	 * @return byte[]  
	 */
	
	/**  
	 * 16进制字符串转换为byte数组  
	 * @param hexString the hex string  
	 * @return byte[]  
	 */  
	public static byte[] hexStringToBytesLow(String hexString) {   
		if (hexString == null || hexString.equals("")) {   
			return new byte[0];   
		}   
		hexString = hexString.toUpperCase();   
		int length = hexString.length() / 2;   
		char[] hexChars = hexString.toCharArray();   
		byte[] d = new byte[length];   
		for (int i = 0; i < length; i++) {   
			int pos = (length-1-i) * 2;   
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		}   
		return d;   
	}  

	/**
	 * 字符转为byte
	 * @param c
	 * @return byte
	 */
	private static byte charToByte(char c) {   
		return (byte) "0123456789ABCDEF".indexOf(c);   
	}

//	public static void main(String[] args) {
//		int a = 16;
//		hexStringToBytes(Integer.toHexString(a));
//		bytesToHexString(intToByteArray(16), 0, 4);
//		intToByteArray(a);
//		BytesUtil.hexStringToBytes(bytesToHexString(intToByteArray(16), 0, 4));
//	}

	/**
	 * 
	 * @描述:将二进制转换为mac地址
	 * @方法名: bytesToHexMac
	 * @返回类型 String
	 * @创建人 李家乐
	 * @创建时间 2018年9月4日下午5:09:43
	 * @修改人 李家乐
	 * @修改时间 2018年9月4日下午5:09:43
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static String bytesToHexMac(byte[] src,int start,int length){
		String mac = "";
		for (int i = 0; i < length; i++) {
			mac +=":"+BytesUtil.bytesToHexString(src,start+i,1);
		}
		return mac.substring(1, mac.length());
	}
}
