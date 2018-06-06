package pers.xyx.parser.wave.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * WaveIO相关工具类
 * @author Administrator
 *
 */
public class WaveIOUtils {
	private WaveIOUtils() {}
	
	//读取指定位数的字符串
	public static String readString(int length, InputStream is) throws IOException {
		byte[] buffer = new byte[length];
		
		is.read(buffer);
		
		return new String(buffer);
	}

	//读取一个无符号的1位整数
	public static int readByte(InputStream is) throws IOException {
		return is.read();
	}
	
	//读取一个有符号的2位整数
	public static int readInt(InputStream is) throws IOException {
		int result = 0;
		byte[] buffer = new byte[2];
		
		is.read(buffer);
		//Data数据部分16位表示的范围是有符号的2位整数
		result = (buffer[0] & 0xff) | (buffer[1] << 8);
		return result;
	}
	
	//读取一个无符号的4位整数
	public static long readLong(InputStream is) throws IOException {
		long result = 0;
		
		long[] buffer = new long[4];
		
		for(int i = 0; i < 4; i++) {
			buffer[i] = is.read();
		}
		
		result = buffer[0] | 
				(buffer[1] << 8) | 
				(buffer[2] << 16) | 
				(buffer[3] << 24);
		return result;
	}
	
	//写入一个字符串
	public static void writeString(String str, int len, OutputStream os) throws IOException, WaveFileIllegalException {
		if(str.length() != len)
			throw new WaveFileIllegalException("字符标识不合法！");
		byte[] buffer = str.getBytes();
		os.write(buffer);
	}
	
	//写入一个字节
	public static void writeByte(byte b, OutputStream os) throws IOException {
		os.write(new byte[]{b});
	}
	
	//写入两个字节
	public static void writeInt(int num, OutputStream os) throws IOException {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) (num);
		buffer[1] = (byte) (num >>> 8);
		
		os.write(buffer);
	}
	
	//写入四个字节
	public static void writeLong(long num, OutputStream os) throws IOException {
		byte[] buffer = new byte[4];
		buffer[0] = (byte) (num);
		buffer[1] = (byte) (num >>> 8);
		buffer[2] = (byte) (num >>> 16);
		buffer[3] = (byte) (num >>> 24);
		
		os.write(buffer);
	}
}
