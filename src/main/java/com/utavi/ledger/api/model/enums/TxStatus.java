package com.utavi.ledger.api.model.enums;

public enum  TxStatus {

	COMMITTED, REJECTED;

	public static TxStatus fromBoolean(final boolean valid) {
		return valid ? COMMITTED : REJECTED;
	}

}
