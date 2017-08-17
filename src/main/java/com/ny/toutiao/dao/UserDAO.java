package com.ny.toutiao.dao;

import com.ny.toutiao.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by ny on 2017/8/1.
 */
@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name,password,salt,head_url ";
    String SELECT_FIELDS = " id,name,password,salt,head_url ";
//    @Insert({"insert into user(name,password,salt,head_url) values()"})
//    通过注解的方式操作数据库
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME," where id=#{id}"})
    User selectaById(int id);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME," where name=#{name}"})
    User selectaByName(String name);

    @Update({"update",TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}

