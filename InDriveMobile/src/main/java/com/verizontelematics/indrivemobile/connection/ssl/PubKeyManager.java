package com.verizontelematics.indrivemobile.connection.ssl;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class PubKeyManager implements X509TrustManager {

	// DER encoded public key
//	private static String PUB_KEY = "30820122300d06092a864886f70d0101"
//			+ "0105000382010f003082010a0282010100b35ea8adaf4cb6db86068a836f3c85"
//			+ "5a545b1f0cc8afb19e38213bac4d55c3f2f19df6dee82ead67f70a990131b6bc"
//			+ "ac1a9116acc883862f00593199df19ce027c8eaaae8e3121f7f329219464e657"
//			+ "2cbf66e8e229eac2992dd795c4f23df0fe72b6ceef457eba0b9029619e0395b8"
//			+ "609851849dd6214589a2ceba4f7a7dcceb7ab2a6b60c27c69317bd7ab2135f50"
//			+ "c6317e5dbfb9d1e55936e4109b7b911450c746fe0d5d07165b6b23ada7700b00"
//			+ "33238c858ad179a82459c4718019c111b4ef7be53e5972e06ca68a112406da38"
//			+ "cf60d2f4fda4d1cd52f1da9fd6104d91a34455cd7b328b02525320a35253147b"
//			+ "e0b7a5bc860966dc84f10d723ce7eed5430203010001";

    private static final String PUB_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a028201010097c90a707f5a5044feddffd1570986bcbc881317e4c87ac184db4691ce23bcf45da283cda1372ac9e75faafa53dce5fa1ab7467b75c64f9c2ba0f53d64026e907ea0fc9a317bd398393657fe483b21797615f16cf554f851a52bf073a9265abc6132f4fe6f8dda6bdc9dec985d1644f80ddac5ef6a9996f016875ae08cb7d9fb2f094660d28ca1a32e9f6e5e4b567747806fbc93a9d7489fb9e4d9c3a1fbfc747d819747af017654b51652ac92b7f9e5d1b980dcb7a8ebdacd460d69c712d0c93cbe9e7d1bd71b8f7ad11cf768d279fbc5b7ad842619dce3c76fc1c1bde1b6ee6035abf1cedef7c32ec749b290de157d220efd0a6235db9a9c183fb9cb50a73f0203010001";

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

		assert (chain != null);
		if (chain == null) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate array is null");
		}

		assert (chain.length > 0);
		if (!(chain.length > 0)) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate is empty");
		}

		assert (null != authType && authType.equalsIgnoreCase("RSA"));
		if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
			throw new CertificateException(
					"checkServerTrusted: AuthType is not RSA");
		}

		// Perform customary SSL/TLS checks
		TrustManagerFactory tmf;
		try {
			tmf = TrustManagerFactory.getInstance("X509");
			tmf.init((KeyStore) null);

			for (TrustManager trustManager : tmf.getTrustManagers()) {
				((X509TrustManager) trustManager).checkServerTrusted(
						chain, authType);
			}

		} catch (Exception e) {
			throw new CertificateException(e);
		}

		// Hack ahead: BigInteger and toString(). We know a DER encoded Public
		// Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
		// no leading 0x00 to drop.
		RSAPublicKey publicKey = (RSAPublicKey) chain[0].getPublicKey();
		String encoded = new BigInteger(1 /* positive */, publicKey.getEncoded())
				.toString(16);

		// Pin it!
		final boolean expected = PUB_KEY.equalsIgnoreCase(encoded);
		assert(expected);
		if (!expected) {
			throw new CertificateException(
					"checkServerTrusted: Expected public key: " + PUB_KEY
							+ ", got public key:" + encoded);
		}
	}

	public void checkClientTrusted(X509Certificate[] xcs, String string) {
		// throw new
		// UnsupportedOperationException("checkClientTrusted: Not supported yet.");
	}

	public X509Certificate[] getAcceptedIssuers() {
		// throw new
		// UnsupportedOperationException("getAcceptedIssuers: Not supported yet.");
		return null;
	}
}
