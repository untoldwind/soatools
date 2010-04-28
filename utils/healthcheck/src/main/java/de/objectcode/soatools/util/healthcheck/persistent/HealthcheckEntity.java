package de.objectcode.soatools.util.healthcheck.persistent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "HEALTHCHECK")
public class HealthcheckEntity {
	long id;
	Date createdAt;
	String data;

	protected HealthcheckEntity() {
	}

	public HealthcheckEntity(String data) {
		this.createdAt = new Date();
	}

	@Id
	@GeneratedValue(generator = "SEQ_HEALTHCHECK_ID")
	@GenericGenerator(name = "SEQ_HEALTHCHECK_ID", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_HEALTHCHECK_ID"))
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "DATA")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


	@Column(name = "CREATED_AT")
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
