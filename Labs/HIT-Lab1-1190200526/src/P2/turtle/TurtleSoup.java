/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
    	// 画出一个正方形
    	for (int i = 1; i <= 4; ++i) {
    		turtle.forward(sideLength);
    		turtle.turn(90);
    	}
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
    	// 计算正sides边形的一个内角的大小
    	double result;
    	result = (sides - 2) * 180.0 / sides;
    	return result;
	}

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
    	// 通过内角大小计算正多边形边数
    	// 此函数在实验中未要求实现，但被测试，故此处进行了实现
        return (int) Math.round((360.0 / (180.0 - angle)));
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
    	// 画正sides边形
    	double degree = 180.0 - calculateRegularPolygonAngle(sides);
    	for (int i = 1; i <= sides; ++i) {
    		turtle.forward(sideLength);
    		turtle.turn(degree);
    	}
	}

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
		double res;
		int dis_X = targetX - currentX;
		int dis_Y = targetY - currentY;
		res = Math.toDegrees(Math.atan2(dis_X, dis_Y)); // 计算与y轴正方向夹角
		res -= currentBearing; //计算角度差
		if (res < 0)
			res += 360.0; // 处理负角度差情况
		return res;
    }
    
	public static double calculateBearingToPoint(double currentBearing, double currentX, double currentY, 
			                                     double targetX, double targetY) {
		// 重载方法用于convexHull()
		double res;
		double dis_X = targetX - currentX;
		double dis_Y = targetY - currentY;
		res = Math.toDegrees(Math.atan2(dis_X, dis_Y)); // 计算与y轴正方向夹角
		res -= currentBearing; // 计算角度差
		if (res < 0)
			res += 360.0; // 处理负角度差情况
		return res;
	}

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
		int cur_X, cur_Y;
		int dst_X, dst_Y;
		double cur_Degree = 0.0;
		double adjust;
		int X_size = xCoords.size();
		int Y_size = yCoords.size();
		if (X_size != Y_size) {
			// 非法参数处理，一般来说不会发生此情况
			System.out.print("Error:Illegal input for calculateBearings()!\n");
			return null;
		}
		List<Double> res = new ArrayList<>(); // 保存结果
		if (X_size == 0 || X_size == 1) // 结果为空的情况
			return res;
		// 获取初始坐标
		cur_X = xCoords.get(0);
		cur_Y = yCoords.get(0);
		for (int p = 1; p < X_size; ++p) {
			// 获取目标坐标
			dst_X = xCoords.get(p);
			dst_Y = yCoords.get(p);
			// 计算调整角度、更新当前角度
			adjust = calculateBearingToPoint(cur_Degree, cur_X, cur_Y, dst_X, dst_Y);
			cur_Degree = (cur_Degree + adjust) % 360.0;
			// 写入结果
			res.add(adjust);
			// 更新当前坐标
			cur_X = dst_X;
			cur_Y = dst_Y;
		}
		return res;
	}
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
		// 点的个数小于4直接返回
		if (points.size() < 4) {
			return points;
		}
		Set<Point> res = new HashSet<Point>(); // 保存结果
		Point base = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
		// 确定基点
		for (Point p : points) {
			if (p.x() < base.x() || (p.x() == base.x() && p.y() < base.y()))
				base = p;
		}
		Point cur_P = base, next_P = null;
		double cur_Angle = 0.0;
		double min_Angle = 360.0;
		double tmp_Angle;
		double dis;
		double max_dis = -1.0;
		// 不断寻找符合条件的点加入点集
		do {
			res.add(cur_P);
			for (Point p : points) {
				if ((!res.contains(p) || p == base)) {
					tmp_Angle = calculateBearingToPoint(cur_Angle, cur_P.x(), cur_P.y(), p.x(), p.y());
					// 计算距离（平方和）
					dis = (p.x() - cur_P.x()) * (p.x() - cur_P.x())
							+ (p.y() - cur_P.y()) * (p.y() - cur_P.y());
					// 寻找偏转角最小的点，偏转角相等的时候选择距离最远的点
					if (tmp_Angle < min_Angle || ((tmp_Angle == min_Angle) && (dis > max_dis))) {
						min_Angle = tmp_Angle;
						next_P = p;
						max_dis = dis;
					}
				}
			}
			// 更新当前角度、点，恢复用于寻找的min_Angle和max_dis变量
			cur_Angle += min_Angle;
			min_Angle = 360;
			max_dis = -1.0;
			cur_P = next_P;
		} while (cur_P != base); // 重复直至回到基点
		return res;
    }
    
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
    	int sides = 5;
    	int sideLength = 150;
    	int num = 12;
    	int sel_Color = 0;
    	PenColor[] color = {PenColor.YELLOW, PenColor.ORANGE, PenColor.BLUE, PenColor.GREEN, PenColor.PINK, PenColor.RED};
    	double degree = calculateRegularPolygonAngle(sides);
    	for (int i = 1; i <= num; ++i) {
    		sel_Color = sel_Color % 6;
    		for (int j = 1; j <= sides; ++j) {
    			turtle.color(color[sel_Color]);
        		turtle.forward(sideLength);
        		turtle.turn(degree);
        	}
    		turtle.turn(360.0 / (double)num);
    		++sel_Color;
    	}
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //drawSquare(turtle, 40);
        
        //drawRegularPolygon(turtle, 8, 40);
        
        drawPersonalArt(turtle);

        // draw the window
        turtle.draw();
    }

}
