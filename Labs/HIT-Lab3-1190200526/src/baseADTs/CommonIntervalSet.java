package baseADTs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import exceptions.IllegalIntervalException;
import exceptions.LabelConflictException;

/**
 * 实现接口IntervalSet<L>
 * 
 * @param <L> 时间段附着的标签的类型，可以是任何immutable的ADT
 */
public class CommonIntervalSet<L> implements IntervalSet<L> {
	
    // rep / fields:
	protected final Set<L> labelSet; // 标签集
	protected final Map<L, Long> startMap; // 开始时间记录
	protected final Map<L, Long> endMap; // 结束时间记录

    // Abstraction function:
    // AF(labelSet) = 标签的集合
    // AF(startMap) = 标签与时间段开始时间绑定对应
    // AF(endMap) = 标签与时间段结束时间绑定对应
	
    // Representation invariant:
    // 1) 一个标签有且仅有一个时间段与其对应
    // 2) startMap和endMap中元素个数相等
    // 3) 时间段开始时间小于结束时间
    // 4) 标签不能为null
    
    // Safety from rep exposure:
    // 1) 类的属性均设置为protected final
    // 2) labels()方法返回mutable属性labelSet时进行defensive copy
	
	// constructor1:
	public CommonIntervalSet() {
		this.labelSet = new HashSet<>();
		this.startMap = new HashMap<>();
		this.endMap = new HashMap<>();
	}
	
	// constructor2:
	public CommonIntervalSet(Set<L> labelSet, Map<L, Long> startMap, Map<L, Long> endMap) {
		this.labelSet = labelSet;
		this.startMap = startMap;
		this.endMap = endMap;
	}
	
	// checkRep
	private void checkRep() {
		for (L label : labelSet) {
			assert (label != null);
			assert (startMap.get(label) != null && endMap.get(label) != null);
			assert (startMap.get(label) < endMap.get(label));
		}
		assert (labelSet.size() == startMap.size());
		assert (startMap.size() == endMap.size());
	}
	
	@Override
	public void insert(long start, long end, L label) throws Exception {
		if (labelSet.contains(label)) // 重复添加抛出异常
			throw new LabelConflictException(label.toString());
		if (label == null || start < 0 || start > end) // 非法时间段
			throw new IllegalIntervalException();
		labelSet.add(label);
		startMap.put(label, start);
		endMap.put(label, end);
		checkRep();
	}

	@Override
	public Set<L> labels() {
		checkRep();
		return new HashSet<L>(labelSet);
	}

	@Override
	public boolean remove(L label) {
		// 如果没有此标签关联的时间段，则不进行任何修改
		// 返回false说明没有可移除的时间段
		if (!labelSet.contains(label))
			return false;
		startMap.remove(label);
		endMap.remove(label);
		labelSet.remove(label);
		checkRep();
		return true;
	}

	@Override
	public long start(L label) {
		checkRep();
		if (startMap.get(label) == null) // 此标签未对应时间段
			return -1;
		return startMap.get(label);
	}

	@Override
	public long end(L label) {
		checkRep();
		if (endMap.get(label) == null) // 此标签未对应时间段
			return -1;
		return endMap.get(label);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IntervalSet: Label Cnt:");
		sb.append(labelSet.size());
		sb.append(" Intervals:");
		for (L label : labelSet) {
			sb.append(label.toString() + ":" + 
		              startMap.get(label).toString() + "-" 
					+ endMap.get(label).toString() + " ");
		}
		checkRep();
		return sb.toString();
	}
	
	@Override
	public boolean isEmpty() {
		if (labelSet.isEmpty())
			return true;
		return false;
	}
	
}
