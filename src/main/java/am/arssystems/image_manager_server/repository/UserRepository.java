package am.arssystems.image_manager_server.repository;

import am.arssystems.image_manager_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findAllByPhoneNumber(String phoneNumber);

}
