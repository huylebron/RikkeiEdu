package phoneStore.services.authent;

import phoneStore.entity.Admin;

public interface IAuthService {

    Admin loginService(String username, String passwordPlain);

    void changePassword(Long adminId, String oldPassword, String newPassword);
}
