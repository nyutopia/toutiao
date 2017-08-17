package com.ny.toutiao.dao;

import com.ny.toutiao.model.News;
import com.ny.toutiao.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by ny on 2017/8/1.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title,link,image,like_count,comment_count,created_date,user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    //    @Insert({"insert into user(name,password,salt,head_url) values()"})
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    //通过 xml配置
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,
                                       @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

}
