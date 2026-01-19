package phoneStore.app;

import phoneStore.constant.MenuConstant;
import phoneStore.dao.admin.AdminDaoImpl;
import phoneStore.exception.ConsoleErrorHandler;
import phoneStore.services.authent.AuthServiceImpl;
import phoneStore.services.authent.IAuthService;
import phoneStore.utils.InputUtil;



public class Main {

    public static void main(String[] args) {
        System.out.println(MenuConstant.APP_TITLE);

        IAuthService authService = new AuthServiceImpl(new AdminDaoImpl()) ;

        Menu menu = new Menu() ;

        while (true) {
            try {
                System.out.println("\n --- Đăng nhập ---") ;
                String username = InputUtil.readNotNull("tên đăng nhập : ")  ;
                String password  = InputUtil.readNotNull(" mật khẩu") ;

                authService.loginService(username , password) ;
                System.out.println(" dăng nhập thành công ");
                menu.mainMenu(); // vào menu chính
                break;
            } catch (Exception e ) {
                ConsoleErrorHandler.handle(e);
                System.err.println("vui lòng thử lại ");
            }
        }
    }
}
