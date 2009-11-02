package de.objectcode.soatools.mfm.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.ValidationException;
import de.objectcode.soatools.mfm.api.accessor.DataAccessorFactory;
import de.objectcode.soatools.mfm.api.accessor.Dom4jDataAccessor;
import de.objectcode.soatools.mfm.io.XMLIO;


@Test(groups = "simple-test")
public class SimpleTest
{
  IMessageFormatRepository messageFormatRepository;

  @BeforeClass
  public void initialize() throws Exception
  {
    MessageFormatModel messageFormatModel = XMLIO.INSTANCE.read(getClass().getResourceAsStream("simple-mfm.xml"));

    messageFormatRepository = RepositoryTest.getInstance();
    messageFormatRepository.registerModel(messageFormatModel);

  }

  @Test
  public void simple1Map() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test1", 1);

    assertNotNull(type);

    Map<String, Object> data = new HashMap<String, Object>();
    data.put("val1", "Test1");
    data.put("val2", 1);

    type.validate(DataAccessorFactory.INSTANCE.getAccessor(data));

    Map<String, Object> data2 = new HashMap<String, Object>();
    data.put("val2", 1);

    boolean thrown = false;
    try {
      type.validate(DataAccessorFactory.INSTANCE.getAccessor(data2));
    } catch (ValidationException e) {
      thrown = true;
    }
    assertTrue(thrown);
  }

  @Test
  public void simple1Xml() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test1", 1);

    assertNotNull(type);

    SAXReader reader = new SAXReader();

    Document doc1 = reader.read(getClass().getResourceAsStream("simple1-data-ok.xml"));

    type.validate(new Dom4jDataAccessor(doc1.getRootElement()));

    Document doc2 = reader.read(getClass().getResourceAsStream("simple1-data-nok.xml"));

    boolean thrown = false;
    try {
      type.validate(new Dom4jDataAccessor(doc2.getRootElement()));
    } catch (ValidationException e) {
      thrown = true;
    }
    assertTrue(thrown);
  }

  @Test
  public void simple2Map() throws Exception
  {
    ComponentType type = messageFormatRepository.getComplexType("test2", 1);

    assertNotNull(type);

  }
}
