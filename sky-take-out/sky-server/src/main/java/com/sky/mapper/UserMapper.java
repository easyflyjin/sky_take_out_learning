package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getUserById(String openid);

    @Insert("insert into user (openid, create_time) values (#{openid},#{createTime})")
    void setNonExistUser(User user);
}
