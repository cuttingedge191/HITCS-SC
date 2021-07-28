package labels;

public class Course {

	// rep / fields:
	private final int courseID;
	private final String courseName;
	private final String teacherName;
	private final String location;
	private final int hourPerWeek;

	// Abstraction function:
	/**
	 * AF(courseID) = 课程ID
	 * AF(courseName) = 课程名
	 * AF(teacherName) = 教师名字
	 * AF(location) = 地点
	 */

	// Representation invariant:
	// 无要求

	// Safety from rep exposure:
	// 属性均设置为private final

	// constructor
	public Course(int courseID, String courseName, String teacherName, String location, int hourPerWeek) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.teacherName = teacherName;
		this.location = location;
		this.hourPerWeek = hourPerWeek;
	}

	/**
	 * 获取课程ID
	 * 
	 * @return 课程ID
	 */
	public int getCourseID() {
		return this.courseID;
	}

	/**
	 * 获取课程名
	 * 
	 * @return 课程名
	 */
	public String getCourseName() {
		return this.courseName;
	}

	/**
	 * 获取教师名字
	 * 
	 * @return 教师名字
	 */
	public String getTeacherName() {
		return this.teacherName;
	}
	
	/**
	 * 获取课程地点
	 * 
	 * @return 课程地点
	 */
	public String getLocation() {
		return this.location;
	}
	
	/**
	 * 获取课程总周学时
	 * 
	 * @return 课程总周学时
	 */
	public int hourPerWeek() {
		return this.hourPerWeek;
	}
	
	@Override
	public String toString() {
		return "" + this.courseID + "\t" + this.courseName + "\t" + this.teacherName + "\t" + this.location + "\t" + this.hourPerWeek;
	}

	@Override
	public int hashCode() {
		return this.courseID + this.courseName.hashCode() + this.teacherName.hashCode() + this.location.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Course))
			return false;
		Course c = (Course) o;
		return c.courseID == this.courseID 
				&& c.courseName.equals(this.courseName) 
				&& c.teacherName == this.teacherName
				&& c.location == this.location;
	}
}
