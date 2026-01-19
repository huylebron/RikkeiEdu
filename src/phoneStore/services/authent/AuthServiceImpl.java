package phoneStore.services.authent;

import phoneStore.dao.admin.IAdminDao;
import phoneStore.entity.Admin;
import phoneStore.exception.AuthenticationException;
import phoneStore.exception.ValidationException;
import phoneStore.utils.PasswordUtil;

public class AuthServiceImpl implements IAuthService{

    private final IAdminDao adminDao ;

    public AuthServiceImpl(IAdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * @param username
     * @param passwordPlain
     * @return
     */
    @Override
    public Admin loginService(String username, String passwordPlain) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username không được để trống.");
        }
        if (passwordPlain == null || passwordPlain.trim().isEmpty()) {
            throw new ValidationException("Password không được để trống.");
        }

        Admin admin = adminDao.findByUsername(username.trim())
                .orElseThrow(() -> new AuthenticationException(" sai tài khoản hoặc mật khẩu"));
        boolean ok = PasswordUtil.verify(passwordPlain, admin.getPassword());
        if ( !ok) {
            throw new AuthenticationException(" sai tài khoản hoặc mật khẩu");

        }
        return admin ;



    }
}
