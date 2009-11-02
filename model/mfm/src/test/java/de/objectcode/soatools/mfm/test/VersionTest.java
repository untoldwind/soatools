package de.objectcode.soatools.mfm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.accessor.Dom4jDataAccessor;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;
import de.objectcode.soatools.mfm.io.XMLIO;


@Test(groups = "version-test", dependsOnGroups = "simple-test")
public class VersionTest
{
  IMessageFormatRepository messageFormatRepository;

  @BeforeClass
  public void initialize() throws Exception
  {
    MessageFormatModel messageFormatModel = XMLIO.INSTANCE.read(getClass().getResourceAsStream("version-mfm.xml"));

    messageFormatRepository = RepositoryTest.getInstance();
    messageFormatRepository.registerModel(messageFormatModel);

  }

  @Test
  public void type31Xml() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test3", 1);

    assertNotNull(type);

    SAXReader reader = new SAXReader();

    Document doc1 = reader.read(getClass().getResourceAsStream("data-test3.1.xml"));

    NormalizedData data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc1.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 1);
    assertEquals(data.size(), 2);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);

    Document doc2 = reader.read(getClass().getResourceAsStream("data-test3.2.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc2.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 1);
    assertEquals(data.size(), 2);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);

    Document doc3 = reader.read(getClass().getResourceAsStream("data-test3.3.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc3.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 1);
    assertEquals(data.size(), 2);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);

    Document doc4 = reader.read(getClass().getResourceAsStream("data-test3.4.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc4.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 1);
    assertEquals(data.size(), 2);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 5555);
  }

  @Test
  public void type32Xml() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test3", 2);

    assertNotNull(type);

    SAXReader reader = new SAXReader();

    Document doc1 = reader.read(getClass().getResourceAsStream("data-test3.1.xml"));

    NormalizedData data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc1.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 2);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 default");

    Document doc2 = reader.read(getClass().getResourceAsStream("data-test3.2.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc2.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 2);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc3 = reader.read(getClass().getResourceAsStream("data-test3.3.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc3.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 2);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc4 = reader.read(getClass().getResourceAsStream("data-test3.4.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc4.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 2);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1"), "Value1");
    assertEquals(data.get("val2"), 5555);
    assertEquals(data.get("val3"), "val3 non-default");
  }

  @Test
  public void type33Xml() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test3", 3);

    assertNotNull(type);

    SAXReader reader = new SAXReader();

    Document doc1 = reader.read(getClass().getResourceAsStream("data-test3.1.xml"));

    NormalizedData data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc1.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 3);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 default");

    Document doc2 = reader.read(getClass().getResourceAsStream("data-test3.2.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc2.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 3);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc3 = reader.read(getClass().getResourceAsStream("data-test3.3.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc3.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 3);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc4 = reader.read(getClass().getResourceAsStream("data-test3.4.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc4.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 3);
    assertEquals(data.size(), 3);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 5555);
    assertEquals(data.get("val3"), "val3 non-default");
  }

  @Test
  public void type34Xml() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test3", 4);

    assertNotNull(type);

    SAXReader reader = new SAXReader();

    Document doc1 = reader.read(getClass().getResourceAsStream("data-test3.1.xml"));

    NormalizedData data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc1.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 4);
    assertEquals(data.size(), 4);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val2add"), 0);
    assertEquals(data.get("val3"), "val3 default");

    Document doc2 = reader.read(getClass().getResourceAsStream("data-test3.2.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc2.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 4);
    assertEquals(data.size(), 4);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val2add"), 0);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc3 = reader.read(getClass().getResourceAsStream("data-test3.3.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc3.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 4);
    assertEquals(data.size(), 4);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val2add"), 0);
    assertEquals(data.get("val3"), "val3 non-default");

    Document doc4 = reader.read(getClass().getResourceAsStream("data-test3.4.xml"));

    data = new NormalizedData();
    type.normalize(new Dom4jDataAccessor(doc4.getRootElement()), data, messageFormatRepository);

    assertEquals(data.getType(), "test3");
    assertEquals(data.getVersion(), 4);
    assertEquals(data.size(), 4);
    assertEquals(data.get("val1NewName"), "Value1");
    assertEquals(data.get("val2"), 1234);
    assertEquals(data.get("val2add"), 4321);
    assertEquals(data.get("val3"), "val3 non-default");
  }
}
