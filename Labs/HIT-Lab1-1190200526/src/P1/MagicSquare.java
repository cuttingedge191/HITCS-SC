package P1;

import java.io.*;
import java.util.*;

public class MagicSquare {

	public static boolean isLegalMagicSquare(String fileName) {
		// 尝试读取文件，检测文件是否存在
		FileReader fr;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Error:File not found!");
			e.printStackTrace();
			return false;
		}
		// 逐行读取文件，整理数据
		BufferedReader br = new BufferedReader(fr);
		String line; // 暂存读取到的行
		String[] proc_line; // 暂存一行按"\t"分割后的字符串数组
		int line_cnt = 1; // 记录行数，用于后续检测
		int width = 0; // 记录一行宽度，用于后续检测
		int[][] square; // 保存读取到的Magic Square
		try {
			line = br.readLine();
			// 先单独读取一行，确定二维数组大小，同时检测是否为空文件
			// 空文件情况
			if (line == null) {
				System.out.println("Error:File is empty!");
				return false;
			}
			proc_line = line.split("\t");
			width = proc_line.length;
			square = new int[width][width];
			int i = 0;
			for (String s : proc_line) {
				square[0][i++] = Integer.valueOf(s);
			}
			while ((line = br.readLine()) != null) {
				proc_line = line.split("\t");
				i = 0;
				// 不符合定义情况1（行数大于列数）
				if (line_cnt == width) {
					System.out.println("Error:Rows > columns!");
					return false;
				}
				for (String s : proc_line) {
					int num;
					// 不符合定义情况2（存在非正整数->输出Error提示，或格式不正确输入->exception）
					if ((num = Integer.valueOf(s)) <= 0) {
						System.out.printf("Error:%d is not a positive number!\n", num);
						return false;
					}
					square[line_cnt][i++] = num;
				}
				// 不符合定义情况3（并非矩阵）
				if (i != width) {
					System.out.println("Error:Input is not a matrix!");
					return false;
				}
				++line_cnt;
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			// 格式不正确输入或其他错误处理
			System.out.println("Error:Wrong input format or other errors!");
			e.printStackTrace();
			return false;
		}
		// 不符合定义情况4（行数小于列数）
		if (line_cnt < width) {
			System.out.println("Error:Rows < columns!");
			return false;
		}
		// 形态上符合，进行数值检测
		int sum = 0;
		int tmp = 0;
		// 单独先计算一行和，用于之后比较
		for (int k = 0; k < width; ++k) {
			sum += square[0][k];
		}
		// 检测行和
		for (int m = 1; m < width; ++m) {
			for (int n = 0; n < width; ++n) {
				tmp += square[m][n];
			}
			// 行和不满足
			if (tmp != sum) {
				return false;
			}
			tmp = 0;
		}
		// 检测列和
		for (int m = 0; m < width; ++m) {
			for (int n = 0; n < width; ++n) {
				tmp += square[n][m];
			}
			// 列和不满足
			if (tmp != sum) {
				return false;
			}
			tmp = 0;
		}
		// 检测对角和
		int tmp1 = 0;
		int tmp2 = 0;
		for (int m = 0; m < width; ++m) {
			tmp1 += square[m][m];
			tmp2 += square[width - 1 - m][m];
		}
		// 对角线和不满足
		if (tmp1 != sum || tmp2 != sum) {
			return false;
		}
		return true;
	}

	public static boolean generateMagicSquare(int n) {
		// 创建n*n大小的二维数组，用于保存生成的Magic Square
		int[][] magic;
		try {
			magic = new int[n][n];
		} catch (NegativeArraySizeException e) {
			// 负数异常处理
			System.out.print("Error:Input n is negative!\n");
			e.printStackTrace();
			return false;
		}
		int row = 0, col = n / 2, i, square = n * n; // 初始化变量及循环边界，变量j未被使用，已删去
		for (i = 1; i <= square; i++) {
			try {
				magic[row][col] = i; // 赋值
			} catch (ArrayIndexOutOfBoundsException e) {
				// 偶数异常处理
				System.out.print("Error:Input n is even!\n");
				e.printStackTrace();
				return false;
			}
			// 确定下一个位置（右上或超出范围处理）
			if (i % n == 0)
				row++; // 完成了对一个斜线上所有元素的放置
			else {
				if (row == 0)
					row = n - 1; // 超出范围处理
				else
					row--; // 正常情况（右上移动）
				if (col == (n - 1))
					col = 0; // 超出范围处理
				else
					col++; // 正常情况（右上移动）
			}
		}
		// 完成n*n次赋值（生成n*n Magic Square）后循环结束
		// 输出二维数组至控制台
		/*for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++)
				System.out.print(magic[i][j] + "\t");
			System.out.println();
		}*/
		// 写文件
		File wfile = new File("src/P1/txt/6.txt");
		try {
			wfile.createNewFile(); // 创建文件
			FileWriter writer = new FileWriter(wfile);
			BufferedWriter bwriter = new BufferedWriter(writer);
			// 按格式逐个写入
			for (int a = 0; a < n; ++a) {
				for (int b = 0; b < n; ++b) {
					bwriter.write(magic[a][b] + "\t");
				}
				bwriter.write("\n");
			}
			bwriter.flush(); // 缓冲区内容写入
			bwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String path = "src/P1/txt/";
		String fileName;
		boolean result;
		// 测试isLegalMagicSquare()函数
		for (int i = 1; i <= 5; ++i) {
			fileName = path + Integer.toString(i) + ".txt";
			System.out.print("-----" + Integer.toString(i) + ".txt-----\n");
			result = isLegalMagicSquare(fileName);
			System.out.println(result);
		}
		// 测试generateMagicSquare()函数
		int n;
		Scanner in_n = new Scanner(System.in);
		System.out.print("Input n for generateMagicSquare():");
		n = in_n.nextInt();
		in_n.close();
		result = generateMagicSquare(n);
		System.out.print("generateMagicSquare() returned " + result + "\n");
		// 若生成正常，测试生成的6.txt
		if (result) {
			System.out.print("-----6.txt-----\n");
			result = isLegalMagicSquare(path + "6.txt");
			System.out.println(result);
		}
	}

}
