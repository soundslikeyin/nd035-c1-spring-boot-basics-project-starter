package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT userid FROM USERS WHERE username = #{username}")
    Integer getUserId(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE USERS SET salt = #{salt}, password = #{password} WHERE username = #{username}")
    int updatePassword(User user);

    @Delete("DELETE FROM USERS WHERE username = #{username}")
    int deleteUser(String username);
}
