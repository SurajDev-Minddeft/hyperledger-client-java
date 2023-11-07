package com.utavi.ledger.api.service.misc;

import static com.utavi.ledger.api.util.Utils.fromBase64;
import static com.utavi.ledger.api.util.Utils.toBase64;

import com.utavi.ledger.api.exceptions.DecryptionException;
import com.utavi.ledger.api.exceptions.EncryptionException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtils.class.getName());

	private final byte[] secretKeyBytes;
	private static final String ALGORITHM_AES = "AES";

	public CryptoUtils(@Value("${crypt.secretkey}") final String secretKey) {
		this.secretKeyBytes = secretKey.getBytes();
	}

	public String encrypt(final String data) {
		final Key key = new SecretKeySpec(this.secretKeyBytes, ALGORITHM_AES);
		try {
			final Cipher c = Cipher.getInstance(ALGORITHM_AES);
			c.init(Cipher.ENCRYPT_MODE, key);
			final byte[] encVal = c.doFinal(data.getBytes());
			return toBase64(encVal);
		} catch (final InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
			LOGGER.error("Failed to encrypt data, Error:", e);
			throw new EncryptionException("Failed to encrypt data", e);
		}
	}

	public String decrypt(final String encryptedData) {
		final Key key = new SecretKeySpec(this.secretKeyBytes, ALGORITHM_AES);
		try {
			final Cipher c = Cipher.getInstance(ALGORITHM_AES);
			c.init(Cipher.DECRYPT_MODE, key);
			final byte[] decodedValue = fromBase64(encryptedData);
			final byte[] decValue = c.doFinal(decodedValue);
			return new String(decValue);
		} catch (final InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
			LOGGER.error("Failed to decrypt data, Error:", e);
			throw new DecryptionException("Failed to decrypt data", e);
		}
	}

}
