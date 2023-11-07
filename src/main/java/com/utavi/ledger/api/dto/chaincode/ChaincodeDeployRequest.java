package com.utavi.ledger.api.dto.chaincode;

import static com.utavi.ledger.api.util.Utils.toJson;

import com.utavi.ledger.api.model.enums.DeployType;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChaincodeDeployRequest extends ChaincodeManagementRequest {

	private String channelName;

	@NotNull
	private DeployType deployType;

	public DeployType getDeployType() {
		return this.deployType;
	}

	public void setDeployType(final DeployType deployType) {
		this.deployType = deployType;
	}

	public String getChannelName() {
		return this.channelName;
	}

	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	@NotNull
	@NotEmpty
	private List<ChaincodePrivateCollectionConfig> privateCollectionConfig;

	public List<ChaincodePrivateCollectionConfig> getPrivateCollectionConfig() {
		return this.privateCollectionConfig;
	}

	public void setPrivateCollectionConfig(final List<ChaincodePrivateCollectionConfig> privateCollectionConfig) {
		this.privateCollectionConfig = privateCollectionConfig;
	}

	public JsonArray privateCollectionConfigAsJson() {
		final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		this.privateCollectionConfig.stream()
				.map(config -> Json.createReader(new StringReader(toJson(config))).readObject())
				.map(jsonObject -> Json.createObjectBuilder().add("StaticCollectionConfig", jsonObject).build())
				.forEach(arrayBuilder::add);
		return arrayBuilder.build();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("privateCollectionConfig", this.privateCollectionConfig)
				.append("channelName", getChannelName())
				.toString();
	}
}
