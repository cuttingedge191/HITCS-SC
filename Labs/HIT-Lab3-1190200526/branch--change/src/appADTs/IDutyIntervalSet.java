package appADTs;

import specialOps.NoBlankIntervalSet;
import specialOps.NonOverlapIntervalSet;

public interface IDutyIntervalSet<L> extends NonOverlapIntervalSet<L>, 
                                             NoBlankIntervalSet<L> {
	
	/**
	 * 检查是否存在空白
	 * 输出相关信息
	 */
	public void checkNoBlank();
	
	/**
	 * 获取排班开始日期（毫秒表示）
	 * 
	 * @return 排班开始日期
	 */
	public long getStartTime();
	
	/**
	 * 获取排班结束日期（毫秒表示）
	 * 
	 * @return 排班结束日期
	 */
	public long getEndTime();
	
	/**
	 * 获取排班总天数
	 * 
	 * @return 排班总天数
	 */
	public int getTotalDays();
	
	/**
	 * 检查排班时间是否在范围内
	 * 
	 * @param start 要检查的开始时间
	 * @param end 要检查的结束时间
	 * @return 排班时间是否在范围内
	 */
	public boolean checkInRange(long start, long end);
}
