package com.verizontelematics.indrivemobile.utils.security;

/**
 * Created by Sony on 25-04-2015.
 */
import android.util.Log;

import com.google.android.gms.dynamic.e;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.utils.FileUtils;
import com.verizontelematics.indrivemobile.utils.config.InDrivePreference;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.File;
//import javax.xml.bind.DatatypeConverter;
import java.util.Collection;

import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.KeyStore;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBag;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.pkcs.bc.BcPKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS12PBEOutputEncryptorBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEOutputEncryptorBuilder;

public class CreatePKCS12{

    private static final String TAG = CreatePKCS12.class.getCanonicalName();
    //Just for test purpose, I used fixed values here.
    public static final String PRIVAKE_KEY_FILE = "privateKey.key";
    public static final String PKCS7_CERTIFICATE_FILE = "RA-Certificate_20130809.p7b";

    public static final String PKCS12_CERTIFICATE_FILE = "client-cert.p12";//"myTestPKCS12.cer";

    private static Provider provider = new BouncyCastleProvider();
    static {
        Security.addProvider(provider); //Initializes bouncy castle
    }


    public static void createPKCS12(String mobileUserId){
        String pk12fileName = mobileUserId+".p12";
        Log.d(TAG,"layer7 createPKCS12 user id "+mobileUserId);
        try
        {
            //Read private key from pem file.

//            String privKeyPEM = new String();
//            privKeyPEM = new Scanner(new File(PRIVAKE_KEY_FILE)).useDelimiter("\n").next();
//
//            //Base64 decode the data
//            byte[] encoded = DatatypeConverter.parseBase64Binary(privKeyPEM);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            PrivateKey privKey = kf.generatePrivate(keySpec);

            //Read PKCS#7 certificate from PEM file
//            FileInputStream pkcsFile = new FileInputStream("PKCS7_CERTIFICATE_FILE");
//            InputStream is = new BufferedInputStream(new ByteArrayInputStream(InDrivePreference.getInstance().getStringData("cert", "").getBytes()));
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(Base64Util.decodeBase64AsString(FileUtils.readCertFromAppInternal(IndriveApplication.getInstance())).getBytes()));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

//            CertPath cp = cf.generateCertPath(is,"PKCS7");
//            List<? extends Certificate> certs = cp.getCertificates();
//            for(Certificate certificate:certs){
//                Log.d(TAG,"$$$ cert $$$ "+certificate);
//            }

            Collection<? extends Certificate> c = cf.generateCertificates(is);
            Iterator<? extends Certificate> i = c.iterator();

            //To be stored in PKCS#12 keystore

            Certificate[] chain = new Certificate[3];
            X509Certificate x509Certificate = null;
            while (i.hasNext()) {
                Certificate cert = (Certificate)i.next();
                if(cert instanceof X509Certificate){
                    Log.d(TAG,"layer7 instance of X509Certificate");
                    x509Certificate = (X509Certificate)cert;
//                    if( ((X509Certificate)cert).getBasicConstraints() < 0)
//                    {
                        //Check value of BasicConstraints to see if it is an End Entity cert or //CA certificate.

                        chain[0] = cert;
                        Log.d(TAG,"layer7 chain[0] public key "+chain[0].getPublicKey());
//                    } else {
                        //For test purpose only, You may have more than one CA certificate in your //PKCS#7 file.
                        chain[1] = cert;
                        Log.d(TAG,"layer7 chain[1] type "+chain[1].getType());

                        chain[2] = cert;
//                    }
                }
            }

//            Certificate[] chain = new Certificate[1];
//            Certificate certificate = getCertificate(InDrivePreference.getInstance().getStringData("cert", ""));
//            chain[0] = certificate;

            //PKCS#12 Key Store
//            KeyStore pkcs12KeyStore;
//            pkcs12KeyStore = KeyStore.getInstance("PKCS12","BC");
//            pkcs12KeyStore.load( null, null );
//            Log.d(TAG,"layer7 are private key equal "+PKCS12Attributes.getInstance().getPrivateKey());
//            pkcs12KeyStore.setKeyEntry("userid", PKCS12Attributes.getInstance().getPrivateKey(), "password".toCharArray(), chain);
//
//            //Output to PEM file.
            FileOutputStream fos;
////            File pkcs12File = new File(PKCS12_CERTIFICATE_FILE);
            fos = FileUtils.getAppInternalFileOutputStream(pk12fileName);//new FileOutputStream( pkcs12File );
//            pkcs12KeyStore.store( fos, "password".toCharArray() );
//            fos.flush();
//            fos.close();
            KeyFactory  fact = KeyFactory.getInstance("RSA", "BC");

            createPKCS12File(fos,PKCS12Attributes.getInstance().getPrivateKey(),chain,"passPhrase".toCharArray());

//            createPKCS12File1(fos,PKCS12Attributes.getInstance().getPrivateKey(),chain,"password".toCharArray());

            Log.d(TAG,"layer7 created pkcs12 file");

            FileUtils.printInternalFileDir();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error : " + e.getMessage());
        }
    }

    public static Certificate[] getChain(){
        Certificate[] chain = new Certificate[2];
        try{
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(InDrivePreference.getInstance().getStringData("cert", "").getBytes()));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> c = cf.generateCertificates(is);
            Iterator<? extends Certificate> i = c.iterator();

            //To be stored in PKCS#12 keystore

//            Certificate[] chain = new Certificate[2];
            X509Certificate x509Certificate = null;
            while (i.hasNext()) {
                Certificate cert = (Certificate)i.next();
                if(cert instanceof X509Certificate){
                    Log.d(TAG,"layer7 instance of X509Certificate");
                    x509Certificate = (X509Certificate)cert;
//                    if( ((X509Certificate)cert).getBasicConstraints() < 0)
//                    {
                        //Check value of BasicConstraints to see if it is an End Entity cert or //CA certificate.

                        chain[0] = cert;
                        Log.d(TAG,"layer7 chain[0] public key "+chain[0].getPublicKey());
//                    } else {
                        //For test purpose only, You may have more than one CA certificate in your //PKCS#7 file.
                        chain[1] = cert;
                        Log.d(TAG,"layer7 chain[1] type "+chain[1].getType());
//                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG,"layer7 returing chain");
        return chain;
    }


    private static void createPKCS12File1(FileOutputStream fos,PrivateKey privKey,Certificate[]
            chain,char[] pass){
        try {
            Log.d(TAG,"layer7 createPKCS12File "+privKey);

//            ASN1EncodableVector v =new ASN1EncodableVector();
//            X509CertificateObject clientCert = new X509CertificateObject(x509Certificate);

            PKCS12BagAttributeCarrier bagAttr = (PKCS12BagAttributeCarrier) chain[0];//privKey; // try certificate here
            //
            // this is also optional - in the sense that if you leave this
            // out the keystore will add it automatically, note though that
            // for the browser to recognise which certificate the private key
            // is associated with you should at least use the pkcs_9_localKeyId
            // OID and set it to the same as you do for the private key's
            // corresponding certificate.
            //
            bagAttr.setBagAttribute(
                    PKCSObjectIdentifiers.pkcs_9_at_friendlyName,
                    new DERBMPString("Eric's Key"));

            JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();
            SubjectKeyIdentifier pubKeyId =
                    extUtils.createSubjectKeyIdentifier(chain[0].getPublicKey());
            bagAttr.setBagAttribute(
                    PKCSObjectIdentifiers.pkcs_9_at_localKeyId,
                    pubKeyId);

            //
            // store the key and the certificate chain
            //
            KeyStore store = KeyStore.getInstance("PKCS12", "BC");

            store.load(null, null);

            //
            // if you haven't set the friendly name and local key id above
            // the name below will be the name of the key
            //
            store.setKeyEntry("Eric's Key", privKey, null, chain);

//            FileOutputStream fOut = new FileOutputStream("client-cert.p12");

            store.store(fos, pass);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createPKCS12File(OutputStream pfxOut, PrivateKey key, Certificate[]
            chain,char[] pass){
        Log.d(TAG,"inside createPKCS12File");
      try {
          OutputEncryptor encOut = null;
          try {
//              encOut = new
//                      JcePKCSPBEOutputEncryptorBuilder(NISTObjectIdentifiers.id_aes256_CBC).setProvider(provider).build(pass);
              encOut = new BcPKCS12PBEOutputEncryptorBuilder(PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC, new CBCBlockCipher(new DESedeEngine())).build(pass);
          }catch (Exception e){

              e.printStackTrace();
          }
          PKCS12SafeBagBuilder taCertBagBuilder = new
                  JcaPKCS12SafeBagBuilder((X509Certificate) chain[2]);

          taCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new
                  DERBMPString("Bouncy Primary Certificate"));

          PKCS12SafeBagBuilder caCertBagBuilder = new
                  JcaPKCS12SafeBagBuilder((X509Certificate) chain[1]);

          caCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new
                  DERBMPString("Bouncy Intermediate Certificate"));

          JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

          PKCS12SafeBagBuilder eeCertBagBuilder = new
                  JcaPKCS12SafeBagBuilder((X509Certificate) chain[0]);
          eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new
                  DERBMPString("Eric's Key"));

          SubjectKeyIdentifier pubKeyId =
                  extUtils.createSubjectKeyIdentifier(chain[0].getPublicKey());

          eeCertBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);

          PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(key, encOut);

          keyBagBuilder.addBagAttribute(PKCS12SafeBag.friendlyNameAttribute, new
                  DERBMPString("Eric's Key"));

          keyBagBuilder.addBagAttribute(PKCS12SafeBag.localKeyIdAttribute, pubKeyId);

          PKCS12PfxPduBuilder builder = new PKCS12PfxPduBuilder();

          builder.addEncryptedData(new BcPKCS12PBEOutputEncryptorBuilder(PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, new CBCBlockCipher(new RC2Engine())).build(pass),  new PKCS12SafeBag[]{eeCertBagBuilder.build(),
                      caCertBagBuilder.build(), taCertBagBuilder.build()});

          builder.addData(keyBagBuilder.build());


//          try {
//              builder.addEncryptedData(new
//                      JcePKCSPBEOutputEncryptorBuilder(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC).setProvider(provider).build(pass), new PKCS12SafeBag[]{eeCertBagBuilder.build(),
//                      caCertBagBuilder.build(), taCertBagBuilder.build()});
//          }catch (OperatorCreationException e){
//              Log.d(TAG,"$$$ OperatorCreationException 2");
//              e.printStackTrace();
//          }
//          PKCS12PfxPdu pfx = builder.build(new
//                  JcePKCS12MacCalculatorBuilder(NISTObjectIdentifiers.id_sha256), pass);//id_sha256

          PKCS12PfxPdu pfx = builder.build(new BcPKCS12MacCalculatorBuilder(), pass);

          // make sure we don't include indefinite length encoding
          pfxOut.write(pfx.getEncoded(ASN1Encoding.DL));
          pfxOut.close();
      }catch (IOException e){
          Log.d(TAG,"$$$ IOException here");
          e.printStackTrace();
      }catch (PKCSException e){
          e.printStackTrace();
      }
      catch (NoSuchAlgorithmException e){
          e.printStackTrace();
      }
        Log.d(TAG,"completed this  createPKCS12File");
    }




    public static void createPKCS12_1(){
        Log.d(TAG,"layer7 createPKCS12");
        try
        {
            //Read private key from pem file.

//            String privKeyPEM = new String();
//            privKeyPEM = new Scanner(new File(PRIVAKE_KEY_FILE)).useDelimiter("\n").next();
//
//            //Base64 decode the data
//            byte[] encoded = DatatypeConverter.parseBase64Binary(privKeyPEM);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            PrivateKey privKey = kf.generatePrivate(keySpec);

            //Read PKCS#7 certificate from PEM file
//            FileInputStream pkcsFile = new FileInputStream("PKCS7_CERTIFICATE_FILE");

//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            Collection<? extends Certificate> c = cf.generateCertificates(pkcsFile);
//            Iterator<? extends Certificate> i = c.iterator();
//
//            //To be stored in PKCS#12 keystore
//
//            Certificate[] chain = new Certificate[2];
//
//            while (i.hasNext()) {
//                Certificate cert = (Certificate)i.next();
//                if(cert instanceof X509Certificate){
//                    if( ((X509Certificate)cert).getBasicConstraints() < 0)
//                    {
//                        //Check value of BasicConstraints to see if it is an End Entity cert or //CA certificate.
//
//                        chain[0] = cert;
//                    } else {
//                        //For test purpose only, You may have more than one CA certificate in your //PKCS#7 file.
//                        chain[1] = cert;
//                    }
//                }
//            }

            Certificate[] chain = new Certificate[1];
            Certificate certificate = getCertificate(InDrivePreference.getInstance().getStringData("cert", ""));
            chain[0] = certificate;

            //PKCS#12 Key Store
            KeyStore pkcs12KeyStore;
            pkcs12KeyStore = KeyStore.getInstance("PKCS12","BC");
            pkcs12KeyStore.load( null, null );
            Log.d(TAG,"layer7 are private key equal "+PKCS12Attributes.getInstance().getPrivateKey());
            pkcs12KeyStore.setKeyEntry("userid", PKCS12Attributes.getInstance().getPrivateKey(), "password".toCharArray(), chain);

            //Output to PEM file.
            FileOutputStream fos;
//            File pkcs12File = new File(PKCS12_CERTIFICATE_FILE);
            fos = FileUtils.getAppInternalFileOutputStream(PKCS12_CERTIFICATE_FILE);//new FileOutputStream( pkcs12File );
            pkcs12KeyStore.store( fos, "password".toCharArray() );
            fos.flush();
            fos.close();

            Log.d(TAG,"layer7 created pkcs12 file");

            FileUtils.printInternalFileDir();
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }





    private static KeyStore getKeyStore(String signed_cert){
        KeyStore keyStore = null;
        try {
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            X509Certificate x509Certificate = (X509Certificate) getCertificate(signed_cert);
            keyStore.setCertificateEntry(x509Certificate.getSubjectX500Principal().getName(), x509Certificate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return keyStore;
    }

    private static Certificate getCertificate(String mServerSignedCertificate) {
        Log.d(TAG, "layer7 $$ getCertificate " + mServerSignedCertificate);
        CertificateFactory cf = null;
        InputStream caInput = null;
        try {
            cf = CertificateFactory.getInstance("X.509","BC");
        }catch (CertificateException e){
            e.printStackTrace();
        }catch (NoSuchProviderException e){
            e.printStackTrace();
        }
        Certificate ca = null;
        caInput = new BufferedInputStream(new ByteArrayInputStream(mServerSignedCertificate.getBytes()));
        try {
            Log.d(TAG,"layer7 before generating certificate");
            ca = cf.generateCertificate(caInput);
            if(ca != null) {
                Log.d(TAG, "layer7 ca=" + ((X509Certificate) ca).getSubjectDN());
                Log.d(TAG, "layer7 public key " + ((X509Certificate) ca).getPublicKey());
            }
        }catch (CertificateException e){
            e.printStackTrace();
        }
        finally {
            try {
                caInput.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return ca;
    }

}