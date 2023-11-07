package com.utavi.ledger;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utavi.ledger.config.TestDbRunner;
import com.utavi.ledger.config.TestFlywayMigration;
import com.utavi.ledger.api.service.client.OrgInfoService;
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
@AutoConfigureRestDocs(outputDir = "target/snippets/orderer")
@Import({TestFlywayMigration.class, TestDbRunner.class})
public class OrdererIT {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	OrgInfoService orgInfoService;

//	@Autowired
//	OrdererInfoService ordererService;

	@Test
	public void testFindOrderers() throws Exception {
		this.mockMvc.perform(get("/v1/orderer")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(document("find-orderers", preprocessRequest(prettyPrint())));
	}
}
