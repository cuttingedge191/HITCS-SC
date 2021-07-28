package P2;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendshipGraphTest {

	/**
	 * 整体测试
	 * 此测试主要检测稍复杂网络图的运行情况
	 * 也包括对getDistance()非法情况抛出异常的检测
	 */
	@Test
	public void FriendshipGraphAllTest() {
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
		// 检测Person构造函数
		assertEquals("a", p1.getName());
		assertEquals("h", p8.getName());
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
		// 测试getDistance()非法情况
		try {
			Person pNot = new Person("?");
			graph.getDistance(p1, pNot);
			fail("Failed to catch RuntimeException!");
		} catch (RuntimeException e) {
		}
	}
}
