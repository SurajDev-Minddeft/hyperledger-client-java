package com.utavi.ledger.api.service.client;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import com.utavi.ledger.api.dto.JoinSiloDto;
import com.utavi.ledger.api.dto.MembershipStatus;
import com.utavi.ledger.api.dto.UpdateCAAttributesRequest;
import com.utavi.ledger.api.dto.request.silo.LeaveSiloDto;
import com.utavi.ledger.api.model.Account;
import com.utavi.ledger.api.model.BaseEntity;
import com.utavi.ledger.api.model.accounts.LocalEnrollment;
import com.utavi.ledger.api.model.enums.AccountType;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.network.ca.HFCAServerService;
import com.utavi.ledger.api.service.network.channel.ChannelDetailsService;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MembershipManager {

	private final static Logger LOGGER = LoggerFactory.getLogger(MembershipManager.class.getName());

	private final HFCAServerService hfcaServerService;
	private final AccountService accountService;
	private final ChannelDetailsService channelDetailsService;
	private final CAIdentityService caIdentityService;

	public MembershipManager(final HFCAServerService hfcaServerService, final AccountService accountService,
							 final ChannelDetailsService channelDetailsService, final CAIdentityService caIdentityService) {
		this.hfcaServerService = hfcaServerService;
		this.accountService = accountService;
		this.channelDetailsService = channelDetailsService;
		this.caIdentityService = caIdentityService;
	}

	public void join(final JoinSiloDto dto) {
		final String accountId = dto.getAccountId();
		final AccountType accountType = dto.getAccountType();
		final String siloName = dto.getSiloName();
		LOGGER.info("Joining the Account={} to {}", accountId, siloName);
		final Account account = this.accountService.find(accountId)
				.orElseGet(() -> this.accountService.newAccount(accountId, accountType));
		//todo tutaj chyba trzeba dodac na nowo isActive
		updateMembership(account, siloName, MembershipStatus.APPROVED);
	}

	public void leave(final LeaveSiloDto dto) {
		final String accountName = dto.getAccountId();
		final String siloName = dto.getSiloName();
		LOGGER.info("Received request to leave silo {} from account {}", siloName, accountName);
		final Account account = this.accountService.findOrgByType(accountName);
		updateMembership(account, siloName, MembershipStatus.INACTIVE);
//		this.caIdentityService.revoke(account, siloName);
	}

	private void updateMembership(final Account account, final String siloName, final MembershipStatus membershipStatus) {
		final String enrollmentId = account.getName();
		this.channelDetailsService.findBySiloName(siloName).map(BaseEntity::getName).ifPresent(channelName -> {
			final User registrar = findRegistrar();
			updateCAAttributes(enrollmentId, channelName, membershipStatus, registrar);
			final Enrollment enrollment = this.hfcaServerService.enroll(enrollmentId, account.getEnrollmentSecret(), OrgType.PEER);
			LocalEnrollment localEnrollment = new LocalEnrollment(enrollment.getKey(), enrollment.getCert());
			account.setLocalEnrollment(localEnrollment);
			this.accountService.save(account);
		});
	}

	private void updateCAAttributes(final String enrollmentId, final String channelName, final MembershipStatus status, final User registrar) {
		LOGGER.info("Updating membership attributes in the ecert for {}", enrollmentId);
		final String membershipStatusString = createMembershipStatusString(enrollmentId, channelName, status, registrar);
		final UpdateCAAttributesRequest request = new UpdateCAAttributesRequest(enrollmentId, "member.of", membershipStatusString);
		this.hfcaServerService.updateCAAttributes(request, OrgType.PEER, registrar);
		LOGGER.info("Successfully updated membership attributes in the ecert for {}", enrollmentId);
	}

	private String createMembershipStatusString(final String id, final String channelName, final MembershipStatus status, final User registrar) {
		final Collection<Attribute> caAttributes = this.hfcaServerService.getCAAttributes(registrar, id);
		final Map<String, String> channelToMembership = caAttributes.stream()
				.filter(attribute -> "member.of".equals(attribute.getName()))
				.map(Attribute::getValue)
				.filter(Objects::nonNull)
				.filter(membershipValue -> !membershipValue.isEmpty())
				.map(membershipValues -> Arrays.stream(membershipValues.split("\\s*,\\s*"))
						.filter(membershipValue -> membershipValue.contains(":")))
				.flatMap(Stream::distinct)
				.map(membershipValue -> {
					final String[] split = membershipValue.split(":");
					final String channel = split[0];
					final String updatedStatus = split[1];
					return new SimpleEntry<>(channel, updatedStatus);
				})
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
		channelToMembership.put(channelName, status.name());
		return channelToMembership.entrySet().stream().map(entry -> {
			final String channel = entry.getKey();
			final String membershipStatus = entry.getValue();
			return channel + ":" + membershipStatus;
		}).collect(joining(","));
	}

	private User findRegistrar() {
		return this.accountService.findRegistrar(OrgType.PEER);
	}

}
