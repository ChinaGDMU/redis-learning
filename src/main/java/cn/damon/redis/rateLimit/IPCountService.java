package cn.damon.redis.rateLimit;

public interface IPCountService {
    Long putIp(String ip,String count,String timesec);
}
