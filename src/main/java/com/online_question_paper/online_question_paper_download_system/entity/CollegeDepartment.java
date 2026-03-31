package com.online_question_paper.online_question_paper_download_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "college_department")
@IdClass(CollegeDepartmentId.class)
public class CollegeDepartment {

    @Id
    @Column(name = "college_code")
    private String collegeCode;

    @Id
    @Column(name = "department_id")
    private String departmentId;


    // ===== Getters & Setters =====

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}