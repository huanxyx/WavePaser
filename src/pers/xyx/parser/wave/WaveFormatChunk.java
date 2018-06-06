package pers.xyx.parser.wave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;
import pers.xyx.parser.wave.utils.WaveIOUtils;

public class WaveFormatChunk extends WaveChunk {
	public static final String FORMAT_FOURCC = "fmt ";
	public static final int PCM_FORMAT_TAG = 1;
	
	protected WaveFormatChunk(WaveFile waveFile) {
		super(waveFile);
	}
	
	private int formatTag;					//���뷽ʽ������PCM��ʽΪ1
	private int channels;					//��������������1��˫����2
	private long samplesPerSec;				//������
	private long bytesPerSec;				//ÿ�������ֽ���
	private int blockAlign;					//ÿ�β����Ĵ�С
	private int bitPerSample;				//ÿ������ÿ�β�������bit��
	private int externInfo;					//���ӵ���Ϣ��ͨ��Size�ж��Ƿ����
	public int getFormatTag() {
		return formatTag;
	}

	public void setFormatTag(int formatTag) {
		this.formatTag = formatTag;
	}

	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public long getSamplesPerSec() {
		return samplesPerSec;
	}

	public void setSamplesPerSec(long samplesPerSec) {
		this.samplesPerSec = samplesPerSec;
	}

	public long getBytesPerSec() {
		return bytesPerSec;
	}

	public void setBytesPerSec(long bytesPerSec) {
		this.bytesPerSec = bytesPerSec;
	}

	public int getBlockAlign() {
		return blockAlign;
	}

	public void setBlockAlign(int blockAlign) {
		this.blockAlign = blockAlign;
	}

	public int getBitPerSample() {
		return bitPerSample;
	}

	public void setBitPerSample(int bitPerSample) {
		this.bitPerSample = bitPerSample;
	}

	public int getExternInfo() {
		return externInfo;
	}

	public void setExternInfo(int externInfo) {
		this.externInfo = externInfo;
	}

	//�ж��Ƿ���ڸ�����Ϣ
	public boolean hasExternInfo() {
		long size = getSize();
		if(size == 18) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isLegal() {
		if(FORMAT_FOURCC.equals(getFourCC())) {
			return true;
		}
		return false;
	}

	@Override
	public void parse(InputStream is) throws IOException, WaveFileIllegalException {
		this.setFourCC(WaveIOUtils.readString(4, is));
		this.setSize(WaveIOUtils.readLong(is));
		this.setFormatTag(WaveIOUtils.readInt(is));
		this.setChannels(WaveIOUtils.readInt(is));
		this.setSamplesPerSec(WaveIOUtils.readLong(is));
		this.setBytesPerSec(WaveIOUtils.readLong(is));
		this.setBlockAlign(WaveIOUtils.readInt(is));
		this.setBitPerSample(WaveIOUtils.readInt(is));
		
		if(this.hasExternInfo()) {
			this.setExternInfo(WaveIOUtils.readInt(is));
		}
		
		if(!this.isLegal())
			throw new WaveFileIllegalException("RIFF��ʽ���ڴ���");
	}

	@Override
	public void write(OutputStream os) throws IOException, WaveFileIllegalException {
		WaveIOUtils.writeString(getFourCC(), 4, os);
		WaveIOUtils.writeLong(getSize(), os);
		WaveIOUtils.writeInt(getFormatTag(), os);
		WaveIOUtils.writeInt(getChannels(), os);
		WaveIOUtils.writeLong(getSamplesPerSec(), os);
		WaveIOUtils.writeLong(getBytesPerSec(), os);
		WaveIOUtils.writeInt(getBlockAlign(), os);
		WaveIOUtils.writeInt(getBitPerSample(), os);
		
		if(this.hasExternInfo()) {
			WaveIOUtils.writeInt(getExternInfo(), os);
		}
	}
}
