package com.utavi.ledger.api.model;

import com.utavi.ledger.api.model.accounts.LocalEnrollment;
import com.utavi.ledger.api.service.misc.CryptoUtils;
import com.utavi.ledger.api.service.misc.SerializationUtils;
import java.time.Instant;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.stereotype.Component;

@Component
public class AccountEntityListener {

	private final CryptoUtils cryptoUtils;
	private final SerializationUtils serializationUtils;

	public AccountEntityListener(final CryptoUtils cryptoUtils, final SerializationUtils serializationUtils) {
		this.cryptoUtils = cryptoUtils;
		this.serializationUtils = serializationUtils;
	}

	@PostLoad
	public void decryptAndDeserializeEnrollment(final Account account) {
		final String encryptedEnrollment = account.getEncryptedEnrollment();
		if (encryptedEnrollment != null) {
			final String hexEnrollment = this.cryptoUtils.decrypt(encryptedEnrollment);
			final LocalEnrollment enrollment = this.serializationUtils.deserialize(hexEnrollment, LocalEnrollment.class);
			account.setLocalEnrollment(enrollment);
			final String enrollmentSecret = account.getEncryptedEnrollmentSecret();
			final String decryptedSecret = this.cryptoUtils.decrypt(enrollmentSecret);
			account.setEnrollmentSecret(decryptedSecret);
		}
	}

	@PrePersist
	@PreUpdate
	public void encryptAndSerializeEnrollment(final Account account) {
		prePersistEnrollmentAction(account);
		prePersistSecretAction(account);
		account.setUpdateDate(Instant.now());
	}

	private void prePersistEnrollmentAction(final Account account) {
		final LocalEnrollment localEnrollment = account.getLocalEnrollment();
		if (localEnrollment != null) {
			final String hexEnrollment = this.serializationUtils.serializeAndHex(localEnrollment, LocalEnrollment.class);
			final String encryptedEnrollment = this.cryptoUtils.encrypt(hexEnrollment);
			account.setEncryptedEnrollment(encryptedEnrollment);
		}
	}

	private void prePersistSecretAction(final Account account) {
		final String enrollmentSecret = account.getEnrollmentSecret();
		final String encryptedSecret = this.cryptoUtils.encrypt(enrollmentSecret);
		account.setEncryptedEnrollmentSecret(encryptedSecret);
	}

}
