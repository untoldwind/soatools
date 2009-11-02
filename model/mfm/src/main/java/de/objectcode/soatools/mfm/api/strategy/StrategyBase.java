package de.objectcode.soatools.mfm.api.strategy;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Base class of all up- and downgrade strategies.
 * 
 * @author junglas
 */
@XmlTransient
@Entity
@Table(name = "MFM_STRATEGIES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("0")
public abstract class StrategyBase {
	long id;

	@Id
	@GeneratedValue(generator = "SEQ_MFM_STRATEGIES")
	@GenericGenerator(name = "SEQ_MFM_STRATEGIES", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MFM_STRATEGIES"))
	@XmlTransient
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
