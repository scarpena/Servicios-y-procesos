package org.scarpena.serviciosyprocesos;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.*;


public class Main {

    public static void main(String[] args)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        PrivateKey privateKey = RSA.ReadPrivateKey();
        PublicKey publicKey = RSA.ReadPublicKey();
        File file = new File("mySecretMessage.txt");

        String signature = SignatureProcess(file, privateKey);
        boolean autentication = AuthenticationProcess(file, signature, publicKey);
        System.out.println("\nAutenticación: " + autentication);

    }

    protected static String SignatureProcess(File file, PrivateKey privateKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String myFile = RSA.ReadFile(file);
        System.out.println("\nContenido de mi archivo TXT: \n"+myFile);
        String hash = RSA.Hashing(myFile);
        System.out.println("\nCódigo hash de mi TXT:\n" + hash);
        String signature = RSA.encrypt(hash, privateKey);
        System.out.println("\nFirma con hash encriptado\n" + signature);

        return signature;
    }

    protected static boolean AuthenticationProcess(File file, String signature, PublicKey publicKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String myFile = RSA.ReadFile(file);
        String hash = RSA.Hashing(myFile);
        String  hashDecrypt = RSA.decrypt(signature, publicKey);
        System.out.println("\nDesencriptado de la firma y recuperación del código hash:\n" + hashDecrypt);

        return hash.equals(hashDecrypt);
    }
}