package apps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import appADTs.ProcessIntervalSet;
import helpers.randomGenerator;
import javafx.util.Pair;
import labels.Process;

public class ProcessScheduleApp {
	
	private static ProcessIntervalSet<Process> pisp;
	private static Map<Process, Long> pTime = new HashMap<>();
	private static List<Process> pList = new ArrayList<>();
	private static final long freeMax = 100; // 最大空闲时间

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String choice;
		System.out.println("--------------------进程调度管理系统--------------------");
		do {
			showMenu();
			System.out.print("请选择（输入功能对应数字，其他退出）：");
			choice = sc.nextLine();
			if (choice.equals("1")) {
				System.out.println("----------添加进程信息----------");
				do {
				} while (addProcess(sc)); // 添加一组进程信息
			} else if (choice.equals("2")) {
				System.out.println("----------随机选择进程----------");
				randomSchedule();
			} else if (choice.equals("3")) {
				System.out.println("----------最短进程优先----------");
				priorSchedule();
			} else
				break;
			System.out.print("按enter以返回功能菜单：");
			sc.nextLine();
		} while (true);
		System.out.println("--------------------程序终止--------------------");
	}

	// 最短进程优先模拟调度
	private static void priorSchedule() {
		if (pList.size() == 0) {
			System.out.println("请先添加进程信息！");
			return;
		}
		List<Pair<Process, Long>> list = new ArrayList<>();
		for (Process p : pList) {
			list.add(new Pair<>(p, p.getProcessMaxTime()));
		}
		list.sort(new ProcessComparator());
		System.out.println("时刻\t调度进程ID\t调度进程名\t最短执行时间\t最长执行时间\t本次调度结束时状态\t本次调度时间\t进程总运行时间");
		long curTime = 0L;
		boolean free = randomGenerator.generateBoolean(pList.size());
		while (list.size() > 0) {
			String s = "" + curTime + "\t";
			if (!free) {
				Process toRun = list.get(0).getKey();
				long max = list.get(0).getValue();
				long minTime = list.get(0).getKey().getProcessMinTime();
				s += toRun.toString() + "\t\t";
				long time = randomGenerator.generateRangeLong(1, max);
				long total = time + toRun.getProcessMaxTime() - max;
				curTime += time + 1;
				if (total >= minTime) {
					s += "执行结束\t\t\t" + time + "\t\t" + total;
					list.remove(0);
				} else {
					s += "待继续\t\t\t" + time + "\t\t" + total;
					long tmp = list.remove(0).getValue();
					list.add(new Pair<>(toRun, tmp - time));
					list.sort(new ProcessComparator());
				}
				free = true;
			} else {
				free = false;
				long wait = randomGenerator.generateRangeLong(0, freeMax);
				curTime += wait + 1;
				s += "空闲\t\t空闲\t\t空闲\t\t空闲\t\t空闲\t\t\t" + wait + "\t\t-";
			}
			System.out.println(s);
		}
	}

	// 随机选择进程模拟调度
	private static void randomSchedule() {
		if (pList.size() == 0) {
			System.out.println("请先添加进程信息！");
			return;
		}
		pisp = new ProcessIntervalSet<>(); // 清空
		pTime.clear();
		for (Process p : pList) {
			pTime.put(p, 0L);
		}
		long curTime = 0L;
		int pre = -1;
		Process nop = new Process(-1, "空闲", 0, 0);
		try {
			System.out.println("时刻\t调度进程ID\t调度进程名\t最短执行时间\t最长执行时间\t本次调度结束时状态\t本次调度时间\t进程总运行时间");
			while (pTime.size() > 0) {
				String s = "" + curTime + "\t";
				int i = randomGenerator.generateRangeInt(0, pList.size());
				if (i != pre && i != pList.size() && pTime.containsKey(pList.get(i))) { // 调度进程
					Process p = pList.get(i);
					s += p.toString() + "\t\t";
					long pt = pTime.get(p);
					long maxTime = p.getProcessMaxTime() - pt;
					long time = randomGenerator.generateRangeLong(1, maxTime);
					if (time + pt >= p.getProcessMinTime()) {
						pTime.remove(p);
						s += "执行结束\t\t\t" + time + "\t\t" + (time+pt);
					} else {
						s += "待继续\t\t\t" + time + "\t\t" + (time+pt);
						time += pt;
						pTime.put(p, time);
					}
					pisp.insert(curTime, curTime+time, p);
					curTime += time + 1;
					pre = i; // 避免连续调度同一进程
				} else { // 空闲
					if (pre == -1) // 避免重复空闲
						continue;
					long wait = randomGenerator.generateRangeLong(0, freeMax);
					s += "空闲\t\t空闲\t\t空闲\t\t空闲\t\t空闲\t\t\t" + wait + "\t\t-";
					pisp.insert(curTime, curTime+wait, nop);
					curTime += wait + 1;
					pre = -1;
				}
				System.out.println(s);
			}
			System.out.println("----------模拟调度结束----------");
		} catch (Exception e) { // 一般来说不会抛出异常
			e.printStackTrace();
			return;
		}
	}

	// 添加进程信息
	private static boolean addProcess(Scanner sc) {
		System.out.println("请按顺序输入进程ID、名称、最短执行时间、最长执行时间（空格隔开，输入#结束）：");
		String input = sc.nextLine();
		String[] record = input.split(" ");
		if (record[0].equals("#"))
			return false;
		if (record.length != 4) {
			System.out.println("添加失败：输入格式错误！");
			return false;
		}
		for (Process p : pList) {
			if (p.getProcessID() == Integer.valueOf(record[0])) {
				System.out.println("添加失败：ID为[" + record[0] + "]的进程已经存在！");
				return false;
			}
		}
		Process toAdd = new Process(Integer.valueOf(record[0]), record[1], 
						Long.parseLong(record[2]), Long.parseLong(record[3]));
		pList.add(toAdd);
		return true;
	}

	// 显示功能菜单
	private static void showMenu() {
		System.out.println("----------功能菜单----------");
		System.out.println("[1] 添加一组进程");
		System.out.println("[2] 随机选择进程模拟调度");
		System.out.println("[3] 最短进程优先模拟调度");
	}

}

// 比较器
// 按进程最大执行时间升序排序
class ProcessComparator implements Comparator<Pair<Process, Long>> {

	@Override
	public int compare(Pair<Process, Long> arg0, Pair<Process, Long> arg1) {
		int flag;
		if (arg0.getKey().getProcessMaxTime() - arg1.getKey().getProcessMaxTime() > 0)
			flag = 1;
		else
			flag = -1;
		return flag;
	}
	
}
