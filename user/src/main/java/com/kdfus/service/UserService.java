package com.kdfus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.dto.RegistryDTO;
import com.kdfus.domain.entity.user.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Cra2iTeT
 * @date 2022/6/25 19:12
 */
public interface UserService extends IService<User> {
    String login(LoginDTO loginDTO);

    Boolean logout(String token);

    String registry(RegistryDTO registryDTO);

    String update(String token, String nickName);

    String update(String token, String oldPasswordMd5, String newPasswordMd5, String confirmPasswordMd5);

    String update(String token, MultipartFile file);

    String getInfo(String token);
}