package com.ogabek.edunova.controller;

import com.ogabek.edunova.dto.StudentDTO;
import com.ogabek.edunova.dto.StudentCourseEnrollmentDTO;
import com.ogabek.edunova.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.findByCourseId(courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentDTO> create(@Valid @RequestBody StudentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> update(@PathVariable Long id, @Valid @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @PostMapping("/enroll")
    public ResponseEntity<StudentDTO> enrollInCourses(@Valid @RequestBody StudentCourseEnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(studentService.enrollInCourses(enrollmentDTO));
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentDTO> addToCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.addToCourse(studentId, courseId));
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<StudentDTO> removeFromCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.removeFromCourse(studentId, courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}