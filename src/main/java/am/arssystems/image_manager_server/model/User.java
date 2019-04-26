package am.arssystems.image_manager_server.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user")
@Entity
public class User {

    @Id
    private String id;
    @Column
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "activation_code")
    private String activationCode;
}
