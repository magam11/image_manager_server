package am.arssystems.image_manager_server.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerifyCode {
    private String phoneNumber;
    private String verifyCode;
}
