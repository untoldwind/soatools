package de.objectcode.soatools.util.smooks.recordset;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.milyn.SmooksException;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.annotation.AppContext;
import org.milyn.cdr.annotation.ConfigParam;
import org.milyn.container.ApplicationContext;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.annotation.Initialize;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.milyn.delivery.ordering.Producer;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXVisitAfter;
import org.milyn.delivery.sax.SAXVisitBefore;
import org.milyn.javabean.repository.BeanId;
import org.milyn.javabean.repository.BeanIdRegister;
import org.milyn.javabean.repository.BeanRepository;
import org.milyn.javabean.repository.BeanRepositoryManager;
import org.milyn.util.CollectionsUtil;
import org.w3c.dom.Element;

import de.objectcode.soatools.util.recordset.RecordSetBuilder;

public class RecordSetCreator implements DOMElementVisitor, SAXVisitBefore, SAXVisitAfter, Producer{
    private static Log LOGGER = LogFactory.getLog(RecordSetCreator.class);
    
    @AppContext
    private ApplicationContext appContext;

    @ConfigParam(name="recordSetId")
    private String recordSetIdName;
    
    @ConfigParam(name="recordSetName")
    private String recordSetName;
    
    @ConfigParam(name="recordSetType")
    private String recordSetType;

    private BeanId recordSetId;
    
	@Override
	public void visitBefore(Element element, ExecutionContext executionContext)
			throws SmooksException {
        createAndSetRecordSet(executionContext);
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
        createAndSetRecordSet(executionContext);
	}

	@Override
	public void visitAfter(SAXElement element, ExecutionContext executionContext)
			throws SmooksException, IOException {
		visitAfter(executionContext);
	}

	@Override
	public Set<? extends Object> getProducts() {
        return CollectionsUtil.toSet(recordSetIdName);
	}
	
    @Initialize
    public void initialize() throws SmooksConfigurationException {
    	BeanRepositoryManager beanRepositoryManager = BeanRepositoryManager.getInstance(appContext);
    	BeanIdRegister beanIdRegister = beanRepositoryManager.getBeanIdRegister();
        recordSetId = beanIdRegister.register(recordSetIdName);

        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("RecordSetCreator created for [" + recordSetIdName + "]");
        }
    }

	private void createAndSetRecordSet(ExecutionContext executionContext) {
        BeanRepository beanRepo = BeanRepositoryManager.getBeanRepository(executionContext);
        
        RecordSetBuilder recordSetBuilder = new RecordSetBuilder(recordSetName, recordSetType);
        
        beanRepo.setBeanInContext(recordSetId, false);
        beanRepo.addBean(recordSetId, recordSetBuilder);
        beanRepo.setBeanInContext(recordSetId, true);
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RecordSetBuilder [" + recordSetIdName + "] instance created.");
        }   
	}
	
	private void visitAfter(ExecutionContext executionContext) {
        BeanRepository beanRepo = BeanRepositoryManager.getBeanRepository(executionContext);

        beanRepo.setBeanInContext(recordSetId, false);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RecordSetBuilder [" + recordSetIdName + "] instance removed from context.");
        }   
	}
}
