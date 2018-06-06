package pers.xyx.parser.wave;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;
import pers.xyx.parser.wave.utils.WaveIOUtils;

public class WaveRIFFChunk extends WaveChunk{
	public static final String RIFF_FOURCC = "RIFF";
	public static final String FORM_TYPE = "WAVE";
	
	protected WaveRIFFChunk(WaveFile waveFile) {
		super(waveFile);
	}
	
	//��������
	private String formType;			//�������ͣ�Wav�ļ�Ϊ"WAVE"
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	
	@Override
	public boolean isLegal() {
		if( RIFF_FOURCC.equals(getFourCC()) && FORM_TYPE.equals(formType) ) {
			return true;
		}
		return false;
	}
	@Override
	public void parse(InputStream is) throws IOException, WaveFileIllegalException {
		this.setFourCC(WaveIOUtils.readString(4, is));
		this.setSize(WaveIOUtils.readLong(is));
		this.setFormType(WaveIOUtils.readString(4, is));
		
		if(!this.isLegal())
			throw new WaveFileIllegalException("RIFF��ʽ���ڴ���");
	}
	@Override
	public void write(OutputStream os) throws IOException, WaveFileIllegalException {
		WaveIOUtils.writeString(getFourCC(), 4, os);
		WaveIOUtils.writeLong(getSize(), os);
		WaveIOUtils.writeString(getFormType(), 4, os);
	}
}
