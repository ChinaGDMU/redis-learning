package cn.damon.redis.article.service.impl;

import cn.damon.redis.article.constant.Constants;
import cn.damon.redis.article.service.ArticleService;
import cn.damon.redis.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ArticleServiceImpl
 * @Description
 * @Author Damon
 * @Date 2019/11/11 23:26
 * @Email zdmsjyx@163.com
 * @Version 1.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private JedisUtils jedisUtils;

    @Override
    public String postArticle(String title, String content, String link, String userId) {
        //获取文章id
        String articleId = String.valueOf(jedisUtils.incr("article:"));
        //投票id
        String voteId = "vote:"+articleId;
        //一周内才能投票 投自己一票
        jedisUtils.sadd(voteId,userId);
        jedisUtils.expire(voteId, Constants.ONE_WEEK_IN_SECONDS);

        //now  当前时间秒
        long now = System.currentTimeMillis() / 1000;
        //文章key
        String article = "article:"+articleId;
        //设置map
        Map<String,String> map = new HashMap<>();
        map.put("title",title);
        map.put("content",content);
        map.put("link",link);
        map.put("userId",userId);
        map.put("votes","1");

        //hmset 设置文章内容
        jedisUtils.hmset(article,map);
        //zset 文章分数情况，当前时间+400分每一票  文章
        jedisUtils.zadd("score:info",now+Constants.VOTE_SCORE,article);
        //zset 文章创建时间  时间戳作为score
        jedisUtils.zadd("time",now,article);
        return articleId;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return jedisUtils.hgetAll(key);
    }

    @Override
    public void articleVote(String userId, String articleId) {
        //计算是否还可以投票，如果文章发布的时间小于计算出来的最晚发布时间时间小，那么不可以投票
        long cutOff = System.currentTimeMillis()/1000-Constants.ONE_WEEK_IN_SECONDS;
        if(jedisUtils.zscore("time",articleId) < cutOff){
            return;
        }
        //获取文章的id
        String artiId = articleId.substring(articleId.indexOf(":")) + 1;
        //判断是否已经投过票了
        if(jedisUtils.sadd("vote"+artiId,userId) == 1){
            //如果没有投过票

            //投票，分数+400
            jedisUtils.zincrby("score:info",Constants.VOTE_SCORE,articleId);
            //文章的投票数+1
            jedisUtils.hincrBy(articleId,"votes",1);
        }

    }

    @Override
    public String hget(String key, String field) {
        return jedisUtils.hget(key,field);
    }

    @Override
    public List<Map<String, String>> getArticles(int page, String key) {
        //开始
        int start = (page-1) * Constants.ARTICLES_PER_PAGE;
        //结束
        int end = start + Constants.ARTICLES_PER_PAGE - 1;
        //zrevrange  倒叙获取
        Set<String> ids = jedisUtils.zrevrange(key, start, end);
        //遍历获取文章信息
        List<Map<String,String>> resultList = new LinkedList<>();
        for (String id : ids) {
            Map<String, String> hgetall = jedisUtils.hgetall(id);
            //article 加入序号
            hgetall.put("id",id);
            resultList.add(hgetall);
        }
        //返回结果
        return resultList;
    }

}
