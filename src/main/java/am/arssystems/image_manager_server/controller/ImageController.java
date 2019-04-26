package am.arssystems.image_manager_server.controller;


import am.arssystems.image_manager_server.dto.responseDto.ListOfPickNames;
import am.arssystems.image_manager_server.model.User;
import am.arssystems.image_manager_server.model.UserImage;
import am.arssystems.image_manager_server.repository.UserImageRepository;
import am.arssystems.image_manager_server.repository.UserRepository;
import am.arssystems.image_manager_server.security.CurrentUser;
import am.arssystems.image_manager_server.security.JwtTokenUtil;
import am.arssystems.image_manager_server.service.ImageService;
import javafx.scene.image.Image;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${image.folder}")
    private String imagesDereqtion;
    @Value("${count.limit}")
    private String limit;

    private UserImageRepository userImageRepository;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private ImageService imageService;

    @Autowired
    public ImageController(UserImageRepository userImageRepository, JwtTokenUtil jwtTokenUtil,
                           UserRepository userRepository, ImageService imageService) {
        this.imageService = imageService;
        this.userImageRepository = userImageRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/listPickNames")
    public ResponseEntity getImagesNames(@AuthenticationPrincipal CurrentUser currentUser,
                                         HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(token);
        User user = userRepository.findAllByPhoneNumber(phoneNumber);
        List<String> pickNames = userImageRepository.findPickNamesByUserId(user.getId());
        return ResponseEntity.ok(ListOfPickNames.builder()
                .pickNames(pickNames)
                .build());
    }

    @RequestMapping(value = "/{pickName}", method = RequestMethod.GET)
    public void getImageAsByteArrayForUserPic(HttpServletResponse response,
                                              @PathVariable(name = "pickName") String pickName) throws IOException {
        double picSizeByPicName = userImageRepository.getPicSizeByPicName(pickName);
        response.setHeader("pictureSize", picSizeByPicName + "");
        InputStream in = null;
        try {

            in = new FileInputStream(imagesDereqtion + pickName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            IOUtils.copy(in, response.getOutputStream());

        } catch (Exception e) {
            System.out.println("File not found");
        } finally {
            if (in != null)
                in.close();
        }
    }

    @PostMapping("/deleteImage")
    public ResponseEntity deleteImage(HttpServletRequest httpServletRequest,
                                      @RequestParam(name = "picName") String pickName) {
        String token = httpServletRequest.getHeader("Authorization");
        String phoneNumber = jwtTokenUtil.getPhoneNumberFromToken(token);
        User currentUser = userRepository.findAllByPhoneNumber(phoneNumber);
        UserImage userImage = userImageRepository.findAllByUserAndAndPicName(currentUser, pickName);
        userImageRepository.delete(userImage);
        ResponseEntity.status(HttpStatus.NO_CONTENT);
        File image = new File(imagesDereqtion + pickName);
        boolean delete = image.delete();
        Map<String, Object> resalt = new HashMap<>();
        resalt.put("success", true);
        resalt.put("message", "DELETED");
        return ResponseEntity.ok(resalt);
    }

    @PostMapping("/getImageSize")
    public ResponseEntity getImageSize(@RequestParam(name = "picName") String picName) {
        double picSizeByPicName = userImageRepository.getPicSizeByPicName(picName);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("size", picSizeByPicName);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addImage")
    public ResponseEntity addImage(HttpServletRequest httpServletRequest,
                                   @RequestParam(name = "picture") MultipartFile multipartFile) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization");
        User user = userRepository.findAllByPhoneNumber(jwtTokenUtil.getPhoneNumberFromToken(token));
        int count = userImageRepository.countAllByUser(user);
        if (count < Integer.parseInt(limit)) {
            String filename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File image = new File(imagesDereqtion + filename);
            multipartFile.transferTo(image);
            result.put("success", true);
            double imageFileSize = imageService.getImageFileSize(image);
            userImageRepository.save(UserImage.builder()
                    .id(System.currentTimeMillis() + "_" + UUID.randomUUID().toString())
                    .picName(filename)
                    .user(user)
                    .picSize(imageFileSize)
                    .build());
        } else {
            result.put("success", false);
            result.put("message", "The maximum amount of storage for your pictures has expired");

        }
        return ResponseEntity.ok(result);
    }
}
