package per.stu.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * The type Redis util.
 */
@Slf4j
@Component
public class RedisUtil {


    private static RedisTemplate<String, Object> redisTemplate;

    @Resource
    private  RedisTemplate<String, Object> objectRedisTemplate;

    @PostConstruct
    public void init() {
        redisTemplate = objectRedisTemplate;
    }


    /**
     * 设置键值对 0过期时间永不过期 过期时间单位为秒
     * Set boolean.
     *
     * @param key   the key
     * @param value the value
     * @return the boolean
     */
    public static boolean set(String key, Object value, long time) {
        // key value 不能为空
        if (key == null || value == null) {
            return false;
        }
        try {
            // 设置过期时间
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set error: {}", e.getMessage());
            return false;
        }
    }


    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * Get object.
     *
     * @param <T>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the object
     */
    public static <T> T  get(String key,Class clazz) {
        // 获取数据并转换成指定类型 捕获异常
        try {
            return key == null ? null : (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("RedisUtil get error: {}", e.getMessage());
            return null;
        }
    }


    /**
     * 指定键自增1 返回自增后的值
     * Increment long.
     *
     * @param key the key
     * @return the long
     */
    public static Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 指定键自增 delta
     * Increment long.
     *
     * @param key   the key
     * @param delta the delta
     * @return the long
     */
    public static Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 删除指定键
     * Delete boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public static boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断键是否存在
     * Has key boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    // 指定的set集合中是否存在指定的值
    public static boolean hasKeyInSet(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }



}
