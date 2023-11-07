package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.dto.request.balance.get.GetWalletEntryRequest;
import com.utavi.ledger.api.dto.request.balance.getMany.GetWalletsEntriesRequest;
import com.utavi.ledger.api.dto.request.balance.move.MoveBalanceRequest;
import com.utavi.ledger.api.dto.request.credits.CreateCreditsRequest;
import com.utavi.ledger.api.dto.request.history.GetHistoryRequest;
import com.utavi.ledger.api.dto.response.GetWalletEntryResponsePayload;
import com.utavi.ledger.api.dto.response.GetWalletsEntriesResponsePayload;
import com.utavi.ledger.api.dto.response.HistoryResponsePayload;
import com.utavi.ledger.api.service.client.ChaincodeInvokeService;
import com.utavi.ledger.api.util.ChaincodeUtils.ProposalValidationResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/ledger")
public class ChaincodeInvocationController {

	private final ChaincodeInvokeService chaincodeInvokeService;
	public ChaincodeInvocationController(final ChaincodeInvokeService chaincodeInvokeService) {
		this.chaincodeInvokeService = chaincodeInvokeService;
	}

	@PostMapping("/query/entry")
	public ResponseEntity<GetWalletEntryResponsePayload> query(@Valid @RequestBody final GetWalletEntryRequest request) {
		return ResponseEntity.ok(this.chaincodeInvokeService.getEntry(request));
	}

	//todo used by utavi-credits-exchange
	@PostMapping("/invoke/move-balance")
	public ResponseEntity<ProposalValidationResponse> invoke(@Valid @RequestBody final MoveBalanceRequest request) {
		return this.chaincodeInvokeService.moveBalance(request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	//todo used by utavi-credits-exchange
	@PostMapping("/invoke/create-credits")
	public ResponseEntity<ProposalValidationResponse> invoke(@Valid @RequestBody final CreateCreditsRequest request) {
		return this.chaincodeInvokeService.createCredits(request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}

	//todo used by utavi-credits-exchange
	@PostMapping("/query/entries")
	public ResponseEntity<GetWalletsEntriesResponsePayload> invoke(@Valid @RequestBody final GetWalletsEntriesRequest request) {
		return ResponseEntity.ok(this.chaincodeInvokeService.getEntries(request));
	}

	// seems to be not used
	@PostMapping("/query/history")
	public ResponseEntity<HistoryResponsePayload> query(@Valid @RequestBody final GetHistoryRequest request) {
		return ResponseEntity.ok(this.chaincodeInvokeService.getHistory(request));
	}

}
