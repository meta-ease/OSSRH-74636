package com.open.cloud.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author Leijian
 * @date 2020/4/21 22:36
 */
@Slf4j
public class NotThrowCacheErrorHandler implements CacheErrorHandler {
    private RedisTemplate redisTemplate;

    NotThrowCacheErrorHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        //log.error("cache get error, cacheName:{}, key:{}, msg:{}", cache.getName(), key, ExceptionUtils.getMessage(exception));
        logError("Get", key, exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        //log.error("cache put error, cacheName:{}, key:{}, msg:{}", cache.getName(), key, ExceptionUtils.getMessage(exception));
        logError("Put", key, exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        //log.error("cache evict error, cacheName:{}, key:{}, msg:{}", cache.getName(), key, ExceptionUtils.getMessage(exception));
        logError("Evict", key, exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("cache Clear error, cacheName:{}", cache.getName());
        try {
            if (StringUtils.hasText(cache.getName())) {
                Set<String> keys = redisTemplate.keys(cache.getName());
                log.error("cache clear error,手工清除{},大小{}", cache.getName(), keys.size());
                if (!CollectionUtils.isEmpty(keys)) {
                    redisTemplate.delete(keys);
                }
            }
        } catch (Exception e) {
            log.error("{},{}", e, e.getMessage());
        }
    }

    protected void logError(String oper, Object key, Throwable e) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("cache(")
                .append(this.getClass().getSimpleName()).append(") ")
                .append(oper)
                .append(" error. key=")
                .append(key)
                .append(".");
        if (needLogStackTrace(e)) {
            log.error(sb.toString(), e);
        } else {
            sb.append(' ');
            while (e != null) {
                sb.append(e.getClass().getName());
                sb.append(':');
                sb.append(e.getMessage());
                e = e.getCause();
                if (e != null) {
                    sb.append("\ncause by ");
                }
            }
            log.error(sb.toString());
        }

    }

    protected boolean needLogStackTrace(Throwable e) {
        return false;
    }
}