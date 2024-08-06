package com.monetize360.student.service;

import com.monetize360.student.domain.StudentDetails;
import com.monetize360.student.dto.CountDTO;
import com.monetize360.student.dto.StudentDTO;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseStatImpl implements CourseStat {

    private List<StudentDetails> allStudents;

    public CourseStatImpl(String csvFileName) {
        try {
            List<String[]> csvData = CsvReaderUtil.readCsv(csvFileName);
            this.allStudents = parseStudentData(csvData);
        } catch (IOException e) {
            e.printStackTrace();
            this.allStudents = new ArrayList<>();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private List<StudentDetails> parseStudentData(List<String[]> csvData) {
        final boolean[] isFirstRow = {true};

        return csvData.stream()
                .filter(values -> {
                    if (isFirstRow[0]) {
                        isFirstRow[0] = false;
                        return false;
                    }
                    return values.length == 6;
                })
                .map(values -> {
                    try {
                        String name = values[0];
                        String batch = values[1];
                        String completed = values[2];
                        String placed = values[3];
                        String qualification = values[4];
                        double score = Double.parseDouble(values[5]);
                        return new StudentDetails(name, batch, completed, placed, qualification, score);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(student -> student != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDetails> studentsByQualification(String qualification) {
        return allStudents.stream()
                .filter(student -> qualification.equalsIgnoreCase(student.getQualification()))
                .collect(Collectors.toList());
    }

    @Override
    public int getStudentCountByQualification(String qualification) {
        return (int) allStudents.stream()
                .filter(student -> qualification.equalsIgnoreCase(student.getQualification()))
                .count();
    }

    @Override
    public int getPlacedStudentCount() {
        return (int) allStudents.stream()
                .filter(student -> "Y".equals(student.getPlaced()))
                .count();
    }

    @Override
    public int getNotPlacedStudentCount() {
        return (int) allStudents.stream()
                .filter(student -> "Y".equals(student.getCompleted()) && "N".equals(student.getPlaced()))
                .count();
    }
    public int getTotalNotPlacedStudentCount() {
        return (int) allStudents.stream()
                .filter(student ->"N".equals(student.getPlaced()))
                .count();
    }

    @Override
    public CountDTO getPlacedAndNotPlacedCount() {
        return new CountDTO(getPlacedStudentCount(), getTotalNotPlacedStudentCount());
    }

    @Override
    public List<StudentDetails> search(String str) {
        return allStudents.stream()
                .filter(student -> student.getName() != null && student.getName().equalsIgnoreCase(str))
                .collect(Collectors.toList());
    }

    @Override
    public float successRate(String batchName) {
        int totalStudents = (int) allStudents.stream()
                .filter(student -> student.getBatch().equalsIgnoreCase(batchName))
                .count();

        int placedStudents = (int) allStudents.stream()
                .filter(student -> student.getBatch().equalsIgnoreCase(batchName) && "Y".equalsIgnoreCase(student.getPlaced()))
                .count();

        return (float) placedStudents / totalStudents * 100;
    }

    @Override
    public List<StudentDetails> maxScoreStudent() {
        double maxScore = allStudents.stream()
                .mapToDouble(StudentDetails::getScore)
                .max()
                .orElse(-1);

        return allStudents.stream()
                .filter(student -> student.getScore() == maxScore)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getStudentNames() {
        return allStudents.stream()
                .map(StudentDetails::getName)
                .collect(Collectors.toList());
    }
    public List<StudentDTO> getStudentDetails() {
        return allStudents.stream()
                .map(student -> new StudentDTO(
                        student.getName(),
                        student.getQualification(),
                        student.getScore()
                ))
                .collect(Collectors.toList());
    }
}
