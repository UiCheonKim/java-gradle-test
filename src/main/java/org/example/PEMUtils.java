package org.example;

import java.io.StringWriter;
import java.math.BigInteger;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class PEMUtils {
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
