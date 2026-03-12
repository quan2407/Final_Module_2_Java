package presentation;

import model.Student;
import service.IStudentService;
import service.impl.StudentService;
import utils.InputMethods;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final IStudentService studentService = new StudentService();
    private final Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n***************** QUẢN LÝ HỌC VIÊN *****************");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên");
            System.out.println("4. Xóa học viên");
            System.out.println("5. Tìm kiếm học viên (Tên/Email/ID)");
            System.out.println("6. Sắp xếp danh sách");
            System.out.println("0. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": displayAll(); break;
                case "2": handleAdd(); break;
                case "3": handleUpdate(); break;
                case "4": handleDelete(); break;
                case "5": handleSearch(); break;
                case "6": handleSort(); break;
                case "0": return;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void displayAll() {
        printTable(studentService.findAll());
    }

    private void handleAdd() {
        System.out.println("--- THÊM MỚI HỌC VIÊN ---");
        Student s = new Student();

        System.out.print("Nhập tên học viên: ");
        s.setName(InputMethods.getName());

        // Nhập ngày sinh
        while (true) {
            try {
                System.out.print("Nhập ngày sinh (yyyy-mm-dd): ");
                s.setDob(Date.valueOf(InputMethods.getString()));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("=> Lỗi: Định dạng ngày sinh không đúng!");
            }
        }

        System.out.print("Nhập email: ");
        s.setEmail(InputMethods.getEmail());

        System.out.print("Giới tính (1: Nam / 0: Nữ): ");
        s.setSex(InputMethods.getBoolean()); // Chỉ cho phép 1 hoặc 0

        System.out.print("Số điện thoại: ");
        s.setPhone(InputMethods.getPhone()); // Phải đúng 10 số

        System.out.print("Mật khẩu: ");
        String rawPassword = InputMethods.getString();

        String hashedPass = org.mindrot.jbcrypt.BCrypt.hashpw(rawPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        s.setPassword(hashedPass);

        studentService.save(s);
        System.out.println("=> Thêm học viên thành công!");
    }

    private void handleUpdate() {
        System.out.print("Nhập ID học viên cần sửa: ");
        int id = InputMethods.getInteger();
        Student s = studentService.findById(id);
        if (s == null) {
            System.out.println("=> Không tìm thấy học viên có ID: " + id);
            return;
        }

        boolean isExit = false;
        while (!isExit) {
            System.out.println("\n--- CHỈNH SỬA HỌC VIÊN: " + s.getName() + " ---");
            System.out.println("1. Sửa Tên | 2. Sửa Ngày sinh | 3. Sửa Email | 4. Sửa Giới tính | 5. Sửa SĐT | 0. Lưu & Thoát");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Tên mới (Hiện tại: " + s.getName() + " - Enter để bỏ qua): ");
                    String nameInput = sc.nextLine().trim(); // Trim đầu cuối trước

                    if (!nameInput.isEmpty()) {
                        String cleanedName = nameInput.replaceAll("\\s+", " ");

                        // 2. Kiểm tra Regex: Chỉ cho phép chữ cái và khoảng trắng (Unicode)
                        if (java.util.regex.Pattern.matches("^[\\p{L} ]+$", cleanedName)) {
                            s.setName(cleanedName);
                            System.out.println("=> Đã ghi nhận tên mới: " + cleanedName);
                        } else {
                            System.out.println("=> Lỗi: Tên không hợp lệ (không được chứa số hoặc ký tự đặc biệt)!");
                        }
                    }
                    break;

                case "2":
                    System.out.print("Ngày sinh mới (Hiện tại: " + s.getDob() + " - Định dạng yyyy-mm-dd): ");
                    String dobInput = sc.nextLine().trim();
                    if (!dobInput.isEmpty()) {
                        try {
                            s.setDob(Date.valueOf(dobInput));
                        } catch (IllegalArgumentException e) {
                            System.out.println("=> Lỗi: Định dạng ngày sai! Không cập nhật.");
                        }
                    }
                    break;

                case "3":
                    System.out.print("Email mới (Hiện tại: " + s.getEmail() + "): ");
                    String emailInput = sc.nextLine().trim();
                    if (!emailInput.isEmpty()) {
                        // Kiểm tra Regex email trực tiếp tại đây để vẫn cho phép Enter bỏ qua
                        if (java.util.regex.Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", emailInput)) {
                            s.setEmail(emailInput);
                        } else {
                            System.out.println("=> Lỗi: Email sai định dạng! Không cập nhật.");
                        }
                    }
                    break;

                case "4":
                    System.out.print("Giới tính mới (Hiện tại: " + (s.isSex() ? "Nam" : "Nữ") + " - Chọn 1:Nam/0:Nữ): ");
                    String sexInput = sc.nextLine().trim();
                    if (!sexInput.isEmpty()) {
                        if (sexInput.equals("1") || sexInput.equals("0")) {
                            s.setSex(sexInput.equals("1"));
                        } else {
                            System.out.println("=> Lỗi: Chỉ nhập 1 hoặc 0! Không cập nhật.");
                        }
                    }
                    break;

                case "5":
                    System.out.print("SĐT mới (Hiện tại: " + s.getPhone() + "): ");
                    String phoneInput = sc.nextLine().trim();
                    if (!phoneInput.isEmpty()) {
                        // Kiểm tra Regex SĐT 10 số bắt đầu bằng 0
                        if (java.util.regex.Pattern.matches("^0[0-9]{9}$", phoneInput)) {
                            s.setPhone(phoneInput);
                        } else {
                            System.out.println("=> Lỗi: SĐT phải có 10 số và bắt đầu bằng 0! Không cập nhật.");
                        }
                    }
                    break;

                case "0":
                    studentService.update(s);
                    System.out.println("=> Hệ thống: Cập nhật dữ liệu học viên thành công!");
                    isExit = true;
                    break;

                default:
                    System.out.println("=> Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleDelete() {
        System.out.print("Nhập ID học viên cần xóa: ");
        int id = InputMethods.getInteger();
        Student s = studentService.findById(id);
        if (s != null) {
            System.out.printf("Xác nhận xóa học viên [%s]? (Y/N): ", s.getName());
            if (sc.nextLine().equalsIgnoreCase("Y")) {
                studentService.delete(id);
                System.out.println("=> Đã xóa thành công!");
            }
        } else {
            System.out.println("=> Không tìm thấy ID!");
        }
    }

    private void handleSearch() {
        System.out.print("Nhập tên, email hoặc ID để tìm: ");
        printTable(studentService.search(sc.nextLine()));
    }

    // 6. Sắp xếp học viên - Đồng bộ cấu trúc với CourseMenu
    private void handleSort() {
        String sortBy = "";
        String order = "";

        // Bước 1: Chọn tiêu chí sắp xếp
        while (true) {
            System.out.println("\n--- TIÊU CHÍ SẮP XẾP HỌC VIÊN ---");
            System.out.println("1. Sắp xếp theo Tên");
            System.out.println("2. Sắp xếp theo ID");
            System.out.println("3. Quay lại Menu Quản lý học viên");
            System.out.print("Lựa chọn của bạn: ");
            String sortChoice = sc.nextLine();

            if (sortChoice.equals("1")) {
                sortBy = "name";
                break;
            } else if (sortChoice.equals("2")) {
                sortBy = "id";
                break;
            } else if (sortChoice.equals("3")) {
                return; // Thoát hàm sắp xếp
            } else {
                System.out.println("=> Lỗi: Lựa chọn không hợp lệ, vui lòng nhập lại (1, 2 hoặc 3)!");
            }
        }

        // Bước 2: Chọn thứ tự sắp xếp
        while (true) {
            System.out.println("\n--- THỨ TỰ SẮP XẾP ---");
            System.out.println("1. Tăng dần (ASC)");
            System.out.println("2. Giảm dần (DESC)");
            System.out.println("3. Quay lại Menu Quản lý học viên");
            System.out.print("Lựa chọn của bạn: ");
            String orderChoice = sc.nextLine();

            if (orderChoice.equals("1")) {
                order = "ASC";
                break;
            } else if (orderChoice.equals("2")) {
                order = "DESC";
                break;
            } else if (orderChoice.equals("3")) {
                return;
            } else {
                System.out.println("=> Lỗi: Lựa chọn không hợp lệ, vui lòng nhập lại (1, 2 hoặc 3)!");
            }
        }

        // Bước 3: Gọi service và hiển thị kết quả
        List<Student> list = studentService.findAllSorted(sortBy, order);
        System.out.println("\n=> KẾT QUẢ SẮP XẾP HỌC VIÊN THEO " + sortBy.toUpperCase() + " (" + order + "):");
        printTable(list);
    }

    private void printTable(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("Danh sách trống.");
            return;
        }
        System.out.println("=".repeat(110));
        System.out.printf("| %-4s | %-20s | %-12s | %-25s | %-8s | %-12s | %-12s |\n",
                "ID", "Họ Tên", "Ngày Sinh", "Email", "G.Tính", "Số ĐT", "Ngày Tạo");
        System.out.println("-".repeat(110));
        for (Student s : list) {
            System.out.printf("| %-4d | %-20s | %-12s | %-25s | %-8s | %-12s | %-12s |\n",
                    s.getId(), s.getName(), s.getDob(), s.getEmail(),
                    (s.isSex() ? "Nam" : "Nữ"), s.getPhone(), s.getCreatedAt());
        }
        System.out.println("=".repeat(110));
    }
}