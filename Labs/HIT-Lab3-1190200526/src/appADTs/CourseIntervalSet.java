package appADTs;

import baseADTs.MultiIntervalSet;
import specialOps.PeriodicIntervalSetImpl;

public class CourseIntervalSet<L> extends MultiIntervalSet<L> implements ICourseIntervalSet<L> {
	
	// fields:
	private final PeriodicIntervalSetImpl<L> pis;
	
	// Abstraction function:
	// 沿用MultiIntervalSet<L>的AF
	// 整体代表一个班级的特定课表

	// Representation invariant:
	// 沿用MultiIntervalSet<L>的RI

	// Safety from rep exposure:
	// 大部分在MultiIntervalSet<L>中保证
	// 类的属性设置为private final

	// constructor:
	public CourseIntervalSet(long startTime, long period, int n) {
		this.pis = new PeriodicIntervalSetImpl<>(startTime, period, n, 
				new MultiIntervalSet<>(this.multiList));
	}

	@Override
	public long getStartTime() {
		return this.pis.getStartTime();
	}

	@Override
	public int getWeeks() {
		return this.pis.getN();
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		this.pis.insert(start, end, label);
	}
	
}
