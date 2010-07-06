package de.objectcode.soatools.util.smooks.recordset;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.milyn.SmooksException;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.annotation.AnnotationConstants;
import org.milyn.cdr.annotation.AppContext;
import org.milyn.cdr.annotation.ConfigParam;
import org.milyn.container.ApplicationContext;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.annotation.Initialize;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.milyn.delivery.ordering.Consumer;
import org.milyn.delivery.ordering.Producer;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXUtil;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.milyn.delivery.sax.SAXVisitBefore;
import org.milyn.expression.MVELExpressionEvaluator;
import org.milyn.javabean.BeanInstanceCreator;
import org.milyn.javabean.repository.BeanId;
import org.milyn.javabean.repository.BeanIdRegister;
import org.milyn.javabean.repository.BeanRepository;
import org.milyn.javabean.repository.BeanRepositoryManager;
import org.milyn.util.CollectionsUtil;
import org.milyn.xml.DomUtils;
import org.w3c.dom.Element;

import de.objectcode.soatools.util.recordset.RecordSetBuilder;
import de.objectcode.soatools.util.recordset.UpdatableRecordRow;

public class RecordRowCreator implements DOMElementVisitor, SAXVisitBefore,
		SAXVisitAfter, Producer, Consumer {
	private static Log LOGGER = LogFactory.getLog(RecordRowCreator.class);

	@AppContext
	private ApplicationContext appContext;

	@ConfigParam(name = "recordSetId")
	private String recordSetIdName;

	@ConfigParam(name = "recordRowId")
	private String recordRowIdName;

	@ConfigParam(defaultVal = AnnotationConstants.NULL_STRING)
	private String idAttributeName;

	@ConfigParam(defaultVal = AnnotationConstants.NULL_STRING)
	private MVELExpressionEvaluator idExpression;

	private BeanId recordSetId;

	private BeanId recordRowId;

	@Override
	public void visitBefore(Element element, ExecutionContext executionContext)
			throws SmooksException {
        BeanRepository beanRepo = BeanRepositoryManager.getBeanRepository(executionContext);
        
		RecordSetBuilder recordSetBuilder = (RecordSetBuilder) beanRepo.getBean(recordSetId);
		String rowId;
		
		if (recordSetBuilder == null)
			throw new SmooksConfigurationException(
					"Can't populate recordSet '"
							+ recordSetId
							+ "' because there is no object in the bean context under that bean id.");

		if (idExpression != null) {
			Map<String, Object> beanMap = BeanRepositoryManager
					.getBeanRepository(executionContext).getBeanMap();
			Object idValue = idExpression.getValue(beanMap);

			rowId = String.valueOf(idValue);
		} else if (idAttributeName != null ) {
			rowId = DomUtils.getAttributeValue(element, idAttributeName);
		} else {
			rowId = String.valueOf(recordSetBuilder.getRowCount());
		}

		UpdatableRecordRow recordRow = recordSetBuilder.addRow(rowId);
		
        beanRepo.setBeanInContext(recordRowId, false);
        beanRepo.addBean(recordRowId, recordRow);
        beanRepo.setBeanInContext(recordRowId, true);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RecordRow [" + recordRowIdName + "] instance created.");
        }   
	}

	@Override
	public void visitAfter(Element element, ExecutionContext executionContext)
			throws SmooksException {
		visitAfter(executionContext);
	}

	@Override
	public void visitBefore(SAXElement element,
			ExecutionContext executionContext) throws SmooksException,
			IOException {
        BeanRepository beanRepo = BeanRepositoryManager.getBeanRepository(executionContext);
        
		RecordSetBuilder recordSetBuilder = (RecordSetBuilder) beanRepo.getBean(recordSetId);
		String rowId;
		
		if (recordSetBuilder == null)
			throw new SmooksConfigurationException(
					"Can't populate recordSet '"
							+ recordSetId
							+ "' because there is no object in the bean context under that bean id.");

		if (idExpression != null) {
			Map<String, Object> beanMap = BeanRepositoryManager
					.getBeanRepository(executionContext).getBeanMap();
			Object idValue = idExpression.getValue(beanMap);

			rowId = String.valueOf(idValue);
		} else if (idAttributeName != null ) {
			rowId = SAXUtil.getAttribute(idAttributeName, element.getAttributes());
		} else {
			rowId = String.valueOf(recordSetBuilder.getRowCount());
		}

		UpdatableRecordRow recordRow = recordSetBuilder.addRow(rowId);
		
        beanRepo.setBeanInContext(recordRowId, false);
        beanRepo.addBean(recordRowId, recordRow);
        beanRepo.setBeanInContext(recordRowId, true);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RecordRow [" + recordRowIdName + "] instance created.");
        }   
	}

	@Override
	public void visitAfter(SAXElement element, ExecutionContext executionContext)
			throws SmooksException, IOException {
		visitAfter(executionContext);
	}

	@Override
	public Set<? extends Object> getProducts() {
		return CollectionsUtil.toSet(recordRowIdName);
	}

	@Override
	public boolean consumes(Object object) {
		if (object.equals(recordSetIdName)) {
			return true;
		}
		return false;
	}

	@Initialize
	public void initialize() throws SmooksConfigurationException {
		BeanRepositoryManager beanRepositoryManager = BeanRepositoryManager
				.getInstance(appContext);
		BeanIdRegister beanIdRegister = beanRepositoryManager
				.getBeanIdRegister();
		recordSetId = beanIdRegister.getBeanId(recordSetIdName);
		recordRowId = beanIdRegister.register(recordRowIdName);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RecordRowCreator created for [" + recordSetIdName
					+ "," + recordRowIdName + "]");
		}
	}

	private void visitAfter(ExecutionContext executionContext) {
		BeanRepository beanRepo = BeanRepositoryManager
				.getBeanRepository(executionContext);

		beanRepo.setBeanInContext(recordRowId, false);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RecordRow [" + recordRowIdName
					+ "] instance removed from context.");
		}
	}

}
