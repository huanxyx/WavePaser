package pers.xyx.parser.wave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * <pre>
 * WaveChunk�����ࣺ
 * 		1.fourcc�����ַ���ʶ
 * 		2.Chunk���С
 * 		3.�жϸ�Chunk�Ƿ�Ϸ�
 * 		4.�����ļ���
 * 		5.����Chunk��Ϣд�������
 * </pre>
 * @author Administrator
 *
 */
public abstract class WaveChunk {

	private String fourCC;						//���ַ���ʶ
	private long size;							//Chunk���С
	
	public String getFourCC() {
		return fourCC;
	}

	public void setFourCC(String fourCC) {
		this.fourCC = fourCC;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	
	private WaveFile waveFile;
	protected WaveChunk(WaveFile waveFile) {
		this.waveFile = waveFile;
	}
	public WaveFile getWaveFile() {
		return waveFile;
	}

	//�жϸ�Chunk�Ƿ�Ϸ�
	public abstract boolean isLegal();				
	//�����ļ���
	public abstract void parse(InputStream is) throws IOException, WaveFileIllegalException;	
	//��Chunk��Ϣд�뵽�������
	public abstract void write(OutputStream os) throws IOException, WaveFileIllegalException;			
}
