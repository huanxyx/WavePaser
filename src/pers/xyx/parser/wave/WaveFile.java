package pers.xyx.parser.wave;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * <pre>
 * ����Wave�ļ�����
 * �����������֣�
 * 		1.WaveRIFFChunk
 * 		2.FormatChunk
 * 		3.DataChunk
 * </pre>
 * @author Administrator
 * 
 */
public class WaveFile {
	private WaveRIFFChunk riffChunk;
	private WaveFormatChunk formatChunk;
	private WaveDataChunk dataChunk;
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public int getChannels() {
		return formatChunk.getChannels();
	}
	/**
	 * ��ȡÿ�β����������
	 * @return
	 */
	public int getBitPerSample() {
		return formatChunk.getBitPerSample();
	}
	/**
	 * ��ȡ�����Ĵ���
	 * @return
	 */
	public long getSampleTimes() {
		return dataChunk.getSize() / (formatChunk.getBitPerSample() / 8) /getChannels();
	}
	/**
	 * ��ȡ����
	 * @return
	 */
	public int[][] getData() {
		return dataChunk.getData();
	}
	/**
	 * ��ȡ������
	 * @return
	 */
	public long getSamplesPerSec() {
		return formatChunk.getSamplesPerSec();
	}
	
	/**
	 * ʹ��8���ֽڱ�ʾһ�β���
	 * @param samplesPerSec		������
	 * @param data				��Ƶ����
	 */
	public WaveFile(long samplesPerSec, int[][] data) {
		this(samplesPerSec, 8, data.length, data);
	}
	/**
	 * ͨ���û�������ȡ�ļ�����
	 * @param samplesPerSec		������
	 * @param bitPerSample		ÿ�β���ʹ�õı�����
	 * @param channels			������
	 * @param data				��Ƶ����
	 */
	public WaveFile(long samplesPerSec, int bitPerSample, int channels, int[][] data) {
		createFormatChunk(samplesPerSec, bitPerSample, channels);
		createDataChunk(data);
		createRIFFChunk();
	}
	

	//����FormatChunk
	private void createFormatChunk(long samplesPerSec, int bitPerSample, int channels) {
		formatChunk = new WaveFormatChunk(this);
		formatChunk.setFourCC(WaveFormatChunk.FORMAT_FOURCC);
		formatChunk.setSize(16);
		formatChunk.setFormatTag(WaveFormatChunk.PCM_FORMAT_TAG);
		formatChunk.setChannels(channels);
		formatChunk.setSamplesPerSec(samplesPerSec);
		formatChunk.setBytesPerSec(samplesPerSec * (bitPerSample / 8) * channels);
		formatChunk.setBlockAlign((bitPerSample / 8) * channels);
		formatChunk.setBitPerSample(bitPerSample);
	}

	//����DataChunk
	//Before��ʹ����createFormatChunk
	private void createDataChunk(int[][] data) {
		dataChunk = new WaveDataChunk(this);
		dataChunk.setFourCC(WaveDataChunk.DATA_FOURCC);
		dataChunk.setSize(data[0].length * (formatChunk.getBitPerSample()/8) * formatChunk.getChannels());
		dataChunk.setData(data);
	}
	//����RIFFChunk��
	//Before��ʹ����createFormatChunk��createDataChunk
	private void createRIFFChunk() {
		riffChunk = new WaveRIFFChunk(this);
		riffChunk.setFourCC(WaveRIFFChunk.RIFF_FOURCC);
		riffChunk.setSize(formatChunk.getSize() + dataChunk.getSize() + 16);
		riffChunk.setFormType(WaveRIFFChunk.FORM_TYPE);
	} 
	
	/**
	 * ͨ��ָ���ļ�������WaveFile
	 * @param fileName						�ļ���
	 * @throws IOException					IO�쳣
	 * @throws WaveFileIllegalException		�ļ���ʽ�����쳣
	 */
	public WaveFile(String fileName) throws IOException, WaveFileIllegalException {
		//��ȡ�ļ���
		try(InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
			parse(is);
		}
	}
	
	/**
	 * ��������
	 * @param is							�������
	 * @throws IOException					IO�쳣
	 * @throws WaveFileIllegalException		WaveFile��ʽ�Ƿ��쳣
	 */
	public void parse(InputStream is) throws IOException, WaveFileIllegalException {
		//����RIFF����
		riffChunk = new WaveRIFFChunk(this);
		riffChunk.parse(is);
		//����FORMAT����
		formatChunk = new WaveFormatChunk(this);
		formatChunk.parse(is);
		//����DATA����
		dataChunk = new WaveDataChunk(this);
		dataChunk.parse(is);
	}
	
	/**
	 * ������д���ļ���
	 * @param os							д����io��
	 * @throws IOException					IO�쳣
	 * @throws WaveFileIllegalException 	WaveFile��ʽ�Ƿ��쳣
	 */
	public void write(OutputStream os) throws IOException, WaveFileIllegalException {
		riffChunk.write(os);
		formatChunk.write(os);
		dataChunk.write(os);
		os.flush();
	}
}
