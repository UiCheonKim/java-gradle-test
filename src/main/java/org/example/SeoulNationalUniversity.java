package org.example;

import org.json.JSONObject;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class SeoulNationalUniversity {
    private BigInteger prime;
    private BigInteger generator;

    public SeoulNationalUniversity(BigInteger prime, BigInteger generator) {
        this.prime = prime;
        this.generator = generator;
    }

    public JSONObject issueCertificate(Student student, BigInteger randomK) throws NoSuchAlgorithmException {
        BigInteger[] signature = Test.generateSignature(student.getPrivateKey(), student.getPublicKey(), prime, generator, randomK);
        JSONObject certificate = new JSONObject();
        certificate.put("r", signature[0].toString());
        certificate.put("s", signature[1].toString());
        certificate.put("public_key", student.getPublicKey().toString());
        return certificate;
    }
}
