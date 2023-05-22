package org.example;

import org.json.JSONObject;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Verifier {
    public static boolean verifyProof(BigInteger r, BigInteger s, String pemPublicKey, BigInteger p, BigInteger g) {
        try {
            // PEM 형식의 공개키를 ECPublicKey로 변환
            byte[] publicKeyBytes = Base64.getDecoder().decode(pemPublicKey.replaceAll("\\n", "").replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", ""));
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            ECPublicKey y = (ECPublicKey) keyFactory.generatePublic(publicKeySpec);

            BigInteger v1 = g.modPow(s, p);

            BigInteger e = new BigInteger(1, sha256(r.toByteArray(), y.getW().getAffineX().toByteArray()));
            BigInteger v2 = y.getW().getAffineX().modPow(e, p).multiply(r).mod(p);

            return v1.compareTo(v2) == 0;
        } catch (Exception e) {
            throw new RuntimeException("증명 검증 중 오류 발생", e);
        }
    }

    public static byte[] sha256(byte[]... inputs) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            for (byte[] input : inputs) {
                md.update(input);
            }
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘이 지원되지 않습니다.", e);
        }
    }
}