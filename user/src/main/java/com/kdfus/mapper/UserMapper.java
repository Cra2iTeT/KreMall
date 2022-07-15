package com.kdfus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kdfus.domain.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/6/25 19:08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
