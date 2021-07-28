package baseADTs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 描述了一组在时间轴上分布的“时间段”（interval），
 * 每个时间段附着一个特定的标签，
 * 但同一个标签对象可被绑定到多个时间段上。
 *
 * @param <L> 时间段附着的标签的类型，可以是任何immutable的ADT
 */
public class MultiIntervalSet<L> {
	
	// rep / fields:
	protected final List<IntervalSet<L>> multiList;
	
	// Abstraction function:
    // 由多个内部无重复标签的时间段集合组成，
    // 但各集合间可能有相同标签但时间不同的时间段，
    // 以此表示一个标签对应多个时间段。

    // Representation invariant:
    // 1) 各集合内部符合IntervalSet<L>的RI
    // 2) mutiList不为null
    
    // Safety from rep exposure:
    // 1) 类的属性设置为protected
    // 2) 没有公共方法返回对mutable属性的引用
	// 3) 带参数的构造函数复制参数，避免参数修改导致对象属性被修改
	
	// checkRep
	private void checkRep() {
		assert (this.multiList != null); 
	}
	
	/**
	 * 不带任何参数的构造函数
	 * 创建一个空对象
	 */
	public MultiIntervalSet() {
		this.multiList = new ArrayList<>();
		this.multiList.add(IntervalSet.empty());
		checkRep();
	}
	
	/**
	 * 带参数的构造函数1
	 * 创建一个有初始数据的对象
	 * 
	 * @param initial 作为初始数据的时间段集合
	 */
	public MultiIntervalSet(IntervalSet<L> initial) {
		this.multiList = new ArrayList<>();
		this.multiList.add(IntervalSet.empty());
		try { // 复制并检测，避免直接使用传入参数的引用而导致传入参数在之后被修改和表示泄露
			for (L label : initial.labels()) {
				this.insert(initial.start(label), initial.end(label), label);
			}
		} catch (Exception e) {
			// 说明initial集合存在非法数据等问题，创建空对象
			this.multiList.clear();
			this.multiList.add(IntervalSet.empty());
		}
		checkRep();
	}
	
	/**
	 * 带参数的构造函数2
	 * 使用存有IntervalSet的List初始化
	 * @param multiList 存有IntervalSet的List
	 */
	public MultiIntervalSet(List<IntervalSet<L>> multiList) {
		this.multiList = multiList;
	}
	
	/**
	 * 在当前对象中插入新的时间段和标签
	 * 
	 * @param start 开始时间
	 * @param end  结束时间
	 * @param label 标签
	 * @throws Exception 非法时间段抛出异常
	 */
	public void insert(long start, long end, L label) throws Exception {
		for (IntervalSet<L> i : multiList) {
			if (!i.labels().contains(label)) {
				// 当前集合中无带此标签的时间段则插入
				i.insert(start, end, label);
				checkRep();
				return;
			}
		}
		// 所有时间段集合中均有带此标签的时间段
		// 需要创建一个新的时间段集合来加入
		IntervalSet<L> toAdd = IntervalSet.empty();
		toAdd.insert(start, end, label);
		this.multiList.add(toAdd);
		checkRep();
	}
	
	/**
	 * 获得当前对象中的标签集合
	 * 
	 * @return 标签集合
	 */
	public Set<L> labels() {
		checkRep();
		// 由于仅在前一个集合中此标签出现过才插入到其后的集合中，
		// 最前面的集合会包含所有标签
		return multiList.get(0).labels();
	}
	
	/**
	 * 从当前对象中移除某个标签所关联的所有时间段
	 * 
	 * 如果含有此标签对应的时间段，则进行移除，
	 * 否则不进行任何修改。
	 * 
	 * @param label 标签
	 * @return 是否执行了移除
	 */
	public boolean remove(L label) {
		// 如果没有此标签对应的时间段，则不进行任何修改
		// 返回false说明没有可移除的时间段
		if (!this.labels().contains(label))
			return false;
		// 否则移除此标签关联的所有时间段
		Iterator<IntervalSet<L>> it = multiList.iterator();
		while (it.hasNext()) {
			IntervalSet<L> tmp = it.next();
			tmp.remove(label);
			if (tmp.isEmpty() && multiList.size() > 1)
				it.remove(); // 若集合为空且不是唯一元素，从对象中删除
		}
		return true;
	}
	
	/**
	 * 从当前对象中获取与某个标签所关联的所有时间段
	 * 其中的时间段按开始时间从小到大的次序排列
	 * 
	 * @param label 标签
	 * @return 按开始时间从小到大的次序排列的时间段集合
	 *         如果没有此标签对应的时间段，返回空集
	 */
	public IntervalSet<Integer> intervals(L label) {
		// 如果没有此标签对应的时间段，返回空集
		if (!this.labels().contains(label))
			return IntervalSet.empty();
		// 用于排序的Map，key记录起始时间，value为时间段集合在对象中的索引值
		Map<Long, Integer> sort = new TreeMap<>();
		for (int i = 0; i < multiList.size(); ++i) {
			if (multiList.get(i).start(label) != -1) {
				sort.put(multiList.get(i).start(label), i);
			}
		}
		Collection<Integer> c = sort.values();
		Iterator<Integer> it = c.iterator();
		IntervalSet<Integer> result = IntervalSet.empty();
		int cnt = 0;
		try {
			while (it.hasNext()) {
				// 按开始时间次序向结果集添加项
				int v = (int)it.next();
				long s = multiList.get(v).start(label);
				long e = multiList.get(v).end(label);
				result.insert(s, e, cnt);
				++cnt;
			}
		} catch (Exception e) { // 正常情况下不会抛出异常
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MultiIntervalSet: Label Cnt:");
		sb.append(this.labels().size());
		sb.append(" Detail: ");
		for (L l : this.labels()) {
			sb.append("| " + l.toString() + " - ");
			sb.append(this.intervals(l));
		}
		return sb.toString();
	}
	
	/**
	 * 判断对象是否为空
	 * 
	 * @return 对象是否为空的判断结果
	 */
	public boolean isEmpty() {
		if (this.multiList.size() == 1 && multiList.get(0).isEmpty())
			return true;
		return false;
	}
	
}
