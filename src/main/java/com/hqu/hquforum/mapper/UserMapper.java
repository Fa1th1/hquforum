package com.hqu.hquforum.mapper;

import com.hqu.hquforum.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Drf
 * @create 2021-03-27-0:29
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);
}
