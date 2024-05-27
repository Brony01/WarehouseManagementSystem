package com.java.warehousemanagementsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.java.warehousemanagementsystem.aspect.CacheLoggingAspect;
import com.java.warehousemanagementsystem.pojo.User;
import com.java.warehousemanagementsystem.repository.UserRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    private static final String VERSION_CLAIM = "version";
    private static final Logger logger = LoggerFactory.getLogger(CacheLoggingAspect.class);
    /**
     * 默认JWT标签头
     */
    public static final String HEADER = "Authorization";
    /**
     * JWT配置信息
     */
    private static JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

    private JwtUtils() {
    }

    /**
     * 初始化参数
     *
     * @param header         JWT标签头
     * @param tokenHead      Token头
     * @param issuer         签发者
     * @param secretKey      密钥 最小长度：4
     * @param expirationTime Token过期时间 单位：秒
     * @param issuers        签发者列表 校验签发者时使用
     * @param audience       接受者
     */
    public static void initialize(String header, String tokenHead, String issuer, String secretKey, long expirationTime, List<String> issuers, String audience) {
        jwtConfig = new JwtConfig();
        jwtConfig.setHeader(StringUtils.isNotBlank(header) ? header : HEADER);
        jwtConfig.setTokenHead(tokenHead);
        jwtConfig.setIssuer(issuer);
        jwtConfig.setSecretKey(secretKey);
        jwtConfig.setExpirationTime(expirationTime);
        if (CollectionUtils.isEmpty(issuers)) {
            issuers = Collections.singletonList(issuer);
        }
        jwtConfig.setIssuers(issuers);
        jwtConfig.setAudience(audience);
        jwtConfig.setAlgorithm(Algorithm.HMAC256(jwtConfig.getSecretKey()));
    }

    /**
     * 初始化参数
     */
    public static void initialize(String header, String issuer, String secretKey, long expirationTime) {
        initialize(header, null, issuer, secretKey, expirationTime, null, null);
    }

    /**
     * 初始化参数
     */
    public static void initialize(String header, String tokenHead, String issuer, String secretKey, long expirationTime) {
        initialize(header, tokenHead, issuer, secretKey, expirationTime, null, null);
    }

    /**
     * 生成 Token
     *
     * @param subject 主题
     * @return Token
     */
    public static String generateToken(String subject, Integer versionId) {
        return generateToken(subject, versionId, jwtConfig.getExpirationTime());
    }

    /**
     * 生成 Token
     *
     * @param subject        主题
     * @param expirationTime 过期时间
     * @return Token
     */
    public static String generateToken(String subject, Integer versionId, long expirationTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime * 1000);

        return JWT.create()
                .withClaim(VERSION_CLAIM, versionId)
                .withSubject(subject)
                .withIssuer(jwtConfig.getIssuer())
                .withAudience(jwtConfig.getAudience())
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(jwtConfig.getAlgorithm());
    }

    /**
     * 获取Token数据体
     */
    public static String getTokenContent(String token) {
        if (StringUtils.isNotBlank(jwtConfig.getTokenHead())) {
            token = token.substring(jwtConfig.getTokenHead().length()).trim();
        }
        return token;
    }

    /**
     * 验证 Token
     *
     * @param token token
     * @return 验证通过返回true，否则返回false
     */
    public static boolean isValidToken(String token) {
        try {
            String subject = getSubject(token);
            token = getTokenContent(token);
            DecodedJWT jwt = JWT.decode(token);
            int version = jwt.getClaim(VERSION_CLAIM).asInt();
            logger.info("version: " + version);
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);

            UserRepository userRepository = ContextUtils.getApplicationContext().getBean(UserRepository.class);
            Mono<User> userMono = userRepository.findByUsername(subject);

            return userMono.map(user -> version == user.getVersion())
                    .defaultIfEmpty(false)
                    .block(); // 使用阻塞操作，因为此方法仍然是同步的
        } catch (JWTVerificationException exception) {
            // Token验证失败
            return false;
        }
    }

    /**
     * 判断Token是否过期
     *
     * @param token token
     * @return 过期返回true，否则返回false
     */
    public static boolean isTokenExpired(String token) {
        try {
            token = getTokenContent(token);
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);

            Date expirationDate = JWT.decode(token).getExpiresAt();
            return expirationDate != null && expirationDate.before(new Date());
        } catch (JWTVerificationException exception) {
            // Token验证失败
            return false;
        }
    }

    /**
     * 获取 Token 中的主题
     *
     * @param token token
     * @return 主题
     */
    public static String getSubject(String token) {
        token = getTokenContent(token);
        return JWT.decode(token).getSubject();
    }

    /**
     * 获取当前Jwt配置信息
     */
    public static JwtConfig getCurrentConfig() {
        return jwtConfig;
    }

    @Data
    public static class JwtConfig {
        /**
         * JwtToken Header标签
         */
        private String header;
        /**
         * Token头
         */
        private String tokenHead;
        /**
         * 签发者
         */
        private String issuer;
        /**
         * 密钥
         */
        private String secretKey;
        /**
         * Token 过期时间
         */
        private long expirationTime;
        /**
         * 签发者列表
         */
        private List<String> issuers;
        /**
         * 接受者
         */
        private String audience;
        /**
         * 加密算法
         */
        private Algorithm algorithm;
    }
}
