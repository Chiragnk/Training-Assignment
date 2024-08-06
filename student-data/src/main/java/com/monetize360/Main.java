package com.monetize360;
import com.monetize360.student.domain.StudentDetails;
import com.monetize360.student.dto.CountDTO;
import com.monetize360.student.dto.StudentDTO;
import com.monetize360.student.service.CourseStat;
import com.monetize360.student.service.CourseStatImpl;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CourseStat courseStat = new CourseStatImpl("coursedata.csv");
        Scanner s=new Scanner(System.in);
        System.out.println("Enter the qualification");
        String qualification=s.nextLine();
        List<StudentDetails> studentsBE = courseStat.studentsByQualification(qualification);
        System.out.println("Students with qualification: "+qualification);
        for (StudentDetails student : studentsBE) {
            System.out.println(student);
        }
        System.out.println("Enter the qualification");
        String qualification1=s.nextLine();
        int count = courseStat.getStudentCountByQualification(qualification1);
        System.out.println("Count of students with qualification " + qualification1 + ": " + count);
        int placedCount = courseStat.getPlacedStudentCount();
        System.out.println("Number of students who have been placed: " + placedCount);
        int notplacedCount=courseStat.getNotPlacedStudentCount();
        System.out.println("Number of students who have completed the course and not been placed:"+notplacedCount);
        CountDTO countDTO = courseStat.getPlacedAndNotPlacedCount();
        System.out.println("Number of students who have been placed: " + countDTO.getPlacedCount());
        System.out.println("Number of students who have not been placed: " + countDTO.getNotPlacedCount());
        System.out.println("Enter the Name");
        String name=s.nextLine();
        List<StudentDetails> students = courseStat.search(name);
        if (students.isEmpty()) {
            System.out.println("No students found with the name containing: " + name);
        } else {
            System.out.println("Students found:" );
            for (StudentDetails student : students) {
                System.out.println(student);
            }
        }
        System.out.println("enter the batch");
        String batch=s.nextLine();
        float successRate=courseStat.successRate(batch);
        System.out.println("Average Success Rate in "+batch+":"+successRate);
        List<StudentDetails> maxScoreStudent=courseStat.maxScoreStudent();
        System.out.println("The Students with maximum score are:"+maxScoreStudent);
        List<String>studentNames=courseStat.getStudentNames();
        System.out.println("All Student Names:"+studentNames);
        List<StudentDTO> studentDTOList = courseStat.getStudentDetails();
        if (studentDTOList.isEmpty()) {
            System.out.println("No student details found.");
        } else {
            System.out.println("Student Details:");
           studentDTOList.forEach(System.out::println);
            }
        }
    }



