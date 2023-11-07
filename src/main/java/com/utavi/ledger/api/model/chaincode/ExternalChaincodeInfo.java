package com.utavi.ledger.api.model.chaincode;

import com.utavi.ledger.api.model.ChaincodeInfo;
import com.utavi.ledger.api.model.PeerInfo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "lc_external_chaincode_info")
public class ExternalChaincodeInfo {

    public ExternalChaincodeInfo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "chaincode_info_id")
    private ChaincodeInfo chaincodeInfo;

    @NotNull
    @Column(name = "packageId")
    private String packageId;

    @NotNull
    @Column(name = "name")
    private String name;

    public ExternalChaincodeInfo(ChaincodeInfo chaincodeInfo, String packageId, String name) {
        this.chaincodeInfo = chaincodeInfo;
        this.packageId = packageId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChaincodeInfo getChaincodeInfo() {
        return chaincodeInfo;
    }

    public void setChaincodeInfo(ChaincodeInfo chaincodeInfo) {
        this.chaincodeInfo = chaincodeInfo;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
