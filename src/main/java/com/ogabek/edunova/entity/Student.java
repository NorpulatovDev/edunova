package com.ogabek.edunova.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students", indexes = {
    @Index(name = "idx_student_name", columnList = "name")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;
}