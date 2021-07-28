/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // test other vertex label types in Problem 3.2
    
    // 测试Integer作为L时的正确性
    // 相当于简化整合的GraphInstanceTest
    @Test
    public void testIntegerAsL(){
        final Graph<Integer> graph = Graph.empty();
        final Integer vertex1 = 1;
        final Integer vertex2 = 2;
        final Integer vertex3 = 3;
        final int weight1 = 2147483647;
        final int weight2 = 100;
        assertTrue(graph.add(vertex1));
        assertTrue(graph.add(vertex2));
        assertFalse(graph.add(vertex1));
        assertEquals(0, graph.set(vertex1, vertex2, weight2));
        assertEquals(0, graph.set(vertex3, vertex1, weight1));
        assertEquals(weight1, graph.set(vertex3, vertex1, weight2));   
        assertEquals(weight2, graph.set(vertex1, vertex2, 0));
        assertEquals(0, graph.set(vertex2, vertex1, weight1));
        assertTrue(graph.remove(vertex3));
        assertFalse(graph.remove(vertex3));
        assertFalse(graph.vertices().contains(vertex3));
        Map<Integer, Integer> test = new HashMap<>();
        test.put(vertex2, weight1);
        assertEquals(test, graph.sources(vertex1));
        test.clear();
        test.put(vertex1, weight1);
        assertEquals(test, graph.targets(vertex2));
    }
    
    // 测试Character作为L时的正确性
    // 相当于简化整合的GraphInstanceTest
    @Test
    public void testCharacterAsL(){
        final Graph<Character> graph = Graph.empty();
        final Character vertex1 = '1';
        final Character vertex2 = '2';
        final Character vertex3 = '3';
        final int weight1 = 2147483647;
        final int weight2 = 100;
        assertTrue(graph.add(vertex1));
        assertTrue(graph.add(vertex2));
        assertFalse(graph.add(vertex1));
        assertEquals(0, graph.set(vertex1, vertex2, weight2));
        assertEquals(0, graph.set(vertex3, vertex1, weight1));
        assertEquals(weight1, graph.set(vertex3, vertex1, weight2));   
        assertEquals(weight2, graph.set(vertex1, vertex2, 0));
        assertEquals(0, graph.set(vertex2, vertex1, weight1));
        assertTrue(graph.remove(vertex3));
        assertFalse(graph.remove(vertex3));
        assertFalse(graph.vertices().contains(vertex3));
        Map<Character, Integer> test = new HashMap<>();
        test.put(vertex2, weight1);
        assertEquals(test, graph.sources(vertex1));
        test.clear();
        test.put(vertex1, weight1);
        assertEquals(test, graph.targets(vertex2));
    }
}
