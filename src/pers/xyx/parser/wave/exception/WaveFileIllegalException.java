package pers.xyx.parser.wave.exception;

/**
 * 文件格式不合法的异常
 * @author Administrator
 *
 */
public class WaveFileIllegalException extends Exception{
	private static final long serialVersionUID = 5219542538950983423L;
	
	public WaveFileIllegalException() {
		super();
	}
	
	public WaveFileIllegalException(String message) {
		super(message);
	}
}
