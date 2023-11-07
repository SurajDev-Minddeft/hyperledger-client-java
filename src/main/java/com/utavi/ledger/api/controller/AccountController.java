package com.utavi.ledger.api.controller;

import com.utavi.ledger.api.dto.AccountInfoDto;
import com.utavi.ledger.api.dto.JoinSiloDto;
import com.utavi.ledger.api.dto.request.silo.LeaveSiloDto;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.service.client.AccountService;
import com.utavi.ledger.api.service.client.CAIdentityService;
import com.utavi.ledger.api.service.client.MembershipManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/v1/account")
public class AccountController {

	private final AccountService accountService;
	private final MembershipManager membershipManager;
	private final CAIdentityService caIdentityService;

	public AccountController(final AccountService accountService, final MembershipManager membershipManager,
			final CAIdentityService caIdentityService) {
		this.accountService = accountService;
		this.membershipManager = membershipManager;
		this.caIdentityService = caIdentityService;
	}

	@GetMapping("/{accountName}")
	public ResponseEntity<AccountInfoDto> find(@PathVariable final String accountName) {
		return ResponseEntity.ok(this.accountService.findWithCAInfo(accountName));
	}

	@GetMapping
	public ResponseEntity<Set<AccountInfoDto>> find(@RequestParam(value = "accountType", required = false) final AccountType accountType) {
		final Set<AccountInfoDto> accountInfoDtos = Optional.ofNullable(accountType).map(this.accountService::findAccounts)
				.orElseGet(this.accountService::findAccounts);
		return ResponseEntity.ok(accountInfoDtos);
	}

	//TODO wujek - uzywane przez utavi-user-service. tworzy nowe konto w CA z tym zapisem w certyfikacie
	@PostMapping("/join")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void join(@Valid @RequestBody final JoinSiloDto dto) {
		this.membershipManager.join(dto);
	}

	//TODO wujek - uzywane przez utavi-user-service. de facto też tworzy nowe konto w CA z tym zapisem w certyfikacie ze opuscil silosa - czyli wywala typa z silosa
	@PostMapping("/leave")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void leave(@Valid @RequestBody final LeaveSiloDto dto) {
		this.membershipManager.leave(dto);
	}

	//TODO wujek - uzywane przez utavi-user-service. w zasadzie calkowicie wycofuje usera z sieci
	@PutMapping("/revoke/{name}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void revoke(@PathVariable final String name) {
		this.caIdentityService.revoke(name);
	}

//	@PutMapping("/re-enroll/{name}")
//	@ResponseStatus(HttpStatus.ACCEPTED)
//	public void reEnroll(@PathVariable final String name, @RequestParam(value = "orgType") final OrgType orgType) {
//		this.caIdentityService.reEnroll(name, orgType);
//	}

//	@PutMapping("/re-enroll")
//	@ResponseStatus(HttpStatus.ACCEPTED)
//	public void reEnrollAll(@RequestParam(value = "orgType") final OrgType orgType) {
//		this.caIdentityService.reEnrollAll();
//	}

//	@PutMapping("/enroll/{name}")
//	@ResponseStatus(HttpStatus.ACCEPTED)
//	public void enroll(@PathVariable final String name, @RequestParam(value = "orgType") final OrgType orgType) {
//		this.caIdentityService.enroll(name, orgType);
//	}


	//nieuzwane chyba
//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public void create(@Valid @RequestBody final CreateAccount dto) {
//		this.accountService.create(dto);
//	}

//	@PutMapping("/admin/{name}")
//	@ResponseStatus(HttpStatus.ACCEPTED)
//	public void publishAdminCerts(@PathVariable final String name, @RequestParam(value = "orgType") final OrgType orgType) {
//		this.accountService.addAdminToMsp(name, orgType);
//	}

//	@PutMapping("/admin/{orgType}/reload")
//	@ResponseStatus(HttpStatus.ACCEPTED)
//	public void reloadAdminCerts(@PathVariable final OrgType orgType) {
////		this.networkConfigManager.writeAdmins(orgType);
//	}

	@DeleteMapping("/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAccount(@PathVariable final String name) {
		this.accountService.delete(name);
	}

//	@PutMapping("/{name}/password")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void changePassword(@PathVariable final String name, @Valid @RequestBody final ChangePasswordRequest request) {
//		this.accountService.changeCaPassword(name, request);
//	}

}
