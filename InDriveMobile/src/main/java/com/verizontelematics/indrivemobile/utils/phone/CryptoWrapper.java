package com.verizontelematics.indrivemobile.utils.phone;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.verizontelematics.indrivemobile.constants.SQLiteConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by z688522 on 9/22/14.
 */
public class CryptoWrapper {

    private static final String TAG = "CryptoWrapper";
    private static String DELIMITER = "]";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static int KEY_LENGTH = 256;
    private static SecureRandom random = new SecureRandom();
    private static final String password = "somepass";

    // Encrypt DB file.
    // Delete the decrypted file.
    public static void encryptDB(Context aContext){
        String lDBFilePath = aContext.getDatabasePath(SQLiteConstants.DATABASE_NAME).getPath();
        int lIndexOfDelimiter = lDBFilePath.lastIndexOf('/');
        lDBFilePath = lDBFilePath.substring(0, lIndexOfDelimiter);

        printDirDetails(lDBFilePath);

        String lDecryptedFilePathName = lDBFilePath + "/" +SQLiteConstants.DATABASE_NAME;
        String lEncryptedFilePathName = lDBFilePath + "/" +SQLiteConstants.DATABASE_NAME_ENCRYPTED;
        try {
            // Here you read the clear-text.
            FileInputStream fis  =   new FileInputStream(lDecryptedFilePathName);
            File lFile = new File(lDecryptedFilePathName);
            long lSize = lFile.length();
            if(lSize > 0) {
                // This stream write the encrypted text. This stream will be wrapped by another stream.
                FileOutputStream fos = new FileOutputStream(lEncryptedFilePathName);
                byte[] buffer = new byte[(int) lSize];
                int lByteRead = fis.read(buffer, 0, buffer.length);
                if (lByteRead != buffer.length) {
                    // we have some problem.
                    throw new IOException("not able to read all file data");
                }
                byte[] lEncrypted = encrypt(buffer);
                if(lEncrypted != null) {
                    // write to the encrypted file.
                    fos.write(lEncrypted, 0, lEncrypted.length);
                }
                fos.flush();
                fos.close();
            }
            fis.close();
            lFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void printDirDetails(String aDir){
        File dir = new File(aDir);
        String[] files = dir.list();
        if(files != null) {
            for (String lFile:files){
                Log.d("DirectoryDetails",lFile);
            }
        }
    }

    // Decrypt DB file.
    // Delete the encrypted file.
    public static void decryptDB(Context aContext){
        // since we do not open/Create the database ourselves need to check if this can be done.
        String lDBFilePath = aContext.getDatabasePath(SQLiteConstants.DATABASE_NAME_ENCRYPTED).getPath();
        int lIndexOfDelimiter = lDBFilePath.lastIndexOf('/');
        lDBFilePath = lDBFilePath.substring(0, lIndexOfDelimiter);

        printDirDetails(lDBFilePath);

        String lDecryptedFilePathName = lDBFilePath + "/" +SQLiteConstants.DATABASE_NAME;
        String lEncryptedFilePathName = lDBFilePath + "/" +SQLiteConstants.DATABASE_NAME_ENCRYPTED;
        try {
            // Here you read the clear-text.
            FileInputStream fis  =   new FileInputStream(lEncryptedFilePathName);
            File lFile = new File(lEncryptedFilePathName);
            long lSize = lFile.length();
            if(lSize > 0) {
                // This stream write the encrypted text. This stream will be wrapped by another stream.
                FileOutputStream fos = new FileOutputStream(lDecryptedFilePathName);
                // encrypt the entire file in 1024 KB single go.
                // int is 4 byte that is 16 GIG memory so it should be enough.
                byte[] buffer = new byte[(int) lSize];
                int lByteRead = fis.read(buffer, 0, buffer.length);
                if (lByteRead != buffer.length) {
                    // we have some problem.
                    throw new IOException("not able to read all file data");
                }
                byte[] lDecrypted = decrypt(buffer);
                if (lDecrypted != null) {
                    // write to the encrypted file.
                    fos.write(lDecrypted, 0, lDecrypted.length);
                }
                fos.flush();
                fos.close();
            }
            fis.close();
            lFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String data) {
        // get key from key store
        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
        }
        try {
            // create cipher algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = generateIv(cipher.getBlockSize());
            // create cipher parameters
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher with key, params and encrypt mode.
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            // do encryption for data
            byte[] cipherText = cipher.doFinal(data.getBytes("UTF-8"));
            // convert bytes into base64 string format and return.
            return String.format("%s%s%s", toBase64(iv), DELIMITER, toBase64(cipherText));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encrypt1(String data,Key key) {
        // get key from key store
//        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
        }
        try {
            // create cipher algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = generateIv(cipher.getBlockSize());
            // create cipher parameters
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher with key, params and encrypt mode.
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            // do encryption for data
            byte[] cipherText = cipher.doFinal(data.getBytes("UTF-8"));
            // convert bytes into base64 string format and return.
            return String.format("%s%s%s", toBase64(iv), DELIMITER, toBase64(cipherText));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    // override for bytes.
    private static byte[] encrypt(byte[] data) {
        // get key from key store
        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
            return null;
        }
        try {
            // create cipher algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] iv = generateIv(cipher.getBlockSize());
            // create cipher parameters
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher with key, params and encrypt mode.
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            // do encryption for data
            byte[] encrypted = cipher.doFinal(data);

            // store the length of IV in the first 4 byte;
            // store IV.
            // store the encrypted data.

            byte[] outputBuffer = new byte[4 + iv.length + encrypted.length];
            int lengthInInt = iv.length;
            byte[] bytes = ByteBuffer.allocate(4).putInt(lengthInInt).array();
            System.arraycopy(bytes,0,outputBuffer,0,bytes.length);
            System.arraycopy(iv,0,outputBuffer,bytes.length,iv.length);
            System.arraycopy(encrypted,0,outputBuffer,bytes.length+iv.length,encrypted.length);

            // convert bytes into base64 string format and return.
            return outputBuffer;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    private static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }

    private static byte[] generateIv(int blockSize) {
        byte[] b = new byte[blockSize];
        random.nextBytes(b);
        return b;
    }




    private static byte[] decrypt(byte[] data) {
        // get key from key store
        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
        }
        try {
            // create cipher algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            int lLength = ByteBuffer.wrap(data,0,4).getInt();
            byte[] iv = new byte[lLength];
            ByteBuffer.wrap(data,4,lLength).get(iv);
            byte[] lDataToDecrypt = new byte[data.length - (4+lLength)];
            ByteBuffer.wrap(data,4+lLength,(data.length - (4+lLength))).get(lDataToDecrypt);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            // decrypt data
            byte[] decrypted = cipher.doFinal(lDataToDecrypt);
            return decrypted;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        // return empty string in error cases.
        return null;
    }


    public static String decrypt(String data) {
        // get key from key store
        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
        }
        String[] fields = data.split(DELIMITER);
        if (fields.length != 2) {
            Log.d(TAG, "invalid encrypted text format");
            return "";
        }
        // get params
        byte[] iv = fromBase64(fields[0]);
        // get encrypted data
        byte[] encrypted_data = fromBase64(fields[1]);

        try {
            // get algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // create params
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            // decrypt data
            byte[] plainText = cipher.doFinal(encrypted_data);
            return new String(plainText, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // return empty string in error cases.
        return "";
    }

    public static String decrypt1(String data,Key key) {
        // get key from key store
//        Key key = KeyStoreWrapper.getInstance().getKey();
        if (key == null) {
            Log.d(TAG, "Key is not in key store");
        }
        String[] fields = data.split(DELIMITER);
        if (fields.length != 2) {
            Log.d(TAG, "invalid encrypted text format");
            return "";
        }
        // get params
        byte[] iv = fromBase64(fields[0]);
        // get encrypted data
        byte[] encrypted_data = fromBase64(fields[1]);

        try {
            // get algorithm
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            // create params
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            // initialize cipher
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            // decrypt data
            byte[] plainText = cipher.doFinal(encrypted_data);
            return new String(plainText, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // return empty string in error cases.
        return "";
    }
}
