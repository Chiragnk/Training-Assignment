package com.monetize360.student.service;

import com.monetize360.student.domain.StudentDetails;
import com.monetize360.student.dto.CountDTO;
import com.monetize360.student.dto.StudentDTO;

import java.util.List;

public interface CourseStat {
    public List<StudentDetails> studentsByQualification(String qualification);
    public int getStudentCountByQualification(String qualification);
    public int getPlacedStudentCount();
    public int getNotPlacedStudentCount();
    public int getTotalNotPlacedStudentCount();
    public CountDTO getPlacedAndNotPlacedCount();
    public List<StudentDetails> search(String str);
    public float successRate(String batchName);
    public List<StudentDetails> maxScoreStudent();
    public List<String> getStudentNames();
    public List<StudentDTO> getStudentDetails();
}
