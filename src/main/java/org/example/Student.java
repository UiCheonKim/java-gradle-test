package org.example;

import java.math.BigInteger;

public class Student {
    private BigInteger privateKey;
    private BigInteger publicKey;

    public Student(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }
}

