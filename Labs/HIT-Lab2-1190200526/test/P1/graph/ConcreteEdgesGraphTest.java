/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    // 使用几个合法的简单边图检查返回的字符串是否正确
    // 空图、有顶点无边图、有顶点有边图
    
    // tests for ConcreteEdgesGraph.toString()
    @Test
    public void testEdgeGraphToString() {
    	Graph<String> emptyG = emptyInstance();
    	assertEquals("V:0, E:0, Vs: empty, Es: empty", emptyG.toString());
    	emptyG.add("a");
    	emptyG.add("b");
    	assertEquals("V:2, E:0, Vs: a b, Es: empty", emptyG.toString());
    	emptyG.set("a", "b", 1);
    	emptyG.set("b", "c", 2);
    	assertEquals("V:3, E:2, Vs: a b c, Es: a->b:1 b->c:2", emptyG.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    /**
     * 测试Edge类中的各方法
     * 使用空顶点、负权和始终点相同情况检测checkRep()是否正常（在构造函数里被调用）
     * 使用合法输入，通过observer检查构造函数是否正常
     * 相同边 / 不同边检测isSameEdge()是否正确
     * 合法输入检查toString返回值是否与预期相同
     */
    
    // tests for operations of Edge
    
	@Test
    public void testEdgeCheckRep() {
    	try {
    		Edge<String> e1 = new Edge<>("a", "b", -1);
    		fail("Failed to catch AssertionError!");
    		e1.getWeight();
    	} catch (AssertionError e) {
    	}
    	try {
    		Edge<String> e2 = new Edge<>("a", "a", 1);
    		fail("Failed to catch AssertionError!");
    		e2.getWeight();
    	} catch (AssertionError e) {
    	}
    	try {
    		Edge<String> e3 = new Edge<>(null, "b", 1);
    		fail("Failed to catch AssertionError!");
    		e3.getWeight();
    	} catch (AssertionError e) {
    	}
    }
    
    @Test
    public void testEdge() {
    	Edge<String> e = new Edge<>("a", "b", 5);
    	assertEquals("a", e.getStart());
    	assertEquals("b", e.getEnd());
    	assertEquals(5, e.getWeight());
    }
    
    @Test
    public void testIsSameEdge() {
    	Edge<String> e = new Edge<>("1", "2", 3);
    	assertEquals(true, e.isSameEdge("1", "2"));
    	assertEquals(false, e.isSameEdge("2", "1"));
    }
    
    @Test
    public void testEdgeToString() {
    	Edge<String> e = new Edge<>("1", "2", 3);
    	assertEquals("1->2:3", e.toString());
    }
}
