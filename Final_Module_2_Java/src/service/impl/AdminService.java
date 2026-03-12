package service.impl;

import dao.impl.AdminDAO;
import model.Admin;
import org.mindrot.jbcrypt.BCrypt;

public class AdminService {
    private final AdminDAO adminDAO = new AdminDAO();

    // Tự động tạo Admin mặc định nếu DB trống
    public void initAdmin() {
        if (adminDAO.countAdmin() == 0) {
            String hashPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
            adminDAO.save(new Admin(0, "admin", hashPassword));
            System.out.println(">>> Đã tạo tài khoản admin mặc định: admin / admin123");
        }
    }

    public Admin login(String username, String password) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null && BCrypt.checkpw(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }
}