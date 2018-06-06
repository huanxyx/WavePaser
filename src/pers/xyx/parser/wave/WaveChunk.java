package pers.xyx.parser.wave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * <pre>
 * WaveChunk抽象类：
 * 		1.fourcc：四字符标识
 * 		2.Chunk块大小
 * 		3.判断该Chunk是否合法
 * 		4.解析文件流
 * 		5.将该Chunk信息写入输出流
 * </pre>
 * @author Administrator
 *
 */
public abstract class WaveChunk {

	private String fourCC;						//四字符标识
	private long size;							//Chunk块大小
	
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

	//判断该Chunk是否合法
	public abstract boolean isLegal();				
	//解析文件流
	public abstract void parse(InputStream is) throws IOException, WaveFileIllegalException;	
	//将Chunk信息写入到输出流中
	public abstract void write(OutputStream os) throws IOException, WaveFileIllegalException;			
}
