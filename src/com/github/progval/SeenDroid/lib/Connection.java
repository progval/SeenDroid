package com.github.progval.SeenDroid.lib;

import java.io.IOException;
import java.net.ProxySelector;


import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Base64;

public class Connection {
	private String username, password;
	private String base_api_url = "https://seenthis.net/api";
	//private String base_api_url = "http://192.168.1.150:12345/api";

	
	public Connection(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse query(HttpGet message) throws ClientProtocolException, IOException {
		// SSL fixes (javax.net.ssl.SSLPeerUnverifiedException: No peer certificate)
		// From http://www.virtualzone.de/2011-02-27/how-to-use-apache-httpclient-with-httpsssl-on-android/
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		 
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
		
		// Real code:
		DefaultHttpClient client = new DefaultHttpClient(cm, params);
		
		HttpResponse response = client.execute(message);
		return response;
	}
	/**
	 * 
	 * @param uri Relative path for the request; starts with a /.
	 * @return A HTTP post suitable for Seenthis requests.
	 */
	public HttpPost getHttpPost(String uri) {
		HttpPost post = new HttpPost(this.base_api_url + uri);
		this.addHeaders(post);
		post.addHeader("Content-type", "application/atom+xml;type=entry");
		return post;
	}
	public HttpGet getHttpGet(String uri) {
		HttpGet get = new HttpGet(this.base_api_url + uri);
		this.addHeaders(get);
		return get;
	}
	private void addHeaders(HttpMessage message) {
		String credentials = Base64.encodeToString((this.username + ":" + this.password).getBytes(),Base64.DEFAULT);
		message.addHeader("User-agent", "SeenDroid");
		message.addHeader("Authorization", "Basic " + credentials.substring(0, credentials.length() - 1));
	} 
}
