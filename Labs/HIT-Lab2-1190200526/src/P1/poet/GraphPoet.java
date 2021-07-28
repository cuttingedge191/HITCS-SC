/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    /**
     * AF(graph) = 从文件读取的语料库(corpus)
     * 图中顶点是单词，边是两个单词在文本中的相邻关系
     * 边A->B的权代表AB在文本中出现的次数
     */
    // Representation invariant:
    /**
     * 1) 顶点信息（单词）不能为空(null)
     * 2) graph对象不能为null
     */
    // Safety from rep exposure:
    /**
     * 将类中graph设置为private final
     * 不提供返回其引用或修改它的公共方法
     */
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(corpus));
        String line; // 暂存读到的行
        String[] words; // 暂存一行按空格分割后的一行内容（单词）
        List<String> wordList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
        	words = line.split(" "); // 空格分割
        	for (String w : words) {
        		wordList.add(w.toLowerCase()); // 转换为小写
        	}
        }
        br.close();
        for (int i = 0; i < wordList.size() - 1; ++i) {
        	String left = wordList.get(i);
        	String right = wordList.get(i + 1);
        	if (left.equals(right)) // 避免在始点与终点相同
        		continue;
        	int before = graph.set(left, right, 0); // 获取原边权或添加顶点并返回0
        	graph.set(left, right, before + 1); // 更新边权
        }
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
    	assert (graph != null);
    	Set<String> words = graph.vertices();
    	for (String w : words) {
    		assert (w != null);
    	}
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
	public String poem(String input) {
		StringBuilder sb = new StringBuilder();
		List<String> wordList = new ArrayList<>();
		String[] inputWords = input.split(" ");
		for (String s : inputWords) {
			wordList.add(s);
		}
		Map<String, Integer> sourceMap = new HashMap<>();
		Map<String, Integer> targetMap = new HashMap<>();
		for (int i = 0; i < wordList.size() - 1; ++i) {
			sb.append(wordList.get(i)).append(" "); // 加入输入的一个单词
			// 转换为小写便于查找图中两点间bridge word
			String source = wordList.get(i).toLowerCase();
			String target = wordList.get(i + 1).toLowerCase();
			targetMap = graph.targets(source); // 得到前一个点的所有终点
			sourceMap = graph.sources(target); // 得到后一个点的所有源点
			// 在这两个map中查找边权最大的公共点即为bridge word
			int max = 0;
			String bridgeWord = "";
			for (String tar : targetMap.keySet()) {
				if (sourceMap.containsKey(tar) && sourceMap.get(tar) + targetMap.get(tar) > max) {
					max = sourceMap.get(tar) + targetMap.get(tar);
					bridgeWord = tar;
				}
			}
			if (max > 0) {
				sb.append(bridgeWord + " "); // 假如找到则加入
			}
		}
		sb.append(wordList.get(wordList.size() - 1)); // 加入最后一个单词
		checkRep();
		return sb.toString();
	}
    
    // toString()
    @Override
    public String toString() {
    	checkRep();
    	return "GraphPoet: " + graph.toString(); // 利用ConcreteEdgeGraph的toString方法
    }
    
}
