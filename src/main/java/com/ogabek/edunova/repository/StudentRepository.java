package com.ogabek.edunova.repository;

import com.ogabek.edunova.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByDeletedFalse();

    @Query("SELECT s FROM Student s JOIN FETCH s.courses c WHERE s.deleted = false AND c.deleted = false")
    List<Student> findAllWithCourses();

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId AND s.deleted = false AND c.deleted = false")
    List<Student> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id AND s.deleted = false")
    Student findByIdWithCourses(@Param("id") Long id);
}