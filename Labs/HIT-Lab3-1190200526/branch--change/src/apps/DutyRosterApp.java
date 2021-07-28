package apps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appADTs.DutyIntervalSet;
import helpers.dateHandler;
import labels.Employee;

public class DutyRosterApp {

	private static DutyIntervalSet<Employee> dise;
	private static final Set<Employee> eSet = new HashSet<>(); 
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String choice;
		System.out.println("--------------------排班管理系统--------------------");
		if (!(readFile(sc) || setGeneral(sc))) { // 初始化错误则退出
			return;
		}
		Formatter f = new Formatter(System.out);
		do {
			showMenu();
			System.out.print("请选择（输入功能对应数字，其他退出）：");
			choice = sc.nextLine();
			if (choice.equals("1")) {
				System.out.println("----------添加员工信息----------");
				do {
				} while (addEmployee(sc)); // 添加一组员工信息
			} else if (choice.equals("2")) {
				System.out.println("----------删除员工信息----------");
				removeEmployee(sc);
			} else if (choice.equals("3")) {
				System.out.println("----------查看员工信息----------");
				showEmployeeInfo(f);
			} else if (choice.equals("4")) {
				System.out.println("----------添加排班记录----------");
				do {
				} while (addRecord(sc)); // 添加一组排班信息
			} else if (choice.equals("5")) {
				System.out.println("----------自动生成排班----------");
				generateDuty();
			} else if (choice.equals("6")) {
				System.out.println("----------检查排班情况----------");
				dise.checkNoBlank();
			} else if (choice.equals("7")) {
				System.out.println("-------------排班表-------------");
				showDutyTable(f);
			} else
				break;
			System.out.print("按enter以返回功能菜单：");
			sc.nextLine();
		} while (true);
		sc.close();
		System.out.println("--------------------程序终止--------------------");
		f.close();
	}
	
	// 设置排班开始、结束日期
	private static boolean setGeneral(Scanner sc) {
		String inputLine;
		long startTime = -1;
		long endTime = -1;
		System.out.print("请输入排班开始日期（格式：yyyy-MM-dd）：");
		if (sc.hasNext()) {
			inputLine = sc.next();
			startTime = dateHandler.parseDate(inputLine);
		}
		System.out.print("请输入排班结束日期（格式：yyyy-MM-dd）：");
		if (sc.hasNext()) {
			inputLine = sc.next();
			endTime = dateHandler.parseDate(inputLine);
		}
		if (startTime < 0 || endTime < 0 || startTime > endTime) {
			System.out.println("输入格式错误或非法输入，程序退出！");
			return false;
		}
		dise = new DutyIntervalSet<>(startTime, endTime);
		sc.nextLine(); // 除去回车符
		return true;
	}

	// 展示菜单
	private static void showMenu() {
		System.out.println("----------功能菜单----------");
		System.out.println("[1] 添加一组员工");
		System.out.println("[2] 删除指定员工");
		System.out.println("[3] 查看员工信息");
		System.out.println("[4] 手动添加排班记录");
		System.out.println("[5] 自动生成排班记录");
		System.out.println("[6] 检查排班情况");
		System.out.println("[7] 展示当前排班表");
	}
	
	// 添加员工
	private static boolean addEmployee(Scanner sc) {
		System.out.print("请输入员工" + (eSet.size()+1) + "姓名、职务和手机号（空格隔开，输入#结束）：");
		String line;
		String[] info;
		if (sc.hasNextLine()) {
			line = sc.nextLine();
			info = line.split(" ");
			if (info[0].equals("#"))
				return false;
			if (info.length != 3) {
				System.out.println("添加失败：输入格式错误！");
				return false;
			}
			for (Employee e : eSet) { // 避免添加同名员工信息
				if (e.getName().equals(info[0])) {
					System.out.println("添加失败：此员工已存在！");
					return false;
				}
			}
			Employee e = new Employee(info[0], info[1], info[2]);
			eSet.add(e);
			System.out.println("添加成功！");
			return true;
		}
		return false;
	}
	
	// 删除指定员工
	private static void removeEmployee(Scanner sc) {
		System.out.print("请输入要删除的员工姓名：");
		if (sc.hasNext()) {
			String name = sc.next();
			boolean find = false;
			Employee toRemove = new Employee("#", "#", "#");
			for (Employee e : eSet) {
				if (e.getName().equals(name)) {
					find = true;
					toRemove = e;
					break;
				}
			}
			sc.nextLine(); // 除去回车符
			if (!find) {
				System.out.println("删除失败：员工[" + name + "]不存在！");
				return;
			}
			if (find && dise.labels().contains(toRemove))
				System.out.println("员工[" + name + "]存在排班信息，将被一同删除！");
			System.out.print("是否确认删除（输入Y/y确认，其他取消）：");
			String confirm = sc.nextLine();
			if (confirm.equals("Y") || confirm.equals("y")) {
				dise.remove(toRemove);
				eSet.remove(toRemove);
				System.out.println("删除成功！");
			} else {
				System.out.println("删除取消！");
			}
		}
	}
	
	// 输出员工信息
	private static void showEmployeeInfo(Formatter f) {
		if (eSet.size() == 0) {
			System.out.println("请先添加至少一名员工！");
			return;
		}
		System.out.println("总人数：" + eSet.size());
		f.format("%-5s %-20s %-23s %-15s\n", "序号", "姓名", "职务", "手机号码");
		Integer num = 1;
		for (Employee e : eSet) {
			f.format("%-5d %-20s %-23s %-15s\n", num, e.getName(), e.getPosition(), e.getPhoneNumber());
			++num;
		}
	}
	
	// 手动添加排班记录
	private static boolean addRecord(Scanner sc) {
		System.out.println("请按顺序输入开始时间、结束时间及员工姓名（空格隔开，时间采用yyyy-MM-dd格式，输入#结束）：");
		String input = sc.nextLine();
		String[] record = input.split(" ");
		if (record[0].equals("#"))
			return false;
		if (record.length != 3) {
			System.out.println("添加失败：输入格式错误！");
			return false;
		}
		Employee find = new Employee("#", "#", "#");
		boolean flag = false;
		for (Employee em : eSet) {
			if (em.getName().equals(record[2])) {
				find = em;
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.out.println("添加失败：员工[" + record[2] + "]不存在！");
			return false;
		}
		try {
			long start = dateHandler.parseDate(record[0]);
			long end = dateHandler.parseDate(record[1]);
			if (!dise.checkInRange(start, end)) {
				System.out.println("添加失败：输入时间不在排班起止范围内！");
				return false;
			}
			dise.insert(start, end, find);
			System.out.println("添加成功！");
			return true;
		} catch (Exception e) {
			System.out.println("添加失败：" + e.getMessage());
			return false;
		}
	}
	
	// 自动生成排班
	private static void generateDuty() {
		int totalDays = dise.getTotalDays(); // 排班总天数
		long startTime = dise.getStartTime();
		if (eSet.size() == 0) {
			System.out.println("请先添加至少一名员工！");
			return;
		}
		int daysPerE = totalDays / eSet.size(); // 平均每个人的天数
		int extraDays = totalDays - daysPerE * eSet.size(); // 未整除的余下天数
		try {
			for (Employee l : eSet) {
				long e = dateHandler.incnDayLong(startTime, daysPerE - 1);
				if (extraDays > 0) {
					e = dateHandler.incnDayLong(e, 1);
					--extraDays;
				}
				dise.insert(startTime, e, l);
				startTime = dateHandler.incnDayLong(e, 1);
			}
		} catch (Exception e) {
			System.out.println("由于已经存在排班记录，自动排班失败！");
			return;
		}
		System.out.println("自动排班成功！");
	}
	
	// 展示排班表
	private static void showDutyTable(Formatter f) {
		if (dise.labels().size() == 0) {
			System.out.println("还未进行排班！");
			return;
		}
		Map<Long, Employee> sortM = new TreeMap<>(); // 对排班记录进行升序排序
		for (Employee e : dise.labels()) {
			for (Integer i : dise.intervals(e).labels())
			sortM.put(dise.intervals(e).start(i), e);
		}
		f.format("%-5s %-12s %-12s %-15s %-15s %-15s\n", "序号", "开始日期", "结束日期", "姓名", "职务", "手机号码");
		Integer cnt = 1;
		Set<Employee> set = new HashSet<>();
		set.addAll(sortM.values());
		for (Employee e : set) {
			for (Integer i : dise.intervals(e).labels()) {
				f.format("%-5d %-15s %-15s %-15s %-18s %-15s\n", cnt, dateHandler.parseLong(dise.intervals(e).start(i)), 
						dateHandler.parseLong(dise.intervals(e).end(i)), e.getName(), e.getPosition(), e.getPhoneNumber());
				++cnt;
			}
		}
	}
	
	// 从外部文件读入
	private static boolean readFile(Scanner sc) {
		System.out.println("请输入相对文件路径（如“txt/test1.txt”，输入“#”或文件读入错误会跳过文件读入，切换至手动操作）：");
		String line = sc.nextLine();
		if (line.equals("#")) {
			return false;
		}
		FileReader fr;
		try {
			fr = new FileReader(line);
		} catch (FileNotFoundException e) {
			System.out.println("读入失败：无法找到文件！");
			return false;
		}
		BufferedReader br = new BufferedReader(fr);
		String pattern_Name = "[A-Za-z]+";
		String pattern_Position = "[A-Za-z\\s]+";
		String pattern_Phone = "[0-9]{3}\\-[0-9]{4}\\-[0-9]{4}";
		String pattern_Date = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		String pattern_E1 = "\s*Employee\\{\s*";
		String pattern_E2 = "\s*" + pattern_Name + "\\{" + pattern_Position + "\\," + pattern_Phone + "\\}";
		String pattern_P = "\s*Period\\{" + pattern_Date + "," + pattern_Date + "\\}";
		String pattern_R1 = "\s*Roster\\{\s*";
		String pattern_R2 = "\s*" + pattern_Name+ "\\{" + pattern_Date + "," + pattern_Date + "\\}";
		try {
			long start;
			long end;
			br.mark(2000); // 标记文件头部，方便跳回
			// 找到“Employee{”
			do {
				line = br.readLine();
			} while (!Pattern.matches(pattern_E1, line) && line != null);
			if (line == null) {
				System.out.println("读入失败：文件中不存在员工信息！");
				br.close();
				return false;
			}
			line = br.readLine();
			// 读入员工信息
			do {
				if (!Pattern.matches(pattern_E2, line)) {
					System.out.println("读入失败：\"" + line + "\"" + "格式错误！");
					infoClear(br);
					return false;
				} else {
					Pattern p = Pattern.compile(pattern_Name);
					Matcher m = p.matcher(line);
					m.find();
					String name = line.substring(m.start(), m.end()); // 截取姓名
					int tmp = m.end() + 1;
					p = Pattern.compile(pattern_Phone);
					m = p.matcher(line);
					m.find();
					String position = line.substring(tmp, m.start() - 1); // 截取职务
					String phone = line.substring(m.start(), m.end()); // 截取手机号
					eSet.add(new Employee(name, position, phone));
				}
				line = br.readLine();
			} while (!Pattern.matches("\s*\\}", line) && line != null);
			boolean reset = false;
			// 找到“Period{”
			do {
				line = br.readLine();
				if (line == null) {
					if (reset) {
						System.out.println("读入失败：文件中不存在排班起止时间信息！");
						infoClear(br);
						return false;
					}
					reset = true;
					br.reset();
					line = br.readLine();
				}
			} while (!Pattern.matches(pattern_P, line) && line != null);
			// 获取排班起止时间信息
			Pattern p = Pattern.compile(pattern_Date);
			Matcher m = p.matcher(line);
			m.find();
			start = dateHandler.parseDate(line.substring(m.start(), m.end()));
			m.find();
			end = dateHandler.parseDate(line.substring(m.start(), m.end()));
			if (start < 0 || end < 0 || start > end) {
				System.out.println("读入失败：文件中排班起止时间信息不正确！");
				infoClear(br);
				return false;
			}
			// 使用排班起止时间信息初始化
			dise = new DutyIntervalSet<>(start, end);
			reset = false;
			// 找到“Roster{”
			do {
				line = br.readLine();
				if (line == null) {
					if (reset) {
						System.out.println("读入失败：文件中不存在排班信息！");
						infoClear(br);
						return false;
					}
					reset = true;
					br.reset();
					line = br.readLine();
				}
			} while (!Pattern.matches(pattern_R1, line) && line != null);
			line = br.readLine();
			// 读入排班信息
			do {
				if (!Pattern.matches(pattern_R2, line)) {
					System.out.println("读入失败：\"" + line + "\"" + "格式错误！");
					infoClear(br);
					return false;
				} else {
					p = Pattern.compile(pattern_Name);
					m = p.matcher(line);
					m.find();
					String name = line.substring(m.start(), m.end()); // 截取姓名
					p = Pattern.compile(pattern_Date);
					m = p.matcher(line);
					m.find();
					start = dateHandler.parseDate(line.substring(m.start(), m.end())); // 值班开始时间
					m.find();
					end = dateHandler.parseDate(line.substring(m.start(), m.end())); // 值班结束时间
					for (Employee e : eSet) {
						if (e.getName().equals(name)) {
							dise.insert(start, end, e);
							break;
						}
					}
				}
				line = br.readLine();
			} while (!Pattern.matches("\s*\\}", line) && line != null);
			br.close();
		} catch (Exception e) {
			System.out.println("读入失败：" + e.getMessage());
			return false;
		}
		System.out.println("文件读入成功！");
		return true;
	}

	// 文件信息错误时清空数据
	private static void infoClear(BufferedReader br) {
		eSet.clear();
		try {
			br.close();
		} catch (IOException e) {
			System.out.println("文件关闭失败！");
		}
	}
	
}
