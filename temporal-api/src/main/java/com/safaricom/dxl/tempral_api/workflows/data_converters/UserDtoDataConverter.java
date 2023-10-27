package com.safaricom.dxl.tempral_api.workflows.data_converters;

import com.google.protobuf.ByteString;
import com.safaricom.dxl.tempral_api.dtos.UserDto;
import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import io.temporal.common.converter.DataConverter;
import io.temporal.common.converter.DataConverterException;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.payload.codec.PayloadCodec;
import io.temporal.payload.codec.PayloadCodecException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class UserDtoDataConverter implements PayloadCodec {

    private final String encryptionKey;

    public UserDtoDataConverter(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }


    private byte [] encrypt(byte[] value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
           return  cipher.doFinal(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt PII data", e);
        }
    }

    private byte[] decrypt(byte[] value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(getKeyBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = Base64.getDecoder().decode(value);
            return  cipher.doFinal(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt PII data", e);
        }
    }

    private byte[] getKeyBytes() {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = encryptionKey.getBytes(StandardCharsets.UTF_8);
            return sha.digest(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate encryption key", e);
        }
    }

    @Nonnull
    @Override
    public List<Payload> encode(@Nonnull List<Payload> payloads) {
        log.info("Encoding payload");
        return payloads.stream().map(this::encodePayload).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public List<Payload> decode(@Nonnull List<Payload> payloads) {
        log.info("Decoding payload");
        return payloads.stream().map(this::decodePayload).collect(Collectors.toList());
    }

    private Payload encodePayload(Payload payload) {

        log.info("Encoding payload",payload.toString());
        byte[] encryptedData=null;
        try{

        encryptedData=encrypt(payload.toByteArray());
        } catch (Throwable e) {
            throw new DataConverterException(e);
        }
        return Payload.newBuilder()
                .setData(ByteString.copyFrom(encryptedData))
                .build();
    }

    private Payload decodePayload(Payload payload) {
            byte[] plainData;
            Payload decryptedPayload;
            try {
                plainData = decrypt(payload.getData().toByteArray());
                decryptedPayload = Payload.parseFrom(plainData);
                return decryptedPayload;
            } catch (Throwable e) {
                throw new PayloadCodecException(e);
            }

    }
}
