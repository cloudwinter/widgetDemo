package com.summer.network.ssl;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by xiayundong on 2017/10/18.
 */

public class TrustAllSSLSocketFactoryUtil {

	/**
	 * 信任所有https证书
	 * 
	 * @return
	 */
	public static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactory factory = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new TrustAllCerts() },
					new SecureRandom());
			factory = sslContext.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return factory;

	}
}
