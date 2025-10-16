package com.ogabek.edunova.service;

import com.ogabek.edunova.dto.ExpenseDTO;
import com.ogabek.edunova.entity.Expense;
import com.ogabek.edunova.exception.ResourceNotFoundException;
import com.ogabek.edunova.mapper.EntityMapper;
import com.ogabek.edunova.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExpenseService {
    
    private final ExpenseRepository expenseRepository;
    private final EntityMapper mapper;
    
    public ExpenseService(ExpenseRepository expenseRepository, EntityMapper mapper) {
        this.expenseRepository = expenseRepository;
        this.mapper = mapper;
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseDTO> findAll() {
        return expenseRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExpenseDTO> findByMonth(String month) {
        return expenseRepository.findByMonthAndDeletedFalse(month)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ExpenseDTO findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
        if (expense.getDeleted()) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        
        return mapper.toDTO(expense);
    }
    
    @Transactional
    public ExpenseDTO create(ExpenseDTO dto) {
        Expense expense = mapper.toEntity(dto);
        expense.setDeleted(false);
        Expense saved = expenseRepository.save(expense);
        
        log.info("Expense created with id: {}", saved.getId());
        return mapper.toDTO(saved);
    }
    
    @Transactional
    public ExpenseDTO update(Long id, ExpenseDTO dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
        if (expense.getDeleted()) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setMonth(dto.getMonth());
        
        Expense updated = expenseRepository.save(expense);
        
        log.info("Expense updated with id: {}", id);
        return mapper.toDTO(updated);
    }
    
    @Transactional
    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
        expense.setDeleted(true);
        expenseRepository.save(expense);
        
        log.info("Expense soft deleted with id: {}", id);
    }
}
