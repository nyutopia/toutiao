package com.ny.toutiao;

import com.ny.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ny on 2017/8/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
    }

    @Test
    public void testLikeB(){
        likeService.disLike(123,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxxx");
    }

    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void BeforeClass(){
        System.out.println("BeforeClass");

    }

    @AfterClass
    public static void AfterClass(){
        System.out.println("AfterClass");

    }


}
