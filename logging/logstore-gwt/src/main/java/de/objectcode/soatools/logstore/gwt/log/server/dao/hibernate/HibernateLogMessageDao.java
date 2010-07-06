package de.objectcode.soatools.logstore.gwt.log.server.dao.hibernate;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.soatools.logstore.gwt.log.server.dao.ILogMessageDao;

@Repository("logMessageDao")
@Transactional(propagation = Propagation.MANDATORY)
public class HibernateLogMessageDao implements
		ILogMessageDao {
	@Resource
	private SessionFactory sessionFactory;
}
