package am.arssystems.image_manager_server.security;


import am.arssystems.image_manager_server.model.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getPhoneNumber(), user.getActivationCode(),true,true,true,true, AuthorityUtils.createAuthorityList("user"));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
