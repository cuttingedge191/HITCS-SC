package P3;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendshipGraphTest {

	/**
	 * 测试addVertex()方法
	 * 由于此方法无返回值，此测试仅检测重复添加时是否抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void addVertexTest() {
		Person p1 = new Person("name");
		Person p2 = new Person("name");
		FriendshipGraph graph = new FriendshipGraph();
		// 加入重复名字
		graph.addVertex(p1);
		graph.addVertex(p2);
	}

	/**
	 * 测试addEdge()方法
	 * 由于此方法无返回值，此测试仅检测存在非图中顶点时是否抛出异常
	 */
	@Test(expected = RuntimeException.class)
	public void addEdgeTest() {
		Person p_in = new Person("abc");
		Person p_out = new Person("def");
		FriendshipGraph graph = new FriendshipGraph();
		graph.addVertex(p_in);
		// p_in顶点在图中，p_out顶点未加入图，无法构建边
		graph.addEdge(p_in, p_out);
	}

	/**
	 * 测试getDistance()方法
	 * 由于在之前的测试中包含了异常处理，此测试主要检测稍复杂网络图的运行情况
	 */
	@Test
	public void getDistanceTest() {
		// 创建九个顶点
		Person p1 = new Person("a");
		Person p2 = new Person("b");
		Person p3 = new Person("c");
		Person p4 = new Person("d");
		Person p5 = new Person("e");
		Person p6 = new Person("f");
		Person p7 = new Person("g");
		Person p8 = new Person("h");
		FriendshipGraph graph = new FriendshipGraph();
		graph.addVertex(p1);
		graph.addVertex(p2);
		graph.addVertex(p3);
		graph.addVertex(p4);
		graph.addVertex(p5);
		graph.addVertex(p6);
		graph.addVertex(p7);
		graph.addVertex(p8);
		graph.addEdge(p1, p2);
		graph.addEdge(p2, p1);
		graph.addEdge(p1, p3);
		graph.addEdge(p3, p1);
		graph.addEdge(p3, p4);
		graph.addEdge(p4, p3);
		graph.addEdge(p2, p3);
		graph.addEdge(p3, p2);
		graph.addEdge(p2, p4);
		graph.addEdge(p4, p2);
		graph.addEdge(p2, p6);
		graph.addEdge(p6, p2);
		graph.addEdge(p6, p7);
		graph.addEdge(p8, p7);
		graph.addEdge(p6, p8);
		// 测试不可达
		assertEquals(-1, graph.getDistance(p5, p4));
		assertEquals(-1, graph.getDistance(p4, p5));
		// 测试单向可达
		assertEquals(-1, graph.getDistance(p7, p6));
		assertEquals(1, graph.getDistance(p6, p7));
		// 测试正常情况下的最短距离
		assertEquals(2, graph.getDistance(p3, p6));
		assertEquals(1, graph.getDistance(p2, p3));
		// 测试含单向边的最短距离
		assertEquals(3, graph.getDistance(p3, p7));
		assertEquals(-1, graph.getDistance(p7, p3));
		// 测试顶点到自身的距离
		assertEquals(0, graph.getDistance(p5, p5));
		assertEquals(0, graph.getDistance(p7, p7));
	}
}
