/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    // AF(vertices) = 图中所有顶点及顶点间有向边信息

    // Representation invariant:
    /**
     * 1) vertices中无重复点
     * 2) 出边和入边数目相同
     * 注：部分RI在Vertex类中检测
     */
    
    // Safety from rep exposure:
    /**
     * 1) vertices声明为private final
     * 2) 无返回vertices的方法
     */
    
    // constructor
    public ConcreteVerticesGraph() {
    }
    
    // checkRep
    private void checkRep() {
    	// 使用HashSet检测是否有重复顶点
    	Set<Vertex<L>> test = new HashSet<>();
    	test.addAll(vertices);
    	assert (test.size() == vertices.size());
    	// 出边、入边数目应相等
    	int inCount = 0;
    	int outCount = 0;
    	for (Vertex<L> v : vertices) {
    		inCount += v.getIns().size();
    		outCount += v.getOuts().size();
    	}
		assert (inCount == outCount);
    }
    
    @Override public boolean add(L vertex) {
        for (Vertex<L> v : vertices) {
        	if (v.getName().equals(vertex)) // 添加存在点
            	return false;
        }
		// 正常情况
		Vertex<L> newVertex = new Vertex<>(vertex);
		vertices.add(newVertex);
		checkRep();
		return true;
    }
    
    @Override public int set(L source, L target, int weight) {
    	if (weight > 0) { // 正边权，添加边或修改边
    		this.add(source);
            this.add(target);
    	} else if (weight < 0) { // 非法情况
    		return -1;
    	}
        int ret = 0;
        for (Vertex<L> v : vertices) { // 同时设置出边和入边
        	if (v.getName().equals(source)) {
        		v.setOut(target, weight);
        	}
        	if (v.getName().equals(target)) {
        		ret = v.setIn(source, weight);
        	}
        }
        checkRep();
        return ret;
    }
    
    @Override public boolean remove(L vertex) {
		Iterator<Vertex<L>> it = vertices.iterator();
		boolean res = false;
		while (it.hasNext()) {
			Vertex<L> v = it.next();
			if (v.getName().equals(vertex)) { // 删除顶点
				it.remove();
				res = true;
			} else { // 删除相关边
				if (v.getIns().containsKey(vertex)) {
					v.setIn(vertex, 0);
				}
				if (v.getOuts().containsKey(vertex)) {
					v.setOut(vertex, 0);
				}
			}
		}
		checkRep();
		return res;
    }
    
    @Override public Set<L> vertices() {
        Set<L> res = new HashSet<>();
        // 将每个顶点名加入结果集
        for (Vertex<L> v : vertices) {
        	res.add(v.getName());
        }
        checkRep();
        return res;
    }
    
    @Override public Map<L, Integer> sources(L target) {
        for (Vertex<L> v : vertices) {
        	if (v.getName().equals(target)) {
        		return v.getIns();
        	}
        }
        return new HashMap<>();
    }
    
    @Override public Map<L, Integer> targets(L source) {
    	for (Vertex<L> v : vertices) {
        	if (v.getName().equals(source)) {
        		return v.getOuts();
        	}
        }
        return new HashMap<>();
    }
    
    // toString()
    /**
     * 返回边图可读表示
     * @return 边图可读表示
     */
    @Override public String toString() {
    	String s = "V:" + Integer.toString(vertices.size()) + ", Detail:";
    	for (Vertex<L> v : vertices) {
    		s += v.toString() + " | ";
    	}
    	return s;
    }
}

/**
 * specification
 * 保存顶点及顶点间关系信息（出/入边指向的点及边权）
 * 提供访问信息的公共方法
 * 提供一些辅助点图实现的方法
 * 提供toString()方法将信息转换为可理解的输出
 * 具体规约见各方法前
 * 
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    // fields
    private final L vertexName; // 顶点名
    private final Map<L, Integer> inMap; // 入边信息
    private final Map<L, Integer> outMap; // 出边信息
    
    // Abstraction function:
    /**
     *  AF(vertexName) = 顶点名
     *  AF(inMap) = 指向这个顶点的所有顶点及边权（数据对）
     *  AF(outMap) = 以此顶点为始点的所有顶点及边权（数据对）
     */
    
    // Representation invariant:
    /**
     * 1) 顶点名不能为空(null)
     * 2) 边权均大于0
     * 3) 顶点的边对应的始点和终点不能为本身
     *    （inMap和outMap中没有key值为此顶点名的元素）
     */
    
    // Safety from rep exposure:
    /**
     *  1) 将类中属性均声明为private final
     *  2) 由于inMap、outMap均为mutable，返回时进行defensive copy
     */
    
    // constructor
    /**
     * 构造函数，创建一个新顶点
     * @param name 新顶点名
     */
    public Vertex(L name) {
    	this.vertexName = name;
    	this.inMap = new HashMap<>();
    	this.outMap = new HashMap<>();
    	checkRep();
    }
    
    // checkRep
    private void checkRep() {
    	assert (this.vertexName != null); // 自身顶点名不应为空
    	// 边始点与终点不应相同，顶点名不应为空，边权应大于0
    	for (L name : inMap.keySet()) {
    		assert (name != null && !name.equals(vertexName) && inMap.get(name) > 0);
    	}
    	for (L name : outMap.keySet()) {
    		assert (name != null && !name.equals(vertexName) && outMap.get(name) > 0);
    	}
    }
    
    // methods
    
    /**
     * 获取顶点名
     * @return 顶点名
     */
    public L getName() {
    	checkRep();
    	return this.vertexName;
    }
    
    /**
     * 获取指向此顶点的所有边相关信息
     * @return 指向此顶点的所有边相关信息
     */
    public Map<L, Integer> getIns() {
    	checkRep();
    	return new HashMap<L, Integer>(inMap);
    }
    
    /**
     * 获取以此顶点为始点的所有边相关信息
     * @return 以此顶点为始点的所有边相关信息
     */
    public Map<L, Integer> getOuts() {
    	checkRep();
    	return new HashMap<L, Integer>(outMap);
    }
    
    /**
     * 操作以该顶点为终点的边信息
     * 边权大于0则修改或新增边
     *     等于0则删除边（不存在不作任何修改）
     *     小于0非法情况，返回-1，不作任何修改
     * @param sourceName 始点名
     * @param weight 边权
     * @return 原边权值，不存在则为0，非法输入为-1
     */
    public int setIn(L sourceName, int weight) {
    	Integer lastWeight = -1;
    	if (weight > 0) {
    		lastWeight = inMap.put(sourceName, weight);
    	} else if (weight == 0) {
    		lastWeight = inMap.remove(sourceName);
    	}
    	if (lastWeight == null) {
    		lastWeight = 0;
    	}
    	checkRep();
    	return lastWeight.intValue();
    }
    
    /**
     * 操作以该顶点为始点的边信息
     * 边权大于0则修改或新增边
     *     等于0则删除边（不存在不作任何修改）
     *     小于0非法情况，返回-1，不作任何修改
     * @param targetName 终点名
     * @param weight 边权
     * @return 原边权值，不存在则为0，非法输入为-1
     */
    public int setOut(L targetName, int weight) {
    	Integer lastWeight = -1;
    	if (weight > 0) {
    		lastWeight = outMap.put(targetName, weight);
    	} else if (weight == 0) {
    		lastWeight = outMap.remove(targetName);
    	}
    	if (lastWeight == null) {
    		lastWeight = 0;
    	}
    	checkRep();
    	return lastWeight.intValue();
    }
    
    // toString()
    /**
     * 返回Vertex类对象的可读表示
     * @return Vertex类对象的可读表示
     */
    @Override
    public String toString() {
    	String res = "Vertex " + vertexName + " source:";
    	for (L s : inMap.keySet()) {
    		res += " " + s.toString() + "-" + inMap.get(s).toString();
    	}
    	res += " target:";
    	for (L s : outMap.keySet()) {
    		res += " " + s.toString() + "-" + outMap.get(s).toString();
    	}
    	return res;
    }
    
}
