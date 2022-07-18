package com.kdfus.controller;

import com.kdfus.domain.dto.UpdatePwdDTO;
import com.kdfus.system.Constants;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.dto.RegistryDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.service.UserService;
import com.kdfus.util.NumberUtils;
import com.kdfus.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Cra2iTeT
 * @date 2022/6/25 19:16
 */
@RestController
@Slf4j
@Api(value = "v1", tags = "1.商城用户信息操作相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        if (!NumberUtils.isPhoneInvalid(loginDTO.getAccountId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_ACCOUNT_ID_VALID.getResult());
        }
        log.info("登录接口收到的信息 => " + loginDTO);
        String loginResult = userService.login(loginDTO);

        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        System.out.println(loginDTO);
        //登录失败
        return ResultGenerator.genFailResult("loginResult");
    }

    @PostMapping("/logout")
    @ApiOperation(value = "登出接口", notes = "删除token")
    public Result<String> logout(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        Boolean logoutResult = userService.logout(token);

        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.LOGOUT_ERROR.getResult());
    }

    @PostMapping("/registry")
    @ApiOperation(value = "注册接口", notes = "")
    public Result<String> registry(@RequestBody @Valid RegistryDTO registryDTO) {
        log.info("注册接口收到的手机号 => " + registryDTO.toString());
        if (!NumberUtils.isPhoneInvalid(registryDTO.getAccountId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_ACCOUNT_ID_VALID.getResult());
        }
        String registryResult = userService.registry(registryDTO);

        if (registryResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        // 注册失败
        return ResultGenerator.genFailResult(registryResult);
    }

    @PutMapping("/update/info")
    @ApiOperation(value = "修改个人昵称接口", notes = "")
    public Result<String> update(HttpServletRequest request, String nickName) {
        if (nickName == null) {
            return ResultGenerator.genFailResult("新昵称不允许为空");
        }

        String token = request.getHeader("authorization");
        log.info("修改昵称接口接受信息 => " + nickName);

        String updateResult = userService.update(token, nickName);
        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改个人密码接口", notes = "")
    public Result<String> update(HttpServletRequest request, @RequestBody UpdatePwdDTO updatePwdDTO) {
        String token = request.getHeader("authorization");

        log.info("------");
        log.info("修改密码接口：");
        log.info("修改密码接口\t旧密码：{},新密码：{},确认密码：{}", updatePwdDTO.getOldPasswordMd5(),
                updatePwdDTO.getNewPasswordMd5(), updatePwdDTO.getConfirmPasswordMd5());
        log.info("------");

        String updateResult = userService.update(token, updatePwdDTO.getOldPasswordMd5(),
                updatePwdDTO.getNewPasswordMd5(), updatePwdDTO.getConfirmPasswordMd5());

        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }

    @PutMapping("/update/img")
    @ApiOperation(value = "修改个人头像", notes = "")
    public Result<String> update(HttpServletRequest request, MultipartFile file) {
        String token = request.getHeader("authorization");

        String updateResult = userService.update(token, file);
        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }

    @PostMapping("/info")
    @ApiOperation(value = "获取个人信息", notes = "")
    public Result<String> getInfo(HttpServletRequest request) {
        String token = request.getHeader("authorization");

        String infoResult = userService.getInfo(token);
        if (infoResult != null) {
            return ResultGenerator.genSuccessResult(infoResult);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.INFO_GET_ERROR.getResult());
    }

}
