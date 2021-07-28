package helpers;

public class randomGenerator {
	
	/**
	 * 产生在[start, end]范围内的long型值
	 * 
	 * @param start 最小值
	 * @param end 最大值
	 * @return [start, end]范围内的long型值
	 */
	public static long generateRangeLong(long start, long end) {
		return (start + (long)((end - start + 1) * Math.random()));
	}
	
	/**
	 * 产生在[start, end]范围内的int型值
	 * 
	 * @param start 最小值
	 * @param end 最大值
	 * @return [start, end]范围内的int型值
	 */
	public static int generateRangeInt(int start, int end) {
		return (start + (int)((end - start + 1) * Math.random()));
	}
	
	
	public static boolean generateBoolean(int p) {
		double pos = 1.0 / (double)p;
		if (Math.random() < pos) {
			return true;
		}
		return false;
	}
}
