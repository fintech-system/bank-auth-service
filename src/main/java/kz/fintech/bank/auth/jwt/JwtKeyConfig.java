package kz.fintech.bank.auth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Slf4j
@Configuration
public class JwtKeyConfig {

    @Bean
    public RSAKey rsaKey(JwtProperties properties) throws JOSEException {
        KeyPair keyPair = properties.privateKey() != null
                ? loadKeyPair(properties)
                : generateKeyPair();

        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate());
        if (properties.keyId() != null) {
            builder.keyID(properties.keyId());
        } else {
            builder.keyIDFromThumbprint();
        }
        return builder.build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey, JwtProperties properties) throws JOSEException {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(properties.issuer()));
        return decoder;
    }

    private KeyPair loadKeyPair(JwtProperties properties) {
        if (properties.publicKey() == null) {
            throw new IllegalStateException("security.jwt.public-key must be set together with security.jwt.private-key");
        }
        try (InputStream privateIn = properties.privateKey().getInputStream();
             InputStream publicIn = properties.publicKey().getInputStream()) {
            RSAPrivateKey privateKey = RsaKeyConverters.pkcs8().convert(privateIn);
            RSAPublicKey publicKey = RsaKeyConverters.x509().convert(publicIn);
            return new KeyPair(publicKey, privateKey);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read JWT keys", e);
        }
    }

    private KeyPair generateKeyPair() {
        log.warn("security.jwt.private-key is not set, generating a temporary RSA key. "
                + "Tokens will become invalid after restart. Do not use this outside local development.");
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
