package com.ogabek.edunova.repository;

import com.ogabek.edunova.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByDeletedFalse();
    List<Expense> findByMonthAndDeletedFalse(String month);
}
