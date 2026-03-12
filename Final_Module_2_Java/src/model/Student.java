package model;

import java.sql.Date;

public class Student {
    private int id;
    private String name;
    private Date dob;
    private String email;
    private boolean sex; // Sử dụng boolean để dùng setBoolean(index, isSex)
    private String phone;
    private String password;
    private Date createdAt;

    public Student() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isSex() { return sex; }
    public void setSex(boolean sex) { this.sex = sex; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}