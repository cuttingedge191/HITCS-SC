package baseADTs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.util.Pair;

public class APIs<L> {
	
	/**
	 * 计算两个MultiIntervalSet对象的相似度
	 * 
	 * @param <L> 对象中标签的类型
	 * @param s1 第一个对象
	 * @param s2 第二个对象
	 * @return 两个对象的相似度
	 */
	public static <L> double Similarity(MultiIntervalSet<L> s1, MultiIntervalSet<L> s2) {
		if (s1.isEmpty() || s2.isEmpty()) // 存在空对象
			return 0.0;
		List<Pair<Long, Long>> l1 = new ArrayList<>();
		List<Pair<Long, Long>> l2 = new ArrayList<>();
		for (IntervalSet<L> isl : s1.multiList) {
			for (L l : isl.labels()) {
				l1.add(new Pair<>(isl.start(l), isl.end(l))); // 保存到可排序的List中
			}
		}
		for (IntervalSet<L> isl : s2.multiList) {
			for (L l : isl.labels()) {
				l2.add(new Pair<>(isl.start(l), isl.end(l))); // 同样保存到可排序的List中
			}
		}
		l1.sort(new TimeComparator2());
		l2.sort(new TimeComparator2());
		long end = l1.get(0).getValue() < l2.get(0).getValue() ? l2.get(0).getValue() : l1.get(0).getValue();
		l1.sort(new TimeComparator1());
		l2.sort(new TimeComparator1());
		long start = l1.get(0).getKey() < l2.get(0).getKey() ? l1.get(0).getKey() : l2.get(0).getKey();
		long total = end - start + 1;
		long cnt = 0;
		// 遍历检查相似
		int i = 0;
		int j = 0;
		while (i < l1.size() && j < l2.size()) {
			long a1, a2, b1, b2;
			a1 = l1.get(i).getKey();
			b1 = l1.get(i).getValue();
			a2 = l2.get(j).getKey();
			b2 = l2.get(j).getValue();
			if (a1 == a2 && b1 == b2) { // 相似
				cnt += b1 - a1 + 1;
				++i;
				++j;
			} else if (a1 < a2) {
				++i;
			} else
				++j;
		}
		return (double)cnt / (double)total;
	}
	
	/**
	 * 计算一个IntervalSet<L>对象中的时间冲突比例
	 * 
	 * @param <L> 对象中标签的类型
	 * @param set IntervalSet<L>对象
	 * @return 时间冲突比例
	 */
	public static <L> double calcConflictRatio(IntervalSet<L> set) {
		if (set.isEmpty()) { // 空对象
			return 0.0;
		}
		List<Pair<Long, Long>> setL = new ArrayList<>();
		for (L l : set.labels()) {
			setL.add(new Pair<>(set.start(l), set.end(l)));
		}
		return commonCalcConflictRatio(setL);
	}
	
	/**
	 * 计算一个MultiIntervalSet<L>对象中的时间冲突比例
	 * 
	 * @param <L> 对象中标签的类型
	 * @param set MultiIntervalSet<L>对象
	 * @return 时间冲突比例
	 */
	public static <L> double calcConflictRatio(MultiIntervalSet<L> set) {
		if (set.isEmpty()) { // 空对象
			return 0.0;
		}
		List<Pair<Long, Long>> setL = new ArrayList<>();
		for (IntervalSet<L> isl : set.multiList) {
			for (L l : isl.labels()) {
				setL.add(new Pair<>(isl.start(l), isl.end(l)));
			}
		}
		return commonCalcConflictRatio(setL);
	}
	
	/**
	 * 计算一个IntervalSet<L>对象中的空闲时间比例
	 * 
	 * @param <L> 对象中标签的类型
	 * @param set IntervalSet<L>对象
	 * @return 空闲时间比例
	 */
	public static <L> double calcFreeTimeRatio(IntervalSet<L> set) {
		if (set.isEmpty()) // 空对象
			return 1.0;
		List<Pair<Long, Long>> setL = new ArrayList<>();
		for (L l : set.labels()) {
			setL.add(new Pair<>(set.start(l), set.end(l)));
		}
		return commonCalcFreeTimeRatio(setL);
	}
	
	/**
	 * 计算一个MultiIntervalSet<L>对象中的空闲时间比例
	 * 
	 * @param <L> 对象中标签的类型
	 * @param set MultiIntervalSet<L>对象
	 * @return 空闲时间比例
	 */
	public static <L> double calcFreeTimeRatio(MultiIntervalSet<L> set) {
		if (set.isEmpty()) // 空对象
			return 1.0;
		List<Pair<Long, Long>> setL = new ArrayList<>();
		for (IntervalSet<L> isl : set.multiList) {
			for (L l : isl.labels()) {
				setL.add(new Pair<>(isl.start(l), isl.end(l)));
			}
		}
		return commonCalcFreeTimeRatio(setL);
	}
	
	private static double commonCalcConflictRatio(List<Pair<Long, Long>> setL) {
		long conflict = 0; // 重叠记录
		setL.sort(new TimeComparator2());
		long end = setL.get(0).getValue();
		setL.sort(new TimeComparator1());
		long start = setL.get(0).getKey();
		long total = end - start + 1;
		long next = -1;
		for (int i = 0; i < setL.size() - 1; ++i) {
			long a1, b1, a2, b2;
			a1 = Math.max(setL.get(i).getKey(), next);
			b1 = Math.max(setL.get(i).getValue(), next);
			a2 = Math.max(setL.get(i + 1).getKey(), next+1);
			b2 = Math.max(setL.get(i + 1).getValue(), next+1);
			if (!(a2 > b1)) { // 存在重叠
				long cStart = a1 > a2 ? a1 : a2;
				long cEnd = b1 > b2 ? b2 : b1;
				conflict += cEnd - cStart + 1;
				next = cEnd; // 避免重复计算同一冲突时间段
			}
		}
		return (double)conflict / (double)total;
	}
	
	public static double commonCalcFreeTimeRatio(List<Pair<Long, Long>> setL) {
		long freeTime = 0;
		setL.sort(new TimeComparator2());
		long end = setL.get(0).getValue();
		setL.sort(new TimeComparator1());
		long start = setL.get(0).getKey();
		long total = end - start + 1;
		long cur = setL.get(0).getValue();
		int i = 1;
		while (i < setL.size()) {
			if (cur < setL.get(i).getKey()) {
				freeTime += setL.get(i).getKey() - cur;
			}
			cur = setL.get(i).getValue() > cur ? setL.get(i).getValue() : cur;
			++i;
		}
		return (double)freeTime / (double)total;
	}
}

// 比较器1：开始时间升序排序
class TimeComparator1 implements Comparator<Pair<Long, Long>> {

	@Override
	public int compare(Pair<Long, Long> arg0, Pair<Long, Long> arg1) {
		int flag = arg0.getKey().compareTo(arg1.getKey());
		if (flag == 0) {
			flag = arg0.getValue().compareTo(arg1.getValue());
		}
		return flag;
	}
	
}

//比较器2：结束时间降序排序
class TimeComparator2 implements Comparator<Pair<Long, Long>> {

	@Override
	public int compare(Pair<Long, Long> arg0, Pair<Long, Long> arg1) {
		int flag = arg1.getValue().compareTo(arg0.getValue());
		if (flag == 0) {
			flag = arg1.getKey().compareTo(arg0.getKey());
		}
		return flag;
	}
	
}
