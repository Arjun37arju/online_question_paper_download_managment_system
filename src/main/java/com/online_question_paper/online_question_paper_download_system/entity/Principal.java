package com.online_question_paper.online_question_paper_download_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "principal")
public class Principal {

    @Id
    @Column(name = "principal_code", length = 20)
    private String principalCode;

    @Column(nullable = false, length = 100)
    private String principalName;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(nullable = false, length = 100)
    private String email;   // ✅ NEW FIELD

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String collegeCode;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    // ================= GETTERS & SETTERS =================

    public String getPrincipalCode() {
        return principalCode;
    }

    public void setPrincipalCode(String principalCode) {
        this.principalCode = principalCode;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {     // ✅ NEW
        return email;
    }

    public void setEmail(String email) {   // ✅ NEW
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}