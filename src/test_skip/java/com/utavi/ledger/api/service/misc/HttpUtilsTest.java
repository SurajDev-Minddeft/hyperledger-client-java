package com.utavi.ledger.api.service.misc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.expect;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtils.class, Charset.class, HttpClients.class, MultipartEntityBuilder.class, EntityUtils.class})
@PowerMockIgnore("javax.net.ssl.*")
public class HttpUtilsTest {

	@InjectMocks
	private HttpUtils httpUtils;

	@Mock
	private HttpClient httpClient;

	@Mock
	private HttpResponse httpResponse;

	private static final String URL = "URL";

	@Test
	public void whenPostPayloadAsString_NoErrors_SuccessfullyPosted() throws Exception {
		final String payload = "payload";
		mockStatic(Charset.class);
		final Charset charset = Mockito.mock(Charset.class, Mockito.RETURNS_DEEP_STUBS);
		final StringEntity stringEntity = createNiceMock(StringEntity.class);
		final HttpPost httpPost = createNiceMock(HttpPost.class);

		when(Charset.forName("UTF-8")).thenReturn(charset);
		expectNew(StringEntity.class, payload, charset).andReturn(stringEntity);
		replay(stringEntity, StringEntity.class);
		expectNew(HttpPost.class, URL).andReturn(httpPost);
		replay(httpPost, HttpPost.class);
		when(this.httpClient.execute(httpPost)).thenReturn(this.httpResponse);

		this.httpUtils.post(URL, payload);

		verifyStatic(Charset.class);
		Charset.forName("UTF-8");
		PowerMock.verify(stringEntity, StringEntity.class);
		PowerMock.verify(httpPost, HttpPost.class);
		verify(this.httpClient).execute(httpPost);
	}

	@Test
	public void whenPostPayloadAsByteArray_NoErrors_SuccessfullyPosted() throws Exception {
		final byte[] payload = new byte[]{};
		final ByteArrayEntity byteArrayEntity = createNiceMock(ByteArrayEntity.class);
		final HttpPost httpPost = createNiceMock(HttpPost.class);

		expectNew(ByteArrayEntity.class, payload).andReturn(byteArrayEntity);
		replay(byteArrayEntity, ByteArrayEntity.class);
		expectNew(HttpPost.class, URL).andReturn(httpPost);
		replay(httpPost, HttpPost.class);
		when(this.httpClient.execute(httpPost)).thenReturn(this.httpResponse);

		this.httpUtils.post(URL, payload);

		PowerMock.verify(byteArrayEntity, ByteArrayEntity.class);
		PowerMock.verify(httpPost, HttpPost.class);
		verify(this.httpClient).execute(httpPost);
	}

	@Test
	public void whenPostPayloadAsMap_NoErrors_SuccessfullyPosted() throws Exception {
		final byte[] payload = new byte[]{};
		final Map<String, byte[]> binaryPayload = new HashMap<>();
		binaryPayload.put("original", payload);
		binaryPayload.put("updated", payload);
		binaryPayload.put("channel", payload);
		mockStatic(MultipartEntityBuilder.class);
		final HttpPost httpPost = createNiceMock(HttpPost.class);
		final MultipartEntityBuilder multipartEntityBuilder = Mockito.mock(MultipartEntityBuilder.class, Mockito.RETURNS_DEEP_STUBS);
		final HttpEntity httpEntity = Mockito.mock(HttpEntity.class, Mockito.RETURNS_DEEP_STUBS);

		when(MultipartEntityBuilder.create()).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.addBinaryBody("original", binaryPayload.get("original"), ContentType.APPLICATION_OCTET_STREAM, "originalFakeFilename")).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.addBinaryBody("updated", binaryPayload.get("updated"), ContentType.APPLICATION_OCTET_STREAM, "updatedFakeFilename")).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.addBinaryBody("channel", binaryPayload.get("channel"))).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.build()).thenReturn(httpEntity);
		expectNew(HttpPost.class, URL).andReturn(httpPost);
		replay(httpPost, HttpPost.class);
		when(this.httpClient.execute(httpPost)).thenReturn(this.httpResponse);

		this.httpUtils.post(URL, binaryPayload);

		PowerMock.verify(httpPost, HttpPost.class);
		verifyStatic(MultipartEntityBuilder.class);
		MultipartEntityBuilder.create();
		verify(this.httpClient).execute(httpPost);
	}

	@Test
	public void whenPostWithBinaryAndMetaData_NoErrors_SuccessfullyPosted() throws Exception {
		final byte[] binary = new byte[]{};
		final String metaData = "metaData";
		mockStatic(MultipartEntityBuilder.class);
		final HttpPost httpPost = createNiceMock(HttpPost.class);
		final MultipartEntityBuilder multipartEntityBuilder = Mockito.mock(MultipartEntityBuilder.class, Mockito.RETURNS_DEEP_STUBS);
		final HttpEntity httpEntity = Mockito.mock(HttpEntity.class, Mockito.RETURNS_DEEP_STUBS);

		when(MultipartEntityBuilder.create()).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.addBinaryBody("uploadedChaincode", binary, ContentType.APPLICATION_OCTET_STREAM, "chaincode.tar.gz")).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.addTextBody("metaData", metaData, ContentType.APPLICATION_JSON)).thenReturn(multipartEntityBuilder);
		when(multipartEntityBuilder.build()).thenReturn(httpEntity);
		expectNew(HttpPost.class, URL).andReturn(httpPost);
		replay(httpPost, HttpPost.class);
		when(this.httpClient.execute(httpPost)).thenReturn(this.httpResponse);

		this.httpUtils.post(URL, binary, metaData);

		PowerMock.verify(httpPost, HttpPost.class);
		verifyStatic(MultipartEntityBuilder.class);
		MultipartEntityBuilder.create();
		verify(this.httpClient).execute(httpPost);
	}

	@Test
	public void whenHttpResponseToString_NoErrors_ReturnString() throws IOException {
		final HttpResponse httpResponse = Mockito.mock(HttpResponse.class, Mockito.RETURNS_DEEP_STUBS);
		final HttpEntity httpEntity = Mockito.mock(HttpEntity.class, Mockito.RETURNS_DEEP_STUBS);
		mockStatic(EntityUtils.class);
		final String expectedHttpEntity = "httpEntityString";

		when(httpResponse.getEntity()).thenReturn(httpEntity);
		when(EntityUtils.toString(httpEntity)).thenReturn("httpEntityString");

		final String actualHttpEntity = this.httpUtils.httpResponseToString(httpResponse);

		verify(httpResponse).getEntity();
		verifyStatic(EntityUtils.class);
		EntityUtils.toString(httpEntity);

		assertThat(actualHttpEntity).isEqualTo(expectedHttpEntity);
	}

	@Test
	public void whenHttpResponseToByte_NoErrors_ReturnByte() throws IOException {
		final HttpResponse httpResponse = Mockito.mock(HttpResponse.class, Mockito.RETURNS_DEEP_STUBS);
		final HttpEntity httpEntity = Mockito.mock(HttpEntity.class, Mockito.RETURNS_DEEP_STUBS);
		mockStatic(EntityUtils.class);
		final byte[] expectedHttpEntity = new byte[]{};

		when(httpResponse.getEntity()).thenReturn(httpEntity);
		when(EntityUtils.toByteArray(httpEntity)).thenReturn(expectedHttpEntity);

		final byte[] actualHttpEntity = this.httpUtils.httpResponseToBytes(httpResponse);

		verify(httpResponse).getEntity();
		verifyStatic(EntityUtils.class);
		EntityUtils.toByteArray(httpEntity);

		assertThat(actualHttpEntity).isEqualTo(expectedHttpEntity);
	}

	@Test
	public void whenGet_NoErrors_SuccessfullyGet() throws Exception {
		final Map<String, String> params = new HashMap<>();
		params.put("string", "string");
		final BasicNameValuePair basicNameValuePair = createNiceMock(BasicNameValuePair.class);
		final URIBuilder uriBuilder = createNiceMock(URIBuilder.class);
		final List<NameValuePair> paramsList = new ArrayList<>();
		paramsList.add(basicNameValuePair);
		final URI uri = createMock(URI.class);
		final HttpGet httpGet = createNiceMock(HttpGet.class);

		expectNew(BasicNameValuePair.class, "string", "string").andReturn(basicNameValuePair);
		replay(basicNameValuePair, BasicNameValuePair.class);
		expectNew(URIBuilder.class, URL).andReturn(uriBuilder);
		expect(uriBuilder.addParameters(paramsList)).andReturn(uriBuilder);
		expect(uriBuilder.build()).andReturn(uri);
		replay(uriBuilder, URIBuilder.class);
		expectNew(HttpGet.class, uri).andReturn(httpGet);
		replay(httpGet, HttpGet.class);
		when(this.httpClient.execute(httpGet)).thenReturn(this.httpResponse);

		this.httpUtils.get(URL, params);

		PowerMock.verify(basicNameValuePair, BasicNameValuePair.class);
		PowerMock.verify(uriBuilder, URIBuilder.class);
		PowerMock.verify(httpGet, HttpGet.class);
		verify(this.httpClient).execute(httpGet);
	}

}
