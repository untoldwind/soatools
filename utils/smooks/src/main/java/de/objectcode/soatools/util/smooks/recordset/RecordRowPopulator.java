package de.objectcode.soatools.util.smooks.recordset;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXUtil;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.milyn.delivery.sax.SAXVisitBefore;
import org.milyn.expression.MVELExpressionEvaluator;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.javabean.repository.BeanId;
import org.milyn.javabean.repository.BeanIdRegister;
import org.milyn.javabean.repository.BeanRepository;
import org.milyn.javabean.repository.BeanRepositoryManager;
import org.milyn.xml.DomUtils;
import org.w3c.dom.Element;

import de.objectcode.soatools.util.recordset.UpdatableRecordRow;

public class RecordRowPopulator implements DOMElementVisitor, SAXVisitBefore,
		SAXVisitAfter, Consumer {
	private static Log LOGGER = LogFactory.getLog(RecordSetCreator.class);
	@AppContext
	private ApplicationContext appContext;

	@ConfigParam(name = "recordRowId")
	private String recordRowIdName;

	@ConfigParam(defaultVal = AnnotationConstants.NULL_STRING)
	private String columnName;

	@ConfigParam(defaultVal = AnnotationConstants.NULL_STRING)
	private String attributeName;

	@ConfigParam(defaultVal = AnnotationConstants.NULL_STRING)
	private MVELExpressionEvaluator expression;

	@ConfigParam(name = "type", defaultVal = AnnotationConstants.NULL_STRING)
	private String typeAlias;

	@ConfigParam(name = "default", defaultVal = AnnotationConstants.NULL_STRING)
	private String defaultVal;

	private BeanId recordRowId;
	private DataDecoder decoder;

	@Override
	public void visitBefore(Element element, ExecutionContext executionContext)
			throws SmooksException {
		String mapColumnName;

		if (columnName != null) {
			mapColumnName = columnName;
		} else {
			mapColumnName = DomUtils.getName(element);
		}

		if (expression != null) {
			Map<String, Object> beanMap = BeanRepositoryManager
					.getBeanRepository(executionContext).getBeanMap();
			Object dataObject = expression.getValue(beanMap);

			setDataObject(mapColumnName, dataObject, executionContext);
		} else if ( attributeName != null ){
			String dataString = DomUtils.getAttributeValue(element, attributeName);
			
			setDataObject(mapColumnName, dataString, executionContext);
		} else {
			String dataString = DomUtils.getAllText(element, false);
			
			setDataObject(mapColumnName, dataString, executionContext);
		}
	}

	@Override
	public void visitAfter(Element element, ExecutionContext executionContext)
			throws SmooksException {
	}

	@Override
	public void visitBefore(SAXElement element,
			ExecutionContext executionContext) throws SmooksException,
			IOException {
		if (attributeName == null && expression == null)
			element.accumulateText();
	}

	@Override
	public void visitAfter(SAXElement element, ExecutionContext executionContext)
			throws SmooksException, IOException {
		String mapColumnName;

		if (columnName != null) {
			mapColumnName = columnName;
		} else {
			mapColumnName = element.getName().getLocalPart();
		}

		if (expression != null) {
			Map<String, Object> beanMap = BeanRepositoryManager
					.getBeanRepository(executionContext).getBeanMap();
			Object dataObject = expression.getValue(beanMap);

			setDataObject(mapColumnName, dataObject, executionContext);
		} else if ( attributeName != null ){
			String dataString = SAXUtil.getAttribute(attributeName, element.getAttributes());
			
			setDataObject(mapColumnName, dataString, executionContext);
		} else {
			String dataString = element.getTextContent();
			
			setDataObject(mapColumnName, dataString, executionContext);
		}
	}

	@Override
	public boolean consumes(Object object) {
		if (object.equals(recordRowIdName)) {
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
		recordRowId = beanIdRegister.getBeanId(recordRowIdName);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RecordRowPopulator created for [" + recordRowIdName
					+ "]");
		}
	}

	private void setDataObject(String mapColumnName,
			Object dataObject, ExecutionContext executionContext) {
		BeanRepository beanRepo = BeanRepositoryManager
				.getBeanRepository(executionContext);

		UpdatableRecordRow recordRow = (UpdatableRecordRow) beanRepo
				.getBean(recordRowId);

		if (recordRow == null)
			throw new SmooksConfigurationException(
					"Can't populate recordRow '"
							+ recordRowId
							+ "' because there is no object in the bean context under that bean id.");

		if (dataObject instanceof String) {
			recordRow.setValue(mapColumnName, String.valueOf(decodeDataString((String) dataObject, executionContext)));
		} else  {
			recordRow.setValue(mapColumnName, String.valueOf(dataObject));
		}			
	}

	private Object decodeDataString(String dataString,
			ExecutionContext executionContext) throws DataDecodeException {
		if ((dataString == null || dataString.equals("")) && defaultVal != null) {
			if (defaultVal.equals("null")) {
				return null;
			}
			dataString = defaultVal;
		}

		if (decoder == null) {
			decoder = getDecoder(executionContext);
		}

		try {
			return decoder.decode(dataString);
		} catch (DataDecodeException e) {
			throw new DataDecodeException("Failed to decode binding value '"
					+ dataString + "' for column '" + columnName
					+ "' on bean '" + recordRowId.getName() + "'.", e);
		}
	}

	private DataDecoder getDecoder(ExecutionContext executionContext)
			throws DataDecodeException {
		@SuppressWarnings("unchecked")
		List decoders = executionContext.getDeliveryConfig().getObjects(
				"decoder:" + typeAlias);

		if (decoders == null || decoders.isEmpty()) {
			decoder = DataDecoder.Factory.create(typeAlias);
		} else if (!(decoders.get(0) instanceof DataDecoder)) {
			throw new DataDecodeException("Configured decoder '" + typeAlias
					+ ":" + decoders.get(0).getClass().getName()
					+ "' is not an instance of " + DataDecoder.class.getName());
		} else {
			decoder = (DataDecoder) decoders.get(0);
		}

		return decoder;
	}

}
