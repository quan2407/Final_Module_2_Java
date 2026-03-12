package presentation;

import model.Student;
import service.impl.AdminService;
import service.impl.StudentService;

import java.util.Scanner;

public class MenuManagement {
    private final AdminService adminService = new AdminService();
    private final AdminMenu adminMenu = new AdminMenu();
    private final StudentService studentService = new StudentService();
    private final StudentLoginMenu studentLoginMenu = new StudentLoginMenu();
    private final Scanner sc = new Scanner(System.in);

    public void run() {
        adminService.initAdmin(); // Khởi tạo admin mặc định
        while (true) {
            System.out.println("\n========== HỆ THỐNG QUẢN LÝ ĐÀO TẠO ==========");
            System.out.println("1. Đăng nhập tư cách QUẢN TRỊ VIÊN (Admin)");
            System.out.println("2. Đăng nhập tư cách HỌC VIÊN (Student)");
            System.out.println("0. Thoát");
            System.out.print("Mời chọn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleAdminLogin();
                    break;
                case "2":
                    handleStudentLogin();
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleAdminLogin() {
        System.out.println("\n--- XÁC THỰC QUẢN TRỊ ---");
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (adminService.login(user, pass) != null) {
            System.out.println("=> Đăng nhập Admin thành công!");
            adminMenu.displayMenu(); // Chuyển sang Menu dành riêng cho Admin
        } else {
            System.out.println("=> Sai tài khoản hoặc mật khẩu!");
        }
    }

    private void handleStudentLogin() {
        System.out.println("\n--- ĐĂNG NHẬP HỌC VIÊN ---");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        // Bạn cần gọi hàm login đã viết ở bước 1 (thông qua Service)
        Student s = studentService.login(email, pass);
        if (s != null) {
            System.out.println("=> Đăng nhập thành công!");
            studentLoginMenu.displayMenu(s);
        } else {
            System.out.println("=> Sai Email hoặc Mật khẩu!");
        }
    }
}