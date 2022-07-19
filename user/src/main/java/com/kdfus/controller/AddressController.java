package com.kdfus.controller;

import com.kdfus.domain.dto.user.AddressDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.domain.vo.user.AddressVO;
import com.kdfus.service.AddressService;
import com.kdfus.system.ServiceResultEnum;
import com.kdfus.util.ResultGenerator;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    public Result<String> save(HttpServletRequest request, @RequestBody @Valid AddressDTO addressDTO) {
        String addResult = addressService.save(request.getHeader("authorization"), addressDTO);
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
    public Result<String> update(HttpServletRequest request, @RequestBody @Valid AddressDTO addressDTO) {
        String token = request.getHeader("authorization");
        String updateResult = addressService.update(token, addressDTO);
        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }

    /**
     * 更改默认地址
     *
     * @param request
     * @param id
     * @return
     */
    @PutMapping("/default")
    public Result<String> update(HttpServletRequest request, @Valid Long id) {
        String token = request.getHeader("authorization");
        String updateResult = addressService.update(token, id);
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
        AddressVO addressVO = addressService.getDefault(request.getHeader("authorization"));
        if (addressVO != null) {
            return ResultGenerator.genSuccessResult(addressVO);
        }
        // 说明没有默认地址，前端这个情形应该是在下单的时候，显示为空就行，提醒用户填收货地址，不需要弹出错误信息
        return ResultGenerator.genFailResult(ServiceResultEnum.DATE_NULL.getResult());
    }

    /**
     * 获取收货地址列表
     *
     * @param request
     * @return
     */
    @GetMapping("/address")
    public Result<List<AddressVO>> getList(HttpServletRequest request) {
        List<AddressVO> addressVOList = addressService.getList(request.getHeader("authorization"));
        if (addressVOList != null) {
            return ResultGenerator.genSuccessResult(addressVOList);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATE_NULL.getResult());
    }

    /**
     * 逻辑删除收获地址
     *
     * @param id
     * @return
     */
    @PutMapping("/delete")
    public Result<String> delete(Long id) {
        String delResult = addressService.delete(id);
        if (delResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(delResult);
    }
}
