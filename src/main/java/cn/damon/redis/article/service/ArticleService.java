package cn.damon.redis.article.service;

import java.util.List;
import java.util.Map;

public interface ArticleService {
     String postArticle(String title, String content,String link, String userId);
     Map<String, String> hgetAll(String key);
     void articleVote(String userId, String articleId);
     String hget(String key, String field);
     List<Map<String,String>> getArticles(int page, String key);
}
