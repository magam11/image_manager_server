package am.arssystems.image_manager_server.controller;


import am.arssystems.image_manager_server.dto.request.VerifyCode;
import am.arssystems.image_manager_server.dto.responseDto.LoginResponse;
import am.arssystems.image_manager_server.dto.request.LoginDto;
import am.arssystems.image_manager_server.dto.responseDto.VerifyResponse;
import am.arssystems.image_manager_server.model.User;

import am.arssystems.image_manager_server.repository.UserImageRepository;
import am.arssystems.image_manager_server.repository.UserRepository;
import am.arssystems.image_manager_server.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;
    private UserImageRepository userImageRepository;

    @Autowired
    public UserController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil,
                          UserImageRepository userImageRepository) {
        this.userImageRepository = userImageRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        LoginResponse response = new LoginResponse();
        String phoneNumber = loginDto.getPhoneNumber();
        User currentUser = userRepository.findAllByPhoneNumber(phoneNumber);
        response.setSuccess(false);
        if (currentUser != null) {
            response.setSuccess(true);
            response.setToken(jwtTokenUtil.generateToken(phoneNumber));
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity verifyCode(@RequestBody VerifyCode verifyCode){
        VerifyResponse result = new VerifyResponse();
        User currentUser = userRepository.findAllByPhoneNumber(verifyCode.getPhoneNumber());
        result.setSuccess(false);
        if(currentUser!=null && currentUser.getActivationCode().equals(verifyCode.getVerifyCode())){
            String token = jwtTokenUtil.generateToken(verifyCode.getPhoneNumber());
            List<String> pickNamesByUserId = userImageRepository.findPickNamesByUserId(currentUser.getId());
            result.setSuccess(true);
            result.setName(currentUser.getName());
            result.setPickNames(pickNamesByUserId);
            result.setToken(token);
        }
        System.out.println("------------------list------------");
        System.out.println(result.getPickNames());
        return ResponseEntity.ok(result);
    }
}
