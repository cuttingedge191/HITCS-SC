package specialOps;

import baseADTs.IntervalSet;
import exceptions.IntervalConflictException;

public class NonOverlapIntervalSetImpl<L> implements NonOverlapIntervalSet<L> {

	private final IntervalSet<L> isl;
	
	// 用于传递对象，实现委托操作
	public NonOverlapIntervalSetImpl(IntervalSet<L> isl) {
		this.isl = isl;
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		isOverLap(start, end, label);
		isl.insert(start, end, label);
	}
	
	public void isOverLap(long start, long end, L label) throws IntervalConflictException {
		for (L l : isl.labels()) {
			long s = isl.start(l);
			long e = isl.end(l);
			if (!(s > end || e < start)) {
				throw new IntervalConflictException("与[" + l.toString() + "]" + "存在时间段重叠!");
			}
		}
	}
}
