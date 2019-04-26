package am.arssystems.image_manager_server.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_image")
@Entity
@Builder
public class UserImage {

    @Id
    @Column
    private String id;
    @Column(name = "pic_name")
    private String picName;
    @ManyToOne
    private User user;
    @Column(name = "pic_size")
    private double picSize;

}
