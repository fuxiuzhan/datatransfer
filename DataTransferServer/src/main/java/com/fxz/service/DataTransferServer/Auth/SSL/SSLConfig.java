package com.fxz.service.DataTransferServer.Auth.SSL;

import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLConfig {
	public static SSLContext getSSLContext(String jksPath) {
		try {
			/*
			 * 
			 */
			KeyStore ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream(jksPath), "fuxiuzhan".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ts);
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(jksPath), "fuxiuzhan".toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, "fuxiuzhan".toCharArray());
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			return sslContext;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
