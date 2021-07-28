package apps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import appADTs.CourseIntervalSet;
import baseADTs.APIs;
import helpers.dateHandler;
import javafx.util.Pair;
import labels.Course;

public class CourseScheduleApp {

	private static CourseIntervalSet<Course> cisc;
	private static final Map<Course, Integer> hourMap = new HashMap<>(); // 记录每门课程剩余可安排的周学时数
	private static final long period = dateHandler.incnDayLong(0, 7); // 一周7天
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String choice;
		System.out.println("--------------------课表管理系统--------------------");
		if (!setGeneral(sc)) { // 学期开始日期、总周数设置错误则退出
			sc.close();
			return;
		}
		Formatter f = new Formatter(System.out);
		do {
			showMenu();
			System.out.print("请选择（输入功能对应数字，其他退出）：");
			choice = sc.nextLine();
			if (choice.equals("1")) {
				System.out.println("----------添加课程信息----------");
				do {
				} while (addCourse(sc)); // 添加一组课程信息
			} else if (choice.equals("2")) {
				System.out.println("----------安排课程时间----------");
				arrangeCourse(sc);
			} else if (choice.equals("3")) {
				System.out.println("----------查看安排情况----------");
				checkArrangement(f);
			} else if (choice.equals("4")) {
				System.out.println("----------单日课表查询----------");
				showDaySchedule(sc);
			} else
				break;
			System.out.print("按enter以返回功能菜单：");
			sc.nextLine();
		} while (true);
		sc.close();
		System.out.println("--------------------程序终止--------------------");
		f.close();
	}
	
	// 展示指定日期课表
	private static void showDaySchedule(Scanner sc) {
		System.out.print("请输入要查询的日期（格式：yyyy-MM-dd）：");
		String input = sc.nextLine();
		long dateL = dateHandler.parseDate(input);
		if (dateL < cisc.getStartTime() || dateL > cisc.getStartTime() + period * cisc.getWeeks()) {
			System.out.println("输入的日期超出学期范围！");
			return;
		}
		int d = dateHandler.calcWeekDay(dateL) - dateHandler.calcWeekDay(cisc.getStartTime()); // 在一周中与开学日期的距离
		long min = dateHandler.incnDayLong(cisc.getStartTime(), d);
		List<Pair<Integer, Course>> info = new ArrayList<>();
		long t;
		for (Course c : hourMap.keySet()) {
			int i = 0;
			do {
				t = cisc.intervals(c).start(i);
				++i;
			} while (t < min && t != -1); // 确定第一个可能在当天的课程时间
			while (true) { // 记录所有在当天的此课程
				Integer s = dateHandler.convertCourseTimeToSchedule(min, t);
				if (s == -1)
					break;
				info.add(new Pair<>(s, c));
				t = cisc.intervals(c).start(i);
				++i;
			}
		}
		info.sort(new ScheduleComparator());
		String[] week = {"一", "二", "三", "四", "五", "六", "日"};
		String s;
		s = input + " （周" + week[dateHandler.calcWeekDay(dateHandler.parseDate(input)) - 1] + "） 课程表";
		int size = info.size();
		if (size == 0) {
			s += "\n---无课程---";
			System.out.println(s);
			return;
		}
		System.out.println(s);
		int i = 0;
		System.out.println("---08:00 - 10:00---");
		while (i < size && info.get(i).getKey() == 8) {
			Course tmp = info.get(i).getValue();
			System.out.println("[" + tmp.getCourseID() + "] "+ tmp.getCourseName() + " "
								+ tmp.getTeacherName() + "@" + tmp.getLocation());
			++i;
		}
		System.out.println("---10:00 - 12:00---");
		while (i < size && info.get(i).getKey() == 10) {
			Course tmp = info.get(i).getValue();
			System.out.println("[" + tmp.getCourseID() + "] "+ tmp.getCourseName() + " "
								+ tmp.getTeacherName() + "@" + tmp.getLocation());
			++i;
		}
		System.out.println("---13:00 - 15:00---");
		while (i < size && info.get(i).getKey() == 13) {
			Course tmp = info.get(i).getValue();
			System.out.println("[" + tmp.getCourseID() + "] "+ tmp.getCourseName() + " "
								+ tmp.getTeacherName() + "@" + tmp.getLocation());
			++i;
		}
		System.out.println("---15:00 - 17:00---");
		while (i < size && info.get(i).getKey() == 15) {
			Course tmp = info.get(i).getValue();
			System.out.println("[" + tmp.getCourseID() + "] "+ tmp.getCourseName() + " "
								+ tmp.getTeacherName() + "@" + tmp.getLocation());
			++i;
		}
		System.out.println("---19:00 - 21:00---");
		while (i < size && info.get(i).getKey() == 19) {
			Course tmp = info.get(i).getValue();
			System.out.println("[" + tmp.getCourseID() + "] "+ tmp.getCourseName() + " "
								+ tmp.getTeacherName() + "@" + tmp.getLocation());
			++i;
		}
	}

	// 查看安排情况
	private static void checkArrangement(Formatter f) {
		f.format("%-8s %-8s %-10s %-10s %-10s %-10s\n", 
				"课程ID", "课程名称", "教师姓名", "地点", "剩余/总周学时", "安排情况");
		for (Course c : hourMap.keySet()) {
			Integer r = hourMap.get(c);
			f.format("%-10d %-12s %-12s %-12s %5d / %-5d", c.getCourseID(), c.getCourseName(), 
					c.getTeacherName(), c.getLocation(), r, c.hourPerWeek());
			if (r == 0)
				f.format("%8s\n", "安排完成");
			else
				f.format("%8s\n", "待安排");
		}
		System.out.printf("空闲时间比例：%.2f%%\n", (APIs.calcFreeTimeRatio(cisc) * 24 - 14) * 10);
		System.out.printf("重复时间比例：%.2f%%\n", APIs.calcConflictRatio(cisc) * 240);
	}

	// 手动安排课程
	private static void arrangeCourse(Scanner sc) {
		System.out.print("请输入要进行安排课程的ID：");
		String input = sc.nextLine();
		int ID;
		boolean ok = false;
		try {
			ID = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			System.out.println("安排失败：输入格式错误！");
			return;
		}
		for (Course c : hourMap.keySet()) {
			if (c.getCourseID() == ID) {
				if (hourMap.get(c) == 0) {
					System.out.println("安排失败：此课程周学时已经安排满！");
					return;
				}
				System.out.print("请输入安排的上课时间（如周四，8-10时应输入“4 8-10”）：");
				input = sc.nextLine();
				String[] record = input.split(" ");
				if (record.length != 2) {
					System.out.println("安排失败：输入格式错误！");
					return;
				}
				int day = Integer.parseInt(record[0]);
				if (day > 7 || day < 1) {
					System.out.println("安排失败：输入[" + record[0] + "]不正确！");
					return;
				}
				Pair<Long, Long> p = dateHandler.getCourseTime(day, record[1]);
				if (p.getKey() < 0) {
					System.out.println("安排失败：输入[" + record[1] + "]不正确！");
					return;
				}
				try {
					cisc.insert(p.getKey(), p.getValue(), c);
					int hour = hourMap.get(c);
					hourMap.put(c, hour - 2);
				} catch (Exception e) {
					System.out.println("安排失败：" + e.getMessage());
					return;
				}
				ok = true;
				System.out.println("安排成功！");
				break;
			}
		}
		if (!ok) {
			System.out.println("安排失败：此课程不存在！");
		}
	}

	// 添加课程信息
	private static boolean addCourse(Scanner sc) {
		System.out.println("请按顺序输入课程ID、课程名称、教师姓名、地点和周学时数（空格隔开，输入#结束）：");
		String input = sc.nextLine();
		String[] record = input.split(" ");
		if (record[0].equals("#"))
			return false;
		if (record.length != 5) {
			System.out.println("添加失败：输入格式错误！");
			return false;
		}
		int hourPerWeek = Integer.parseInt(record[4]);
		if (hourPerWeek % 2 != 0 || hourPerWeek < 0) {
			System.out.println("添加失败：周学时数需为正偶数！");
		}
		for (Course c : hourMap.keySet()) {
			if (c.getCourseID() == Integer.parseInt(record[0])) {
				System.out.println("添加失败：输入的课程ID与已有的发生重复！");
				return false;
			}
		}
		Course toAdd = new Course(Integer.parseInt(record[0]), record[1], record[2], record[3], hourPerWeek);
		hourMap.put(toAdd, Integer.parseInt(record[4]));
		System.out.println("添加成功！");
		return true;
	}

	// 显示功能菜单
	private static void showMenu() {
		System.out.println("----------功能菜单----------");
		System.out.println("[1] 添加一组课程");
		System.out.println("[2] 安排课程时间");
		System.out.println("[3] 查看安排情况");
		System.out.println("[4] 单日课表查询");
	}

	// 设置学期开始日期和总周数
	private static boolean setGeneral(Scanner sc) {
		System.out.print("请输入学期开始日期（格式：yyyy-MM-dd）：");
		String input;
		long startTime = -1;
		int n;
		if (sc.hasNext()) {
			input = sc.next();
			startTime = dateHandler.parseDate(input);
		}
		sc.nextLine();
		System.out.print("请输入学期总周数（例如：18）：");
		input = sc.nextLine();
		n = Integer.parseInt(input);
		if (startTime < 0) {
			System.out.println("输入格式错误或非法输入，程序退出！");
			return false;
		}
		cisc = new CourseIntervalSet<>(startTime, period, n);
		return true;
	}

}

//比较器：按时间排序
class ScheduleComparator implements Comparator<Pair<Integer, Course>> {

	@Override
	public int compare(Pair<Integer, Course> arg0, Pair<Integer, Course> arg1) {
		int flag = arg0.getKey().compareTo(arg1.getKey());
		if (flag == 0) {
			flag = arg0.getValue().getCourseID() - arg1.getValue().getCourseID();
		}
		return flag;
	}
	
}
