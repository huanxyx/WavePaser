package pers.xyx.parser.wave.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * WaveIO��ع�����
 * @author Administrator
 *
 */
public class WaveIOUtils {
	private WaveIOUtils() {}
	
	//��ȡָ��λ�����ַ���
	public static String readString(int length, InputStream is) throws IOException {
		byte[] buffer = new byte[length];
		
		is.read(buffer);
		
		return new String(buffer);
	}

	//��ȡһ���޷��ŵ�1λ����
	public static int readByte(InputStream is) throws IOException {
		return is.read();
	}
	
	//��ȡһ���з��ŵ�2λ����
	public static int readInt(InputStream is) throws IOException {
		int result = 0;
		byte[] buffer = new byte[2];
		
		is.read(buffer);
		//Data���ݲ���16λ��ʾ�ķ�Χ���з��ŵ�2λ����
		result = (buffer[0] & 0xff) | (buffer[1] << 8);
		return result;
	}
	
	//��ȡһ���޷��ŵ�4λ����
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
	
	//д��һ���ַ���
	public static void writeString(String str, int len, OutputStream os) throws IOException, WaveFileIllegalException {
		if(str.length() != len)
			throw new WaveFileIllegalException("�ַ���ʶ���Ϸ���");
		byte[] buffer = str.getBytes();
		os.write(buffer);
	}
	
	//д��һ���ֽ�
	public static void writeByte(byte b, OutputStream os) throws IOException {
		os.write(new byte[]{b});
	}
	
	//д�������ֽ�
	public static void writeInt(int num, OutputStream os) throws IOException {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) (num);
		buffer[1] = (byte) (num >>> 8);
		
		os.write(buffer);
	}
	
	//д���ĸ��ֽ�
	public static void writeLong(long num, OutputStream os) throws IOException {
		byte[] buffer = new byte[4];
		buffer[0] = (byte) (num);
		buffer[1] = (byte) (num >>> 8);
		buffer[2] = (byte) (num >>> 16);
		buffer[3] = (byte) (num >>> 24);
		
		os.write(buffer);
	}
}
