/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.its.bugzillaonline;

import static com.google.common.collect.Sets.newSetFromMap;
import static org.apache.xmlbeans.impl.common.XMLChar.isInvalid;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;

import com.google.common.base.Joiner;

/**
 * 
 * @author Michał Negacz, Wrocław University of Technology
 * 
 */
public class XmlRpcTransportFactoryWithCookies extends XmlRpcSunHttpTransportFactory {

    private Set<HttpCookie> cookies;

    public XmlRpcTransportFactoryWithCookies(XmlRpcClient client) {
        super(client);
        cookies = newSetFromMap(new ConcurrentHashMap<HttpCookie, Boolean>());
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
        protected void initHttpHeaders(XmlRpcRequest request) throws XmlRpcClientException {
            super.initHttpHeaders(request);
            if (!cookies.isEmpty()) {
                addCookiesToRequest();
            }
        }

        private void addCookiesToRequest() {
            setRequestHeader(REQUEST_COOKIE_HEADER, Joiner.on(REQUEST_COOKIE_DELIMITER).join(cookies));
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

        @Override
        protected InputStream getInputStream() throws XmlRpcException {
            return new XmlInvalidCharacterFilterInputStream(super.getInputStream());
        }

    }

    private class XmlInvalidCharacterFilterInputStream extends FilterInputStream {

        private static final int BYTE_MASK = 0xFF;

        private static final byte SPACE = ' ';

        protected XmlInvalidCharacterFilterInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            int numberOfBytesRead = super.read(buffer, offset, length);
            filterInvalidCharacterOutOfTheBuffer(buffer, numberOfBytesRead);
            return numberOfBytesRead;
        }

        private void filterInvalidCharacterOutOfTheBuffer(byte[] buffer, int numberOfBytes) {
            for (int byteNo = 0; byteNo < numberOfBytes; byteNo++) {
                if (isAnInvalidCharacterInXML(buffer[byteNo])) {
                    buffer[byteNo] = SPACE;
                }
            }
        }

        private boolean isAnInvalidCharacterInXML(byte value) {
            return isInvalid(convertByteToInt(value));
        }

        private int convertByteToInt(byte value) {
            return value & BYTE_MASK;
        }

    }

}
