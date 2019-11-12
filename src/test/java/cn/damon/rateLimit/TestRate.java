package cn.damon.rateLimit;

import cn.damon.redis.article.service.ArticleService;
import cn.damon.redis.config.MainConfig;
import cn.damon.redis.rateLimit.IPCountService;
import cn.damon.redis.utils.FileUtils;
import cn.damon.redis.utils.JedisUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TestArticle
 * @Description
 * @Author Damon
 * @Date 2019/11/12 0:15
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class TestRate {

    @Autowired
    private IPCountService ipCountService;
    @Autowired
    private JedisUtils jedisUtils;

    @Test
    public void test01() throws Exception{
        String ip = "localhost";
        Integer count = 10;
        Integer timesec = 10;
        for (int i = 0; i <100 ; i++) {
            Long result = ipCountService.putIp(ip, count + "", timesec + "");
            if (result.equals(0L)) {
                System.out.println("你被限速啦............"+result);
            } else {
                System.out.println("正常访问........."+result);
            }
        }
    }

}
