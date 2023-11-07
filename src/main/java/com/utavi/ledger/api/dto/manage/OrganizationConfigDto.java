package com.utavi.ledger.config.network;

import com.utavi.ledger.api.dto.manage.CaUserDto;
import com.utavi.ledger.api.dto.manage.OrgNodeDto;
import com.utavi.ledger.api.model.enums.OrgType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class OrganizationConfigDto {
    @NotNull
    @NotEmpty
    private final String name;
    @NotNull
    @NotEmpty
    private final String mspId;
    @NotNull
    @NotEmpty
    @Valid
    private final List<OrgNodeDto> caNodes;
    @NotNull
    @NotEmpty
    @Valid
    private final List<OrgNodeDto> nodes;
    @NotNull
    @Valid
    private final CaUserDto bootstrapUser;
    @NotNull
    @NotEmpty
    private final String tlsPemCertificate;

    @NotNull
    @NotEmpty
    private final String caTlsPemCertificate;

    @NotNull
    private final OrgType orgType;

    public OrganizationConfigDto(OrgType orgType, String name, String mspId, List<OrgNodeDto> caNodes, List<OrgNodeDto> nodes, CaUserDto bootstrapUser,
                                 String tlsPemCertificate, String caTlsPemCertificate) {
        this.orgType = orgType;
        this.name = name;
        this.mspId = mspId;
        this.caNodes = caNodes;
        this.nodes = nodes;
        this.bootstrapUser = bootstrapUser;
        this.tlsPemCertificate = tlsPemCertificate;
        this.caTlsPemCertificate = caTlsPemCertificate;
    }

    public OrgType getOrgType() {
        return orgType;
    }

    public String getName() {
        return name;
    }

    public String getMspId() {
        return mspId;
    }

    public List<OrgNodeDto> getCaNodes() {
        return caNodes;
    }

    public List<OrgNodeDto> getNodes() {
        return nodes;
    }

    public CaUserDto getBootstrapUser() {
        return bootstrapUser;
    }

    public String getTlsPemCertificate() {
        return tlsPemCertificate;
    }

    public String getCaTlsPemCertificate() {
        return caTlsPemCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationConfigDto that = (OrganizationConfigDto) o;
        return Objects.equals(name, that.name) && Objects.equals(mspId, that.mspId) && Objects.equals(caNodes, that.caNodes) && Objects.equals(nodes, that.nodes) && Objects.equals(bootstrapUser, that.bootstrapUser) && Objects.equals(tlsPemCertificate, that.tlsPemCertificate) && Objects.equals(caTlsPemCertificate, that.caTlsPemCertificate) && orgType == that.orgType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mspId, caNodes, nodes, bootstrapUser, tlsPemCertificate, caTlsPemCertificate, orgType);
    }

    @Override
    public String toString() {
        return "OrganizationConfigDto{" +
                   "name='" + name + '\'' +
                   ", mspId='" + mspId + '\'' +
                   ", caNodes=" + caNodes +
                   ", nodes=" + nodes +
                   ", bootstrapUser=" + bootstrapUser +
                   ", tlsPemCertificate='" + tlsPemCertificate + '\'' +
                   ", caTlsPemCertificate='" + caTlsPemCertificate + '\'' +
                   ", orgType=" + orgType +
                   '}';
    }
}
