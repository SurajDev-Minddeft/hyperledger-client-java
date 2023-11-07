package com.utavi.ledger.api.service.network.ca;

import com.utavi.ledger.api.dto.IdentityRegistrationRequest;
import com.utavi.ledger.api.dto.UpdateCAAttributesRequest;
import com.utavi.ledger.api.exceptions.HFClientException;
import com.utavi.ledger.api.model.enums.IdentityType;
import com.utavi.ledger.api.model.enums.OrgType;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.*;
import org.hyperledger.fabric_ca.sdk.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class HFCAServerService {

	private final static Logger LOGGER = LoggerFactory.getLogger(HFCAServerService.class.getName());
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private final HFCAClientService hfcaClientService;

	public HFCAServerService(final HFCAClientService hfcaClientService) {
		this.hfcaClientService = hfcaClientService;
	}

	public void registerUser(final User registrar, final OrgType orgType, final IdentityRegistrationRequest registrationRequest) {
		final HFCAClient hfcaClient = this.hfcaClientService.createFabricCAClient(orgType);
		final String identityCaName = registrationRequest.getName();
		if (isAccountRegistered(hfcaClient, identityCaName, registrar)) {
			LOGGER.info("Account={} is already registered", identityCaName);
		} else {
			LOGGER.info("Account={} is not registered", identityCaName);
			LOGGER.info("Registering Account={}", identityCaName);
			try {
				hfcaClient.register(createRegistrationRequest(registrationRequest), registrar);
				LOGGER.info("Successfully registered Account={}", identityCaName);
			} catch (final Exception e) {
				LOGGER.error("Failed to register Account={}, Error={}", identityCaName, e);
				throw new HFClientException("Failed to register Account");
			}
		}
	}

	public void changePassword(final String targetAccountName, final String secret, final User registrar, final OrgType orgType) {
		final HFCAClient fabricCAClient = this.hfcaClientService.createFabricCAClient(orgType);
		try {
			final HFCAIdentity hfcaIdentity = fabricCAClient.newHFCAIdentity(targetAccountName);
			hfcaIdentity.setSecret(secret);
			hfcaIdentity.update(registrar);
		} catch (final IdentityException | InvalidArgumentException e) {
			throw new RuntimeException(String.format("Failed to update secret for %s", targetAccountName), e);
		}
	}

	public Collection<Attribute> getCAAttributes(final User registrar, final String enrollmentId) {
		final HFCAClient caClient = this.hfcaClientService.createFabricCAClient(OrgType.PEER);
		try {
			final HFCAIdentity hfcaIdentity = caClient.newHFCAIdentity(enrollmentId);
			final int status = hfcaIdentity.read(registrar);
			if (status < 400) {
				return hfcaIdentity.getAttributes();
			}
			return Collections.emptyList();
		} catch (final InvalidArgumentException | IdentityException e) {
			LOGGER.error("Failed to read CA attributes of account {}, Error={}", enrollmentId, e);
			throw new HFClientException("Failed to read CA attributes");
		}
	}

	public void updateCAAttributes(final UpdateCAAttributesRequest updateRequest, final OrgType orgType, final User registrar) {
		final HFCAClient fabricCAClient = this.hfcaClientService.createFabricCAClient(orgType);
		final String accountName = updateRequest.getAccountName();
		final String key = updateRequest.getKey();
		try {
			LOGGER.info("Updating CA attribute {} for account {}", key, accountName);
			final HFCAIdentity hfcaIdentity = fabricCAClient.newHFCAIdentity(accountName);
			final List<Attribute> attributeList = hfcaIdentity
					.getAttributes()
					.stream()
					.filter(attribute -> !key.equalsIgnoreCase(attribute.getName()))
					.collect(toList());
			attributeList.add(new Attribute(key, updateRequest.getValue(), Boolean.TRUE));
			hfcaIdentity.setAttributes(attributeList);
			hfcaIdentity.setType(HFCAClient.HFCA_TYPE_CLIENT);
			hfcaIdentity.update(registrar);
			LOGGER.info("Successfully updated CA attribute {} for account {}", key, accountName);
		} catch (final IdentityException | InvalidArgumentException e) {
			LOGGER.error("Failed to update CA attribute {} for account {}, Error={}", key, accountName, e);
			throw new HFClientException("Failed to update CA attribute");
		}
	}

	public Enrollment reEnroll(final User user, final OrgType orgType) {
		final HFCAClient fabricCAClient = this.hfcaClientService.createFabricCAClient(orgType);
		try {
			return fabricCAClient.reenroll(user);
		} catch (final InvalidArgumentException | EnrollmentException e) {
			throw new RuntimeException(e);
		}
	}

	//todo wujek unused
//	public Enrollment enrollBaseAdmin(final String adminName, final String orgName) {
//		LOGGER.info("Enrolling peer admin {}", adminName);
//		final Map<String, byte[]> stringMap = this.resourceServerService.loadAdminCertificates(orgName);
//
//		final String certificate = getCertificateFromBytes(stringMap.get("certificate"))
//				.orElseThrow(() -> new IllegalStateException(String.format("Failed to get certificate string from bytes for admin %s", adminName)));
//
//		final PrivateKey privatekey = getPrivateKeyFromBytes(stringMap.get("privateKey"))
//				.orElseThrow(() -> new IllegalStateException(String.format("Failed to get private key from bytes for admin %s", adminName)));
//		LOGGER.info("Successfully enrolled peer admin {}", adminName);
//		return new OrganizationAdminEnrollment(privatekey, certificate);
//	}

	public Enrollment enroll(final String enrollmentId, final String enrollmentSecret, final OrgType orgType) {
		try {
			LOGGER.info("Enrolling identity {}", enrollmentId);
			final HFCAClient hfcaClient = this.hfcaClientService.createFabricCAClient(orgType);
			final Enrollment enrollment = hfcaClient.enroll(enrollmentId, enrollmentSecret);
			LOGGER.info("Successfully enrolled identity {}", enrollmentId);
			return enrollment;
		} catch (final Exception e) {
			LOGGER.error("Failed to enroll identity {}, Error={}", enrollmentId, e);
			throw new HFClientException("Failed to enroll identity");
		}
	}

	private RegistrationRequest createRegistrationRequest(final IdentityRegistrationRequest request) throws Exception {
		final RegistrationRequest registrationRequest = new RegistrationRequest(request.getName(), request.getAffiliation());
		final IdentityType identityType = request.getIdentityType();
		registrationRequest.setSecret(request.getSecret());
		switch (identityType) {
			case PEER:
				registrationRequest.setType(HFCAClient.HFCA_TYPE_PEER);
				break;
			case USER:
			case REGISTRAR:
				registrationRequest.setType(HFCAClient.HFCA_TYPE_CLIENT);
				break;
			case ADMIN:
				registrationRequest.setType("admin");
				break;
			case ORDERER:
				registrationRequest.setType(HFCAClient.HFCA_TYPE_ORDERER);
				break;
			default:
				throw new IllegalArgumentException(String.format("Identity type %s is not supported", identityType));
		}
		registrarAttributes(request).forEach(registrationRequest::addAttribute);
		return registrationRequest;
	}

	private boolean isAccountRegistered(final HFCAClient hfcaClient, final String identityCaName, final User registrarIdentity) {
		LOGGER.info("Checking if account {} is registered", identityCaName);
		return findAllByRegistrar(hfcaClient, registrarIdentity).stream().anyMatch(identity -> identityCaName.equals(identity.getEnrollmentId()));
	}

	private List<Attribute> registrarAttributes(final IdentityRegistrationRequest registrationRequest) {
		final List<Attribute> attributes = new ArrayList<>();
		final IdentityType identityType = registrationRequest.getIdentityType();
		switch (identityType) {
			case USER:
				attributes.add(new Attribute("account.type", registrationRequest.getAccountType().name(), Boolean.TRUE));
				attributes.add(new Attribute("member.of", registrationRequest.getMembershipInfo(), Boolean.TRUE));
				break;
			case PEER:
			case ORDERER:
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFGENCRL, FALSE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREVOKER, FALSE));
				break;
			case ADMIN:
				attributes.add(new Attribute("admin", TRUE, Boolean.TRUE));
				// can create CRL (list of revoked certs)
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFGENCRL, TRUE));
				// can revoke certificates
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREVOKER, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFAFFILIATIONMGR, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFINTERMEDIATECA, TRUE));
				// can register these roles
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREGISTRARROLES, "user,client,admin,peer,orderer"));
				// can register all attributes
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREGISTRARATTRIBUTES, "*"));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREGISTRARDELEGATEROLES, "user,client,admin,peer,orderer"));
				break;
			case REGISTRAR:
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFAFFILIATIONMGR, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFINTERMEDIATECA, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFGENCRL, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREVOKER, TRUE));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREGISTRARROLES, "client,user"));
				attributes.add(new Attribute(HFCAClient.HFCA_ATTRIBUTE_HFREGISTRARATTRIBUTES, "member.of,account.type"));
				break;
			default:
				throw new IllegalArgumentException(String.format("Identity type %s is not supported", identityType));
		}
		return Collections.unmodifiableList(attributes);
	}

	public String revoke(final User revoker, final User revokee, final OrgType orgType) {
		final String enrollmentId = revokee.getName();
		try {
			LOGGER.info("Revoking the ECert for account {}", enrollmentId);
			final HFCAClient hfcaClient = this.hfcaClientService.createFabricCAClient(orgType);
			final String crl = hfcaClient.revoke(revoker, revokee.getEnrollment(), "", true);
			LOGGER.info("Successfully revoked the ECert for account {}", enrollmentId);
			return crl;
		} catch (final RevocationException | InvalidArgumentException e) {
			LOGGER.error("Failed to revoke ECert for account {}, Error=", enrollmentId, e);
			throw new HFClientException("Failed to revoke Account");
		}
	}

	public Set<HFCAX509Certificate> findHFCAX509Certificates(final User registrar, final String identityName) {
		final HFCAClient fabricCAClient = this.hfcaClientService.createFabricCAClient(OrgType.PEER);
		final HFCACertificateRequest request = fabricCAClient.newHFCACertificateRequest();
		request.setEnrollmentID(identityName);
		request.setRevoked(false);
		return findHFCACertificates(fabricCAClient, registrar, request).stream().map(HFCAX509Certificate.class::cast).collect(Collectors.toSet());
	}

	private Collection<HFCACredential> findHFCACertificates(final HFCAClient hfcaClient, final User registrar,
			final HFCACertificateRequest request) {
		final HFCACertificateResponse response;
		try {
			response = hfcaClient.getHFCACertificates(registrar, request);
			return response.getCerts();
		} catch (final HFCACertificateException e) {
			throw new RuntimeException(e);
		}
	}

	private Collection<HFCAIdentity> findAllByRegistrar(final HFCAClient hfcaClient, final User registrar) {
		try {
			return hfcaClient.getHFCAIdentities(registrar);
		} catch (final InvalidArgumentException | IdentityException e) {
			throw new RuntimeException("Failed to fetch CA identities for registrar " + registrar.getName(), e);
		}
	}

}
