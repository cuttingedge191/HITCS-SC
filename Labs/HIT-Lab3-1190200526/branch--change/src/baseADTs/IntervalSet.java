package baseADTs;
import java.util.Set;

/**
 * 一个mutable的ADT接口。
 * 描述了一组在时间轴上分布的“时间段”（interval），
 * 每个时间段附着一个特定的标签，且标签不重复。
 *
 * @param <L> 时间段附着的标签的类型，可以是任何immutable的ADT
 */
public interface IntervalSet<L> {
	
	/**
	 * 创建一个空的时间段集合
	 * 
	 * @param <L> 时间段附着的标签的类型，可以是任何immutable的ADT
	 * @return 一个新的空时间段集合
	 */
	public static <L> IntervalSet<L> empty() {
		return new CommonIntervalSet<>();
	}
	
	/**
	 * 在集合中插入新的带标签时间段
	 * 
	 * @param start 开始时间
	 * @param end   结束时间
	 * @param label 标签
	 * @throws Exception 标签重复或添加非法时间段时抛出异常
	 *         （非法指空标签或开始时间大于结束时间或时间存在负数）
	 */
	public void insert(long start, long end, L label) throws Exception;
	
	/**
	 * 获得当前时间段集合的标签集合
	 * 
	 * @return 当前对象的标签集合
	 */
	public Set<L> labels();
	
	/**
	 * 从当前时间段集合中移除某个标签所关联的时间段
	 * 
	 * 如果含有此标签关联的时间段，则进行移除，
	 * 否则不进行任何修改。
	 * 
	 * @param label 标签
	 * @return 是否执行了移除
	 */
	public boolean remove(L label);
	
	/**
	 * 返回某个标签对应的时间段的开始时间
	 * 
	 * @param label 标签
	 * @return 此标签对应时间段的开始时间
	 *       假如此标签未对应时间段返回-1
	 */
	public long start(L label);
	
	/**
	 * 返回某个标签对应的时间段的结束时间
	 * 
	 * @param label 标签
	 * @return 此标签对应时间段的结束时间
	 *       假如此标签未对应时间段返回-1
	 */
	public long end(L label);
	
	/**
	 * 判断此时间段集合是否为空
	 * 
	 * @return 时间段集合是否为空的判断结果
	 */
	public boolean isEmpty();
	
}