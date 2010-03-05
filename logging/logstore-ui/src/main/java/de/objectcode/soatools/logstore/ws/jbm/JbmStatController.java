package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.QueryExp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.jms.server.messagecounter.MessageCounter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Name("messageStatisticsController")
@Scope(ScopeType.CONVERSATION)
public class JbmStatController implements Serializable {
	private static final long serialVersionUID = 2417094693685338583L;

	private final static Log LOG = LogFactory.getLog(JbmStatController.class);

	public final static String VIEW_ID = "/secure/messageStatistics.xhtml";

	private List<JbmQueueService> queueServiceList = null;

	@DataModel
	List<JbmQueueStat> messageStatistics;

	@Factory("messageStatistics")
	public void getMessageStatistics() {
		synchronized (this) {
			if (queueServiceList == null) {
				queueServiceList = queryMBeans();
			}

			List<JbmQueueStat> result = new ArrayList<JbmQueueStat>();

			final MBeanServer server = (MBeanServer) MBeanServerFactory
					.findMBeanServer(null).get(0);

			for (JbmQueueService queueService : queueServiceList) {
				try {
					MessageCounter counter = (MessageCounter) server
							.getAttribute(queueService.getServiceName(),
									"MessageCounter");

					result.add(new JbmQueueStat(queueService, counter
							.getCount(), counter.getCountDelta(), counter
							.getMessageCount(), counter.getMessageCountDelta(),
							counter.getLastUpdate()));
				} catch (Exception e) {
					LOG.error("Exception on " + queueService.getServiceName(),
							e);
				}
			}

			messageStatistics = result;
		}
	}

	@End
	public String enter() {
		return VIEW_ID;
	}

	public String refresh() {
		return VIEW_ID;
	}

	private List<JbmQueueService> queryMBeans() {
		final MBeanServer server = (MBeanServer) MBeanServerFactory
				.findMBeanServer(null).get(0);

		Set<ObjectName> names = server.queryNames(null, new QueryExp() {
			private static final long serialVersionUID = 1L;

			MBeanServer current;

			public void setMBeanServer(MBeanServer s) {
				current = s;
			}

			public boolean apply(ObjectName name)
					throws BadStringOperationException,
					BadBinaryOpValueExpException,
					BadAttributeValueExpException, InvalidApplicationException {

				String serviceName = name.getKeyProperty("service");

				if (serviceName != null && "Queue".equals(serviceName)) {
					try {
						MBeanInfo info = current.getMBeanInfo(name);

						for (MBeanAttributeInfo attribute : info
								.getAttributes()) {
							if ("MessageCounter".equals(attribute.getName()))
								return true;
						}
					} catch (Exception e) {
					}
				}
				return false;
			}
		});
		List<JbmQueueService> result = new ArrayList<JbmQueueService>();

		for (ObjectName name : names) {
			result.add(new JbmQueueService(name.getKeyProperty("name"), name));
		}

		Collections.sort(result, new Comparator<JbmQueueService>() {
			public int compare(JbmQueueService o1, JbmQueueService o2) {
				return o1.getQueueName().compareTo(o2.getQueueName());
			}
		});

		return result;
	}

	private List<JbmQueueService> readConfig() {
		List<JbmQueueService> result = new ArrayList<JbmQueueService>();

		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(getClass().getResourceAsStream(
					"jbm-queues.xml"));
			Element root = document.getRootElement();

			Iterator<?> it = root.elementIterator();
			while (it.hasNext()) {
				Element queueElement = (Element) it.next();

				result.add(new JbmQueueService(queueElement
						.attributeValue("name"), new ObjectName(queueElement
						.attributeValue("service-name"))));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception", e);
		}

		return result;
	}

	public static void main(String[] args) {
		JbmStatController cont = new JbmStatController();

		System.out.println(">> " + cont.readConfig());
	}
}
