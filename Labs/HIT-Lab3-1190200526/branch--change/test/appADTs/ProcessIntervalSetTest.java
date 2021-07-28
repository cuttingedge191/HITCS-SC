package appADTs;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.IntervalConflictException;

public class ProcessIntervalSetTest {

	/*
	 * Testing strategy for DutyIntervalSet<L>:
	 * 简单测试insert()方法
	 * 重叠时间段 / 非重叠时间段（非法输入之前已经测试过）
	 * 检测异常抛出情况
	 * 使用继承于MultiIntervalSet<L>的observer方法检查插入是否成功
	 */
	@Test
	public void testProcessIntervalSet() {
		ProcessIntervalSet<String> test = new ProcessIntervalSet<>();
		try {
			test.insert(0, 10, "a");
			test.insert(11, 20, "a");
			test.insert(21, 25, "b");
			test.insert(5, 16, "c");
			fail("Failed to catch exception!");
		} catch (IntervalConflictException e) {
		} catch (Exception ee) {
			fail("Wrong exception type!");
		}
		Set<String> res = new HashSet<>();
		res.add("a");
		res.add("b");
		assertEquals(res, test.labels());
		assertEquals("IntervalSet: Label Cnt:2 Intervals:0:0-10 1:11-20 ", test.intervals("a").toString());
	}

}
