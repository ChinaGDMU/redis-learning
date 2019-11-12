package cn.damon.redis.config;

import cn.damon.redis.utils.JedisUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName ArticleConfig
 * @Description
 * @Author Damon
 * @Date 2019/11/11 23:26
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
@Configuration
@ComponentScan(value = "cn.damon.redis.*")
@Import(JedisUtils.class)
public class MainConfig {
}
