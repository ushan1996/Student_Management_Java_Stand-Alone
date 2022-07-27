package tm;

public class StudentsDetailsTM {
    private String id;
    private String user_id;
    private String name;
    private int age;
    private int grade;
    private String city;

    public StudentsDetailsTM(String id, String user_id, String name, int age, int grade, String city) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.age = age;
        this.grade = grade;
        this.city = city;
    }

    public StudentsDetailsTM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return  id + ',' + user_id + ',' + name + ',' + age +',' + grade +','+city ;
    }
}
