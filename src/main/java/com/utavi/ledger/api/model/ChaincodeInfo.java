package com.utavi.ledger.api.model;

import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto;
import com.utavi.ledger.api.dto.chaincode.ChaincodeMetaInfDto.Type;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "lc_chaincode_info")
public class ChaincodeInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "name")
	private String name;

	@NotBlank
	@Column(name = "path")
	private String path;

	@NotNull
	@Column(name = "lang")
	@Enumerated(EnumType.STRING)
	private ChaincodeMetaInfDto.Type lang;

	@NotBlank
	@Column(name = "version")
	private String version;

	public ChaincodeInfo() {
	}

	public ChaincodeInfo(final ChaincodeMetaInfDto metaInf) {
		this.version = metaInf.getVersion();
		this.lang = metaInf.getLang();
		this.path = metaInf.getPath();
		this.name = metaInf.getName();
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public String getPath() {
		return this.path;
	}

	public Type getLang() {
		return this.lang;
	}

	public String getVersion() {
		return this.version;
	}

	public String getName() {
		return this.name;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChaincodeInfo that = (ChaincodeInfo) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(path, that.path) && lang == that.lang && Objects.equals(version, that.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, path, lang, version);
	}

	@Override
	public String toString() {
		return "ChaincodeInfo{" +
				   "id=" + id +
				   ", name='" + name + '\'' +
				   ", path='" + path + '\'' +
				   ", lang=" + lang +
				   ", version='" + version + '\'' +
				   '}';
	}
}
