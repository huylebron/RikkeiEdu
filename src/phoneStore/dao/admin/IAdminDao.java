package phoneStore.dao.admin;

import phoneStore.entity.Admin;

public interface IAdminDao {

    Admin insertAdminDao( Admin admin);
    void updatePassword(Long id  ,  String newPasswordHash) ;



}
