package pers.xyx.parser.wave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;
import pers.xyx.parser.wave.utils.WaveIOUtils;

public class WaveDataChunk extends WaveChunk{
	public static final String DATA_FOURCC = "data";
	
	protected WaveDataChunk(WaveFile waveFile) {
		super(waveFile);
	}
	
	//存储音频数据
	private int[][] data;			
	public int[][] getData() {
		return data;
	}

	public void setData(int[][] data) {
		this.data = data;
	}


	@Override
	public boolean isLegal() {
		if(DATA_FOURCC.equals(getFourCC())) {
			return true;
		}
		return false;
	}

	@Override
	public void parse(InputStream is) throws IOException, WaveFileIllegalException {
		this.setFourCC(WaveIOUtils.readString(4, is));
		if(!this.isLegal())
			throw new WaveFileIllegalException("RIFF格式存在错误！");
		this.setSize(WaveIOUtils.readLong(is));
		
		//获取读取音频数据需要的相关信息
		WaveFile waveFile = getWaveFile();
		int channels = waveFile.getChannels();
		int sampleTimes = (int) waveFile.getSampleTimes();
		int perBit = waveFile.getBitPerSample();
		
		//解析
		int[][] data = new int[channels][sampleTimes];
		for(int i = 0; i < sampleTimes; i++) {
			for(int j = 0; j < channels; j++) {
				if(perBit == 8) {
					data[j][i] = WaveIOUtils.readByte(is);
				} else if(perBit == 16) {
					data[j][i] = WaveIOUtils.readInt(is);
				}
			}
		}
		
		this.setData(data);
	}

	@Override
	public void write(OutputStream os) throws IOException, WaveFileIllegalException {
		WaveIOUtils.writeString(getFourCC(), 4, os);
		WaveIOUtils.writeLong(getSize(), os);
		
		WaveFile waveFile = getWaveFile();
		int channels = waveFile.getChannels();
		int sampleTimes = (int) waveFile.getSampleTimes();
		int perBit = waveFile.getBitPerSample();
		
		//写入音频数据
		int[][] data = getData();
		for(int i = 0; i < sampleTimes; i++) {
			for(int j = 0; j < channels; j++) {
				if(perBit == 8) {
					WaveIOUtils.writeByte((byte) data[j][i], os);
				} else if(perBit == 16) {
					WaveIOUtils.writeInt(data[j][i], os);
				}
			}
		}
	}
}
