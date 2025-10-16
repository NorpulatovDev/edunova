package com.ogabek.edunova.mapper;

import com.ogabek.edunova.dto.*;
import com.ogabek.edunova.entity.*;
import org.springframework.stereotype.Component;

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
        return new StudentDTO(
                student.getId(),
                student.getName()
        );
    }

    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        return student;
    }

    public CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getTeacher().getId(),
                course.getTeacher().getName(),
                course.getMonthlyFee()
        );
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