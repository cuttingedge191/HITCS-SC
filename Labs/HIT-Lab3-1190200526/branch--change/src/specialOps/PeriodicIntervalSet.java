package specialOps;

// 周期性重复时间段接口
public interface PeriodicIntervalSet<L> {

	/**
	 * 插入周期性重复时间段的插入方法
	 * 
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param label 标签
	 * @throws Exception 非法时间段、超出时间范围抛出异常
	 */
	public void insert(long start, long end, L label) throws Exception;

}
