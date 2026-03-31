package com.online_question_paper.online_question_paper_download_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "question_paper")
public class QuestionPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "selected_paper_name")
    private String selectedPaperName;

    @Column(name = "qp_selected")
    private String qpSelected = "NO";

    // ===== Getters & Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSelectedPaperName() {
        return selectedPaperName;
    }

    public void setSelectedPaperName(String selectedPaperName) {
        this.selectedPaperName = selectedPaperName;
    }

    public String getQpSelected() {
        return qpSelected;
    }

    public void setQpSelected(String qpSelected) {
        this.qpSelected = qpSelected;
    }
}