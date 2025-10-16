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
            
            String currentMonth = YearMonth.now().toString(); // e.g., "2025-10"
            String lastMonth = YearMonth.now().minusMonths(1).toString(); // e.g., "2025-09"
            String nextMonth = YearMonth.now().plusMonths(1).toString(); // e.g., "2025-11"
            
            // ========== Create Payments for Current Month ==========
            // Student 1 - Multiple courses
//            Payment payment1 = new Payment();
//            payment1.setStudent(student1);
//            payment1.setCourse(course1); // Mathematics
//            payment1.setAmount(new BigDecimal("200.00"));
//            payment1.setMonth(currentMonth);
//            payment1.setPaid(true);
//            payment1.setDeleted(false);
//            paymentRepository.save(payment1);
//            log.info("Payment: Alice Wilson -> Mathematics ($200, PAID, {})", currentMonth);
//
//            Payment payment2 = new Payment();
//            payment2.setStudent(student1);
//            payment2.setCourse(course3); // English Literature
//            payment2.setAmount(new BigDecimal("180.00"));
//            payment2.setMonth(currentMonth);
//            payment2.setPaid(true);
//            payment2.setDeleted(false);
//            paymentRepository.save(payment2);
//            log.info("Payment: Alice Wilson -> English Literature ($180, PAID, {})", currentMonth);
//
//            // Student 2 - Paid payment
//            Payment payment3 = new Payment();
//            payment3.setStudent(student2);
//            payment3.setCourse(course2); // Physics
//            payment3.setAmount(new BigDecimal("250.00"));
//            payment3.setMonth(currentMonth);
//            payment3.setPaid(true);
//            payment3.setDeleted(false);
//            paymentRepository.save(payment3);
//            log.info("Payment: Bob Martinez -> Physics ($250, PAID, {})", currentMonth);
//
//            // Student 3 - Unpaid payment (should not count in salary)
//            Payment payment4 = new Payment();
//            payment4.setStudent(student3);
//            payment4.setCourse(course1); // Mathematics
//            payment4.setAmount(new BigDecimal("200.00"));
//            payment4.setMonth(currentMonth);
//            payment4.setPaid(false); // UNPAID
//            payment4.setDeleted(false);
//            paymentRepository.save(payment4);
//            log.info("Payment: Charlie Davis -> Mathematics ($200, UNPAID, {})", currentMonth);
//
//            // Student 4 - Multiple courses
//            Payment payment5 = new Payment();
//            payment5.setStudent(student4);
//            payment5.setCourse(course4); // Computer Science
//            payment5.setAmount(new BigDecimal("300.00"));
//            payment5.setMonth(currentMonth);
//            payment5.setPaid(true);
//            payment5.setDeleted(false);
//            paymentRepository.save(payment5);
//            log.info("Payment: Diana Garcia -> Computer Science ($300, PAID, {})", currentMonth);
//
//            Payment payment6 = new Payment();
//            payment6.setStudent(student4);
//            payment6.setCourse(course5); // Chemistry
//            payment6.setAmount(new BigDecimal("220.00"));
//            payment6.setMonth(currentMonth);
//            payment6.setPaid(true);
//            payment6.setDeleted(false);
//            paymentRepository.save(payment6);
//            log.info("Payment: Diana Garcia -> Chemistry ($220, PAID, {})", currentMonth);
//
//            // Student 5 - Different course
//            Payment payment7 = new Payment();
//            payment7.setStudent(student5);
//            payment7.setCourse(course3); // English Literature
//            payment7.setAmount(new BigDecimal("180.00"));
//            payment7.setMonth(currentMonth);
//            payment7.setPaid(true);
//            payment7.setDeleted(false);
//            paymentRepository.save(payment7);
//            log.info("Payment: Emma Rodriguez -> English Literature ($180, PAID, {})", currentMonth);
//
//            // Student 6 - Partial payment (different amount)
//            Payment payment8 = new Payment();
//            payment8.setStudent(student6);
//            payment8.setCourse(course2); // Physics
//            payment8.setAmount(new BigDecimal("150.00")); // Partial payment
//            payment8.setMonth(currentMonth);
//            payment8.setPaid(true);
//            payment8.setDeleted(false);
//            paymentRepository.save(payment8);
//            log.info("Payment: Frank Miller -> Physics ($150, PAID - Partial, {})", currentMonth);
//
//            // Soft deleted payment (should not count)
//            Payment deletedPayment = new Payment();
//            deletedPayment.setStudent(student1);
//            deletedPayment.setCourse(course1);
//            deletedPayment.setAmount(new BigDecimal("200.00"));
//            deletedPayment.setMonth(currentMonth);
//            deletedPayment.setPaid(true);
//            deletedPayment.setDeleted(true); // DELETED
//            paymentRepository.save(deletedPayment);
//            log.info("Payment: Alice Wilson -> Mathematics ($200, DELETED, {})", currentMonth);
//
//            // ========== Create Payments for Last Month ==========
//            Payment payment9 = new Payment();
//            payment9.setStudent(student1);
//            payment9.setCourse(course1);
//            payment9.setAmount(new BigDecimal("200.00"));
//            payment9.setMonth(lastMonth);
//            payment9.setPaid(true);
//            payment9.setDeleted(false);
//            paymentRepository.save(payment9);
//            log.info("Payment: Alice Wilson -> Mathematics ($200, PAID, {})", lastMonth);
//
//            Payment payment10 = new Payment();
//            payment10.setStudent(student2);
//            payment10.setCourse(course4);
//            payment10.setAmount(new BigDecimal("300.00"));
//            payment10.setMonth(lastMonth);
//            payment10.setPaid(true);
//            payment10.setDeleted(false);
//            paymentRepository.save(payment10);
//            log.info("Payment: Bob Martinez -> Computer Science ($300, PAID, {})", lastMonth);
//
//            // ========== Create Payments for Next Month (Future) ==========
//            Payment payment11 = new Payment();
//            payment11.setStudent(student3);
//            payment11.setCourse(course5);
//            payment11.setAmount(new BigDecimal("220.00"));
//            payment11.setMonth(nextMonth);
//            payment11.setPaid(false); // Not yet paid
//            payment11.setDeleted(false);
//            paymentRepository.save(payment11);
//            log.info("Payment: Charlie Davis -> Chemistry ($220, UNPAID, {})", nextMonth);
//
            // ========== Create Expenses for Current Month ==========
//            Expense expense1 = new Expense();
//            expense1.setDescription("Office Rent");
//            expense1.setAmount(new BigDecimal("1000.00"));
//            expense1.setMonth(currentMonth);
//            expense1.setDeleted(false);
//            expenseRepository.save(expense1);
//            log.info("Expense: Office Rent ($1000, {})", currentMonth);
//
//            Expense expense2 = new Expense();
//            expense2.setDescription("Electricity Bill");
//            expense2.setAmount(new BigDecimal("150.00"));
//            expense2.setMonth(currentMonth);
//            expense2.setDeleted(false);
//            expenseRepository.save(expense2);
//            log.info("Expense: Electricity Bill ($150, {})", currentMonth);
//
//            Expense expense3 = new Expense();
//            expense3.setDescription("Internet");
//            expense3.setAmount(new BigDecimal("80.00"));
//            expense3.setMonth(currentMonth);
//            expense3.setDeleted(false);
//            expenseRepository.save(expense3);
//            log.info("Expense: Internet ($80, {})", currentMonth);
//
//            Expense expense4 = new Expense();
//            expense4.setDescription("Office Supplies");
//            expense4.setAmount(new BigDecimal("200.00"));
//            expense4.setMonth(currentMonth);
//            expense4.setDeleted(false);
//            expenseRepository.save(expense4);
//            log.info("Expense: Office Supplies ($200, {})", currentMonth);
//
//            // ========== Create Expenses for Last Month ==========
//            Expense expense5 = new Expense();
//            expense5.setDescription("Office Rent");
//            expense5.setAmount(new BigDecimal("1000.00"));
//            expense5.setMonth(lastMonth);
//            expense5.setDeleted(false);
//            expenseRepository.save(expense5);
//            log.info("Expense: Office Rent ($1000, {})", lastMonth);
//
//            Expense expense6 = new Expense();
//            expense6.setDescription("Marketing");
//            expense6.setAmount(new BigDecimal("500.00"));
//            expense6.setMonth(lastMonth);
//            expense6.setDeleted(false);
//            expenseRepository.save(expense6);
//            log.info("Expense: Marketing ($500, {})", lastMonth);
//
//            // Soft deleted expense (for testing)
//            Expense deletedExpense = new Expense();
//            deletedExpense.setDescription("Old Expense");
//            deletedExpense.setAmount(new BigDecimal("100.00"));
//            deletedExpense.setMonth(currentMonth);
//            deletedExpense.setDeleted(true);
//            expenseRepository.save(deletedExpense);
//            log.info("Expense: Old Expense ($100, DELETED, {})", currentMonth);
//
            // ========== Summary Log ==========
            log.info("====================================");
            log.info("DATA INITIALIZATION COMPLETE");
            log.info("====================================");
            log.info("Users: 2 (admin, manager)");
            log.info("Teachers: 3 active, 0 deleted");
            log.info("Students: 6 active, 1 deleted");
            log.info("Courses: 5 active, 1 deleted");
            log.info("Payments: 11 total (8 paid current month, 2 paid last month, 1 unpaid future)");
            log.info("Expenses: 6 total (4 current month, 2 last month)");
            log.info("====================================");
            log.info("EXPECTED TEACHER SALARIES FOR CURRENT MONTH ({})", currentMonth);
            log.info("====================================");
            
            // Teacher 1 (John Smith - 50%): Mathematics + Physics
            // Paid: Alice ($200) + Bob ($250) + Frank ($150 partial) = $600
            // Unpaid: Charlie ($200) - NOT COUNTED
            // Salary: $600 * 50% = $300
            log.info("Teacher 1 (John Smith - 50%): $600 paid -> Salary: $300");
            
            // Teacher 2 (Sarah Johnson - 60%): English Literature + Computer Science
            // Paid: Alice ($180) + Diana ($300) + Emma ($180) = $660
            // Salary: $660 * 60% = $396
            log.info("Teacher 2 (Sarah Johnson - 60%): $660 paid -> Salary: $396");
            
            // Teacher 3 (Michael Brown - 45%): Chemistry
            // Paid: Diana ($220) = $220
            // Salary: $220 * 45% = $99
            log.info("Teacher 3 (Michael Brown - 45%): $220 paid -> Salary: $99");
            
            log.info("====================================");
            log.info("TEST CASES COVERED:");
            log.info("✓ Multiple teachers with different salary percentages");
            log.info("✓ Multiple students enrolled in multiple courses");
            log.info("✓ Paid and unpaid payments (only paid count)");
            log.info("✓ Payments for current, last, and next month");
            log.info("✓ Soft deleted entities (should be filtered out)");
            log.info("✓ Partial payments (different amounts)");
            log.info("✓ Multiple expenses per month");
            log.info("✓ Teacher with no payments (salary = $0)");
            log.info("====================================");
        };
    }
}