package de.objectcode.soatools.mfm.test;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibGenSchema {
	public static void main(String[] args) {
		try {
			AnnotationConfiguration cfg = new AnnotationConfiguration();

			cfg.configure("/de/objectcode/soatools/mfm/test/hibernate.cfg.xml");

			SchemaExport export = new SchemaExport(cfg);

			export.create(true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
