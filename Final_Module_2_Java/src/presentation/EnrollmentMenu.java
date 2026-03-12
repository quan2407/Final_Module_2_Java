package presentation;

import model.Enrollment;
import service.IEnrollmentService;
import service.impl.EnrollmentService;
import utils.InputMethods;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EnrollmentMenu {
    private final IEnrollmentService enrollmentService = new EnrollmentService();
    private final Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n***************** QUẢN LÝ ĐĂNG KÝ KHÓA HỌC (ADMIN) *****************");
            System.out.println("1. Hiển thị danh sách theo từng khóa học");
            System.out.println("2. Thêm học viên trực tiếp (Auto CONFIRMED)");
            System.out.println("3. Phê duyệt yêu cầu đăng ký (WAITING)");
            System.out.println("4. Xóa/Hủy bản đăng ký");
            System.out.println("0. Quay về menu chính");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": handleDisplayByCourse(); break;
                case "2": handleAddEnrollment(); break;
                case "3": handleApproveEnrollment(); break;
                case "4": handleDeleteEnrollment(); break;
                case "0": return;
                default: System.out.println("=> Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    // 1. Hiển thị học viên theo từng khóa học
    private void handleDisplayByCourse() {
        System.out.print("Nhập ID Khóa học cần xem: ");
        int courseId = InputMethods.getPositiveInteger();

        List<Enrollment> list = enrollmentService.findByCourseId(courseId);
        if (list.isEmpty()) {
            System.out.println("=> Thông báo: Khóa học này hiện chưa có dữ liệu đăng ký.");
        } else {
            System.out.println("\n--- DANH SÁCH ĐĂNG KÝ KHÓA HỌC ID: " + courseId + " ---");
            printTable(list);
        }
    }

    // 2. Thêm học viên trực tiếp (Admin thêm thì mặc định là CONFIRMED)
    private void handleAddEnrollment() {
        System.out.println("--- THÊM ĐĂNG KÝ TRỰC TIẾP ---");
        System.out.print("Nhập ID Học viên: ");
        int studentId = InputMethods.getPositiveInteger();
        System.out.print("Nhập ID Khóa học: ");
        int courseId = InputMethods.getPositiveInteger();

        Enrollment e = new Enrollment();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setStatus("CONFIRMED");

        try {
            enrollmentService.save(e);
            System.out.println("=> Thành công: Đã thêm học viên vào khóa học!");
        } catch (Exception ex) {
            System.out.println("=> Lỗi: Không thể thực hiện (Kiểm tra trùng lặp hoặc ID không tồn tại)");
        }
    }

    // 3. Phê duyệt yêu cầu đăng ký (Xử lý các đơn WAITING)
    private void handleApproveEnrollment() {
        List<Enrollment> all = enrollmentService.findAll();
        List<Enrollment> waitingList = all.stream()
                .filter(e -> "WAITING".equals(e.getStatus()))
                .collect(Collectors.toList());

        if (waitingList.isEmpty()) {
            System.out.println("=> Hiện tại không có yêu cầu nào đang chờ (WAITING).");
            return;
        }

        System.out.println("\n--- DANH SÁCH YÊU CẦU ĐANG CHỜ ---");
        printTable(waitingList);

        System.out.print("Nhập ID bản đăng ký muốn xử lý: ");
        int id = InputMethods.getPositiveInteger();

        Enrollment found = enrollmentService.findById(id);
        if (found == null || !"WAITING".equals(found.getStatus())) {
            System.out.println("=> Lỗi: ID không tồn tại hoặc đơn này đã được xử lý rồi!");
            return;
        }

        System.out.println("Chọn hành động: [1]. Chấp nhận (CONFIRMED) | [2]. Từ chối (DENIED) | [0]. Quay lại");
        System.out.print("Lựa chọn: ");
        String action = sc.nextLine();

        if (action.equals("1")) {
            enrollmentService.updateStatus(id, "CONFIRMED");
            System.out.println("=> Thành công: Đã xác nhận đăng ký.");
        } else if (action.equals("2")) {
            enrollmentService.updateStatus(id, "DENIED");
            System.out.println("=> Thành công: Đã từ chối yêu cầu.");
        }
    }

    // 4. Xóa bản đăng ký
    private void handleDeleteEnrollment() {
        System.out.print("Nhập ID bản đăng ký cần xóa: ");
        int id = InputMethods.getPositiveInteger();

        Enrollment e = enrollmentService.findById(id);
        if (e == null) {
            System.out.println("=> Lỗi: Không tìm thấy ID: " + id);
            return;
        }

        System.out.print("Xác nhận xóa bản đăng ký này? (Y/N): ");
        if (sc.nextLine().equalsIgnoreCase("Y")) {
            enrollmentService.delete(id);
            System.out.println("=> Thành công: Đã xóa dữ liệu.");
        }
    }

    private void printTable(List<Enrollment> list) {
        System.out.println("=".repeat(85));
        System.out.printf("| %-5s | %-10s | %-10s | %-12s | %-25s |\n",
                "ID", "St.ID", "Co.ID", "Trạng thái", "Ngày đăng ký");
        System.out.println("-".repeat(85));
        for (Enrollment en : list) {
            System.out.printf("| %-5d | %-10d | %-10d | %-12s | %-25s |\n",
                    en.getId(), en.getStudentId(), en.getCourseId(), en.getStatus(), en.getRegisteredAt());
        }
        System.out.println("=".repeat(85));
    }
}