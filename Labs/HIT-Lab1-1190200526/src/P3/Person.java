package P3;

public class Person {

	/* 私有属性 */
	private String name;
	
	/* 构造函数 */
	public Person(String name) {
		this.name = name;
	}
	
	/**
	 * 获取姓名
	 * 
	 * @return 姓名拷贝
	 */
	public String getName() {
		String nameCopy = name;
		return nameCopy;
	}
	
}
