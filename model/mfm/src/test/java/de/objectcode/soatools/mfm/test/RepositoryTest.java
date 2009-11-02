package de.objectcode.soatools.mfm.test;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.testng.annotations.BeforeSuite;

import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.impl.MessageFormatRepositoryImpl;

public class RepositoryTest {
	private static IMessageFormatRepository instance;

	public static IMessageFormatRepository getInstance() {
		return instance;
	}

	@BeforeSuite
	public void createTestRepository() throws Exception {
		AnnotationConfiguration cfg = new AnnotationConfiguration();

		cfg.configure("/de/objectcode/soatools/mfm/test/hibernate.cfg.xml");

		SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);

		schemaUpdate.execute(true, true);

		instance = new MessageFormatRepositoryImpl(cfg.buildSessionFactory());
	}
}
