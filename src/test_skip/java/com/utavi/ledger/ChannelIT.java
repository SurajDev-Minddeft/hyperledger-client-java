package com.utavi.ledger;

import static com.utavi.ledger.api.util.Utils.fromJson;
import static com.utavi.ledger.api.util.Utils.toJson;
import static org.junit.Assert.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.utavi.ledger.api.OrganizationProperties;
import com.utavi.ledger.api.OrganizationProperties.TestTarget;
import com.utavi.ledger.config.CompleteBlockchainSetupRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.dto.AttachSiloDto;
import com.utavi.ledger.api.dto.ChannelInfoDto;
import com.utavi.ledger.api.dto.CreateChannelDto;
import com.utavi.ledger.api.dto.SetChannelPeersDto;
import com.utavi.ledger.api.model.enums.SiloType;
import com.utavi.ledger.api.service.network.chaincode.management.ChaincodeInfoService;
import com.utavi.ledger.api.service.network.chaincode.management.ChaincodeManager;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets/channel")
@Import({TestFlywayMigration.class, CompleteBlockchainSetupRunner.class})
public class ChannelIT {
//
//	@Autowired
//	MockMvc mockMvc;
//
//	@Autowired
//	ChannelInfoManager channelInfoManager;
//
//	@Autowired
//	PeerInfoService peerInfoService;
//
//	@Autowired
//	OrganizationProperties orgProperties;
//
//	@Autowired
//	ChaincodeManager chaincodeManager;
//
//	@Autowired
//	ChaincodeInfoService chaincodeInfoService;
//
//	@Test
//	public void createChannel() throws Exception {
//		final String channelName = getChannelName() + System.currentTimeMillis();
//		final Set<PeerInfo> peers = this.peerInfoService.findPeers();
//		final CreateChannelDto dto = new CreateChannelDto();
//		dto.setChannelName(channelName);
//		dto.setOrgName("main-utavi");
//		dto.setOrgType(OrgType.PEER);
//		final Set<String> names = peers.stream().map(PeerInfo::getName).collect(Collectors.toSet());
//		dto.setPeers(names);
//		this.mockMvc.perform(post("/v1/channel")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(dto)))
//				.andExpect(status().isCreated())
//				.andDo(document("create-channel", preprocessRequest(prettyPrint())));
//
//		final ChannelInfo channelInfo = this.channelInfoManager.find(channelName);
//		channelInfo.setSiloName("DUMMY_CHANNEL");
//		this.channelInfoManager.update(channelInfo);
//	}
//
//	@Test
//	public void findChannels() throws Exception {
//		this.mockMvc.perform(get("/v1/channel")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(document("find-channels", preprocessRequest(prettyPrint())));
//	}
//
//	@Test
//	public void joinChannel() throws Exception {
//		final String channelName = getChannelName();
//		final Set<PeerInfo> peers = this.peerInfoService.findPeers();
//		final ChannelInfo channelInfo = this.channelInfoManager.find(channelName);
//		peers.stream().filter(peerInfo -> !channelName.equals(peerInfo.getChannelName())).forEach(peerInfo -> {
//			peerInfo.setChannelInfo(channelInfo);
//			this.peerInfoService.update(peerInfo);
//		});
//		final SetChannelPeersDto dto = new SetChannelPeersDto();
//		dto.setChannelName(channelName);
//		final Set<String> names = peers.stream().map(PeerInfo::getName).collect(Collectors.toSet());
//		dto.setPeers(names);
//		this.mockMvc.perform(post("/v1/channel/join")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(dto)))
//				.andExpect(status().isAccepted())
//				.andDo(document("join-channel", preprocessRequest(prettyPrint())));
//	}
//
//	@Test
//	public void testAttach() throws Exception {
//		final String channelName = getChannelName();
//		final String siloName = "testsilo";
//		final AttachSiloDto attachSiloDto = new AttachSiloDto();
//		attachSiloDto.setName(siloName);
//		attachSiloDto.setType(SiloType.PUBLIC);
//
//		final ChannelInfo info = this.channelInfoManager.find(channelName);
//		info.setSiloName(null);
//		this.channelInfoManager.update(info);
//
//		this.mockMvc.perform(post("/v1/channel/attach")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(toJson(attachSiloDto)))
//				.andExpect(status().isAccepted())
//				.andDo(document("attach-channel", preprocessRequest(prettyPrint())));
//
//		assertTrue(fromJson(this.mockMvc.perform(get("/v1/channel")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andReturn().getResponse().getContentAsString(), new TypeReference<Set<ChannelInfoDto>>() {}).stream()
//				.anyMatch(infoDto -> Objects.equals(infoDto.getChannelName(), channelName) && Objects.equals(infoDto.getSiloName(), siloName)));
//	}
//
//	private String getChannelName() {
//		return this.orgProperties.getTargetTestChannel(TestTarget.BLOCKCHAIN);
//	}

}
