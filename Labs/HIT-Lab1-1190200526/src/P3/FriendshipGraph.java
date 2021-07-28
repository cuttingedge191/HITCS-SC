package P3;

import java.util.*;

public class FriendshipGraph {
	private List<Person> personList;
	private List<List<Integer>> friendGraph;

	/* 构造函数 */
	public FriendshipGraph() {
		personList = new ArrayList<>();
		friendGraph = new ArrayList<>();
	}

	/**
	 * 社交网络图添加顶点，要求每个顶点对应的姓名不同
	 * 
	 * @param p 被添加的顶点
	 * @throw RuntimeException 重复添加抛出异常
	 */
	public void addVertex(Person p) {
		for (Person q : personList) { // 检测是否重复
			if (p.getName().contentEquals(q.getName()))
				throw new RuntimeException("Error:" + p.getName() + " is already added!");
		}
		personList.add(p);
		extendfriendGraph();
	}

	/**
	 * 社交网络图添加有向边
	 * 
	 * @param p1 边起始顶点
	 * @param p2 边目标顶点
	 * @throw RuntimeException 顶点不存在抛出异常
	 */
	public void addEdge(Person p1, Person p2) {
		int index1, index2;
		index1 = personList.indexOf(p1);
		index2 = personList.indexOf(p2);
		if (index1 < 0 || index2 < 0) {
			throw new RuntimeException("Error:At least one vertex is not in the graph!");
		}
		friendGraph.get(index1).set(index2, 1); // 设置边
	}

	/**
	 * 计算两顶点的距离
	 * 
	 * @param p1 起始顶点
	 * @param p2 目标顶点
	 * @throw RuntimeException 顶点不存在抛出异常
	 * @return 距离
	 */
	public int getDistance(Person p1, Person p2) {
		if (p1 == p2)
			return 0;
		int curIndex, dstIndex;
		curIndex = personList.indexOf(p1);
		dstIndex = personList.indexOf(p2);
		if (curIndex < 0 || dstIndex < 0) {
			throw new RuntimeException("Error:At least one vertex is not in the graph!");
		}
		// 使用广度优先搜索的思想求单顶点对最短路径
		Queue<Integer> queue = new LinkedList<Integer>(); // 队列用于广度优先搜索
		Map<Integer, Integer> disMap = new HashMap<>(); // 保存中间结果并用于顶点判断是否访问过
		queue.add(curIndex);
		disMap.put(curIndex, 0);
		while (!queue.isEmpty()) {
			curIndex = queue.poll();
			int curDis = disMap.get(curIndex);
			for (int i = 0; i < personList.size(); ++i) {
				if (friendGraph.get(curIndex).get(i) == 1 && !disMap.containsKey(i)) {
					disMap.put(i, curDis + 1);
					if (i == dstIndex) {
						queue.clear();
						break;
					}
					queue.add(i);
				}
			}
		}
		// 两点之间无通路
		if (!disMap.containsKey(dstIndex))
			return -1;
		return disMap.get(dstIndex);
	}

	/* 私有方法，添加后扩展friendGraph */
	private void extendfriendGraph() {
		int n = personList.size();
		List<Integer> newLine = new ArrayList<>();
		for (int i = 0; i < n - 1; ++i) {
			newLine.add(0);
			friendGraph.get(i).add(0);
		}
		newLine.add(0);
		friendGraph.add(newLine);
	}

	public static void main(String args[]) {
		FriendshipGraph graph = new FriendshipGraph();
		Person rachel = new Person("Rachel");
		Person ross = new Person("Ross");
		Person ben = new Person("Ben");
		Person kramer = new Person("Kramer");
		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);
		graph.addEdge(rachel, ross);
		graph.addEdge(ross, rachel);
		graph.addEdge(ross, ben);
		graph.addEdge(ben, ross);
		System.out.println(graph.getDistance(rachel, ross));
		// should print 1
		System.out.println(graph.getDistance(rachel, ben));
		// should print 2
		System.out.println(graph.getDistance(rachel, rachel));
		// should print 0
		System.out.println(graph.getDistance(rachel, kramer));
		// should print -1
	}
}
