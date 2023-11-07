package com.utavi.ledger.api.service.misc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class.getName());

	private final HttpClient httpClient;

	public HttpUtils(final HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpResponse post(final String url, final String payload) {
		final StringEntity httpEntity = new StringEntity(payload, Charset.forName("UTF-8"));
		httpEntity.setContentType(ContentType.APPLICATION_JSON.toString());
		return doPost(url, httpEntity);
	}

	public HttpResponse post(final String url, final byte[] payload) {
		return doPost(url, new ByteArrayEntity(payload));
	}

	public HttpResponse post(final String url, final Map<String, byte[]> binaryPayload) {
		final HttpEntity multipartEntity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addBinaryBody("original", binaryPayload.get("original"), ContentType.APPLICATION_OCTET_STREAM, "originalFakeFilename")
				.addBinaryBody("updated", binaryPayload.get("updated"), ContentType.APPLICATION_OCTET_STREAM, "updatedFakeFilename")
				.addBinaryBody("channel", binaryPayload.get("channel")).build();
		return doPost(url, multipartEntity);
	}

	public HttpResponse post(final String url, final byte[] binary, final String metaData) {
		final HttpEntity multipartEntity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addBinaryBody("uploadedChaincode", binary, ContentType.APPLICATION_OCTET_STREAM, "chaincode.tar.gz")
				.addTextBody("metaData", metaData, ContentType.APPLICATION_JSON)
				.build();
		return doPost(url, multipartEntity);
	}

	public String httpResponseToString(final HttpResponse httpResponse) {
		LOGGER.info("Converting http response to String");
		final HttpEntity entity = httpResponse.getEntity();
		try {
			final String httpResponseString = EntityUtils.toString(entity);
			LOGGER.info("Converted http response to String");
			return httpResponseString;
		} catch (final IOException e) {
			throw new RuntimeException("Failed httpResponse to String");
		}
	}

	public byte[] httpResponseToBytes(final HttpResponse httpResponse) {
		LOGGER.info("Converting http response to Bytes");
		final HttpEntity entity = httpResponse.getEntity();
		try {
			final byte[] httpResponseByte = EntityUtils.toByteArray(entity);
			LOGGER.info("Successfully converted http response to String");
			return httpResponseByte;
		} catch (final IOException e) {
			throw new RuntimeException("Failed to convert http response to Byte", e);
		}
	}

	public HttpResponse get(final String url, final Map<String, String> params) {
		final List<NameValuePair> paramsList = new ArrayList<>();
		params.forEach((k, v) -> paramsList.add(new BasicNameValuePair(k, v)));
		try {
			final URIBuilder uriBuilder = new URIBuilder(url).addParameters(paramsList);
			return doGet(uriBuilder.build());
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private HttpResponse doPost(final String url, final HttpEntity httpEntity) {
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(httpEntity);
		return execute(httpPost);
	}

	private HttpResponse doGet(final URI uri) {
		final HttpGet httpGet = new HttpGet(uri);
		return execute(httpGet);
	}

	private HttpResponse execute(final HttpRequestBase requestBase) {
		try {
			return this.httpClient.execute(requestBase);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

}