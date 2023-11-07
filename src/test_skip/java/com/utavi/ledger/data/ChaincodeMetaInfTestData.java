package com.utavi.ledger.data;

import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto;
import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto.Type;
import com.utavi.ledger.api.model.ChaincodeInfo;

public final class ChaincodeMetaInfTestData {

	public static ChaincodeInfo createChaincodeInfo() {
		return new ChaincodeInfo(createChaincodeMetaInf());
	}

	public static ChaincodeMetaInfDto createChaincodeMetaInf() {
		final ChaincodeMetaInfDto chaincodeMetaInf = new ChaincodeMetaInfDto();
		chaincodeMetaInf.setLang(Type.GO_LANG);
		chaincodeMetaInf.setName("common");
		chaincodeMetaInf.setPath("some/path");
		chaincodeMetaInf.setVersion("1.0");
		return chaincodeMetaInf;
	}

}
