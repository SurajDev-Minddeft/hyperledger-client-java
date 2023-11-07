package com.utavi.ledger.api.service.network;

import static com.utavi.ledger.data.CATestData.makeCAServerInfo;
import static com.utavi.ledger.data.OrgInfoTestData.ORGANIZATION_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import com.utavi.ledger.api.model.CAServerInfo;
import com.utavi.ledger.api.model.OrgInfo;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.model.enums.OrgType;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import com.utavi.ledger.api.service.network.ca.HFCAClientService;
import com.utavi.ledger.api.util.TlsUtils;
import java.util.Properties;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HFCAClientService.class, HFCAClient.class, CryptoSuite.class})
public class HFCAClientServiceTest {

	@InjectMocks
	private HFCAClientService hfcaClientService;

	@Mock
	private CAServerInfoService caServerService;

	@Mock
	private ResourceServerService resourceServerService;

	@Test
	public void whenCreateFabricCAClient_TlsEnableFalse_SuccessfullyCreateClient() throws Exception {
		mockStatic(HFCAClient.class);
		final HFCAClient hfcaClient = Mockito.mock(HFCAClient.class, Mockito.RETURNS_DEEP_STUBS);
		mockStatic(CryptoSuite.Factory.class);
		final CryptoSuite cryptoSuite = Mockito.mock(CryptoSuite.class, Mockito.RETURNS_DEEP_STUBS);
		final OrgInfo mockedOrgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
		final CAServerInfo caServerInfo = makeCAServerInfo("ica0", "HOST", false, 1000, mockedOrgInfo);

		when(HFCAClient.createNewInstance(caServerInfo.getLocation(), null)).thenReturn(hfcaClient);
		when(CryptoSuite.Factory.getCryptoSuite()).thenReturn(cryptoSuite);
		when(this.caServerService.findOneByOrgType(any(OrgType.class))).thenReturn(caServerInfo);

		this.hfcaClientService.createFabricCAClient(OrgType.PEER);

		verifyStatic(HFCAClient.class);
		HFCAClient.createNewInstance(caServerInfo.getLocation(), null);
		verifyStatic(CryptoSuite.Factory.class);
		CryptoSuite.Factory.getCryptoSuite();
	}

	@Test
	public void whenCreateFabricCAClient_TlsEnableTrue_SuccessfullyCreateClient() throws Exception {
		mockStatic(HFCAClient.class);
		final HFCAClient hfcaClient = Mockito.mock(HFCAClient.class, Mockito.RETURNS_DEEP_STUBS);
		mockStatic(CryptoSuite.Factory.class);
		final CryptoSuite cryptoSuite = Mockito.mock(CryptoSuite.class, Mockito.RETURNS_DEEP_STUBS);
		final OrgInfo mockedOrgInfo = Mockito.mock(OrgInfo.class, Mockito.RETURNS_DEEP_STUBS);
		final CAServerInfo caServerInfo = makeCAServerInfo("ica0", "HOST", true, 1000, mockedOrgInfo);
		final byte[] crtBytes = new byte[]{};
		final Properties caProperties = TlsUtils.makeCAServerTlsProperties(crtBytes, caServerInfo.getName());

		when(mockedOrgInfo.getName()).thenReturn(ORGANIZATION_NAME);
		when(HFCAClient.createNewInstance(caServerInfo.getLocation(), caProperties)).thenReturn(hfcaClient);
		when(CryptoSuite.Factory.getCryptoSuite()).thenReturn(cryptoSuite);
		when(this.caServerService.findOneByOrgType(any(OrgType.class))).thenReturn(caServerInfo);
		when(this.resourceServerService.requestCATlsClientCert(ORGANIZATION_NAME)).thenReturn(crtBytes);

		this.hfcaClientService.createFabricCAClient(OrgType.PEER);

		verifyStatic(HFCAClient.class);
		HFCAClient.createNewInstance(caServerInfo.getLocation(), caProperties);
		verifyStatic(CryptoSuite.Factory.class);
		CryptoSuite.Factory.getCryptoSuite();
		verify(this.resourceServerService).requestCATlsClientCert(ORGANIZATION_NAME);
		verify(this.caServerService).findOneByOrgType(any(OrgType.class));
	}
}
