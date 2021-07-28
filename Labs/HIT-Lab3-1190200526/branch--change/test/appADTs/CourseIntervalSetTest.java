package appADTs;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import exceptions.TimeOutOfRangeException;

public class CourseIntervalSetTest {

	/*
	 * Testing strategy for CourseIntervalSet<L>:
	 * 通过构造函数初始化对象
	 * 使用在范围内 / 不在范围内的时间段测试insert()方法抛出异常情况
	 * 检查observer方法返回值是否与预期一致
	 * 使用MultiIntervalSet<L>的observer方法检查插入是否成功
	 */
	
	@Test
	public void testCourseIntervalSet() {
		CourseIntervalSet<String> cis = new CourseIntervalSet<>(0L, 20L, 5);
		try {
			cis.insert(0, 5, "a");
			cis.insert(1, 4, "b");
			cis.insert(81, 90, "c");
			fail("Failed to catch exception!");
		} catch (TimeOutOfRangeException e) {
		} catch (Exception ee) {
			fail("Exception type error!");
		}
		assertEquals(0L, cis.getStartTime());
		assertEquals(5, cis.getWeeks());
		Set<String> res = new HashSet<>();
		res.add("a");
		res.add("b");
		assertEquals(res, cis.labels());
		assertEquals("IntervalSet: Label Cnt:5 Intervals:0:0-5 1:20-25 2:40-45 3:60-65 4:80-85 ", cis.intervals("a").toString());
		assertEquals("IntervalSet: Label Cnt:5 Intervals:0:1-4 1:21-24 2:41-44 3:61-64 4:81-84 ", cis.intervals("b").toString());
	}

}
