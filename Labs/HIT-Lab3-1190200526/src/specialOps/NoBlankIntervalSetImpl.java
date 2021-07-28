package specialOps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import baseADTs.CommonIntervalSet;
import helpers.dateHandler;

public class NoBlankIntervalSetImpl<L> implements NoBlankIntervalSet<L> {
	
	private final long startTime;
	private final long endTime;
	private final CommonIntervalSet<L> cisl;
	
	public NoBlankIntervalSetImpl(long startTime, long endTime, CommonIntervalSet<L> cisl) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.cisl = cisl;
	}
	
	@Override
	public void checkNoBlank() {
		if (cisl.labels().size() == 0) {
			System.out.println("空白段1" + ":" + dateHandler.parseLong(startTime)
            + "~" + dateHandler.parseLong(endTime));
			System.out.println("空白所占比例：100%（未进行排班）");
			return;
		}
		List<Long> timeList = new ArrayList<>();
		for (L l : cisl.labels()) {  // 保存所有的时间信息
			timeList.add(cisl.start(l));
			timeList.add(cisl.end(l));
		}
		long curT = startTime;
		long blankTime = 0;
		int blankCnt = 0;
		Collections.sort(timeList); // 按升序排列
		for (int i = 0; i < timeList.size() - 1; i += 2) {
			long s = timeList.get(i);
			long e = timeList.get(i + 1);
			if (curT < s) {
				blankTime += s - curT;
				++blankCnt;
				System.out.println("空白段" + blankCnt + ":" + dateHandler.parseLong(curT) + "~" 
				                   + dateHandler.parseLong(dateHandler.decnDayLong(s, 1)));
			}
			curT = dateHandler.incnDayLong(e, 1);
		}
		if (curT <= endTime) {
			blankTime += endTime - curT + dateHandler.incnDayLong(0, 1);
			++blankCnt;
			System.out.println("空白段" + blankCnt + ":" + dateHandler.parseLong(curT)
								+ "~" + dateHandler.parseLong(endTime));
		}
		if (blankCnt == 0) {
			System.out.println("当前排班已无空白！");
			return;
		}
		System.out.printf("空白所占比例：%.2f%%\n", 
				((double)blankTime / (double)(dateHandler.incnDayLong(endTime - startTime, 1)) * 100));
	}
	
	public long getStartTime() {
		return this.startTime;
	}
	
	public long getEndTime() {
		return this.endTime;
	}
}
