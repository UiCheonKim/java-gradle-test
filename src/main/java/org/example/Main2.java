package org.example;


import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Main2 {
    public static void main(String[] args)  {
        try {
            generateECDSAKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    // Generate an ECDSA key pair using WEB3J
    public static void generateECDSAKeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        // Generate an ECDSA key pair using WEB3J
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        // Get the public key
        BigInteger publicKey = ecKeyPair.getPublicKey();
        // Get the private key
        BigInteger privateKey = ecKeyPair.getPrivateKey();
        // Get the address
        String address = Keys.getAddress(publicKey);
        // Print the address
        System.out.println("Address: " + address);
        // Print the public key
        System.out.println("Public key: " + publicKey.toString(16));
        // Print the private key
        System.out.println("Private key: " + privateKey.toString(16));
    }

    // ecdsa 키쌍을 파라미터로 받아
    // Message 를 Sha3 Hash 하고
    // 해싱된 메시지를 개인키로 ECDSA 서명하는 코드
    public static void signMessage(ECKeyPair ecKeyPair, String message) {
        // Get the public key
        BigInteger publicKey = ecKeyPair.getPublicKey();
        // Get the private key
        BigInteger privateKey = ecKeyPair.getPrivateKey();
        // Get the address
        String address = Keys.getAddress(publicKey);
        // Print the address
        System.out.println("Address: " + address);
        // Print the public key
        System.out.println("Public key: " + publicKey.toString(16));
        // Print the private key
        System.out.println("Private key: " + privateKey.toString(16));
        // Hash the message
        byte[] messageHash = Hash.sha3(message.getBytes());
        // Sign the message
        Sign.SignatureData signatureData = Sign.signMessage(messageHash, ecKeyPair, false);
        // Print the signature
        System.out.println("Signature: " + signatureData.toString());
    }




}
