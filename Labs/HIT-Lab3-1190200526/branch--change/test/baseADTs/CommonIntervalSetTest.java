package baseADTs;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.IllegalIntervalException;
import exceptions.LabelConflictException;

public class CommonIntervalSetTest {

	// 确认assert正常
	@Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
	
	// 检查静态方法empty()是否创建了空对象
	@Test
	public void testEmpty() {
		assertEquals(Collections.EMPTY_SET, IntervalSet.empty().labels());
	}
	
	/*
	 * Testing strategy for insert():
	 * 使用符合要求的参数调用insert()方法，调用observer方法检查是否正确添加
	 * 使用重复标签、空标签、开始时间大于结束时间等情况测试insert()方法输入检测
	 * 最后使用observer方法检查是否会在输入不满足要求时错误地修改对象
	 */
	@Test
	public void testInsert() {
		CommonIntervalSet<String> ciss = new CommonIntervalSet<>();
		Set<String> test = new HashSet<>();
		try {
			ciss.insert(1L, 2L, "a");
			ciss.insert(3L, 4L, "b");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		test.add("a");
		test.add("b");
		// 检查是否正确添加
		assertEquals(test, ciss.labels());
		assertEquals(1L, ciss.start("a"));
		assertEquals(4L, ciss.end("b"));
		try {
			ciss.insert(5L, 6L, "a"); // 重复标签
			fail("Failed to catch Exception!");
		} catch (LabelConflictException e) {
		} catch (Exception e) {
			fail("Exception type error!");
		}
		try {
			ciss.insert(7L, 8L, null); // 空标签
			fail("Failed to catch Exception!");
		} catch (IllegalIntervalException e) {
		} catch (Exception e) {
			fail("Exception type error!");
		}
		try {
			ciss.insert(-5L, 3L, "c"); // 时间存在负数
			fail("Failed to catch Exception!");
		} catch (IllegalIntervalException e) {
		} catch (Exception e) {
			fail("Exception type error!");
		}
		try {
			ciss.insert(4L, 3L, "d"); // 开始时间不小于结束时间
			fail("Failed to catch Exception!");
		} catch (IllegalIntervalException e) {
		} catch (Exception e) {
			fail("Exception type error!");
		}
		// 检查是否被错误修改
		assertEquals(2L, ciss.end("a"));
		assertEquals(3L, ciss.start("b"));
	}

	/*
	 * Testing strategy for remove():
	 * 等价类划分：对象中不存在 / 存在带此标签的时间段
	 * 不存在情况检查方法返回值是否为false，存在情况检查方法返回值是否为true
	 * 并使用observer方法检查删除是否成功及不存在情况是否错误地修改了对象
	 */
	@Test
	public void testRemove() {
		CommonIntervalSet<String> ciss = new CommonIntervalSet<>();
		Set<String> test = new HashSet<>();
		test.add("2");
		try {
			ciss.insert(7L, 10L, "1");
			ciss.insert(2L, 5L, "2");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertTrue(ciss.remove("1"));
		assertEquals(test, ciss.labels());
		assertEquals(-1L, ciss.start("1"));
		assertEquals(5L, ciss.end("2"));
		assertFalse(ciss.remove("3"));
		assertEquals(test, ciss.labels());
		assertTrue(ciss.remove("2"));
		assertEquals(-1L, ciss.end("2"));
		assertFalse(ciss.remove("1"));
		assertEquals(Collections.EMPTY_SET, ciss.labels());
	}
	
	/*
	 * Testing strategy for labels(), start(), end()
	 * 执行合法的操作向时间段集合中添加若干时间段前后
	 * 检查这些observer方法的返回值是否符合预期
	 */
	@Test
	public void testObservers() {
		CommonIntervalSet<String> ciss = new CommonIntervalSet<>();
		assertEquals(Collections.EMPTY_SET, ciss.labels());
		assertEquals(-1L, ciss.start("1"));
		assertEquals(-1L, ciss.end("1"));
		try {
			ciss.insert(5L, 10L, "a");
			ciss.insert(0L, 4L, "b");
			ciss.insert(11L, 20L, "c");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		Set<String> test = new HashSet<>();
		test.add("a");
		test.add("b");
		test.add("c");
		assertEquals(test, ciss.labels());
		assertEquals(5L, ciss.start("a"));
		assertEquals(4L, ciss.end("b"));
		assertEquals(20L, ciss.end("c"));
		test.remove("b");
		ciss.remove("b");
		assertEquals(test, ciss.labels());
		assertEquals(-1L, ciss.start("b"));
		assertEquals(10L, ciss.end("a"));
		assertEquals(11L, ciss.start("c"));
	}
	
	/*
	 * Testing strategy for toString():
	 * 两种情况：初始空集 / 含若干时间段的集合
	 */
	@Test
	public void testToString() {
		CommonIntervalSet<String> ciss = new CommonIntervalSet<>();
		assertEquals("IntervalSet: Label Cnt:0 Intervals:", ciss.toString());
		try {
			ciss.insert(5L, 10L, "a");
			ciss.insert(0L, 5L, "b");
			ciss.insert(11L, 20L, "c");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertEquals("IntervalSet: Label Cnt:3 Intervals:a:5-10 b:0-5 c:11-20 ", ciss.toString());
	}
	
	/*
	 * Testing strategy for isEmpty():
	 * 等价类划分为两类：
	 * 空对象 / 非空对象
	 * 返回值应分别为true / false
	 */
	@Test
	public void testIsEmpty() {
		assertTrue(IntervalSet.empty().isEmpty());
		CommonIntervalSet<String> ciss = new CommonIntervalSet<>();
		try {
			ciss.insert(0L, 1L, "a");
		} catch (Exception e) {
			fail("Caught exception!");
		}
		assertFalse(ciss.isEmpty());
		ciss.remove("a");
		assertTrue(ciss.isEmpty());
	}
}
