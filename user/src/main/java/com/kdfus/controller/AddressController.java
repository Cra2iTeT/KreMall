package com.kdfus.controller;

import com.kdfus.domain.dto.user.AddressDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.domain.vo.user.AddressVO;
import com.kdfus.service.AddressService;
import com.kdfus.util.ResultGenerator;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Cra2iTeT
 * @date 2022/7/18 15:54
 */

@RestController
@Slf4j
@Api(value = "v1", tags = "1.商城用户地址操作相关接口")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 添加地址
     *
     * @param addressDTO
     * @return
     */
    @PostMapping("/address")
    public Result<String> add(@RequestBody @Valid AddressDTO addressDTO) {
        String addResult = addressService.add(addressDTO);
        if (addResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(addResult);
    }

    /**
     * 修改地址
     *
     * @param addressDTO
     * @return
     */
    @PutMapping("/address")
    public Result<String> update(@RequestBody @Valid AddressDTO addressDTO) {
        String updateResult = addressService.update(addressDTO);
        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }


    /**
     * 获取默认地址
     *
     * @param request
     * @return
     */
    @GetMapping("/default")
    public Result<AddressVO> getDefault(HttpServletRequest request) {
        AddressVO addressVO = addressService.getDefault(request);
        return null;
    }
}
