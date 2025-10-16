package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.CourseDTO;
import com.ogabek.edunova.entity.Course;
import com.ogabek.edunova.entity.Teacher;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.CourseRepository;
import com.ogabek.edunova.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final EntityMapper mapper;
    
    public CourseService(CourseRepository courseRepository, 
                        TeacherRepository teacherRepository,
                        EntityMapper mapper) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<CourseDTO> findAll() {
        return courseRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CourseDTO findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        if (course.getDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        
        return mapper.toDTO(course);
    }
    
    @Transactional
    public CourseDTO create(CourseDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + dto.getTeacherId()));
        
        if (teacher.getDeleted()) {
            throw new ResourceNotFoundException("Teacher not found with id: " + dto.getTeacherId());
        }
        
        Course course = new Course();
        course.setName(dto.getName());
        course.setTeacher(teacher);
        course.setMonthlyFee(dto.getMonthlyFee());
        course.setDeleted(false);
        
        Course saved = courseRepository.save(course);
        
        log.info("Course created with id: {}", saved.getId());
        return mapper.toDTO(saved);
    }
    
    @Transactional
    public CourseDTO update(Long id, CourseDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        if (course.getDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + dto.getTeacherId()));
        
        if (teacher.getDeleted()) {
            throw new ResourceNotFoundException("Teacher not found with id: " + dto.getTeacherId());
        }
        
        course.setName(dto.getName());
        course.setTeacher(teacher);
        course.setMonthlyFee(dto.getMonthlyFee());
        
        Course updated = courseRepository.save(course);
        
        log.info("Course updated with id: {}", id);
        return mapper.toDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        course.setDeleted(true);
        courseRepository.save(course);
        
        log.info("Course soft deleted with id: {}", id);
    }
}