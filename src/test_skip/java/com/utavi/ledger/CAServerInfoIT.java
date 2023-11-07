package com.utavi.ledger;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utavi.ledger.config.TestDbRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.model.OrgInfo.OrgType;
import com.utavi.ledger.api.service.client.CAServerInfoService;
import com.utavi.ledger.api.service.client.OrgInfoService;
import com.utavi.ledger.api.service.network.ResourceServerService;
import com.utavi.ledger.api.service.network.ca.HFCAClientService;
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
@AutoConfigureRestDocs(outputDir = "target/snippets/ca-server")
@Import({TestFlywayMigration.class, TestDbRunner.class})
public class CAServerInfoIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	OrgInfoService orgInfoService;

	@Autowired
	CAServerInfoService caServerService;

	@Autowired
	ResourceServerService resourceServerService;

	@Autowired
    HFCAClientService hfcaClientService;

	@Test
	public void testFindAllServers() throws Exception {
		this.mockMvc.perform(get("/v1/ca").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(document("find-ca-servers", preprocessRequest(prettyPrint())));
	}

	@Test
	public void testFindServerByOrgType() throws Exception {
		this.mockMvc.perform(get("/v1/ca/{orgType}", OrgType.PEER).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(document("find-ca-servers-by-orgtype", preprocessRequest(prettyPrint())));
	}

}
