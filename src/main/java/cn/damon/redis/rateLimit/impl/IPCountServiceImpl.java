package cn.damon.redis.rateLimit.impl;

import cn.damon.redis.rateLimit.IPCountService;
import cn.damon.redis.utils.FileUtils;
import cn.damon.redis.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName IPCountServiceImpl
 * @Description
 * @Author Damon
 * @Date 2019/11/12 8:52
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
@Service
public class IPCountServiceImpl implements IPCountService {

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public Long putIp(String ip, String count, String timesec) {
        String script = FileUtils.getScript("ipCount.lua");
        return (Long) jedisUtils.eval(script, Arrays.asList(ip), Arrays.asList(count,timesec));
    }
}
