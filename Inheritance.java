//类的继承、方法复用、与多态的演示
public class Inheritance
{
	public void testInheritance()
	{
		;
	}
	public static void main(String[] argv)
	{
		System.out.println("Inheritance class No 1");
		Person p = new Person("苏州", true);
		School s = new School("科大附中");
		Student a = new Student("ComputerScience");
		Student b = new Student("SoftwareEngineer");
		Teacher t = new Teacher("Math","中国科技大学");
		ClassManager m = new ClassManager(10);
		//演示一下多态：编译时和运行时的类型不一致
		//多态：相同类型的变量执行同一个方法时呈现出不同的行为特征
		Person q = new Teacher("English", "合肥工业大学");
		p.show();
		q.show();
		//q.teaching(); 编译时报错了
		//旧的方法在新类中复用，即复用了父类方法
		System.out.println(a.getPersonHomeTown());
		System.out.println(a.getPersonGender());
		System.out.println(b.getPersonHomeTown());
		System.out.println(b.getPersonGender());
		System.out.println(q.getPersonGender());
		System.out.println(q.getPersonHomeTown());
		System.out.println(p.getPersonHomeTown());
		System.out.println(p.getPersonGender());
	}
}

class Person
{
	public int kind = 1;
	private String homeTown = null;
	private boolean gender;
	public Person()
	{
		this.homeTown = "合肥";
		this.gender = false;
	}
	public Person(boolean gender)
	{
		this.gender = gender;
		this.homeTown = "合肥";
	}
	public Person(String homeTown)
	{
		this.homeTown = homeTown;
		this.gender = false;
	}
	public Person(String homeTown,boolean gender)
	{
		this.homeTown = homeTown;
		this.gender = gender;
	}
	public boolean setPersonHomeTown(String homeTown)
	{
		this.homeTown = homeTown;
		return true;
	}
	public boolean setPersonGender(boolean male)
	{
		this.gender = male;
		return true;
	}
	public boolean getPersonGender()
	{
		return this.gender;
	}
	public String getPersonHomeTown()
	{
		return this.homeTown;
	}
	public void show()
	{
		System.out.println("This is human beings!");
	}
}

class School
{
	public int kind = 2;
	private String name = null;
	public School()
	{
		this.name = "第十中学";
	}
	public School(String schoolName)
	{
		this.name = schoolName;
	}
	public void setSchoolName(String name)
	{
		this.name = name;
	}
	public String getSchoolName()
	{
		return this.name;
	}
	public void show()
	{
		System.out.println("This is a school!");
	}
}
class Teacher extends Person
{
	public int kind = 3;
	private String course;
	private School s;
	public Teacher()
	{
		this.course = null;
		this.s = new School();
	}
	public Teacher(String course)
	{
		this.course = course;
		this.s = new School();
	}
	public Teacher(String course, School s)
	{
		this.course = course;
		this.s = s;
	}
	public Teacher(String course, String schoolName)
	{
		this.course = course;
		this.s = new School(schoolName);
	}
	public void setCourse(String course)
	{
		this.course = course;
	}
	public String getCourse()
	{
		return this.course;
	}
	public void setSchool(String schoolName)
	{
		this.s = new School(schoolName);
	}
	public School getSchool()
	{
		return this.s;
	}
	public void teaching()
	{
		System.out.println("Teaching students!");
	}
	public void show()
	{
		System.out.println("This is a teacher!");
	}
}

class Student extends Person
{
	public int kind = 4;
	private String major;
	private School s;
	public Student()
	{
		this.major = null;
		this.s = new School();
	}
	public Student(String major)
	{
		this.major = major;
		this.s = new School();
	}
	public Student(String major, School s)
	{
		this.major = major;
		this.s = s;
	}
	public Student(String major, String schoolName)
	{
		this.major = major;
		this.s = new School(schoolName);
	}
	public void setCourse(String major)
	{
		this.major = major;
	}
	public String getMajor()
	{
		return this.major;
	}
	public void setSchool(String schoolName)
	{
		this.s = new School(schoolName);
	}
	public School getSchool()
	{
		return this.s;
	}
	public void studying()
	{
		System.out.println("Studying courses!");
	}
	public void show()
	{
		System.out.println("This is a student!");
	}
}

class ClassManager extends Teacher
{
	public int kind = 5;
	//第number班级的班主任
	private int classNumber;
	public ClassManager()
	{
		this.classNumber = 0;
	}
	public ClassManager(int classNumber)
	{
		this.classNumber = classNumber;
	}
	public void setClassManager(int classNumber)
	{
		this.classNumber = classNumber;
	}
	public int getClassManager()
	{
		return classNumber;
	}
	public void managing()
	{
		System.out.println("Managing the class!");
	}
	public void show()
	{
		System.out.println("This is a classManager!");
		System.out.println("Managing No." + this.classNumber + "class");
	}
}