package appADTs;

import baseADTs.CommonIntervalSet;
import specialOps.NoBlankIntervalSetImpl;
import specialOps.NonOverlapIntervalSetImpl;

public class DutyIntervalSet<L> extends CommonIntervalSet<L> implements IDutyIntervalSet<L> {

	// fields:
	private final NonOverlapIntervalSetImpl<L> nois;
	private final NoBlankIntervalSetImpl<L> nbis;
	
	// Abstraction function:
	// 沿用CommonIntervalSet<L>的AF
	// 此外在nbis中保存了排班的起止日期
	// 整体代表一个排班表
	
	// Representation invariant:
	// 沿用CommonIntervalSet<L>的RI
	
	// Safety from rep exposure:
	// 大部分在CommonIntervalSet<L>中保证
	// 类的属性均设置为private final
	
	// Constructor:
	public DutyIntervalSet(long startTime, long endTime) {
		super(); // 初始化CommonIntervalSet<L>
		CommonIntervalSet<L> pack = new CommonIntervalSet<>(this.labelSet, this.startMap, this.endMap); // 用于传递给特殊操作方法
		nois = new NonOverlapIntervalSetImpl<L>(pack);
		nbis = new NoBlankIntervalSetImpl<L>(startTime, endTime, pack);
	}

	@Override
	public void checkNoBlank() {
		nbis.checkNoBlank();
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		nois.insert(start, end, label);
	}

	@Override
	public long getStartTime() {
		return nbis.getStartTime();
	}

	@Override
	public long getEndTime() {
		return nbis.getEndTime();
	}
	
	@Override
	public int getTotalDays() {
		long totalTime = nbis.getEndTime() - nbis.getStartTime() + 86400000L;
		long totalDays = totalTime / 86400000L;
		return (int)totalDays;
	}
	
	@Override
	public boolean checkInRange(long start, long end) {
		long startTime = nbis.getStartTime();
		long endTime = nbis.getEndTime();
		if (startTime <= start && start <= endTime && startTime <= end && end <= endTime)
			return true;
		return false;
	}
	
}
