package org.example;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.json.JSONObject;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        int bitLength = 256;
        BigInteger p = generatePrime(bitLength);
        System.out.println("소수 p: " + p);
        BigInteger g = BigInteger.valueOf(2);

        BigInteger[] keys = generateKeys(p, g, 2048);

        Student student = new Student(keys[0], keys[1]);
        System.out.println("비밀키 x: " + student.getPrivateKey());
        System.out.println("공개키 y: " + student.getPublicKey());

        SeoulNationalUniversity issuer = new SeoulNationalUniversity(p, g);
        BigInteger k = generateRandomK(p);
        System.out.println("임의의 수 k: " + k);

        JSONObject certificate = issuer.issueCertificate(student, k);
        System.out.println("증명 (JSON 형식): " + certificate.toString());

        // 비밀키와 공개키를 PEM 형식으로 변환
        String privateKeyPEM = PEMUtils.privateKeyToPEM(student.getPrivateKey());
        String publicKeyPEM = PEMUtils.publicKeyToPEM(student.getPublicKey());
        System.out.println("비밀키 PEM 형식:\n" + privateKeyPEM);
        System.out.println("공개키 PEM 형식:\n" + publicKeyPEM);

        String pemPublicKey = PEMUtils.publicKeyToPEM(student.getPublicKey());

        BigInteger[] signature = generateSignature(student.getPrivateKey(), student.getPublicKey(), p, g, k);
        BigInteger r = signature[0];
        BigInteger s = signature[1];
        System.out.printf("증명 r, s: (r=%s, s=%s)\n", r, s);

        boolean isVerified = Verifier.verifyProof(r, s, pemPublicKey, p, g);
        if (isVerified) {
            System.out.println("증명이 성공적으로 검증되었습니다.");
        } else {
            System.out.println("증명 검증에 실패하였습니다.");
        }
    }

    public static BigInteger generatePrime(int bitLength) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        return new BigInteger(bitLength, 100, random);
    }

    public static BigInteger[] generateKeys(BigInteger p, BigInteger g, int bitLength) {
        SecureRandom rand = new SecureRandom();
        BigInteger x = new BigInteger(bitLength, rand); // 비밀 값 x (나이)

        if (x.compareTo(p) >= 0) {
            x = x.mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        }

        BigInteger y = g.modPow(x, p); // 공개 값 y
        return new BigInteger[]{x, y};
    }

    public static BigInteger generateRandomK(BigInteger p) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        return new BigInteger(p.bitLength() - 1, random).mod(p);
    }

    public static BigInteger[] generateSignature(BigInteger x, BigInteger y, BigInteger p, BigInteger g, BigInteger k) throws NoSuchAlgorithmException {
        BigInteger r = g.modPow(k, p);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(r.toByteArray());
        digest.update(y.toByteArray());
        byte[] eBytes = digest.digest();
        BigInteger e = new BigInteger(1, eBytes);

        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger s = x.multiply(e).add(k).mod(pMinusOne);

        return new BigInteger[]{r, s};
    }

    public static boolean verifyProof(BigInteger r, BigInteger s, BigInteger y, BigInteger p, BigInteger g) throws NoSuchAlgorithmException {
        BigInteger v1 = g.modPow(s, p);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(r.toByteArray());
        digest.update(y.toByteArray());
        byte[] eBytes = digest.digest();
        BigInteger e = new BigInteger(1, eBytes);

        BigInteger v2 = y.modPow(e, p).multiply(r).mod(p);

        return v1.equals(v2);
    }

    public static String privateKeyToPEM(BigInteger privateKey) throws Exception {
        StringWriter sw = new StringWriter();
        PemWriter pw = new PemWriter(sw);
        pw.writeObject(new PemObject("PRIVATE KEY", privateKey.toByteArray()));
        pw.close();
        return sw.toString();
    }

    public static String publicKeyToPEM(BigInteger publicKey) throws Exception {
        StringWriter sw = new StringWriter();
        PemWriter pw = new PemWriter(sw);
        pw.writeObject(new PemObject("PUBLIC KEY", publicKey.toByteArray()));
        pw.close();
        return sw.toString();
    }
}