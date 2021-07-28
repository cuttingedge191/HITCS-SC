package exceptions;

/**
 * 时间段重叠异常
 */
public class IntervalConflictException extends Exception {
	
	private static final long serialVersionUID = 3L;

	public IntervalConflictException(String info) {
		super(info);
	}
}
