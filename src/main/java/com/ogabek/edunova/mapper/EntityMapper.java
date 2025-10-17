package com.ogabek.edunova.mapper;

import com.ogabek.edunova.dto.*;
import com.ogabek.edunova.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getName(),
                teacher.getSalaryPercentage()
        );
    }

    public Teacher toEntity(TeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setName(dto.getName());
        teacher.setSalaryPercentage(dto.getSalaryPercentage());
        return teacher;
    }

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());

        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            dto.setCourses(student.getCourses().stream()
                    .filter(course -> !course.getDeleted())
                    .map(course -> new StudentDTO.CourseInfo(
                            course.getId(),
                            course.getName(),
                            course.getTeacher().getName(),
                            course.getMonthlyFee()
                    ))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        return student;
    }

    public CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setTeacherId(course.getTeacher().getId());
        dto.setTeacherName(course.getTeacher().getName());
        dto.setMonthlyFee(course.getMonthlyFee());

        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            dto.setStudents(course.getStudents().stream()
                    .filter(student -> !student.getDeleted())
                    .map(student -> new CourseDTO.StudentInfo(
                            student.getId(),
                            student.getName()
                    ))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getStudent().getId(),
                payment.getStudent().getName(),
                payment.getCourse().getId(),
                payment.getCourse().getName(),
                payment.getAmount(),
                payment.getMonth(),
                payment.getPaid()
        );
    }

    public ExpenseDTO toDTO(Expense expense) {
        return new ExpenseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getMonth()
        );
    }

    public Expense toEntity(ExpenseDTO dto) {
        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setMonth(dto.getMonth());
        return expense;
    }
}