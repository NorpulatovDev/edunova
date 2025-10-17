package com.ogabek.edunova.config;

import com.ogabek.edunova.entity.*;
import com.ogabek.edunova.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.YearMonth;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            PaymentRepository paymentRepository,
            ExpenseRepository expenseRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Check if data already exists
            if (userRepository.count() > 0) {
                log.info("Data already initialized. Skipping...");
                return;
            }

            log.info("Starting data initialization...");

            // ========== Create Users ==========
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setActive(true);
            userRepository.save(admin);
            log.info("Created user: admin");

            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setActive(true);
            userRepository.save(manager);
            log.info("Created user: manager");

            // ========== Create Teachers ==========
            Teacher teacher1 = new Teacher();
            teacher1.setName("John Smith");
            teacher1.setSalaryPercentage(new BigDecimal("50.00")); // 50% of payments
            teacher1.setDeleted(false);
            teacherRepository.save(teacher1);
            log.info("Created teacher: John Smith (50% salary)");

            Teacher teacher2 = new Teacher();
            teacher2.setName("Sarah Johnson");
            teacher2.setSalaryPercentage(new BigDecimal("60.00")); // 60% of payments
            teacher2.setDeleted(false);
            teacherRepository.save(teacher2);
            log.info("Created teacher: Sarah Johnson (60% salary)");

            Teacher teacher3 = new Teacher();
            teacher3.setName("Michael Brown");
            teacher3.setSalaryPercentage(new BigDecimal("45.00")); // 45% of payments
            teacher3.setDeleted(false);
            teacherRepository.save(teacher3);
            log.info("Created teacher: Michael Brown (45% salary)");

            // ========== Create Students ==========
            Student student1 = new Student();
            student1.setName("Alice Wilson");
            student1.setDeleted(false);
            studentRepository.save(student1);
            log.info("Created student: Alice Wilson");

            Student student2 = new Student();
            student2.setName("Bob Martinez");
            student2.setDeleted(false);
            studentRepository.save(student2);
            log.info("Created student: Bob Martinez");

            Student student3 = new Student();
            student3.setName("Charlie Davis");
            student3.setDeleted(false);
            studentRepository.save(student3);
            log.info("Created student: Charlie Davis");

            Student student4 = new Student();
            student4.setName("Diana Garcia");
            student4.setDeleted(false);
            studentRepository.save(student4);
            log.info("Created student: Diana Garcia");

            Student student5 = new Student();
            student5.setName("Emma Rodriguez");
            student5.setDeleted(false);
            studentRepository.save(student5);
            log.info("Created student: Emma Rodriguez");

            Student student6 = new Student();
            student6.setName("Frank Miller");
            student6.setDeleted(false);
            studentRepository.save(student6);
            log.info("Created student: Frank Miller");

            // Soft deleted student (for testing)
            Student deletedStudent = new Student();
            deletedStudent.setName("Deleted Student");
            deletedStudent.setDeleted(true);
            studentRepository.save(deletedStudent);
            log.info("Created soft-deleted student: Deleted Student");

            // ========== Create Courses ==========
            Course course1 = new Course();
            course1.setName("Mathematics");
            course1.setTeacher(teacher1);
            course1.setMonthlyFee(new BigDecimal("200.00"));
            course1.setDeleted(false);
            courseRepository.save(course1);
            log.info("Created course: Mathematics (Teacher: John Smith, Fee: $200)");

            Course course2 = new Course();
            course2.setName("Physics");
            course2.setTeacher(teacher1);
            course2.setMonthlyFee(new BigDecimal("250.00"));
            course2.setDeleted(false);
            courseRepository.save(course2);
            log.info("Created course: Physics (Teacher: John Smith, Fee: $250)");

            Course course3 = new Course();
            course3.setName("English Literature");
            course3.setTeacher(teacher2);
            course3.setMonthlyFee(new BigDecimal("180.00"));
            course3.setDeleted(false);
            courseRepository.save(course3);
            log.info("Created course: English Literature (Teacher: Sarah Johnson, Fee: $180)");

            Course course4 = new Course();
            course4.setName("Computer Science");
            course4.setTeacher(teacher2);
            course4.setMonthlyFee(new BigDecimal("300.00"));
            course4.setDeleted(false);
            courseRepository.save(course4);
            log.info("Created course: Computer Science (Teacher: Sarah Johnson, Fee: $300)");

            Course course5 = new Course();
            course5.setName("Chemistry");
            course5.setTeacher(teacher3);
            course5.setMonthlyFee(new BigDecimal("220.00"));
            course5.setDeleted(false);
            courseRepository.save(course5);
            log.info("Created course: Chemistry (Teacher: Michael Brown, Fee: $220)");

            // Soft deleted course (for testing)
            Course deletedCourse = new Course();
            deletedCourse.setName("Old Course");
            deletedCourse.setTeacher(teacher3);
            deletedCourse.setMonthlyFee(new BigDecimal("100.00"));
            deletedCourse.setDeleted(true);
            courseRepository.save(deletedCourse);
            log.info("Created soft-deleted course: Old Course");

            // ========== Setup Student-Course Relationships ==========
            // Student 1: Mathematics + English Literature
            student1.addCourse(course1);
            student1.addCourse(course3);

            // Student 2: Physics + Computer Science
            student2.addCourse(course2);
            student2.addCourse(course4);

            // Student 3: Mathematics + Chemistry
            student3.addCourse(course1);
            student3.addCourse(course5);

            // Student 4: Computer Science + Chemistry + Physics
            student4.addCourse(course4);
            student4.addCourse(course5);
            student4.addCourse(course2);

            // Student 5: English Literature + Mathematics
            student5.addCourse(course3);
            student5.addCourse(course1);

            // Student 6: Physics only
            student6.addCourse(course2);

            // Save all students with their course relationships
            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);
            studentRepository.save(student4);
            studentRepository.save(student5);
            studentRepository.save(student6);

            log.info("Student-Course enrollments completed");

            String currentMonth = YearMonth.now().toString(); // e.g., "2025-10"
            String lastMonth = YearMonth.now().minusMonths(1).toString(); // e.g., "2025-09"
            String nextMonth = YearMonth.now().plusMonths(1).toString(); // e.g., "2025-11"

            // ========== Create Payments for Current Month ==========
            // Student 1 - Has payments for both courses
            Payment payment1 = new Payment();
            payment1.setStudent(student1);
            payment1.setCourse(course1); // Mathematics
            payment1.setAmount(new BigDecimal("200.00"));
            payment1.setMonth(currentMonth);
            payment1.setPaid(true);
            payment1.setDeleted(false);
            paymentRepository.save(payment1);
            log.info("Payment: Alice Wilson -> Mathematics ($200, PAID, {})", currentMonth);

            Payment payment2 = new Payment();
            payment2.setStudent(student1);
            payment2.setCourse(course3); // English Literature
            payment2.setAmount(new BigDecimal("180.00"));
            payment2.setMonth(currentMonth);
            payment2.setPaid(true);
            payment2.setDeleted(false);
            paymentRepository.save(payment2);
            log.info("Payment: Alice Wilson -> English Literature ($180, PAID, {})", currentMonth);

            // Student 2 - Has payment for Physics only (missing Computer Science payment)
            Payment payment3 = new Payment();
            payment3.setStudent(student2);
            payment3.setCourse(course2); // Physics
            payment3.setAmount(new BigDecimal("250.00"));
            payment3.setMonth(currentMonth);
            payment3.setPaid(true);
            payment3.setDeleted(false);
            paymentRepository.save(payment3);
            log.info("Payment: Bob Martinez -> Physics ($250, PAID, {})", currentMonth);

            // Student 3 - Has unpaid payment for Mathematics (Chemistry payment missing completely)
            Payment payment4 = new Payment();
            payment4.setStudent(student3);
            payment4.setCourse(course1); // Mathematics
            payment4.setAmount(new BigDecimal("200.00"));
            payment4.setMonth(currentMonth);
            payment4.setPaid(false); // UNPAID
            payment4.setDeleted(false);
            paymentRepository.save(payment4);
            log.info("Payment: Charlie Davis -> Mathematics ($200, UNPAID, {})", currentMonth);

            // Student 4 - Has payments for Computer Science and Chemistry (missing Physics)
            Payment payment5 = new Payment();
            payment5.setStudent(student4);
            payment5.setCourse(course4); // Computer Science
            payment5.setAmount(new BigDecimal("300.00"));
            payment5.setMonth(currentMonth);
            payment5.setPaid(true);
            payment5.setDeleted(false);
            paymentRepository.save(payment5);
            log.info("Payment: Diana Garcia -> Computer Science ($300, PAID, {})", currentMonth);

            Payment payment6 = new Payment();
            payment6.setStudent(student4);
            payment6.setCourse(course5); // Chemistry
            payment6.setAmount(new BigDecimal("220.00"));
            payment6.setMonth(currentMonth);
            payment6.setPaid(true);
            payment6.setDeleted(false);
            paymentRepository.save(payment6);
            log.info("Payment: Diana Garcia -> Chemistry ($220, PAID, {})", currentMonth);

            // Student 5 - Has payment for English Literature only (missing Mathematics)
            Payment payment7 = new Payment();
            payment7.setStudent(student5);
            payment7.setCourse(course3); // English Literature
            payment7.setAmount(new BigDecimal("180.00"));
            payment7.setMonth(currentMonth);
            payment7.setPaid(true);
            payment7.setDeleted(false);
            paymentRepository.save(payment7);
            log.info("Payment: Emma Rodriguez -> English Literature ($180, PAID, {})", currentMonth);

            // Student 6 - Has partial payment for Physics
            Payment payment8 = new Payment();
            payment8.setStudent(student6);
            payment8.setCourse(course2); // Physics
            payment8.setAmount(new BigDecimal("150.00")); // Partial payment
            payment8.setMonth(currentMonth);
            payment8.setPaid(true);
            payment8.setDeleted(false);
            paymentRepository.save(payment8);
            log.info("Payment: Frank Miller -> Physics ($150, PAID - Partial, {})", currentMonth);

            // ========== Create Expenses for Current Month ==========
            Expense expense1 = new Expense();
            expense1.setDescription("Office Rent");
            expense1.setAmount(new BigDecimal("1000.00"));
            expense1.setMonth(currentMonth);
            expense1.setDeleted(false);
            expenseRepository.save(expense1);
            log.info("Expense: Office Rent ($1000, {})", currentMonth);

            Expense expense2 = new Expense();
            expense2.setDescription("Electricity Bill");
            expense2.setAmount(new BigDecimal("150.00"));
            expense2.setMonth(currentMonth);
            expense2.setDeleted(false);
            expenseRepository.save(expense2);
            log.info("Expense: Electricity Bill ($150, {})", currentMonth);

            // ========== Summary Log ==========
            log.info("====================================");
            log.info("DATA INITIALIZATION COMPLETE");
            log.info("====================================");
            log.info("Users: 2 (admin, manager)");
            log.info("Teachers: 3 active, 0 deleted");
            log.info("Students: 6 active, 1 deleted");
            log.info("Courses: 5 active, 1 deleted");
            log.info("Student Enrollments Created:");
            log.info("- Alice: Mathematics + English Literature");
            log.info("- Bob: Physics + Computer Science");
            log.info("- Charlie: Mathematics + Chemistry");
            log.info("- Diana: Computer Science + Chemistry + Physics");
            log.info("- Emma: English Literature + Mathematics");
            log.info("- Frank: Physics only");
            log.info("Payment Summary for {}:", currentMonth);
            log.info("- Paid payments: 6 payments");
            log.info("- Unpaid payments: 1 payment (Charlie - Mathematics)");
            log.info("- Missing payments: 5 expected payments missing");
            log.info("====================================");
            log.info("EXPECTED MISSING PAYMENTS FOR {}:", currentMonth);
            log.info("- Bob: Computer Science ($300)");
            log.info("- Charlie: Chemistry ($220)");
            log.info("- Diana: Physics ($250)");
            log.info("- Emma: Mathematics ($200)");
            log.info("====================================");
        };
    }
}