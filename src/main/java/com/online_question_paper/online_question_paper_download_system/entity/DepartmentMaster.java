package com.online_question_paper.online_question_paper_download_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department_master")
public class DepartmentMaster {

    @Id
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;


    // ===== Getters & Setters =====

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}