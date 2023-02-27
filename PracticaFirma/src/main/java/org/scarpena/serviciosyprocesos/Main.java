package org.scarpena.serviciosyprocesos;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;


public class Main {

    public static void main(String[] args)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        PrivateKey privateKey = RSA.ReadPrivateKey();
        PublicKey publicKey = RSA.ReadPublicKey();
        File file = new File("mySecretMessage.txt");

        File signature = SignatureProcess(file, privateKey);

        boolean authentication = AuthenticationProcess(file, signature, publicKey);
        System.out.println("\nAutenticación: " + authentication);

    }

    protected static File SignatureProcess(File file, PrivateKey privateKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        String myFile = RSA.ReadFile(file);
        System.out.println("\nContenido de mi archivo TXT: \n"+myFile);
        String hash = RSA.Hashing(myFile);
        System.out.println("\nCódigo hash de mi TXT:\n" + hash);
        String signatureTxt = RSA.encrypt(hash, privateKey);
        File signatureFile = new File("signature.txt");

        try(BufferedWriter br = new BufferedWriter(new FileWriter(signatureFile, false))){
            br.write(signatureTxt);
        }
        return signatureFile;
    }

    protected static boolean AuthenticationProcess(File file, File signature, PublicKey publicKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String myFile = RSA.ReadFile(file);
        String hash = RSA.Hashing(myFile);
        String signatureTxt = RSA.ReadFile(signature);
        String  hashDecrypt = RSA.decrypt(signatureTxt, publicKey);
        System.out.println("\nDesencriptado de la firma y recuperación del código hash:\n" + hashDecrypt);

        return hash.equals(hashDecrypt);
    }
}