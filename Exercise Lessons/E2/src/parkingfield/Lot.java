package parkingfield;

//An immutable class 一个停车位
public class Lot {
	private final int number; // 停车位编号，自然数
	private final int width; // 停车位宽度，自然数 (度量单位为厘米)，>150
	
	//AF:
    //停车位由唯一的编号和宽度值表示
    
    // Representation invariant:
    // 停车位编号>0
    // 宽度>150
    
    // Safety from rep exposure:
    //   所有的表示都是private和 final的
	
	public Lot(int number, int width) {// 构造器
		this.number=number;
		this.width=width;
		checkRep();
	}

	public int getNumber() {    //返回停车位的自然数编号
		return number;
	}

	public int getWidth() {    //得到停车位的宽度
		return width;
	}

	/*
	public boolean wideEnough(Car c) {  //判断该停车位宽度是否足够停入车辆
		return width>=c.getWidth();
	}
	*/

	@Override  //利用IDE工具自动生成
	public boolean equals(Object lot) {
		if (this == lot)
			return true;
		if (lot == null)
			return false;
		if (getClass() != lot.getClass())
			return false;
		Lot other = (Lot) lot;
		if (number != other.number)
			return false;
		return width == other.width;
	}

	@Override  //利用IDE工具自动生成
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + width;
		return result;
	}

	@Override  //利用IDE工具自动生成
	public String toString() {
		return "Lot [number=" + number + ", width=" + width + "]";
	}
	
	private void checkRep() {
		assert number>0;
		assert width>150;
	}
	
}
