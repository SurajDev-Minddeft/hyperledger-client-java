package com.utavi.ledger.api.dto.chaincode;

public class ExternalChaincodeInfoDto {

    private String externalChaincodeName;
    private String version;
    private String packageId;

    public ExternalChaincodeInfoDto(String externalChaincodeName, String version, String packageId) {
        this.externalChaincodeName = externalChaincodeName;
        this.version = version;
        this.packageId = packageId;
    }

    public String getExternalChaincodeName() {
        return externalChaincodeName;
    }

    public void setExternalChaincodeName(String externalChaincodeName) {
        this.externalChaincodeName = externalChaincodeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
