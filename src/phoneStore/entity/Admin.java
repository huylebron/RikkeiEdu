package phoneStore.entity;

import java.util.Objects;

public class Admin {

    private Long adminId;
    private String username;
    private String password;

    public Admin() {}

    public Admin( Long adminId , String username, String password) {
        this.adminId = adminId ;
        this.username = username;
        this.password = password;


    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Admin admin)) return false;
        return adminId == admin.adminId && Objects.equals(username, admin.username) && Objects.equals(password, admin.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId, username, password);
    }
}
