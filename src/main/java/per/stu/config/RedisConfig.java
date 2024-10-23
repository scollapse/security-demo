package per.stu.config;

import com.alibaba.fastjson2.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis配置
 *
 * @author babax
 * @version v1.0
 * @date 2024/10/21 18:09
 * @modified by
 */
public class RedisConfig {
    // redis使用fastjson2序列化
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 使用 Fastjson2 序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(new Fastjson2RedisSerializer());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(new Fastjson2RedisSerializer());

        return template;
    }

    // 自定义 Fastjson2 序列化器
    public static class Fastjson2RedisSerializer implements RedisSerializer<Object> {
        @Override
        public byte[] serialize(Object object) {
            return JSON.toJSONBytes(object);
        }

        @Override
        public Object deserialize(byte[] bytes) {
            return JSON.parse(bytes);
        }
    }

}
