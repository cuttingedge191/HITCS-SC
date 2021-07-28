package appADTs;

import specialOps.NonOverlapIntervalSet;

public interface IProcessIntervalSet<L> extends NonOverlapIntervalSet<L> {
	
	public void insert(long start, long end, L label) throws Exception;

}
