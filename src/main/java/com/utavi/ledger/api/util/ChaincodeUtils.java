package com.utavi.ledger.api.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.utavi.ledger.api.exceptions.ProposalExceptionBadRequest;
import com.utavi.ledger.api.exceptions.ProposalExceptionInternalServerError;
import com.utavi.ledger.api.exceptions.ProposalExceptionNotFound;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChaincodeUtils {

	private final static Logger LOGGER = LoggerFactory.getLogger(ChaincodeUtils.class.getName());
	private static final int NOT_FOUND = 404;
	private static final int BAD_REQUEST = 400;

	public static Optional<ProposalValidationResponse> prepareProposalValidationResponse(final Collection<ProposalResponse> proposalResponses) {
		LOGGER.info("Validating proposal responses");
		final Predicate<ProposalResponse> statusSuccess = proposalResponse -> proposalResponse.getStatus() == Status.SUCCESS;
		if (!proposalResponses.stream().allMatch(statusSuccess)) {
			final Collection<ProposalResponse> failedProposalResponses = collectFailedResponses(proposalResponses, statusSuccess);
			final String message = createExceptionMessage(failedProposalResponses);
			isBadRequest(failedProposalResponses, message);
			isNotFound(failedProposalResponses, message);
			LOGGER.error("Validation failed, msg={}", message);
			throw new ProposalExceptionInternalServerError(message);
		}
		LOGGER.info("Successfully validated proposal responses");
		return proposalResponses.stream().map(ProposalValidationResponse::new).distinct().findFirst();
	}

	private static Collection<ProposalResponse> collectFailedResponses(final Collection<ProposalResponse> proposalResponses,
			final Predicate<ProposalResponse> statusSuccess) {
		return proposalResponses.stream().filter(statusSuccess.negate())
				.collect(Collectors.toList());
	}

	private static String createExceptionMessage(final Collection<ProposalResponse> failedProposalResponses) {
		return failedProposalResponses.stream().map(proposalResponse ->
				String.format("Peer:%s, Error message:%s", proposalResponse.getPeer().getName(), proposalResponse.getMessage()))
				.collect(Collectors.joining(", "));
	}

	private static void isNotFound(final Collection<ProposalResponse> failedProposalResponses,
			final String message) {
		if (failedProposalResponses.stream().allMatch(proposalResponse -> getResponseStatus(proposalResponse) == NOT_FOUND)) {
			LOGGER.error("Failed to validate ProposalResponses");
			throw new ProposalExceptionNotFound(message);
		}
	}

	private static void isBadRequest(final Collection<ProposalResponse> failedProposalResponses,
			final String message) {
		if (failedProposalResponses.stream().allMatch(proposalResponse -> getResponseStatus(proposalResponse) == BAD_REQUEST)) {
			LOGGER.error("Failed to validate ProposalResponses, Error: {}", message);
			throw new ProposalExceptionBadRequest(message);
		}
	}

	private static int getResponseStatus(final ProposalResponse proposalResponse) {
		try {
			return proposalResponse.getChaincodeActionResponseStatus();
		} catch (final InvalidArgumentException e) {
			LOGGER.error("Failed to extract chaincode action response status, Error:", e);
			return -1;
		}
	}

	public static class ProposalValidationResponse {

		@JsonProperty("transactionId")
		private final String transactionId;

		@JsonProperty("isValid")
		private final boolean valid;

		@JsonProperty("status")
		private final int status;

		@JsonProperty("message")
		private final String message;

		public ProposalValidationResponse(final ProposalResponse proposalResponse) {
			this.transactionId = proposalResponse.getTransactionID();
			this.valid = !proposalResponse.isInvalid();
			this.status = getResponseStatus(proposalResponse);
			this.message = proposalResponse.getMessage();
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			final ProposalValidationResponse that = (ProposalValidationResponse) o;
			return this.valid == that.valid &&
					this.status == that.status &&
					Objects.equals(this.transactionId, that.transactionId) &&
					Objects.equals(this.message, that.message);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.transactionId, this.valid, this.status, this.message);
		}
	}

}
