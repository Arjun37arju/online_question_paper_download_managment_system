package com.online_question_paper.online_question_paper_download_system.entity;

import java.io.Serializable;

public class CollegeDepartmentId implements Serializable {

    private String collegeCode;
    private String departmentId;

    // Required
    public CollegeDepartmentId() {}

    public CollegeDepartmentId(String collegeCode, String departmentId) {
        this.collegeCode = collegeCode;
        this.departmentId = departmentId;
    }
}