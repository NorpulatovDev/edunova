package com.ogabek.edunova.repository;

import com.ogabek.edunova.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDeletedFalse();

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.deleted = false")
    List<Course> findAllWithStudents();

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId AND c.deleted = false AND s.deleted = false")
    List<Course> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id AND c.deleted = false")
    Course findByIdWithStudents(@Param("id") Long id);
}