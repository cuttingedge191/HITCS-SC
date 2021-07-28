package P2;

import java.util.*;

import P1.graph.Graph;

public class FriendshipGraph {

	private final Graph<Person> graph = Graph.empty();
	
	// Abstraction function:
	/**
	 * graph代表社交网络图
	 * 其中顶点为社交网络中的人
	 * 边为人之间的关系，边权默认均为1
	 */
	
	// Representation invariant:
	// 按照实验要求，沿用ConcreteEdgeGraph的RI
	
	// Safety from rep exposure:
	/**
	 * 将graph设置为private final
	 * 仅能通过公共方法对其进行修改
	 * 无返回其引用的方法
	 */

	/* 构造函数 */
	public FriendshipGraph() {
	}

	/**
	 * 社交网络图添加顶点
	 * 
	 * @param p 被添加的顶点
	 */
	public void addVertex(Person p) {
		// 直接调用Graph类的add()方法
		graph.add(p);
	}

	/**
	 * 社交网络图添加有向边
	 * 
	 * @param p1 边起始顶点
	 * @param p2 边目标顶点
	 */
	public void addEdge(Person p1, Person p2) {
		// 直接调用Graph类的set方法来设置有向边
		graph.set(p1, p2, 1);
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
		if (!graph.vertices().contains(p1) || !graph.vertices().contains(p2)) {
			throw new RuntimeException("Error:At least one vertex is not in the graph!");
		}
		// 使用广度优先搜索的思想求单顶点对最短路径
		Queue<Person> queue = new LinkedList<Person>(); // 队列用于广度优先搜索
		Map<Person, Integer> disMap = new HashMap<>(); // 保存中间结果并用于顶点判断是否访问过
		Person curP = p1;
		Person dstP = p2;
		queue.add(curP);
		disMap.put(curP, 0);
		// BFS过程
		while (!queue.isEmpty()) {
			curP = queue.poll();
			int curDis = disMap.get(curP);
			Map<Person, Integer> nextPList = graph.targets(curP);
			for (Person p : nextPList.keySet()) {
				if (!disMap.containsKey(p)) {
					disMap.put(p, curDis + 1);
					if (p == dstP) { // 已经找到目标点并计算出距离
						queue.clear();
						break;
					}
					queue.add(p);
				}
			}
		}
		// 两点之间无通路
		if (!disMap.containsKey(dstP))
			return -1;
		return disMap.get(dstP);
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
