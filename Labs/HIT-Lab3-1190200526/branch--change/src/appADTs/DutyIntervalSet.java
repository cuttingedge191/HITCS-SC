package appADTs;

import baseADTs.IntervalSet;
import baseADTs.MultiIntervalSet;
import specialOps.NoBlankIntervalSetImpl;
import specialOps.NonOverlapIntervalSetImpl;

public class DutyIntervalSet<L> extends MultiIntervalSet<L> implements IDutyIntervalSet<L> {

	// fields:
	private NonOverlapIntervalSetImpl<L> nois;
	private final NoBlankIntervalSetImpl<L> nbis;
	
	// Abstraction function:
	// 沿用MultiIntervalSet<L>的AF
	// 此外在nbis中保存了排班的起止日期
	// 整体代表一个排班表
	
	// Representation invariant:
	// 沿用CommonIntervalSet<L>的RI
	
	// Safety from rep exposure:
	// 大部分在MultiIntervalSet<L>中保证
	// 类的属性均设置为private final
	
	// Constructor:
	public DutyIntervalSet(long startTime, long endTime) {
		super(); // 初始化
		nbis = new NoBlankIntervalSetImpl<L>(startTime, endTime, this.multiList);
	}

	@Override
	public void checkNoBlank() {
		nbis.checkNoBlank();
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		for (IntervalSet<L> i : this.multiList) {
			nois = new NonOverlapIntervalSetImpl<>(i);
			nois.isOverLap(start, end, label);
		}
		for (IntervalSet<L> i : multiList) {
			if (!i.labels().contains(label)) {
				// 当前集合中无带此标签的时间段则插入
				i.insert(start, end, label);
				return;
			}
		}
		// 所有时间段集合中均有带此标签的时间段
		// 需要创建一个新的时间段集合来加入
		IntervalSet<L> toAdd = IntervalSet.empty();
		toAdd.insert(start, end, label);
		this.multiList.add(toAdd);
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
