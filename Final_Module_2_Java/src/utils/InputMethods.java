package utils;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputMethods {
    private static final Scanner sc = new Scanner(System.in);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^0[0-9]{9}$";
    private static final String NAME_REGEX = "^[\\p{L} ]+$";
    // Nhập chuỗi và không cho phép để trống
    public static String getString() {
        while (true) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("=> Lỗi: Nội dung không được để trống! Vui lòng nhập lại: ");
                continue;
            }
            return input;
        }
    }
    public static String getPhone() {
        while (true) {
            String phone = getString();
            if (Pattern.matches(PHONE_REGEX, phone)) return phone;
            System.out.print("=> Lỗi: SĐT phải bắt đầu bằng số 0 và có 10 chữ số! Nhập lại: ");
        }
    }
    public static boolean getBoolean() {
        while (true) {
            String input = getString();
            if (input.equals("1")) return true;
            if (input.equals("0")) return false;
            System.out.print("=> Lỗi: Chỉ chấp nhận 1 (Nam) hoặc 0 (Nữ)! Nhập lại: ");
        }
    }
    // Nhập số nguyên và xử lý lỗi định dạng + để trống
    public static int getInteger() {
        while (true) {
            try {
                return Integer.parseInt(getString()); // Tận dụng getString() để check trống trước
            } catch (NumberFormatException e) {
                System.out.print("=> Lỗi: Định dạng phải là số nguyên! Vui lòng nhập lại: ");
            }
        }
    }

    public static int getPositiveInteger() {
        while (true) {
            int result = getInteger();
            if (result <= 0) {
                System.out.print("=> Lỗi: Giá trị phải lớn hơn 0! Vui lòng nhập lại: ");
                continue;
            }
            return result;
        }
    }
    public static String getName() {
        while (true) {
            String input = getString(); // Đã trim() 2 đầu

            // 1. Xử lý khoảng trắng thừa ở giữa bằng Regex
            // \\s+ tìm các nhóm khoảng trắng, " " thay thế chúng bằng 1 khoảng trắng duy nhất
            input = input.replaceAll("\\s+", " ");

            // 2. Kiểm tra không chứa số/ký tự đặc biệt
            if (Pattern.matches(NAME_REGEX, input)) {
                return input;
            } else {
                System.out.print("=> Lỗi: Tên chỉ được chứa chữ cái! Nhập lại: ");
            }
        }
    }
    public static String getEmail() {
        while (true) {
            String email = getString(); // Kiểm tra rỗng trước
            if (Pattern.matches(EMAIL_REGEX, email)) {
                return email;
            } else {
                System.out.print("=> Lỗi: Định dạng email không hợp lệ (VD: abc@gmail.com)! Nhập lại: ");
            }
        }
    }
}