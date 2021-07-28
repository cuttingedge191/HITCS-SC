package exceptions;

/**
 * 时间不在范围内异常
 */
public class TimeOutOfRangeException extends Exception {

	private static final long serialVersionUID = 4L;
	
	public TimeOutOfRangeException() {
		super("时间超出可安排范围！");
	}
	
}
