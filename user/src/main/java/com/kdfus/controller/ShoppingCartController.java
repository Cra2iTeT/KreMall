package com.kdfus.controller;

import com.kdfus.domain.dto.user.ShoppingCartDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.domain.vo.user.ShoppingCartVO;
import com.kdfus.service.ShoppingCartService;
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
 * @date 2022/7/20 10:11
 */
@RestController
@Slf4j
@Api(value = "v1", tags = "1.商城用户购物车操作相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加单种商品到购物车
     *
     * @return
     */
    @PostMapping("/shoppingCart")
    public Result<String> save(HttpServletRequest request, @RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        String saveResult = shoppingCartService.save(request.getHeader("authorization"), shoppingCartDTO);
        if (saveResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(saveResult);
    }

    /**
     * 给单种商品修改属性
     *
     * @param request
     * @param shoppingCartDTO
     * @return
     */
    @PutMapping("/shoppingCart")
    public Result<String> update(HttpServletRequest request, @RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        String updateResult = shoppingCartService.update(request.getHeader("authorization"), shoppingCartDTO);
        if (updateResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(updateResult);
    }

    /**
     * 删除单个或多个商品
     *
     * @param request
     * @param ids
     * @return
     */
    @PutMapping("/shoppingCart")
    public Result<String> delete(HttpServletRequest request, List<Long> ids) {
        String deleteResult = shoppingCartService.delete(request.getHeader("authorization"), ids);
        if (deleteResult == null) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(deleteResult);
    }

    /**
     * 返回购物车商品列表
     *
     * @param request
     * @return
     */
    @GetMapping("/shoppingCart")
    public Result<List<ShoppingCartVO>> getList(HttpServletRequest request) {
        List<ShoppingCartVO> list = shoppingCartService.getList(request.getHeader("authorization"));
        if (list != null) {
            return ResultGenerator.genSuccessResult(list);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NULL.getResult());
    }

}
