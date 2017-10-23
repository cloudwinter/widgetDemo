package com.summer.network.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 信任所有https证书<br>
 * Created by xiayundong on 2017/10/18.
 */

public class TrustAllHostnameVerifier implements HostnameVerifier {
	@Override
	public boolean verify(String s, SSLSession sslSession) {
		return true;
	}
}
