package com.verizontelematics.indrivemobile.utils.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS12PfxPdu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Created by Bijesh on 25-04-2015.
 */
public class PK12Util {

//    public static void generatePK12() {
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//
//            KeyStore credentials = JcaUtils.createCredentials();
//            PrivateKey key = (PrivateKey) credentials.getKey(JcaUtils.END_ENTITY_ALIAS, JcaUtils.KEY_PASSWD);
//            Certificate[] chain = credentials.getCertificateChain(JcaUtils.END_ENTITY_ALIAS);
//
//            createPKCS12File(new FileOutputStream("id.p12"), key, chain);
//
//            //
//            // first do a "blow by blow" read of the PKCS#12 file.
//            //
//            PKCS12PfxPdu pfx = readPKCS12File(new FileInputStream("id.p12"));
//
//            //
//            // or alternately just load it up using a KeyStore
//            //
//            KeyStore pkcs12Store = KeyStore.getInstance("PKCS12", "BC");
//
//            pkcs12Store.load(new FileInputStream("id.p12"), JcaUtils.KEY_PASSWD);
//
//            System.out.println("########## KeyStore Dump");
//
//            for (Enumeration en = pkcs12Store.aliases(); en.hasMoreElements(); ) {
//                String alias = (String) en.nextElement();
//
//                if (pkcs12Store.isCertificateEntry(alias)) {
//                    System.out.println("Certificate Entry: " + alias + ", Subject: " + (((X509Certificate) pkcs12Store.getCertificate(alias)).getSubjectDN()));
//                } else if (pkcs12Store.isKeyEntry(alias)) {
//                    System.out.println("Key Entry: " + alias + ", Subject: " + (((X509Certificate) pkcs12Store.getCertificate(alias)).getSubjectDN()));
//                }
//            }
//
//            System.out.println();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
