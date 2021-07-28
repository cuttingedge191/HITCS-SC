/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
	/** 
	 * GraphPoet类功能较少且相互依赖，故在同一测试函数中进行测试。
     * 使用不存在文件/空文件/一行内容文件/多行内容文件测试构造方法，
	 * 使用toString()结果是否正确来判断构造方法正确性，
     * 通过无扩充/简单扩充（仅有一个可能的bridge word）/较复杂扩充（多个位置，多选择）检测poem()正确性。
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // tests
    @Test
    public void testGraphPoet() {
    	File f1 = new File("src/P1/poet/doNotExist.txt");
    	File f2 = new File("src/P1/poet/empty.txt");
    	File f3 = new File("src/P1/poet/mugar-omni-theater.txt");
    	File f4 = new File("src/P1/poet/lines.txt");
    	try {
    		GraphPoet gp1 = new GraphPoet(f1);
    		fail("Failed to catch IOException!");
    		gp1.poem("?");
    	} catch (IOException e) {
    	}
    	try {
    		GraphPoet gp2 = new GraphPoet(f2);
    		GraphPoet gp3 = new GraphPoet(f3);
    		GraphPoet gp4 = new GraphPoet(f4);
    		assertEquals("GraphPoet: V:0, E:0, Vs: empty, Es: empty", gp2.toString());
    		assertEquals("GraphPoet: V:11, E:10, Vs: the a test mugar theater of sound this is omni system., "
    				+ "Es: this->is:1 is->a:1 a->test:1 test->of:1 of->the:1 the->mugar:1 mugar->omni:1 "
    				+ "omni->theater:1 theater->sound:1 sound->system.:1", gp3.toString());
    		String input1 = "Test the system.";
    		String input2 = "This is a test of the system.";
    		assertEquals(input1, gp2.poem(input1));
    		assertEquals("Test of the system.", gp3.poem(input1));
    		assertEquals("This is surely a complex test of the interesting system.", gp4.poem(input2));
    	} catch (IOException e) {
    		e.printStackTrace();
    		fail("Unknown error!");
    	}
    }
}
