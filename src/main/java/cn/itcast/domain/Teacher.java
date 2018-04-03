package cn.itcast.domain;

import javax.persistence.*;

@Entity
@Table(name = "T_TEACHER")
public class Teacher {
    @Id
    @GeneratedValue()
    @Column(name = "T_ID")
    private int id; // 主键id
    @Column(name = "T_USERNAME")
    private String username; // 用户名
    @Column(name = "T_PASSWORD")
    private String password; // 密码
    @Column(name = "T_AGE")
    private int age;
    @Column(name = "T_WORKYEAR")
    private int workyear; //工作年限
    @Column(name = "T_NAME")
    private String name;//姓名
    @Column(name = "T_SEX")
    private String sex; // 性别
    @Column(name = "T_TELEPHONE")
    private String telephone; // 手机
    @Column(name = "T_DEPARTMENT")
    private String department; // 部门
    @Column(name = "T_POSITION")
    private String position; // 职位
    @Column(name = "T_ADDRESS")
    private String address; // 地址

    @Column(name = "T_EMAIL")
    private String email; // 邮箱
    @Column(name = "T_TYPE")
    private String type;//状态（是否离职）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWorkyear() {
        return workyear;
    }

    public void setWorkyear(int workyear) {
        this.workyear = workyear;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", workyear=" + workyear +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", telephone='" + telephone + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
