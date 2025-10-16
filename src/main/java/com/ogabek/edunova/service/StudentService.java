package com.ogabek.edunova.service;

import com.ogabek.edunova.entity.Student;
import com.ogabek.edunova.dto.StudentDTO;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final EntityMapper mapper;
    
    public StudentService(StudentRepository studentRepository, EntityMapper mapper) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<StudentDTO> findAll() {
        return studentRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public StudentDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        if (student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        
        return mapper.toDTO(student);
    }
    
    @Transactional
    public StudentDTO create(StudentDTO dto) {
        Student student = mapper.toEntity(dto);
        student.setDeleted(false);
        Student saved = studentRepository.save(student);
        
        log.info("Student created with id: {}", saved.getId());
        return mapper.toDTO(saved);
    }
    
    @Transactional
    public StudentDTO update(Long id, StudentDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        if (student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        
        student.setName(dto.getName());
        Student updated = studentRepository.save(student);
        
        log.info("Student updated with id: {}", id);
        return mapper.toDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        student.setDeleted(true);
        studentRepository.save(student);
        
        log.info("Student soft deleted with id: {}", id);
    }
}
