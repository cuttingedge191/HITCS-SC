/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    /**
     * AF(vertices) = 图中顶点
     * AF(edges) = 图中边
     */
    // Representation invariant:
    /**
     * 1) 边的起始点和终点均在顶点集中
     * 2) 顶点不能为空(null)
     * 3) 始点与终点不能一致
     * 注：部分RI在Edge类中检测
     */
    // Safety from rep exposure:
    /**
     * 1) 类的属性均声明为private final
     * 2) 由于vertices和edges均为mutable，方法中进行了defensive copy
     */
    
    // constructor
    public ConcreteEdgesGraph() {
    }
    
    // checkRep
    private void checkRep() {
    	for (L v : vertices) { // 检查是否有空顶点
    		assert (v != null);
    	}
    	for (Edge<L> e : edges) {
    		// 检查边的点是否都在图中
    		assert (vertices.contains(e.getStart()) && vertices.contains(e.getEnd()));
    		// 检查是否有始点和终点相同的边
    		assert (!e.getStart().equals(e.getEnd()));
    	}
    }
    
    @Override public boolean add(L vertex) {
        if (vertices.contains(vertex)) {  // 添加存在点
        	return false;
        }
        // 正常情况
        vertices.add(vertex);
        checkRep();
        return true;
    }
    
    @Override public int set(L source, L target, int weight) {
        if (weight < 0) { // 非法情况
        	return -1;
        }
        // 先寻找此边是否存在
        // 因为Edge类为immutable，只要存在就要删除
        // 若为修改情况则在删除后重新构建新边
		Iterator<Edge<L>> it = edges.iterator();
		int lastWeight = 0;
		while (it.hasNext()) {
			Edge<L> e = it.next();
			if (e.isSameEdge(source, target)) {
				lastWeight = e.getWeight();
				it.remove();
			}
		}
		if (weight > 0) { // 修改情况（构建新边加入图）
			// 添加顶点
	        if (!vertices.contains(source))
	        	this.add(source);
	        if (!vertices.contains(target))
	        	this.add(target);
			Edge<L> newEdge = new Edge<L>(source, target, weight);
			edges.add(newEdge);
		}
		// 删除情况无需再操作
		checkRep();
		return lastWeight;
    }
    
    @Override public boolean remove(L vertex) {
    	// 不存在则不作修改
        if (!vertices.contains(vertex))
        	return false;
        Iterator<Edge<L>> it = edges.iterator();
        while (it.hasNext()) { // 删除相连的边
        	Edge<L> e = it.next();
        	if (e.getStart().equals(vertex) || e.getEnd().equals(vertex))
        		it.remove();
        }
        // 删除顶点
        vertices.remove(vertex);
        checkRep();
        return true;
    }
    
    @Override public Set<L> vertices() {
    	checkRep();
        return new HashSet<>(this.vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> res = new HashMap<>();
        for (Edge<L> e : edges) {
        	if (target.equals(e.getEnd())) {
        		res.put(e.getStart(), e.getWeight());
        	}
        }
        checkRep();
        return res;
    }
    
    @Override public Map<L, Integer> targets(L source) {
    	Map<L, Integer> res = new HashMap<>();
        for (Edge<L> e : edges) {
        	if (source.equals(e.getStart())) {
        		res.put(e.getEnd(), e.getWeight());
        	}
        }
        checkRep();
        return res;
    }
    
    // toString()
    /**
     * 返回图的可读表示
     * 注：顶点数、边数、顶点信息及每条边具体信息
     * @return 图的可读表示
     */
    @Override public String toString() {
    	String base =  "V:" + Integer.toString(vertices.size()) + ", E:" 
                       + Integer.toString(edges.size()) + ", Vs:";
    	if (vertices.size() == 0) {
    		checkRep();
    		return base + " empty, Es: empty";
    	}
    	for (L v : vertices) {
    		base += " " + v;
    	}
    	base += ", Es:";
    	if (edges.size() == 0) {
    		checkRep();
    		return base + " empty";
    	}
    	else {
    		for (Edge<L> e : edges)
    			base += " " + e.toString();
    	}
    	checkRep();
    	return base;
    }
}

/**
 * specification
 * 保存有向边相关信息：始点、终点及边权
 * 提供访问属性的公共方法
 * 提供一些辅助边图实现的方法
 * 提供toString()方法将信息转换为可理解的输出
 * 具体规约见各方法前
 * 
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    // fields
	private final L start, end;
    private final int weight;
    // Abstraction function:
    /**
     * AF(start) = 边的始点
     * AF(end) = 边的终点
     * AF(weight) = 边权
     */
    // Representation invariant:
    /**
     * 1) 边的始点和终点均不为空，且不应相同
     * 2) 边权应大于0
     */
    // Safety from rep exposure:
    // 将类中属性均设置为private final
    
    // constructor
    /**
     * 构造函数，构造一个新的有向边
     * @param start 新边的始点
     * @param end 新边的终点
     * @param weight 新边的权
     */
    public Edge(L start, L end, int weight) {
    	this.start = start;
    	this.end = end;
    	this.weight = weight;
    	checkRep();
    }
    
    // checkRep
    private void checkRep() {
    	assert (start != null && end != null && !start.equals(end) && weight > 0);
    }
    
    // methods
    /**
     * 获取有向边的始点
     * @return start 有向边的始点
     */
    public L getStart() {
    	checkRep();
    	return this.start;
    }
    
    /**
     * 获取有向边的终点
     * @return end 有向边的终点
     */
    public L getEnd() {
    	checkRep();
    	return this.end;
    }
    
    /**
     * 获取有向边的权
     * @return weight 有向边的权
     */
    public int getWeight() {
    	checkRep();
    	return this.weight;
    }
    
    /**
     * 判断边是否为同一个
     * @param start
     * @param end
     * @return 是否同边的比较结果
     */
    public boolean isSameEdge(L start, L end) {
    	checkRep();
    	if (start.equals(this.start) && end.equals(this.end))
    		return true;
    	return false;
    }
    
    // toString()
    /**
     * 返回有向边的可读表示
     * @return 有向边的可读表示
     */
    @Override
    public String toString() {
    	checkRep();
    	return this.start.toString() + "->" + this.end.toString() + ":" + Integer.toString(this.weight);
    }
    
}
