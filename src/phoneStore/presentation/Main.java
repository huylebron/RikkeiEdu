package phoneStore.presentation;

import phoneStore.constant.MenuConstant;
import phoneStore.dao.admin.AdminDaoImpl;
import phoneStore.entity.Admin;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.services.authent.AuthServiceImpl;
import phoneStore.services.authent.IAuthService;
import phoneStore.utils.InputUtil;

public class Main {

    public static void main(String[] args) {
        System.out.println(MenuConstant.APP_TITLE);

        IAuthService authService = new AuthServiceImpl(new AdminDaoImpl());
        Menu menu = new Menu(authService);

        while (true) {
            try {
                System.out.println("\n --- ĐĂNG NHẬP ---");
                String username = InputUtil.readNotNull("Tên đăng nhập: ");
                String password = InputUtil.readNotNull("Mật khẩu: ");

                Admin admin = authService.loginService(username, password);
                System.out.println("Đăng nhập thành công! Chào " + admin.getUsername());


                menu.mainMenu(admin);
                break;
            } catch (Exception e) {
                ConsoleErrorHandler.handle(e);
            }
        }
    }
}
