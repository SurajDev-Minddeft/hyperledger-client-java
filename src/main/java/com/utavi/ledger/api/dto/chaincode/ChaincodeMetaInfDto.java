package com.utavi.ledger.api.dto.chaincode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class ChaincodeMetaInfDto {

	public enum Type {
		JAVA,
		GO_LANG,
		NODE
	}

	@NotBlank
	private String path;

	@NotNull
	private Type lang;

	@NotBlank
	private String name;

	@NotBlank
	private String version;

	@NotBlank
	private String packageId;

	public String getPath() {
		return this.path;
	}

	public Type getLang() {
		return this.lang;
	}

	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public void setLang(final Type lang) {
		this.lang = lang;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChaincodeMetaInfDto that = (ChaincodeMetaInfDto) o;
		return Objects.equals(path, that.path) && lang == that.lang && Objects.equals(name, that.name) && Objects.equals(version, that.version) && Objects.equals(packageId, that.packageId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(path, lang, name, version, packageId);
	}

	@Override
	public String toString() {
		return "ChaincodeMetaInfDto{" +
				   "path='" + path + '\'' +
				   ", lang=" + lang +
				   ", name='" + name + '\'' +
				   ", version='" + version + '\'' +
				   ", packageId='" + packageId + '\'' +
				   '}';
	}
}
