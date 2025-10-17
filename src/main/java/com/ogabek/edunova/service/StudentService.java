package com.ogabek.edunova.service;

import com.ogabek.edunova.entity.Course;
import com.ogabek.edunova.entity.Student;
import com.ogabek.edunova.dto.StudentDTO;
import com.ogabek.edunova.dto.StudentCourseEnrollmentDTO;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.CourseRepository;
import com.ogabek.edunova.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EntityMapper mapper;

    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          EntityMapper mapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
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
    public List<StudentDTO> findByCourseId(Long courseId) {
        return studentRepository.findByCourseId(courseId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudentDTO findById(Long id) {
        Student student = studentRepository.findByIdWithCourses(id);
        if (student == null || student.getDeleted()) {
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

    @Transactional
    public StudentDTO enrollInCourses(StudentCourseEnrollmentDTO enrollmentDTO) {
        Student student = studentRepository.findByIdWithCourses(enrollmentDTO.getStudentId());
        if (student == null || student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + enrollmentDTO.getStudentId());
        }

        List<Course> courses = courseRepository.findAllById(enrollmentDTO.getCourseIds());

        // Validate all courses exist and are not deleted
        for (Long courseId : enrollmentDTO.getCourseIds()) {
            Course course = courses.stream()
                    .filter(c -> c.getId().equals(courseId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

            if (course.getDeleted()) {
                throw new ResourceNotFoundException("Course not found with id: " + courseId);
            }
        }

        // Clear existing courses and add new ones
        student.getCourses().clear();
        courses.forEach(student::addCourse);

        Student updated = studentRepository.save(student);
        log.info("Student {} enrolled in {} courses", student.getId(), courses.size());

        return mapper.toDTO(updated);
    }

    @Transactional
    public StudentDTO addToCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findByIdWithCourses(studentId);
        if (student == null || student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (course.getDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        student.addCourse(course);
        Student updated = studentRepository.save(student);

        log.info("Student {} added to course {}", studentId, courseId);
        return mapper.toDTO(updated);
    }

    @Transactional
    public StudentDTO removeFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findByIdWithCourses(studentId);
        if (student == null || student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        student.removeCourse(course);
        Student updated = studentRepository.save(student);

        log.info("Student {} removed from course {}", studentId, courseId);
        return mapper.toDTO(updated);
    }
}