package labels;

public class Process {

	// rep / fields:
	private final int processID;
	private final String processName;
	private final long minTime;
	private final long maxTime;

	// Abstraction function:
	/**
	 * AF(processID) = 进程ID
	 * AF(processName) = 进程名
	 * AF(minTime) = 最短执行时间
	 * AF(maxTime) = 最长执行时间
	 */

	// Representation invariant:
	// 无要求

	// Safety from rep exposure:
	// 属性均设置为private final

	// constructor
	public Process(int processID, String processName, long minTime, long maxTime) {
		this.processID = processID;
		this.processName = processName;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	/**
	 * 获取进程ID
	 * 
	 * @return 进程ID
	 */
	public int getProcessID() {
		return this.processID;
	}

	/**
	 * 获取进程名
	 * 
	 * @return 进程名
	 */
	public String getProcessName() {
		return this.processName;
	}

	/**
	 * 获取进程最短执行时间
	 * 
	 * @return 进程最短执行时间
	 */
	public long getProcessMinTime() {
		return this.minTime;
	}

	/**
	 * 获取进程最长执行时间
	 * 
	 * @return 进程最长执行时间
	 */
	public long getProcessMaxTime() {
		return this.maxTime;
	}

	@Override
	public String toString() {
		return "" + this.processID + "\t\t" + this.processName + "\t\t" + this.minTime + "\t\t" + this.maxTime;
	}

	@Override
	public int hashCode() {
		return (int) (this.processID + this.processName.hashCode() + this.minTime + this.maxTime);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Process))
			return false;
		Process p = (Process) o;
		return p.processID == this.processID 
				&& p.processName.equals(this.processName) 
				&& p.minTime == this.minTime
				&& p.maxTime == this.maxTime;
	}
}
