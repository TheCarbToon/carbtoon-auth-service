package kr.springboot.dcinside.cartoon.auth.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Getter
@NoArgsConstructor
@Component
public class JwtConfig {

    @Value("${security.jwt.uri:/auth/**}")
    private String Uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60*7}}")
    private int expiration;

    @Value("${security.jwt.secret:/j7JOK1JQA+cqZhLQF7WVcOzrI0Et0pUZcjhe47ELqQ=}")
    private String secret;

    private Key key;

    public Key getKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(this.secret),
                SignatureAlgorithm.HS256.getJcaName());
    }

}
