package com.example.testbtl2.models;

public class Employee extends User {
    private String department;

    // Constructor yêu cầu 2 tham số (id và name)
    public Employee(String id, String name) {
        super(id, name);
        this.department = "Default Department";  // Giá trị mặc định cho department
    }

    // Getter method cho department
    public String getDepartment() {
        return department;
    }

    @Override
    public String getUserInfo() {
        return String.format("Employee[ID=%s, Name=%s, Department=%s]", id, name, department);
    }
}
