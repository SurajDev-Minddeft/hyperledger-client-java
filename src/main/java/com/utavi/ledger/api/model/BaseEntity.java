package com.utavi.ledger.api.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	protected Long id;

	@NotBlank
	@Column(name = "name")
	protected String name;

	@Column(name = "create_date")
	private final Instant createDate;

	@Column(name = "update_date")
	private Instant updateDate;

	BaseEntity() {
		this.createDate = Instant.now();
	}

	public Instant getCreateDate() {
		return this.createDate;
	}

	public Instant getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(final Instant updateDate) {
		this.updateDate = updateDate;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final BaseEntity that = (BaseEntity) o;
		return Objects.equals(this.name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
