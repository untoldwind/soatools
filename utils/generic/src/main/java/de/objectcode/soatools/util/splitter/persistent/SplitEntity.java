package de.objectcode.soatools.util.splitter.persistent;

import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MUTIL_SPLIT")
public class SplitEntity {
	long id;
	Date createAt;
	String serviceCategory;
	String serviceName;
	int partCount;
	Map<Integer, SplitPartEntity> parts;

	protected SplitEntity() {
	}

	public SplitEntity(String serviceCategory, String serviceName, int partCount) {
		this.serviceCategory = serviceCategory;
		this.serviceName = serviceName;
		this.partCount = partCount;
		this.createAt = new Date();
	}

	@Id
	@GeneratedValue(generator = "SEQ_MUTIL_SPLIT_ID")
	@GenericGenerator(name = "SEQ_MUTIL_SPLIT_ID", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MUTIL_SPLIT_ID"))
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "CREATED_AT")
	public Date getCreateAt() {
		return createAt;
	}

	@Column(name = "SERVICE_CATEGORY")
	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@Column(name = "PART_COUNT")
	public int getPartCount() {
		return partCount;
	}

	public void setPartCount(int partCount) {
		this.partCount = partCount;
	}

	@OneToMany(mappedBy = "split", cascade = CascadeType.ALL)
	@MapKey(name = "partIndex")
	public Map<Integer, SplitPartEntity> getParts() {
		return parts;
	}

	public void setParts(Map<Integer, SplitPartEntity> parts) {
		this.parts = parts;
	}

}
