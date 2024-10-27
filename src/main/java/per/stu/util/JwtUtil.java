package per.stu.util;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import per.stu.exception.ExceptionTool;
import per.stu.model.vo.UserInfo;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public  class JwtUtil implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static PrivateKey privateKey;
    private static JwtParser jwtParser;

    private final String SECRET_KEY = "your_secret_key"; // 请使用复杂的密钥
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1小时

    // 创建 JWT
    public static String generateToken(Object jwtPayload, long expiredAt) {
        //添加构成JWT的参数
        Map<String, Object> headMap = new HashMap();
        headMap.put("alg", SignatureAlgorithm.RS256.getValue());//使用RS256签名算法
        headMap.put("typ", "JWT");
        Map body = JSON.parseObject(JSON.toJSONString(jwtPayload), HashMap.class);
        String jwt = Jwts.builder()
                .header().add(headMap)
                .and()
                .claims(body)
                .expiration(new Date(expiredAt))
                .signWith(privateKey)
                .compact();
        return jwt;
    }

    // 验证 JWT
    public static <T> T verifyJwt(String jwt, Class<T> jwtPayloadClass) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }
        Jws<Claims> jws = jwtParser.parseSignedClaims(jwt); // 会校验签名，校验过期时间
        Claims jwtPayload = jws.getPayload();
        if (jwtPayload == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(jwtPayload), jwtPayloadClass);
    }

    public static <T> T getPayload(String jwt, Class<T> jwtPayloadClass) {
        if (jwt == null || jwt.isEmpty()) {
            return null;
        }

        try {
            // jwt字符串由3部分组成，用英文的点分割：herder.payload.sign
            // 可以直接取中间一段，进行Base64解码
            byte[] decodedBytes = Base64.getDecoder().decode(jwt.split("\\.")[1]);
            return JSON.parseObject(new String(decodedBytes), jwtPayloadClass);
        } catch (Exception e) {
            return null;
        }
    }

    @Value("${login.jwt.private-key}")
    private String privateKeyBase64;
    //获取私钥，用于生成Jwt
    private PrivateKey getPrivateKey() {
        try {
            // 利用JDK自带的工具生成私钥
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyBase64));
            return kf.generatePrivate(ks);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ExceptionTool.throwException("获取Jwt私钥失败");
            return null;
        }
    }

    @Value("${login.jwt.public-key}")
    private String publicKeyBase64;
    // 公钥，用于解析Jwt
    private JwtParser getJwtParser() {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyBase64));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pk = keyFactory.generatePublic(keySpec);
            return Jwts.parser().verifyWith(pk).build();
        } catch (Exception e) {
            // 获取公钥失败
            ExceptionTool.throwException("获取Jwt公钥失败");
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        privateKey = getPrivateKey();
        jwtParser = getJwtParser();
    }


    public static void main(String[] args) {
        try {
            // 创建 KeyPairGenerator 对象
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom()); // 设置密钥长度为2048位

            // 生成密钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 使用 Base64 编码公钥和私钥
            String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            // 输出公钥和私钥
            System.out.println("公钥:\n" + base64PublicKey);
            System.out.println("私钥:\n" + base64PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
