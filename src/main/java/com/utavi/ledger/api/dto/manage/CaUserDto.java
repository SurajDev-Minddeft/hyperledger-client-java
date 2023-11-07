package com.utavi.ledger.api.dto.manage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CaUserDto {

    @NotNull
    @NotEmpty
    private final String name;
    @NotNull
    @NotEmpty
    private final String pemCertificate;
    @NotNull
    @NotEmpty
    private final String privateKey;

    public CaUserDto(String name, String pemCertificate, String privateKey) {
        this.name = name;
        this.pemCertificate = pemCertificate;
        this.privateKey = privateKey;
    }

    public String getName() {
        return name;
    }

    public String getPemCertificate() {
        return pemCertificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaUserDto caUserDto = (CaUserDto) o;
        return Objects.equals(name, caUserDto.name) && Objects.equals(pemCertificate, caUserDto.pemCertificate) && Objects.equals(privateKey, caUserDto.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pemCertificate, privateKey);
    }

    @Override
    public String toString() {
        return "CaUserDto{" +
                   "name='" + name + '\'' +
                   ", pemCertificate='" + pemCertificate + '\'' +
                   ", privateKey='" + privateKey + '\'' +
                   '}';
    }
}
