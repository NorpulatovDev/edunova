package com.ogabek.edunova.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_name", columnList = "name")
})
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"courses"})
@ToString(exclude = {"courses"})
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"),
            indexes = {
                    @Index(name = "idx_student_courses_student", columnList = "student_id"),
                    @Index(name = "idx_student_courses_course", columnList = "course_id")
            }
    )
    private Set<Course> courses = new HashSet<>();

    // Helper methods for managing relationships
    public void addCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }
}