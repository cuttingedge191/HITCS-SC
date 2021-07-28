package specialOps;

//无重叠时间段接口
public interface NonOverlapIntervalSet<L> {
	
	/**
	 * 时间段无重叠的插入方法
	 * 
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param label 标签
	 * @throws Exception 非法时间段、标签重复、时间段重叠抛出异常
	 */
	public void insert(long start, long end, L label) throws Exception;
	
}