package com.utavi.ledger.api.dto;

public enum ChaincodeDataType implements ChaincodeDataTypeDescriptor {
	CHAINCODE {
		public String typeString() {
			return this.name().toLowerCase();
		}
	}, INFO {
		public String typeString() {
			return this.name().toLowerCase();
		}
	}
}

interface ChaincodeDataTypeDescriptor {
	String typeString();
}

