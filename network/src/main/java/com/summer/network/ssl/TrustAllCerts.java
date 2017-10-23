package com.summer.network.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 信任所有https证书<br>
 * Created by xiayundong on 2017/10/18.
 */

public class TrustAllCerts implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
			throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}
