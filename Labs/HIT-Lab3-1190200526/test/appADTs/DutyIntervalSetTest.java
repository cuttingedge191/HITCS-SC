package appADTs;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.IntervalConflictException;
import helpers.dateHandler;

public class DutyIntervalSetTest {
	
	private PrintStream console = null;
    private ByteArrayOutputStream bytes = null;
 
    @Before
    public void setUp() {
 
        bytes = new ByteArrayOutputStream(); //把标准输出指定到ByteArrayOutputStream中
        console = System.out; // 获取System.out输出流的句柄
        System.setOut(new PrintStream(bytes)); // 将原本输出到控制台Console的字符流重定向到bytes
 
    }
    
    @After
    public void tearDown() {
        System.setOut(console);
    }
    
	/*
	 * Testing strategy for DutyIntervalSet<L>:
	 * 由于方法较少，且功能相互依赖，对其进行整体的测试
	 * 使用构造函数创建一个新对象
	 * 空 / 存在空白 / 不存在空白测试checkNoBlank()，检查输出信息
	 * 非重叠 / 重叠时间段测试insert()方法，检查异常抛出
	 * 在 / 不在起止时间范围内测试checkInRange()方法，检查返回值
	 * 检查observer方法的返回值是否与预期相符
	 */
	@Test
	public void testAll() {
		DutyIntervalSet<String> test = new DutyIntervalSet<>(dateHandler.parseDate("2021-01-01"), dateHandler.parseDate("2021-01-30"));
		test.checkNoBlank();
		String expected = "空白段1:2021-01-01~2021-01-30\n空白所占比例：100%（未进行排班）";
        assertEquals(expected, bytes.toString().trim().replace("\r",""));
        try {
        	test.insert(dateHandler.parseDate("2021-01-01"), dateHandler.parseDate("2021-01-15"), "1");
        	test.insert(dateHandler.parseDate("2021-01-06"), dateHandler.parseDate("2021-01-19"), "2");
        	fail("Failed to catch exception!");
        } catch (IntervalConflictException e) {
        } catch (Exception ee) {
        	fail("Wrong exception type!");
        }
        test.checkNoBlank();
        expected += "\n空白段1:2021-01-16~2021-01-30\n空白所占比例：50.00%";
        assertEquals(expected, bytes.toString().trim().replace("\r",""));
        assertFalse(test.checkInRange(dateHandler.parseDate("2021-01-05"), dateHandler.parseDate("2021-02-05")));
        assertTrue(test.checkInRange(dateHandler.parseDate("2021-01-10"), dateHandler.parseDate("2021-01-20")));
        try {
        	test.insert(dateHandler.parseDate("2021-01-16"), dateHandler.parseDate("2021-01-30"), "2");
        } catch (Exception e) {
        	fail("Caught exception!");
        }
        test.checkNoBlank();
        expected += "\n当前排班已无空白！";
        assertEquals(expected, bytes.toString().trim().replace("\r",""));
        assertEquals(dateHandler.parseDate("2021-01-01"), test.getStartTime());
        assertEquals(dateHandler.parseDate("2021-01-30"), test.getEndTime());
        assertEquals(30, test.getTotalDays());
	}

}