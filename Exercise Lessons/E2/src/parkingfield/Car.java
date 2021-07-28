package parkingfield;

//An immutable class 一辆车
class Car {
	private final String plateNo; // 车牌号
    private final int width; //车辆宽度，自然数 (度量单位为厘米)，>100
    
    //AF:
    //车辆由唯一的车牌号和宽度值表示 
    
    // Representation invariant:
    // 车牌号不为空，长度大于0
    // 车辆宽度>100
    
    // Safety from rep exposure:
    //   All fields are private and final
    
    
    public Car(String plateNo, int width) {  // 构造器
        this.plateNo = plateNo;
        this.width = width;
        checkRep();
    }

    public String getPlateNo() {   //返回车牌号
        return plateNo;
    }

    public int getWidth() {  //返回宽度
        return width;
    }

    @Override  //利用IDE工具自动生成
	public boolean equals(Object car) {
		if (this == car)
			return true;
		if (car == null)
			return false;
		if (getClass() != car.getClass())
			return false;
		Car other = (Car) car;
		if (plateNo == null) {
			if (other.plateNo != null)
				return false;
		} else if (!plateNo.equals(other.plateNo))
			return false;
		return width == other.width;
	}

    @Override   //利用IDE工具自动生成
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plateNo == null) ? 0 : plateNo.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override  //利用IDE工具自动生成
	public String toString() {
		return "Car [plateNo=" + plateNo + ", width=" + width + "]";
	}
	
	private void checkRep() {
		assert plateNo!=null;
		assert plateNo.length()>0;
		assert width>100;
	}
}
