package phoneStore.dao.admin;

import phoneStore.entity.Admin;

import java.util.Optional;

public interface IAdminDao {

    Admin insertAdminDao( Admin admin);
    void updatePassword(Long id  ,  String newPasswordHash) ;

    Optional<Admin> findByUsername(String username);



}
