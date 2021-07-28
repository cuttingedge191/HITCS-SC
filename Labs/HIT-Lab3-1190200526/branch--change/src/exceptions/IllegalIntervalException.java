package exceptions;

/**
 * 非法时间段异常
 * 空标签、开始时间大于结束时间、时间存在负数
 */
public class IllegalIntervalException extends Exception {

	private static final long serialVersionUID = 2L;
	
	public IllegalIntervalException() {
		super("时间段非法！");
	}
}
