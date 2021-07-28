package appADTs;

import baseADTs.IntervalSet;
import baseADTs.MultiIntervalSet;
import specialOps.NonOverlapIntervalSetImpl;

public class ProcessIntervalSet<L> extends MultiIntervalSet<L> implements IProcessIntervalSet<L> {
	
	// fields:
	NonOverlapIntervalSetImpl<L> nois;
	
	// Abstraction function:
	// 沿用MultiIntervalSet<L>的AF
	// 整体代表一个操作系统对进程的调度记录
	
	// Representation invariant:
	// 沿用MultiIntervalSet<L>的RI
	
	// Safety from rep exposure:
	// 大部分在MultiIntervalSet<L>中保证
    // 类的属性设置为private final
	
	// constructor:
	public ProcessIntervalSet() {
		super();
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		int i;
		for (i = 0; i < this.multiList.size(); ++i) {
			nois = new NonOverlapIntervalSetImpl<>(this.multiList.get(i));
			nois.isOverLap(start, end, label); // 检查是否重叠
		}
		for (i = 0; i < this.multiList.size(); ++i) {
			if (!this.multiList.get(i).labels().contains(label)) { // 找到插入位置
				this.multiList.get(i).insert(start, end, label);
				return;
			}
		}
		this.multiList.add(IntervalSet.empty());
		this.multiList.get(i).insert(start, end, label);
	}

}
