package exceptions;

/**
 * 标签重复异常
 * IntervalSet要求标签与时间段一一对应
 */
public class LabelConflictException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LabelConflictException(String label) {
		super("标签[" + label + "]冲突！");
	}

}
