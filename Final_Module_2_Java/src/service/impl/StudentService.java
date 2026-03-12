package service.impl;

import dao.IStudentDAO;
import dao.impl.StudentDAO;
import model.Student;
import service.IStudentService;

import java.util.List;

public class StudentService implements IStudentService {
    // Khởi tạo đối tượng DAO
    private final IStudentDAO studentDAO = new StudentDAO();

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    @Override
    public Student findById(Integer id) {
        return studentDAO.findById(id);
    }

    @Override
    public void save(Student s) {
        // Business Logic: Kiểm tra logic trước khi lưu
        // Ví dụ: Chuẩn hóa tên (Viết hoa chữ cái đầu), kiểm tra email trùng...
        studentDAO.save(s);
    }

    @Override
    public void update(Student s) {
        studentDAO.update(s);
    }
    @Override
    public void updatePassword(int studentId, String newHashedPassword) {
        studentDAO.updatePassword(studentId, newHashedPassword);
    }
    @Override
    public void delete(Integer id) {
        studentDAO.delete(id);
    }

    @Override
    public List<Student> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentDAO.findAll();
        }
        return studentDAO.search(keyword.trim());
    }

    @Override
    public List<Student> findAllSorted(String sortBy, String order) {
        // Mặc định nếu không truyền order thì là ASC
        if (order == null || (!order.equalsIgnoreCase("ASC") && !order.equalsIgnoreCase("DESC"))) {
            order = "ASC";
        }
        return studentDAO.findAllSorted(sortBy, order);
    }
    @Override
    public Student login(String email, String password) {
        // 1. Tìm student theo email
        Student student = studentDAO.findByEmail(email);

        // 2. Nếu tìm thấy, so sánh mật khẩu bằng BCrypt
        if (student != null) {
            // password: mật khẩu người dùng nhập vào (binh123)
            // student.getPassword(): mật khẩu đã băm trong DB ($2a$10$...)
            if (org.mindrot.jbcrypt.BCrypt.checkpw(password, student.getPassword())) {
                return student; // Đăng nhập thành công
            }
        }

        // 3. Nếu không tìm thấy hoặc sai pass
        return null;
    }
}