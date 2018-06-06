package pers.xyx.parser.wave;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pers.xyx.parser.wave.exception.WaveFileIllegalException;

/**
 * <pre>
 * 代表Wave文件对象
 * 内置三个部分：
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
	 * 获取声道数
	 * @return
	 */
	public int getChannels() {
		return formatChunk.getChannels();
	}
	/**
	 * 获取每次采样所需比特
	 * @return
	 */
	public int getBitPerSample() {
		return formatChunk.getBitPerSample();
	}
	/**
	 * 获取采样的次数
	 * @return
	 */
	public long getSampleTimes() {
		return dataChunk.getSize() / (formatChunk.getBitPerSample() / 8) /getChannels();
	}
	/**
	 * 获取数据
	 * @return
	 */
	public int[][] getData() {
		return dataChunk.getData();
	}
	/**
	 * 获取采样率
	 * @return
	 */
	public long getSamplesPerSec() {
		return formatChunk.getSamplesPerSec();
	}
	
	/**
	 * 使用8个字节表示一次采样
	 * @param samplesPerSec		采样率
	 * @param data				音频数据
	 */
	public WaveFile(long samplesPerSec, int[][] data) {
		this(samplesPerSec, 8, data.length, data);
	}
	/**
	 * 通过用户参数获取文件对象
	 * @param samplesPerSec		采样率
	 * @param bitPerSample		每次采样使用的比特数
	 * @param channels			声道数
	 * @param data				音频数据
	 */
	public WaveFile(long samplesPerSec, int bitPerSample, int channels, int[][] data) {
		createFormatChunk(samplesPerSec, bitPerSample, channels);
		createDataChunk(data);
		createRIFFChunk();
	}
	

	//创建FormatChunk
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

	//创建DataChunk
	//Before：使用了createFormatChunk
	private void createDataChunk(int[][] data) {
		dataChunk = new WaveDataChunk(this);
		dataChunk.setFourCC(WaveDataChunk.DATA_FOURCC);
		dataChunk.setSize(data[0].length * (formatChunk.getBitPerSample()/8) * formatChunk.getChannels());
		dataChunk.setData(data);
	}
	//创建RIFFChunk：
	//Before：使用了createFormatChunk和createDataChunk
	private void createRIFFChunk() {
		riffChunk = new WaveRIFFChunk(this);
		riffChunk.setFourCC(WaveRIFFChunk.RIFF_FOURCC);
		riffChunk.setSize(formatChunk.getSize() + dataChunk.getSize() + 16);
		riffChunk.setFormType(WaveRIFFChunk.FORM_TYPE);
	} 
	
	/**
	 * 通过指定文件名创建WaveFile
	 * @param fileName						文件名
	 * @throws IOException					IO异常
	 * @throws WaveFileIllegalException		文件格式错误异常
	 */
	public WaveFile(String fileName) throws IOException, WaveFileIllegalException {
		//获取文件流
		try(InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
			parse(is);
		}
	}
	
	/**
	 * 解析方法
	 * @param is							读入的流
	 * @throws IOException					IO异常
	 * @throws WaveFileIllegalException		WaveFile格式非法异常
	 */
	public void parse(InputStream is) throws IOException, WaveFileIllegalException {
		//解析RIFF部分
		riffChunk = new WaveRIFFChunk(this);
		riffChunk.parse(is);
		//解析FORMAT部分
		formatChunk = new WaveFormatChunk(this);
		formatChunk.parse(is);
		//解析DATA部分
		dataChunk = new WaveDataChunk(this);
		dataChunk.parse(is);
	}
	
	/**
	 * 将数据写入文件中
	 * @param os							写出的io流
	 * @throws IOException					IO异常
	 * @throws WaveFileIllegalException 	WaveFile格式非法异常
	 */
	public void write(OutputStream os) throws IOException, WaveFileIllegalException {
		riffChunk.write(os);
		formatChunk.write(os);
		dataChunk.write(os);
		os.flush();
	}
}
