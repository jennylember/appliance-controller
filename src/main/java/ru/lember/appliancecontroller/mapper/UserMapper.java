package ru.lember.appliancecontroller.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import ru.lember.appliancecontroller.dao.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    public User getUserById(long id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    public User getUserByUsername(String username);
}
