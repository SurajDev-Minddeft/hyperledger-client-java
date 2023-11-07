package com.utavi.ledger.api.dto.chaincode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class SaveChaincodeDeploymentInfoDto {

    @NotNull
    @Valid
    private ChaincodeMetaInfDto chaincodeMetaInfoDto;

    @NotBlank
    private String channelName;

    @NotEmpty
    private List<String> peerNames;

    @NotEmpty
    private String packageId;

    @NotEmpty
    private String externalChaincodeName;

    @NotEmpty
    private List<String> channelOrdererNames;

    public SaveChaincodeDeploymentInfoDto(
            ChaincodeMetaInfDto chaincodeMetaInfoDto,
            String channelName,
            List<String> channelOrdererNames,
            String packageId,
            String externalChaincodeName,
            List<String> peerNames) {
        this.chaincodeMetaInfoDto = chaincodeMetaInfoDto;
        this.channelName = channelName;
        this.channelOrdererNames = channelOrdererNames;
        this.packageId = packageId;
        this.externalChaincodeName = externalChaincodeName;
        this.peerNames = peerNames;
    }

    public ChaincodeMetaInfDto getChaincodeMetaInfoDto() {
        return chaincodeMetaInfoDto;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getExternalChaincodeName() {
        return externalChaincodeName;
    }

    public void setExternalChaincodeName(String externalChaincodeName) {
        this.externalChaincodeName = externalChaincodeName;
    }

    public void setChaincodeMetaInfoDto(ChaincodeMetaInfDto chaincodeMetaInfoDto) {
        this.chaincodeMetaInfoDto = chaincodeMetaInfoDto;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<String> getChannelOrdererNames() {
        return channelOrdererNames;
    }

    public void setChannelOrdererNames(List<String> channelOrdererNames) {
        this.channelOrdererNames = channelOrdererNames;
    }

    public List<String> getPeerNames() {
        return peerNames;
    }

    public void setPeerNames(List<String> peerNames) {
        this.peerNames = peerNames;
    }
}
