package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.TeacherDTO;
import com.ogabek.edunova.entity.Teacher;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final EntityMapper mapper;

    public TeacherService(TeacherRepository teacherRepository, EntityMapper mapper) {
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TeacherDTO> findAll() {
        return teacherRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeacherDTO findById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        if (teacher.getDeleted()) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        return mapper.toDTO(teacher);
    }

    @Transactional
    public TeacherDTO create(TeacherDTO dto) {
        Teacher teacher = mapper.toEntity(dto);
        teacher.setDeleted(false);
        Teacher saved = teacherRepository.save(teacher);

        log.info("Teacher created with id: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Transactional
    public TeacherDTO update(Long id, TeacherDTO dto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        if (teacher.getDeleted()) {
            throw new ResourceNotFoundException("Teacher not found with id: " + id);
        }

        teacher.setName(dto.getName());
        teacher.setSalaryPercentage(dto.getSalaryPercentage());
        Teacher updated = teacherRepository.save(teacher);

        log.info("Teacher updated with id: {}", id);
        return mapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        teacher.setDeleted(true);
        teacherRepository.save(teacher);

        log.info("Teacher soft deleted with id: {}", id);
    }
}
