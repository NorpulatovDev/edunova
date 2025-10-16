package com.ogabek.edunova.controller;

import com.ogabek.edunova.dto.TeacherSalaryDTO;
import com.ogabek.edunova.service.TeacherSalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class TeacherSalaryController {
    
    private final TeacherSalaryService teacherSalaryService;
    
    public TeacherSalaryController(TeacherSalaryService teacherSalaryService) {
        this.teacherSalaryService = teacherSalaryService;
    }
    
    @GetMapping("/teacher/{teacherId}/month/{month}")
    public ResponseEntity<TeacherSalaryDTO> getTeacherSalary(
            @PathVariable Long teacherId,
            @PathVariable String month) {
        return ResponseEntity.ok(teacherSalaryService.calculateSalary(teacherId, month));
    }
    
    @GetMapping("/month/{month}")
    public ResponseEntity<List<TeacherSalaryDTO>> getAllTeachersSalaries(@PathVariable String month) {
        return ResponseEntity.ok(teacherSalaryService.calculateAllTeachersSalaries(month));
    }
}