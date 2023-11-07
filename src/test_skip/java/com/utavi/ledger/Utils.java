package com.utavi.ledger;

import com.utavi.ledger.api.dto.JoinSiloDto;
import com.utavi.ledger.api.dto.request.silo.LeaveSiloDto;
import com.utavi.ledger.api.model.enums.AccountType;

final class Utils {

	static JoinSiloDto createJoinSiloDto(final String siloName, final String accountId, final AccountType accountType) {
		final JoinSiloDto dto = new JoinSiloDto();
		dto.setSiloName(siloName);
		dto.setAccountId(accountId);
		dto.setAccountType(accountType);
		return dto;
	}

	static LeaveSiloDto createLeaveSiloDto(final String siloName, final String accountId) {
		final LeaveSiloDto dto = new LeaveSiloDto();
		dto.setSiloName(siloName);
		dto.setAccountId(accountId);
		return dto;
	}

}
