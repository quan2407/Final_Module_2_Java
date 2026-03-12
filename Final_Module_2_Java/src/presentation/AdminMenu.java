package presentation;

import java.util.Scanner;

public class AdminMenu {
    private final Scanner sc = new Scanner(System.in);
    private final CourseMenu courseMenu = new CourseMenu();
    private final StudentMenu studentMenu = new StudentMenu();
     private final EnrollmentMenu enrollmentMenu = new EnrollmentMenu();
    // private final StatMenu statMenu = new StatMenu();

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- MENU QUẢN TRỊ (ADMIN) ---");
            System.out.println("1. Quản lý Khóa học (Course)");
            System.out.println("2. Quản lý Học viên (Student)");
            System.out.println("3. Quản lý Đăng ký (Enrollment)");
            System.out.println("4. Thống kê & Báo cáo");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    courseMenu.displayMenu();
                    break;
                case "2":
                    studentMenu.displayMenu();
                    break;
                case "3":
                    enrollmentMenu.displayMenu();
                    break;
                case "4":
                    System.out.println("Đang phát triển Statistics...");
                    break;
                case "0":
                    return; // Quay lại MenuManagement
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}