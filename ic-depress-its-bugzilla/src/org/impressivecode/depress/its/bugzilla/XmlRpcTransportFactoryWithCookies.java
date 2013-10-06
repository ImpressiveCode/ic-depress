package org.impressivecode.depress.its.bugzilla;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * 
 * @author Micha³ Negacz
 * 
 */
public class XmlRpcTransportFactoryWithCookies extends XmlRpcSunHttpTransportFactory {

	private Set<HttpCookie> cookies;

	public XmlRpcTransportFactoryWithCookies(XmlRpcClient client) {
		super(client);
		cookies = Sets.newHashSet();
	}

	@Override
	public XmlRpcTransport getTransport() {
		return new XmlRpcTransportWithCookies(getClient());
	}

	private class XmlRpcTransportWithCookies extends XmlRpcSunHttpTransport {

		private static final String RESPONSE_COOKIE_HEADER = "Set-Cookie";

		private static final String REQUEST_COOKIE_HEADER = "Cookie";

		private static final String REQUEST_COOKIE_DELIMITER = "; ";

		private URLConnection connection;

		public XmlRpcTransportWithCookies(XmlRpcClient client) {
			super(client);
		}

		@Override
		protected void initHttpHeaders(XmlRpcRequest pRequest) throws XmlRpcClientException {
			super.initHttpHeaders(pRequest);
			if (!cookies.isEmpty()) {
				setRequestHeader(REQUEST_COOKIE_HEADER, Joiner.on(REQUEST_COOKIE_DELIMITER).join(cookies));
			}
		}

		@Override
		protected URLConnection newURLConnection(URL url) throws IOException {
			connection = super.newURLConnection(url);
			return connection;
		}

		@Override
		protected void close() throws XmlRpcClientException {
			retrieveAndStoreCookiesFromConnection();
			super.close();
		}

		private void retrieveAndStoreCookiesFromConnection() {
			Map<String, List<String>> headers = connection.getHeaderFields();

			if (headers.containsKey(RESPONSE_COOKIE_HEADER)) {
				List<String> cookiesHeader = headers.get(RESPONSE_COOKIE_HEADER);

				for (String cookieHeader : cookiesHeader) {
					cookies.addAll(HttpCookie.parse(cookieHeader));
				}
			}
		}

	}

}
