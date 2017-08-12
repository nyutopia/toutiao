package com.ny.toutiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import static redis.clients.jedis.BinaryClient.*;
import static redis.clients.jedis.BinaryClient.LIST_POSITION.*;


/**
 * Created by ny on 2017/8/10.
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool pool = null;
    public static void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis();
        jedis.flushAll();

        jedis.set("hello","world");
        print(1,jedis.get("hello"));

        //设置过期时间
        jedis.setex("hello2",15,"world");

        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(2,jedis.get("pv"));

        //列表操作
        String listName = "list";
        for(int i = 0;i < 10; i++){
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(3,jedis.lrange(listName,0,12));
        print(5,jedis.lpop(listName));
        print(4,jedis.llen(listName));
        print(6,jedis.lindex(listName,3));
        print(7,jedis.lindex(listName,0));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4","xx"));
        print(9,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4","xy"));
        print(10,jedis.lrange(listName,0,12));


        String userKey = "user12";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","15151859382");

        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hkeys(userKey));
        print(16,jedis.hvals(userKey));
        print(17,jedis.hexists(userKey,"email"));
        print(18,jedis.hexists(userKey,"age"));

        jedis.hsetnx(userKey,"school","zju");
        jedis.hsetnx(userKey,"name","ysx");
        //不存在的才设置 not exist
        print(19,jedis.hgetAll(userKey));

        //set 集合的概念
        String likeKesy1="newsLike1";
        String likeKesy2="newsLike2";

        for(int i = 0;i<10;i++){
            jedis.sadd(likeKesy1,String.valueOf(i));
            jedis.sadd(likeKesy2,String.valueOf(i*2));
        }

        print(20,jedis.smembers(likeKesy1));
        print(21,jedis.smembers(likeKesy2));
        print(22,jedis.sinter(likeKesy1,likeKesy2));
        print(23,jedis.sunion(likeKesy1,likeKesy2));
        print(24,jedis.sdiff(likeKesy1,likeKesy2));
        print(25,jedis.sismember(likeKesy1,"5"));

        jedis.srem(likeKesy1,"5");
        print(26,jedis.smembers(likeKesy1));
        print(27,jedis.scard(likeKesy1));
        jedis.smove(likeKesy2,likeKesy1,"14");
        print(28,jedis.scard(likeKesy1));
        print(29,jedis.smembers(likeKesy1));

        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"ben");
        jedis.zadd(rankKey,90,"lee");
        jedis.zadd(rankKey,87,"mei");
        jedis.zadd(rankKey,75,"lucy");

        print(30,jedis.zcard(rankKey));
        print(31,jedis.zcount(rankKey,61,100));
        print(32,jedis.zscore(rankKey,"lucy"));
        jedis.zincrby(rankKey,2,"lucy");
        print(33,jedis.zscore(rankKey,"lucy"));
        jedis.zincrby(rankKey,2,"luc");

        print(34,jedis.zscore(rankKey,"luc"));

        print(35,jedis.zrange(rankKey,0,3));
        print(36,jedis.zrevrange(rankKey,1,3));

        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"0","100")){
            print(37,tuple.getElement()+":"+String.valueOf(tuple.getScore()));

        }
        print(38,jedis.zrank(rankKey,"luc"));
        print(39,jedis.zrevrank(rankKey,"ben"));


        JedisPool pool = new JedisPool();
        for(int i = 0;i<100;i++){
            Jedis j = pool.getResource();
            j.get("a");
            System.out.println("POOL"+ i);
            j.close();


        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);

    }
    private Jedis getJedis(){
        return pool.getResource();
    }
    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis=pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


}
