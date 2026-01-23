package phoneStore.services.authent;

import phoneStore.dao.admin.IAdminDao;
import phoneStore.entity.Admin;
import phoneStore.exception.AuthenticationException;
import phoneStore.exception.ValidationException;
import phoneStore.utils.PasswordUtil;

public class AuthServiceImpl implements IAuthService {

    private final IAdminDao adminDao;
// inject constructor
    public AuthServiceImpl(IAdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Admin loginService(String username, String passwordPlain) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username không được để trống.");
        }
        if (passwordPlain == null || passwordPlain.trim().isEmpty()) {
            throw new ValidationException("Password không được để trống.");
        }

        Admin admin = adminDao.findByUsername(username.trim())
                .orElseThrow(() -> new AuthenticationException("Sai tài khoản hoặc mật khẩu"));

        if (!PasswordUtil.verify(passwordPlain, admin.getPassword())) {
            throw new AuthenticationException("Sai tài khoản hoặc mật khẩu");
        }
        return admin;
    }

    @Override
    public void changePassword(Long adminId, String oldPassword, String newPassword) {
        if (adminId == null) {
            throw new ValidationException("ID Admin không hợp lệ.");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new ValidationException("Mật khẩu cũ không được để trống.");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ValidationException("Mật khẩu mới không được để trống.");
        }

        // Lấy admin từ DB
        Admin admin = adminDao.findById(adminId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Admin để đổi mật khẩu."));

        // Kiểm tra mật khẩu cũ
        if (!PasswordUtil.verify(oldPassword, admin.getPassword())) {
            throw new AuthenticationException("Mật khẩu cũ không đúng.");
        }

        // Băm mật khẩu mới
        String hashedNewPassword = PasswordUtil.hash(newPassword);

        // Lưu vào DB
        adminDao.updatePassword(adminId, hashedNewPassword);
    }
}
