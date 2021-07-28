/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    // 使用几个合法的简单点图检查返回的字符串是否正确
    // 空图、有顶点无边图、有顶点有边图
    
    // tests for ConcreteVerticesGraph.toString()
    @Test
    public void testVerticeGraphToString() {
    	Graph<String> emptyG = emptyInstance();
    	assertEquals("V:0, Detail:", emptyG.toString());
    	emptyG.add("a");
    	emptyG.add("b");
    	assertEquals("V:2, Detail:Vertex a source: target: | Vertex b source: target: | ", emptyG.toString());
    	emptyG.set("b", "c", 1);
    	emptyG.set("a", "c", 2);
    	emptyG.set("a", "b", 3);
    	assertEquals("V:3, Detail:Vertex a source: target: b-3 c-2 | Vertex b source: a-3 target: c-1 | Vertex c source: a-2 b-1 target: | ", emptyG.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    /**
     * 测试Vertex类中的各方法
     * 使用违反RI的测试检查checkRep()是否正确
     * 使用合法输入，通过observer检测构造函数，同时也测试了observer
     * 分别用顶点名非空不同且weight > 0 / = 0 / < 0（非法）的情况测试setIn()、setOut()
     * （顶点名为空的情况违反RI，检查checkRep()时测试）
     * 使用合法输入（顶点无边/有边）检查toString()方法返回值是否与预期相同
     */
    
    // tests for operations of Vertex
    
	@Test
    public void testVertexCheckRep() {
    	try {
    		Vertex<String> v1 = new Vertex<>(null);
    		fail("Failed to catch AssertionError!");
    		v1.setIn(null, 0);
    	} catch (AssertionError e) {
    	}
    	try {
    		Vertex<String> v2 = new Vertex<>("a");
    		v2.setIn(null, 1);
    		fail("Failed to catch AssertionError!");
    	} catch (AssertionError e) {
    	}
    	try {
    		Vertex<String> v3 = new Vertex<>("b");
    		v3.setOut("b", 2);
    		fail("Failed to catch AssertionError!");
    	} catch (AssertionError e) {
    	}
    }
    
    @Test
    public void testVertex() {
    	Vertex<String> v = new Vertex<>("a");
    	assertEquals(Collections.EMPTY_MAP, v.getIns());
    	assertEquals(Collections.EMPTY_MAP, v.getOuts());
    	assertEquals("a", v.getName());
    }
    
    @Test
    public void testSetIn() {
    	Vertex<String> v = new Vertex<>("1");
    	assertEquals(0, v.setIn("2", 5));
    	assertEquals(5, v.setIn("2", 0));
    	assertEquals(0, v.setIn("2", 0));
    	assertEquals(-1, v.setIn("2", -3));
    }
    
    @Test
    public void testSetOut() {
    	Vertex<String> v = new Vertex<>("1");
    	assertEquals(0, v.setOut("2", 5));
    	assertEquals(5, v.setOut("2", 0));
    	assertEquals(0, v.setOut("2", 0));
    	assertEquals(-1, v.setOut("2", -3));
    }
    
    @Test
    public void testVertexToString() {
    	Vertex<String> v = new Vertex<>("a");
    	assertEquals("Vertex a source: target:", v.toString());
    	v.setIn("b", 1);
    	v.setOut("c", 2);
    	assertEquals("Vertex a source: b-1 target: c-2", v.toString());
    	v.setIn("b", 0);
    	assertEquals("Vertex a source: target: c-2", v.toString());
    }
}
