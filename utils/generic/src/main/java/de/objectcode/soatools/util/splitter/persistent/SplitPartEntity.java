package de.objectcode.soatools.util.splitter.persistent;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MUTIL_SPLIT_PART")
public class SplitPartEntity {
	long id;
	int partIndex;
	SplitEntity split;
	Blob content;
	Date receivedAt;

	protected SplitPartEntity() {

	}

	public SplitPartEntity(int partIndex, SplitEntity split, Blob content) {
		this.partIndex = partIndex;
		this.split = split;
		this.content = content;
		this.receivedAt = new Date();
	}

	@Id
	@GeneratedValue(generator = "SEQ_MUTIL_SPLIT_PART_ID")
	@GenericGenerator(name = "SEQ_MUTIL_SPLIT_PART_ID", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MUTIL_SPLIT_PART_ID"))
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "PART_INDEX")
	public int getPartIndex() {
		return partIndex;
	}

	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}

	@Column(name = "RECEIVED_AT")
	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

	@ManyToOne
	@JoinColumn(name = "SPLIT_ID", nullable = false)
	public SplitEntity getSplit() {
		return split;
	}

	public void setSplit(SplitEntity split) {
		this.split = split;
	}

	@Column(name = "CONTENT")
	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}
}
