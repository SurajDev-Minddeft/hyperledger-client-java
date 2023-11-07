package com.utavi.ledger;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utavi.ledger.config.TestDbRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.dto.CreateChannelDto;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.service.client.OrgInfoService;
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
@AutoConfigureRestDocs(outputDir = "target/snippets/peer")
@Import({TestFlywayMigration.class, TestDbRunner.class})
public class PeerIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	OrgInfoService orgInfoService;

//	@Autowired
//	PeerInfoService peerService;
//
//	@Autowired
//	ChannelInfoManager channelInfoManager;

	@Test
	public void testAllFindPeers() throws Exception {
		this.mockMvc.perform(get("/v1/peer")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(document("find-peers", preprocessRequest(prettyPrint())));
	}
//
//	@Test
//	public void testFindPeersByChannel() throws Exception {
//		final Set<PeerInfo> peers = this.peerService.findPeers();
//		final Set<String> peerNames = peers.stream().map(PeerInfo::getName).collect(Collectors.toSet());
//		final CreateChannelDto createChannelDto = new CreateChannelDto();
//		createChannelDto.setChannelName("newchanneltest" + System.currentTimeMillis());
//		createChannelDto.setPeers(peerNames);
//		createChannelDto.setOrgType(OrgType.PEER);
//		createChannelDto.setOrgName("main-utavi");
//		this.channelInfoManager.create(createChannelDto);
//		this.mockMvc.perform(get("/v1/peer/{channelName}", createChannelDto.getChannelName())
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andDo(document("find-peers-by-channel", preprocessRequest(prettyPrint())));
//	}

}
