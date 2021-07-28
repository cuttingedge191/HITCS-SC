package labels;

public class Employee {
	
	// rep / fields:
	private final String name;
	private final String position;
	private final String phoneNumber;
	
	// Abstraction function:
	/**
	 * AF(name) = 姓名
	 * AF(position) = 职务
	 * AF(phoneNumber) = 手机号码
	 */
	
	// Representation invariant:
	// 无要求
	
	// Safety from rep exposure:
	// 属性均设置为private final
	
	// constructor
	public Employee(String name, String position, String phoneNumber) {
		this.name = name;
		this.position = position;
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 获取职务
	 * 
	 * @return 职务
	 */
	public String getPosition() {
		return this.position;
	}
	
	/**
	 * 获取手机号码
	 * 
	 * @return 手机号码
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	@Override
	public String toString() {
		return this.name + "\t\t" + this.position + "\t\t" + this.phoneNumber;
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode() + this.position.hashCode() + this.phoneNumber.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Employee))
			return false;
		Employee e = (Employee) o;
		return e.name.equals(name)
				&& e.position.equals(position)
				&& e.phoneNumber.equals(phoneNumber);
	}
}
