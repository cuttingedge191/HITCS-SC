/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;


/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    /** Testing strategy（测试策略）
     * 测试用例设计基本按照等价类划分来进行，且保证每个等价类有不少于1个测试用例
     * observers在测试中被调用，相当于一同测试
     * testAdd()：添加同一个点 / 不同的点，
     *            检查返回值并使用observer--vertices()检查结果
	 * testSet()：weight = 0 / > 0 / < 0（非法），不存在的边 / 存在的边，
	 *            点不存在 / 存在，检查返回值
	 * testRemove()：不存在 / 存在的点（存在的点也要测试边移除是否正确），
	 *               检查返回值
	 * testVertices()：空 / 非空（空情况已经被测试）
	 *                 检测返回的集合是否与真实情况一致
	 * testSourceTargets()：同时测试两个函数，空 / 非空情况，
	 *                      检查返回的集合是否与真实情况一致
	 */
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // you may use, change, or remove this test
    	// 使用此函数测试初始空情况，不进行修改
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // 以下是自行编写的测试
    
    @Test
    public void testAdd() {
    	Graph<String> emptyG = emptyInstance();
    	Set<String> res = new HashSet<String>();
    	res.add("1");
    	res.add("2");
    	res.add("3");
    	res.add("aaaaa");
    	assertEquals(true, emptyG.add("1"));
    	assertEquals(true, emptyG.add("2"));
    	assertEquals(false, emptyG.add("1"));
    	assertEquals(true, emptyG.add("3"));
    	assertEquals(true, emptyG.add("aaaaa"));
    	assertEquals(false, emptyG.add("aaaaa"));
    	assertEquals(res, emptyG.vertices());
    }
    
    @Test
    public void testSet() {
    	Graph<String> emptyG = emptyInstance();
    	emptyG.add("a");
    	assertEquals(0, emptyG.set("a", "b", 2));
    	assertEquals(0, emptyG.set("b", "a", 3));
    	assertEquals(2, emptyG.set("a", "b", 5));
    	assertEquals(3, emptyG.set("b", "a", 0));
    	assertEquals(0, emptyG.set("b", "a", 4));
    	assertEquals(0, emptyG.set("c", "d", 0));
    	assertEquals(0, emptyG.set("d", "c", 1));
    	assertEquals(-1, emptyG.set("c", "d", -5));
    }
    
    @Test
    public void testRemove() {
    	Graph<String> emptyG = emptyInstance();
    	emptyG.add("a");
    	emptyG.add("b");
    	emptyG.set("b", "c", 1);
    	emptyG.set("c", "d", 2);
    	assertEquals(false, emptyG.remove("e"));
    	assertEquals(true, emptyG.remove("c"));
    	assertEquals(Collections.EMPTY_MAP, emptyG.targets("b"));
    	assertEquals(Collections.EMPTY_MAP, emptyG.sources("d"));
    	assertEquals(true, emptyG.remove("a"));
    }
    
    @Test
    public void testVertices() {
    	Graph<String> emptyG = emptyInstance();
    	emptyG.add("a");
    	emptyG.add("b");
    	emptyG.set("c", "d", 2);
    	emptyG.remove("c");
    	Set<String> res = new HashSet<String>();
    	res.add("a");
    	res.add("b");
    	res.add("d");
    	assertEquals(res, emptyG.vertices());
    }
    
    @Test
    public void testSourceTargets() {
    	Graph<String> emptyG = emptyInstance();
    	assertEquals(Collections.EMPTY_MAP, emptyG.sources("e"));
    	assertEquals(Collections.EMPTY_MAP, emptyG.targets("e"));
    	Map<String, Integer> res = new HashMap<String, Integer>();
    	res.put("b", 2);
    	res.put("c", 4);
    	emptyG.set("b", "a", 2);
    	emptyG.set("c", "a", 4);
    	assertEquals(res, emptyG.sources("a"));
    	res.clear();
    	res.put("b", 1);
    	res.put("c", 3);
    	emptyG.set("a", "b", 1);
    	emptyG.set("a", "c", 3);
    	assertEquals(res, emptyG.targets("a"));
    	assertEquals(Collections.EMPTY_MAP, emptyG.sources("e"));
    	assertEquals(Collections.EMPTY_MAP, emptyG.targets("e"));
    }
}
