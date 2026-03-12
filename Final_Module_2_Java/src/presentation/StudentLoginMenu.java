package presentation;

import model.Course;
import model.Enrollment;
import model.Student;
import service.ICourseService;
import service.IEnrollmentService;
import service.IStudentService;
import service.impl.CourseService;
import service.impl.EnrollmentService;
import service.impl.StudentService;
import utils.InputMethods;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StudentLoginMenu {
    private final ICourseService courseService = new CourseService();
    private final IEnrollmentService enrollmentService = new EnrollmentService();
    private final IStudentService studentService = new StudentService();
    private final Scanner sc = new Scanner(System.in);

    public void displayMenu(Student student) {
        while (true) {
            System.out.println("\n***************** MENU HỌC VIÊN: " + student.getName().toUpperCase() + " *****************");
            System.out.println("1. Xem danh sách khóa học hệ thống (Có tìm kiếm)");
            System.out.println("2. Đăng ký khóa học mới");
            System.out.println("3. Danh sách khóa học đã đăng ký (Có sắp xếp)");
            System.out.println("4. Hủy đăng ký khóa học (Trạng thái WAITING)");
            System.out.println("5. Thay đổi mật khẩu (Xác thực 2 lớp)");
            System.out.println("6. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": displayAllCourses(); break;
                case "2": handleRegisterCourse(student.getId()); break;
                case "3": displayMyCourses(student.getId()); break;
                case "4": handleCancelEnrollment(student.getId()); break;
                case "5": handleChangePassword(student); break;
                case "6": return;
                default: System.out.println("=> Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAllCourses() {
        while (true) {
            List<Course> list = courseService.findAll();
            System.out.println("\n--- TẤT CẢ KHÓA HỌC ---");
            printCourseTable(list);

            System.out.println("Lựa chọn: [1]. Tìm kiếm theo tên | [0]. Quay lại");
            System.out.print("Chọn: ");
            String sub = sc.nextLine();

            if (sub.equals("1")) {
                System.out.print("Nhập tên khóa học: ");
                String kw = sc.nextLine();
                printCourseTable(courseService.searchByName(kw));
                System.out.println("Nhấn Enter để tiếp tục...");
                sc.nextLine();
            } else if (sub.equals("0")) break;
        }
    }

    private void handleRegisterCourse(int studentId) {
        System.out.print("Nhập ID Khóa học muốn đăng ký: ");
        int courseId = InputMethods.getPositiveInteger();

        if (courseService.findById(courseId) == null) {
            System.out.println("=> Lỗi: Khóa học không tồn tại!");
            return;
        }

        List<Enrollment> myCourses = enrollmentService.findByStudentId(studentId);
        for (Enrollment en : myCourses) {
            if (en.getCourseId() == courseId && (en.getStatus().equals("WAITING") || en.getStatus().equals("CONFIRMED"))) {
                System.out.println("=> Lỗi: Bạn đã đăng ký hoặc đang học khóa này rồi!");
                return;
            }
        }

        Enrollment e = new Enrollment();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setStatus("WAITING");

        enrollmentService.save(e);
        System.out.println("=> Thành công: Đã gửi yêu cầu đăng ký (Đang chờ duyệt)!");
    }

    private void displayMyCourses(int studentId) {
        List<Enrollment> list = enrollmentService.findByStudentId(studentId);
        if (list.isEmpty()) {
            System.out.println("=> Bạn chưa đăng ký khóa học nào.");
            return;
        }
        System.out.println("\n--- KHÓA HỌC CỦA BẠN ---");
        printEnrollmentTable(list);

        while (true) {
            System.out.println("\n[SẮP XẾP]: 1. Tên KH | 2. Ngày đăng ký | 0. Thoát");
            String sChoice = sc.nextLine();
            if (sChoice.equals("0")) break;

            String sortBy = sChoice.equals("1") ? "name" : "registered_at";
            System.out.println("[THỨ TỰ]: 1. Tăng dần | 2. Giảm dần");
            String order = sc.nextLine().equals("2") ? "DESC" : "ASC";

            printEnrollmentTable(enrollmentService.findByStudentIdSorted(studentId, sortBy, order));
        }
    }

    private void handleCancelEnrollment(int studentId) {
        List<Enrollment> waitings = enrollmentService.findByStudentId(studentId).stream()
                .filter(e -> e.getStatus().equals("WAITING"))
                .collect(Collectors.toList());

        if (waitings.isEmpty()) {
            System.out.println("=> Không có đơn đăng ký nào ở trạng thái chờ để hủy.");
            return;
        }

        System.out.println("\n--- DANH SÁCH ĐƠN CHỜ DUYỆT ---");
        printEnrollmentTable(waitings);
        System.out.print("Nhập ID bản đăng ký muốn hủy: ");
        int id = InputMethods.getPositiveInteger();

        Enrollment found = waitings.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        if (found == null) {
            System.out.println("=> Lỗi: ID không hợp lệ hoặc không thể hủy đơn này!");
            return;
        }

        System.out.print("Xác nhận hủy đơn này? (Y/N): ");
        if (sc.nextLine().equalsIgnoreCase("Y")) {
            enrollmentService.updateStatus(id, "CANCEL");
            System.out.println("=> Đã hủy yêu cầu đăng ký thành công.");
        }
    }

    private void handleChangePassword(Student s) {
        System.out.println("\n--- XÁC THỰC BẢO MẬT ---");
        System.out.print("Nhập Email hoặc SĐT đã đăng ký: ");
        String identity = sc.nextLine().trim();

        if (!identity.equals(s.getEmail()) && !identity.equals(s.getPhone())) {
            System.out.println("=> Lỗi: Thông tin định danh không khớp!");
            return;
        }

        System.out.print("Nhập mật khẩu cũ: ");
        String oldPass = sc.nextLine();
        if (!org.mindrot.jbcrypt.BCrypt.checkpw(oldPass, s.getPassword())) {
            System.out.println("=> Lỗi: Mật khẩu cũ không đúng!");
            return;
        }

        System.out.print("Nhập mật khẩu mới: ");
        String newPass = InputMethods.getString();
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(newPass, org.mindrot.jbcrypt.BCrypt.gensalt());

        s.setPassword(hashed);
        studentService.updatePassword(s.getId(), hashed);
        System.out.println("=> Thành công: Mật khẩu đã được cập nhật!");
    }

    private void printCourseTable(List<Course> list) {
        System.out.println("=".repeat(50));
        System.out.printf("| %-5s | %-25s | %-10s |\n", "ID", "Tên khóa học", "Thời gian");
        System.out.println("-".repeat(50));
        list.forEach(c -> System.out.printf("| %-5d | %-25s | %-10d |\n", c.getId(), c.getName(), c.getDuration()));
        System.out.println("=".repeat(50));
    }

    private void printEnrollmentTable(List<Enrollment> list) {
        System.out.printf("| %-5s | %-10s | %-15s | %-12s |\n", "ID", "Mã KH", "Ngày ĐK", "Trạng thái");
        System.out.println("-".repeat(50));
        list.forEach(e -> System.out.printf("| %-5d | %-10d | %-15s | %-12s |\n",
                e.getId(), e.getCourseId(), e.getRegisteredAt().toString().substring(0,10), e.getStatus()));
    }
}