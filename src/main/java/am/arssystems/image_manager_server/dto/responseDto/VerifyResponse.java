package am.arssystems.image_manager_server.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerifyResponse {
    private boolean success;
    private List<String> pickNames;
    private String name;
    private String token;
}
