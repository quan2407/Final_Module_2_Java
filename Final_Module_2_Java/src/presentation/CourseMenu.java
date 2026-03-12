package presentation;

import model.Course;
import service.ICourseService;
import service.impl.CourseService;
import utils.InputMethods;

import java.util.List;
import java.util.Scanner;

public class CourseMenu {
    private final ICourseService courseService = new CourseService();
    private final Scanner sc = new Scanner(System.in);

    public void displayMenu() {
        while (true) {
            System.out.println("\n***************** QUẢN LÝ KHÓA HỌC *****************");
            System.out.println("1. Hiển thị danh sách khóa học (Mặc định)");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Cập nhật thông tin khóa học");
            System.out.println("4. Xóa khóa học");
            System.out.println("5. Tìm kiếm khóa học (Tên/Giảng viên)");
            System.out.println("6. Sắp xếp khóa học");
            System.out.println("0. Quay lại");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    displayAllCourses();
                    break;
                case "2":
                    handleAddCourse();
                    break;
                case "3":
                    handleUpdateCourse();
                    break;
                case "4":
                    handleDeleteCourse();
                    break;
                case "5":
                    handleSearchCourse();
                    break;
                case "6":
                    handleSortCourse();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
            }
        }
    }

    // 1. Hiển thị danh sách
    private void displayAllCourses() {
        List<Course> list = courseService.findAll();
        printTable(list);
    }

    private void handleAddCourse() {
        System.out.println("--- Thêm khóa học mới ---");
        Course c = new Course();

        System.out.print("Nhập tên khóa học: ");
        c.setName(InputMethods.getString());

        System.out.print("Nhập thời lượng (giờ): ");
        c.setDuration(InputMethods.getPositiveInteger());

        System.out.print("Nhập giảng viên: ");
        c.setInstructor(InputMethods.getString());

        try {
            courseService.save(c);
            System.out.println("=> Thêm mới thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi nghiệp vụ: " + e.getMessage());
        }
    }

    // 3. Cập nhật sử dụng service.update()
    private void handleUpdateCourse() {
        System.out.print("Nhập ID khóa học cần sửa: ");
        int id = InputMethods.getInteger();

        Course c = courseService.findById(id);
        if (c == null) {
            System.out.println("Không tìm thấy khóa học có ID: " + id);
            return;
        }

        boolean isExit = false;
        while (!isExit) {
            System.out.println("\n---- CHỈNH SỬA KHÓA HỌC: " + c.getName() + " ----");
            System.out.println("1. Sửa Tên khóa học");
            System.out.println("2. Sửa Thời lượng");
            System.out.println("3. Sửa Giảng viên");
            System.out.println("0. Lưu và Thoát");
            System.out.print("Chọn thuộc tính cần sửa: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Nhập tên mới (Hiện tại: " + c.getName() + " - Nhấn Enter để giữ nguyên): ");
                    String name = sc.nextLine().trim();
                    if (!name.isEmpty()) {
                        c.setName(name);
                    }
                    break;

                case "2":
                    System.out.println("Thời lượng hiện tại: " + c.getDuration());
                    System.out.print("Nhập thời lượng mới (Phải > 0 - Nhấn Enter để giữ nguyên): ");
                    String durationInput = sc.nextLine().trim();
                    if (!durationInput.isEmpty()) {
                        try {
                            int newDuration = Integer.parseInt(durationInput);
                            if (newDuration > 0) {
                                c.setDuration(newDuration);
                            } else {
                                System.out.println("=> Lỗi: Thời lượng phải lớn hơn 0! Không cập nhật.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("=> Lỗi: Định dạng không hợp lệ! Không cập nhật.");
                        }
                    }
                    break;

                case "3":
                    System.out.print("Nhập giảng viên mới (Hiện tại: " + c.getInstructor() + " - Nhấn Enter để giữ nguyên): ");
                    String gv = sc.nextLine().trim();
                    if (!gv.isEmpty()) {
                        c.setInstructor(gv);
                    }
                    break;

                case "0":
                    courseService.update(c);
                    System.out.println("=> Đã lưu các thay đổi và cập nhật thành công!");
                    isExit = true;
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleDeleteCourse() {
        System.out.print("Nhập ID khóa học cần xóa: ");
        int id = InputMethods.getInteger();

        Course course = courseService.findById(id);

        if (course != null) {
            System.out.println("\n--- THÔNG TIN KHÓA HỌC SẮP XÓA ---");
            System.out.printf("ID: %d | Tên: %s | Giảng viên: %s\n",
                    course.getId(), course.getName(), course.getInstructor());

            System.out.print("\nBạn có chắc chắn muốn xóa khóa học này không? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                courseService.delete(id);
                System.out.println("=> Kết quả: Đã xóa khóa học thành công!");
            } else {
                System.out.println("=> Kết quả: Đã hủy thao tác xóa.");
            }
        } else {
            System.out.println("=> Lỗi: Không tìm thấy khóa học nào có ID: " + id);
        }
    }
    private void handleSearchCourse() {
        System.out.print("Nhập từ khóa tìm kiếm: ");
        String keyword = sc.nextLine();
        List<Course> result = courseService.search(keyword);
        printTable(result);
    }

    private void handleSortCourse() {
        String sortBy = "";
        String order = "";

        while (true) {
            System.out.println("\n--- TIÊU CHÍ SẮP XẾP ---");
            System.out.println("1. Sắp xếp theo Tên");
            System.out.println("2. Sắp xếp theo ID");
            System.out.println("3. Thoát về Menu chính");
            System.out.print("Lựa chọn của bạn: ");
            String sortChoice = sc.nextLine();

            if (sortChoice.equals("1")) {
                sortBy = "name";
                break;
            } else if (sortChoice.equals("2")) {
                sortBy = "id";
                break;
            } else if (sortChoice.equals("3")) {
                return; // Thoát hẳn hàm sắp xếp, quay về menu admin
            } else {
                System.out.println("=> Lỗi: Lựa chọn không hợp lệ, vui lòng nhập lại (1, 2 hoặc 3)!");
            }
        }

        while (true) {
            System.out.println("\n--- THỨ TỰ SẮP XẾP ---");
            System.out.println("1. Tăng dần (ASC)");
            System.out.println("2. Giảm dần (DESC)");
            System.out.println("3. Thoát về Menu chính");
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

        List<Course> list = courseService.findAllSorted(sortBy, order);
        System.out.println("\n=> KẾT QUẢ SẮP XẾP THEO " + sortBy.toUpperCase() + " (" + order + "):");
        printTable(list);
    }

    private void printTable(List<Course> list) {
        if (list.isEmpty()) {
            System.out.println("=> Thông báo: Danh sách khóa học đang trống.");
            return;
        }

        String headerFormat = "| %-5s | %-25s | %-12s | %-20s | %-15s |\n";
        String rowFormat    = "| %-5d | %-25s | %-12d | %-20s | %-15s |\n";

        System.out.println("\n" + "=".repeat(87));
        System.out.printf(headerFormat, "ID", "Tên Khóa Học", "Thời Lượng", "Giảng Viên", "Ngày Tạo");
        System.out.println("-".repeat(87));

        for (Course c : list) {
            String dateStr = (c.getCreatedAt() != null) ? c.getCreatedAt().toString() : "N/A";

            System.out.printf(rowFormat,
                    c.getId(),
                    c.getName(),
                    c.getDuration(),
                    c.getInstructor(),
                    dateStr);
        }
        System.out.println("=".repeat(87));
        System.out.println("Tổng số: " + list.size() + " khóa học.");
    }
}