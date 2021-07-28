package appADTs;

import specialOps.NonOverlapIntervalSet;
import specialOps.PeriodicIntervalSet;

public interface ICourseIntervalSet<L> extends PeriodicIntervalSet<L>,
											   NonOverlapIntervalSet<L> {
	
	public void insert(long start, long end, L label) throws Exception;
	
	/**
	 * 获取开始时间
	 * 
	 * @return 开始时间
	 */
	public long getStartTime();
	
	/**
	 * 获取总周数
	 * 
	 * @return 总周数
	 */
	public int getWeeks();
}
