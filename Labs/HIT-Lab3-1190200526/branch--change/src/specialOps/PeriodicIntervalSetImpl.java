package specialOps;

import baseADTs.MultiIntervalSet;
import exceptions.TimeOutOfRangeException;

public class PeriodicIntervalSetImpl<L> implements PeriodicIntervalSet<L> {
	
	// fields:
	private final long startTime; // 开始时间
	private final long period; // 周期长度
	private final int n; // 周期数
	private final MultiIntervalSet<L> misl;
	
	// constructor:
	public PeriodicIntervalSetImpl(long startTime, long period, int n, MultiIntervalSet<L> misl) {
		this.startTime = startTime;
		this.period = period;
		this.n = n;
		this.misl = misl;
	}

	@Override
	public void insert(long start, long end, L label) throws Exception {
		if (end > period) {
			throw new TimeOutOfRangeException();
		}
		for (int i = 0; i < this.n; ++i) {
			misl.insert(startTime+start+period*i, startTime+end+period*i, label);
		}
	}
	
	// 获取开始时间
	public long getStartTime() {
		return this.startTime;
	}
	
	// 获取周期长度
	public long getPeriod() {
		return this.period;
	}
	
	// 获取周期数
	public int getN() {
		return this.n;
	}
	
}
