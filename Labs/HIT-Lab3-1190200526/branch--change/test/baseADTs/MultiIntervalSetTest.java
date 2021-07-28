package baseADTs;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.IllegalIntervalException;

public class MultiIntervalSetTest {
	
	/*
	 * Testing strategy for MultiIntervalSet():
	 * 对于创建空对象的无参数构造函数：
	 * 使用observer方法isEmpty()方法判断是否为空
	 * 对于创建有初始数据的对象的构造函数：
	 * 使用observer方法intervals()检查其数据是否正确
	 */
	@Test
	public void testConstructors() {
		MultiIntervalSet<String> test1 = new MultiIntervalSet<>();
		assertTrue(test1.isEmpty());
		IntervalSet<String> init = new CommonIntervalSet<>();
		IntervalSet<Integer> result = new CommonIntervalSet<>();
		try {
			init.insert(0L, 1L, "a");
			result.insert(0L, 1L, 0);
		} catch (Exception e) {
			fail("Caught exception!");
		}
		MultiIntervalSet<String> test2 = new MultiIntervalSet<>(init);
		assertEquals(result.toString(), test2.intervals("a").toString());
	}

	/*
	 * Testing strategy for insert():
	 * 使用非法参数调用检查异常抛出
	 * 注：由于异常产生于CommonIntervalSet的insert方法且已测试过，
	 * 此处仅简单测试异常是否被传递至此方法
	 * 插入不同标签 / 相同标签的时间段，
	 * 使用observer方法intervals()、labels()检查结果是否符合预期
	 */
	@Test
	public void testInsert() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		try {
			test.insert(5L, 10L, "a");
			test.insert(0L, 4L, null);
			fail("Failed to catch Exception!");
		} catch (IllegalIntervalException e) {
		} catch (Exception e) {
			fail("Exception type error!");
		}
		try {
			test.insert(12L, 21L, "a");
			test.insert(30L, 40L, "b");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertEquals("IntervalSet: Label Cnt:2 Intervals:0:5-10 1:12-21 ", test.intervals("a").toString());
		assertEquals("IntervalSet: Label Cnt:1 Intervals:0:30-40 ", test.intervals("b").toString());
		Set<String> l = new HashSet<>();
		l.add("a");
		l.add("b");
		assertEquals(l, test.labels());
	}
	
	/*
	 * Testing strategy for labels():
	 * 空对象 / 无重复标签 / 有重复标签
	 * 检查其返回的集合是否正确
	 */
	@Test
	public void testLabels() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		assertEquals(Collections.EMPTY_SET, test.labels());
		Set<String> l = new HashSet<>();
		try {
			test.insert(0L, 5L, "a");
			test.insert(6L, 10L, "b");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		l.add("a");
		l.add("b");
		assertEquals(l, test.labels());
		try {
			test.insert(11L, 20L, "a");
			test.insert(21L, 30L, "c");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		l.add("c");
		assertEquals(l, test.labels());
	}
	
	/*
	 * Testing strategy for remove():
	 * 不存在 / 存在且仅有一个相关联的时间段 / 存在且有多个相关联的时间段
	 * 检查返回值是否正确
	 * 使用observer方法检查移除是否正常
	 */
	@Test
	public void testRemove() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		try {
			test.insert(0L, 5L, "a");
			assertFalse(test.remove("b"));
			test.insert(6L, 10L, "b");
			test.insert(11L, 20L, "a");
			test.insert(21L, 30L, "a");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertTrue(test.remove("b"));
		assertTrue(test.intervals("b").isEmpty());
		assertEquals("IntervalSet: Label Cnt:3 Intervals:0:0-5 1:11-20 2:21-30 ", test.intervals("a").toString());
		assertTrue(test.remove("a"));
		assertTrue(test.intervals("a").isEmpty());
		assertTrue(test.isEmpty());
	}
	
	/*
	 * Testing strategy for intervals():
	 * 无关联时间段 / 仅一个关联时间段 / 多个关联时间段
	 * 通过将返回值使用toString()转换后检查是否正确
	 */
	@Test
	public void testIntervals() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		try {
			test.insert(0L, 5L, "a");
			test.insert(6L, 10L, "b");
			test.insert(11L, 20L, "a");
			test.insert(21L, 30L, "a");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertEquals("IntervalSet: Label Cnt:0 Intervals:", test.intervals("c").toString());
		assertEquals("IntervalSet: Label Cnt:3 Intervals:0:0-5 1:11-20 2:21-30 ", test.intervals("a").toString());
		assertEquals("IntervalSet: Label Cnt:1 Intervals:0:6-10 ", test.intervals("b").toString());
	}
	
	/*
	 * Testing strategy for toString():
	 * 空对象 / 标签无重复 / 标签有重复
	 * 检查返回的字符串与预期是否一致
	 */
	@Test
	public void testToString() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		assertEquals("MultiIntervalSet: Label Cnt:0 Detail: ", test.toString());
		try {
			test.insert(0L, 5L, "a");
			test.insert(6L, 10L, "b");
			assertEquals("MultiIntervalSet: Label Cnt:2 Detail: "
					+ "| a - IntervalSet: Label Cnt:1 Intervals:0:0-5 "
					+ "| b - IntervalSet: Label Cnt:1 Intervals:0:6-10 ", test.toString());
			test.insert(11L, 20L, "a");
			test.insert(21L, 30L, "a");
			test.insert(31L, 35L, "c");
			test.insert(36L, 40L, "b");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertEquals("MultiIntervalSet: Label Cnt:3 Detail: "
				+ "| a - IntervalSet: Label Cnt:3 Intervals:0:0-5 1:11-20 2:21-30 "
				+ "| b - IntervalSet: Label Cnt:2 Intervals:0:6-10 1:36-40 "
				+ "| c - IntervalSet: Label Cnt:1 Intervals:0:31-35 ", test.toString());
	}
	
	/*
	 * Testing strategy for isEmpty():
	 * 空对象 / 非空对象
	 * 返回值应分别为true / false
	 */
	@Test
	public void testIsEmpty() {
		MultiIntervalSet<String> test = new MultiIntervalSet<>();
		assertTrue(test.isEmpty());
		try {
			test.insert(0L, 5L, "a");
			assertFalse(test.isEmpty());
			test.insert(11L, 20L, "a");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		test.remove("a");
		assertTrue(test.isEmpty());
	}
}
