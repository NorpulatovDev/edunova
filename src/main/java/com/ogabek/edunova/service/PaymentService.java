package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.PaymentDTO;
import com.ogabek.edunova.entity.Course;
import com.ogabek.edunova.entity.Payment;
import com.ogabek.edunova.entity.Student;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.CourseRepository;
import com.ogabek.edunova.repository.PaymentRepository;
import com.ogabek.edunova.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EntityMapper mapper;
    
    public PaymentService(PaymentRepository paymentRepository,
                         StudentRepository studentRepository,
                         CourseRepository courseRepository,
                         EntityMapper mapper) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll() {
        return paymentRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByMonth(String month) {
        return paymentRepository.findByMonthAndDeletedFalse(month)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PaymentDTO findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        
        if (payment.getDeleted()) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        
        return mapper.toDTO(payment);
    }
    
    @Transactional
    public PaymentDTO create(PaymentDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));
        
        if (student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + dto.getStudentId());
        }
        
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + dto.getCourseId()));
        
        if (course.getDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + dto.getCourseId());
        }
        
        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setAmount(dto.getAmount());
        payment.setMonth(dto.getMonth());
        payment.setPaid(dto.getPaid());
        payment.setDeleted(false);
        
        Payment saved = paymentRepository.save(payment);
        
        log.info("Payment created with id: {}", saved.getId());
        return mapper.toDTO(saved);
    }
    
    @Transactional
    public PaymentDTO update(Long id, PaymentDTO dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        
        if (payment.getDeleted()) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));
        
        if (student.getDeleted()) {
            throw new ResourceNotFoundException("Student not found with id: " + dto.getStudentId());
        }
        
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + dto.getCourseId()));
        
        if (course.getDeleted()) {
            throw new ResourceNotFoundException("Course not found with id: " + dto.getCourseId());
        }
        
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setAmount(dto.getAmount());
        payment.setMonth(dto.getMonth());
        payment.setPaid(dto.getPaid());
        
        Payment updated = paymentRepository.save(payment);
        
        log.info("Payment updated with id: {}", id);
        return mapper.toDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        
        payment.setDeleted(true);
        paymentRepository.save(payment);
        
        log.info("Payment soft deleted with id: {}", id);
    }
}
