package com.example.demo6;

import java.security.*; import javax.crypto.Cipher; import java.util.Base64;

public class RSAExample {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String originalMessage = "Hello, RSA!";
            System.out.println("النص الأصلي: " + originalMessage);

            String encryptedMessage = encrypt(originalMessage, publicKey);
            System.out.println("النص المشفر: " + encryptedMessage);

            String decryptedMessage = decrypt(encryptedMessage, privateKey);
            System.out.println("النص بعد فك التشفير: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}