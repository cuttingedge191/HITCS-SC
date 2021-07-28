package P2;

public class Person {

	/* 私有属性 */
	private final String name;
	
	/* 构造函数 */
	public Person(String name) {
		this.name = name;
	}
	
	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}
	
}
