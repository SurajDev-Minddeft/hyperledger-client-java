package com.utavi.ledger.api.dto.manage;

import com.utavi.ledger.config.network.OrganizationConfigDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SaveOrganizationConfigDto {

    @NotNull
    @Valid
    private final OrganizationConfigDto peerOrganization;

    @NotNull
    @Valid
    private final OrganizationConfigDto ordererOrganization;

    public SaveOrganizationConfigDto(OrganizationConfigDto peerOrganization, OrganizationConfigDto ordererOrganization) {
        this.peerOrganization = peerOrganization;
        this.ordererOrganization = ordererOrganization;
    }

    public OrganizationConfigDto getPeerOrganization() {
        return peerOrganization;
    }

    public OrganizationConfigDto getOrdererOrganization() {
        return ordererOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveOrganizationConfigDto that = (SaveOrganizationConfigDto) o;
        return Objects.equals(peerOrganization, that.peerOrganization) && Objects.equals(ordererOrganization, that.ordererOrganization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peerOrganization, ordererOrganization);
    }

    @Override
    public String toString() {
        return "SaveOrganizationConfigDto{" +
                   "peerOrganization=" + peerOrganization +
                   ", ordererOrganization=" + ordererOrganization +
                   '}';
    }
}
