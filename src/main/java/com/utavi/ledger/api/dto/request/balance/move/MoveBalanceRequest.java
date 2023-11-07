package com.utavi.ledger.api.dto.request.balance.move;

import com.utavi.ledger.api.dto.request.ChaincodeRequest;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MoveBalanceRequest extends ChaincodeRequest {

	public enum TxType {
		POST_OFFER, CANCEL_OFFER, BUY, BUY_ESCROW, EXCHANGE, TRANSFER, AWARD, REDEEM, PAY, OFFER_EXPIRE, CREDIT_EXPIRE,
		REWIND, ESCROW_RELEASE, ESCROW_RETURN
	}

	@NotNull
	private TxType txType;

	@NotEmpty
	private List<MoveBalancePayload> payload;

	@Override
	public List<MoveBalancePayload> getPayload() {
		return this.payload;
	}

	public void setPayload(final List<MoveBalancePayload> payload) {
		this.payload = payload;
	}

	public TxType getTxType() {
		return this.txType;
	}

	public void setTxType(final TxType txType) {
		this.txType = txType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("payload", this.payload)
				.append("txType", this.txType)
				.append("siloName", getSiloName())
				.append("accountId", getAccountId())
				.toString();
	}
}
