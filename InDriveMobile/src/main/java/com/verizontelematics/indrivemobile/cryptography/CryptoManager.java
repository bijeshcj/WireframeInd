package com.verizontelematics.indrivemobile.cryptography;



import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.utils.FileUtils;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by bijesh on 4/10/15.
 */
public class CryptoManager {

    private static final String TAG = CryptoManager.class.getCanonicalName();
    private static Algorithams mAlg;

    public CryptoManager(Algorithams alg){
        this.mAlg = alg;
    }
    public byte[] process(String data){
        Log.d(TAG, "in process");
        byte[] retVal = null;
        switch (mAlg){
            case AES:

                retVal = aesEngine(data);
//                generateSecretKey2("comhticwpandroid",128);
                break;
            default:
                System.err.println("No such Algorithm");
        }
        return retVal;
    }
    private final static byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't',
            'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
    private byte[] aesEngine(String data){

        byte[] encPass = null;
        try {
//            String password = "mypasswordmypassword@gmail.com";
//            encPass = CryptoManager.encrypt(Base64.decode(data,Base64.DEFAULT));//getByteArray(data.toCharArray())
            encPass = CryptoManager.encrypt(data.getBytes());

            Log.d(TAG,"Encryption completed...");
            String decPass = CryptoManager.decrypt(encPass);

            Log.d(TAG,"Plain text "+data);
            Log.d(TAG,"Encrypted pass "+encPass);
            Log.d(TAG,"Decrypted pass "+decPass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encPass;

    }




    public static byte[] encrypt(byte[] Data) throws Exception {
        Context context = IndriveApplication.getInstance();
        SecretKeySpec keySpec = new SecretKeySpec(FileUtils.readKeyContentFromAppInternal(context),"AES");
        byte[] encVal = null;
        try{
        Cipher c = Cipher.getInstance(mAlg.toString());
        c.init(Cipher.ENCRYPT_MODE, keySpec);
        encVal = c.doFinal(Data);
//        String encryptedValue = new BASE64Encoder().encode(encVal);
        }catch(Exception e){
            e.printStackTrace();
        }
        return encVal;
    }
//    backed up the encrypt method on 3/3/2014
//    public static byte[] encrypt(byte[] Data) throws Exception {
//        Key key = generateKey();
//        Cipher c = Cipher.getInstance(mAlg.toString());
//        c.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encVal = c.doFinal(Data);
////        String encryptedValue = new BASE64Encoder().encode(encVal);
//        return encVal;
//    }
    public static String decrypt(byte[] encryptedData) throws Exception {
        Context context = IndriveApplication.getInstance();
        SecretKeySpec keySpec = new SecretKeySpec(FileUtils.readKeyContentFromAppInternal(context),"AES");
        Cipher c = Cipher.getInstance(mAlg.toString());
        c.init(Cipher.DECRYPT_MODE, keySpec);
//        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(encryptedData);
        String decryptedValue = Base64.encodeToString(decValue,Base64.DEFAULT);//new String(decValue);
        return decryptedValue;
    }

    public static String decryptCert(byte[] encryptedData) throws Exception {
        Context context = IndriveApplication.getInstance();
        SecretKeySpec keySpec = new SecretKeySpec(FileUtils.readKeyContentFromAppInternal(context),"AES");
        Cipher c = Cipher.getInstance(mAlg.toString());
        c.init(Cipher.DECRYPT_MODE, keySpec);
//        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedData);
//        byte[] decValue = c.doFinal(Base64.encryptedData);
        byte[] decValue = c.doFinal(encryptedData);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }




    private static Key generateKey(){
        Key key = new SecretKeySpec(generateSecretKey1(),mAlg.toString());
        return key;
    }

    /*
	 * Key generation based on the seed and keylength
	 * @param seed - secret seed can be string/numeric/alphanumeric/mix of acceptable characters
	 * @param keylength - can be 128/192/256
	 * @return key
	 */
    public static SecretKeySpec generateSecretKey2(Context context,String seed, int keylength) {
        // Set up secret key spec for 128/192/256 bit AES encryption and decryption
        SecretKeySpec secretKeySpec = null;
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed.getBytes("UTF-8"));
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keylength, secureRandom);
            secretKeySpec = new SecretKeySpec(
                    (keyGenerator.generateKey()).getEncoded(), "AES");
//            saveKeyToFile(secretKeySpec.getEncoded());
            FileUtils.saveFileToAppInternal(context,seed);
//            FileUtils.readFileContentFromAppInternal(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secretKeySpec;
    }


    private static void saveKeyToFile(byte[] keys){
        Log.d(TAG,"trying to save the key...");
        File file = new File("C:\\Users\\bijesh\\Desktop\\Temp\\key.dat");
        if(file.exists()){
            // deliberate block
        }else{
            try{
            file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.write(keys);
            dos.close();
            System.out.println("saved the key to the file...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static byte[] loadKeyFromFile(){
        Log.d(TAG,"trying to load the key...");
        File file = new File("C:\\Users\\bijesh\\Desktop\\Temp\\key.dat");
        byte[] key = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            System.out.println("key available "+dis.available());
            key = new byte[dis.available()];
            dis.readFully(key);
            dis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return key;
    }

    private static byte[] generateSecretKey1(){
        Log.d(TAG,"generateSecreyKey1...");
        byte[] key = null;
        try{
             KeyGenerator keyGen = KeyGenerator.getInstance("AES");
             keyGen.init(256);
             SecretKey sk = keyGen.generateKey();
             key = sk.getEncoded();
             for(byte b:key)
              System.out.printf("%2x",b);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return key;
    }
    private static byte[] generateSecretKey(){
        String passcode = "comhticspandroid".toUpperCase();
        Log.d(TAG,"Passcode "+passcode);
        char[] chars = passcode.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<chars.length;i++){
            sb.append(getEncryptedString(chars[i]));
        }
        Log.d(TAG,"encrypted string: "+sb.toString());
        char[] encryptesChars = sb.toString().toCharArray();
        byte[] secretKey = getByteArray(encryptesChars);
        return secretKey;
    }


    private static byte[] getByteArray(char[] chars){
        byte[] retBytes = new byte[chars.length];
        for(int i=0;i<chars.length;i++){
            retBytes[i] = (byte)chars[i];
        }
        return retBytes;
    }

    public static String getKey(){
        return "comverizoindrive";
    }

    private static String  getEncryptedString(char i){
        String retVal = "";

        switch (i){
            case 'A':
                retVal = ""+(char)161;
                break;
            case 'B':
                retVal = ""+(char)182;
                break;
            case 'C':
                retVal = ""+(char)177;

                break;
            case 'D':
                retVal = ""+(char)34;

                break;
            case 'E':
                retVal = ""+(char)64;

                break;
            case 'F':
                retVal = ""+(char)191;

                break;
            case 'G':
                retVal = ""+(char)95;

                break;
            case 'H':
                retVal = ""+(char)134;

                break;
            case 'I':
                retVal = ""+(char)145;

                break;
            case 'J':
                retVal = ""+(char)162;

                break;
            case 'K':
                retVal = ""+(char)36;

                break;
            case 'L':
                retVal = ""+(char)131;

                break;
            case 'M':
                retVal = ""+(char)226;

                break;
            case 'N':
                retVal = ""+(char)252;

                break;
            case 'O':
                retVal = ""+(char)195;

                break;
            case 'P':
                retVal = ""+(char)221;
                break;
            case 'Q':
                retVal = ""+(char)238;
                break;

            case 'R':
                retVal = ""+(char)222;
                break;
            case 'S':
                retVal = ""+(char)174;
                break;
            case 'T':
                retVal = ""+(char)189;
                break;
            case 'U':
                retVal = ""+(char)205;
                break;
            case 'V':
                retVal = ""+(char)254;
                break;
            case 'W':
                retVal = ""+(char)241;
                break;
            case 'X':
                retVal = ""+(char)168;
                break;
            case 'Y':
                retVal = ""+(char)155;
                break;
            case 'Z':
                retVal = ""+(char)137;
                break;
        }

        return retVal;
    }

}


