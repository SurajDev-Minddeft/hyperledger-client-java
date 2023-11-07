package com.utavi.ledger.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utavi.ledger.api.exceptions.InvalidStateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Utils {

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getName());

	private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final String CHARSET_NAME_UTF8 = "UTF-8";

	private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
	private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

	public static Optional<PrivateKey> getPrivateKeyFromBytes(final byte[] pemBytes) {
		Security.addProvider(PROVIDER);
		final Reader pemReader = new StringReader(new String(pemBytes));
		try (final PEMParser pemParser = new PEMParser(pemReader)) {
			final PrivateKeyInfo pemPair = (PrivateKeyInfo) pemParser.readObject();
			final PrivateKey privateKey = new JcaPEMKeyConverter()
					.setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.getPrivateKey(pemPair);
			return Optional.of(privateKey);
		} catch (final IOException e) {
			LOGGER.error("Failed to convert bytes to PrivateKey", e);
			return Optional.empty();
		}
	}

	public static Optional<String> getCertificateFromBytes(final byte[] certBytes) {
		final String certificate = new String(certBytes, StandardCharsets.UTF_8);
		return Optional.of(certificate);
	}

	public static String toJson(final Object obj) {
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (final JsonProcessingException e) {
			LOGGER.error("Failed to convert to json, Error:", e);
			throw new InvalidStateException("Failed to convert to json");
		}
	}

	public static <T> T fromJson(final byte[] bytes, final Class<T> clazz) {
                return fromJson(IOUtils.toString(bytes, CHARSET_NAME_UTF8), clazz);
	}

	public static <T> T fromJson(final InputStream inputStream, final Class<T> clazz) {
		try {
			return fromJson(IOUtils.toString(inputStream, CHARSET_NAME_UTF8), clazz);
		} catch (final IOException e) {
			LOGGER.error("Failed to convert from json, Error:", e);
			throw new InvalidStateException("Failed to convert from json");
		}
	}

	public static <T> T fromJson(final String json, final Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (final IOException e) {
			LOGGER.error("Failed to convert from json, Error:", e);
			throw new InvalidStateException("Failed to convert from json");
		}
	}

	public static <T> Set<T> fromJson(final String json, final TypeReference<Set<T>> reference) {
		try {
			return OBJECT_MAPPER.readValue(json, reference);
		} catch (final IOException e) {
			LOGGER.error("Failed to convert from json, Error:", e);
			throw new InvalidStateException("Failed to convert from json");
		}
	}

	public static <T> Collector<T, ?, T> toSingleton() {
		return Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.size() != 1) {
						throw new IllegalStateException();
					}
					return list.get(0);
				}
		);
	}

	public static String randomEnrollmentSecret() {
		final String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
		final String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
		final String numbers = RandomStringUtils.randomNumeric(2);
		final String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
		final String totalChars = RandomStringUtils.randomAlphanumeric(2);
		final String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
				.concat(numbers)
				.concat(specialChar)
				.concat(totalChars);
		final List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		Collections.shuffle(pwdChars);
		return pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
	}

	public static String createAccountName() {
		return UUID.randomUUID().toString();
	}

	public static String privateKeyToString(final PrivateKey privateKey) {
		final StringWriter stringWriter = new StringWriter();
		try (final PemWriter writer = new PemWriter(stringWriter)) {
			writer.writeObject(new PemObject("PRIVATE KEY", privateKey.getEncoded()));
			return new String(stringWriter.getBuffer());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toBase64(final String source) {
		return BASE64_ENCODER.encodeToString(source.getBytes());
	}

	public static String toBase64(final byte[] source) {
		return BASE64_ENCODER.encodeToString(source);
	}

	public static byte[] fromBase64(final String str) {
		return BASE64_DECODER.decode(str);
	}

}
