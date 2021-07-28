package baseADTs;

import static org.junit.Assert.*;

import org.junit.Test;

public class APIsTest {

	/*
	 * Testing strategy for APIs:
	 * 进行简单的功能正确性测试（实验未要求）
	 */
	@Test
	public void testAPIs() {
		CommonIntervalSet<String> test1 = new CommonIntervalSet<>();
		try {
			test1.insert(1, 10, "a");
			test1.insert(15, 20, "b");
			test1.insert(2, 6, "c");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Caught exception!");
		}
		assertEquals(0.25, APIs.calcConflictRatio(test1), 0.001);
		assertEquals(0.25, APIs.calcFreeTimeRatio(test1), 0.001);
		MultiIntervalSet<String> test2 = new MultiIntervalSet<>(test1);
		MultiIntervalSet<String> test3 = new MultiIntervalSet<>(test1);
		try {
			test3.insert(25, 30, "d");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(0.25, APIs.calcConflictRatio(test2), 0.001);
		assertEquals(0.166, APIs.calcConflictRatio(test3), 0.001);
		assertEquals(0.25, APIs.calcFreeTimeRatio(test2), 0.001);
		assertEquals(0.333, APIs.calcFreeTimeRatio(test3), 0.001);
		assertEquals(0.70, APIs.Similarity(test2, test3), 0.001);
	}

}
