package de.objectcode.soatools.mfm.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.internal.soa.esb.message.format.serialized.SerializedMessagePlugin;
import org.jboss.soa.esb.message.Message;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import com.thoughtworks.xstream.XStream;

import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.collector.MessageBodyCollector;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;
import de.objectcode.soatools.mfm.io.XMLIO;

@Test(groups = "message-format-test")
public class MessageFormatTest
{
  IMessageFormatRepository messageFormatRepository;

  @BeforeClass
  public void initialize() throws Exception
  {
    MessageFormatModel messageFormatModel = XMLIO.INSTANCE.read(getClass().getResourceAsStream("message-format.xml"));

    messageFormatRepository = RepositoryTest.getInstance();
    messageFormatRepository.registerModel(messageFormatModel);
  }

  private Message createMessage()
  {
    return new SerializedMessagePlugin().getMessage();
  }

  @Test
  public void messageWithMaps() throws Exception
  {
    Message message = createMessage();
    Message resultMessage = createMessage();

    Map<String, Object> value1 = new HashMap<String, Object>();

    value1.put("val1", "Test1");
    value1.put("val2", 1);

    Map<String, Object> value2 = new HashMap<String, Object>();

    value2.put("val1", "Test2");
    value2.put("val2", 2);
    value2.put("arr1", new String[] { "Arr1", "Arr2" });
    value2.put("arr2", new Integer[] { 1, 2, 3 });
    value2.put("sub1.subVal1", "SubTest21");
    value2.put("sub1.subVal2", 21);

    message.getBody().add("value1", value1);
    message.getBody().add("value2", value2);

    MessageFormat messageFormat = messageFormatRepository.getMessageFormat("message-type-1", 1);

    assertNotNull(messageFormat);

    messageFormat.normalizeMessage(message, new MessageBodyCollector(resultMessage), messageFormatRepository);

    checkResultMessage(resultMessage);
  }

  @Test
  public void messageWithBeans() throws Exception
  {
    Message message = createMessage();
    Message resultMessage = createMessage();

    message.getBody().add("value1", new Type1("Test1", 1));
    message.getBody()
        .add(
            "value2",
            new Type2("Test2", 2, new String[] { "Arr1", "Arr2" }, new Integer[] { 1, 2, 3 }, new SubType1("SubTest21",
                21)));

    MessageFormat messageFormat = messageFormatRepository.getMessageFormat("message-type-1", 1);

    assertNotNull(messageFormat);

    messageFormat.normalizeMessage(message, new MessageBodyCollector(resultMessage), messageFormatRepository);

    checkResultMessage(resultMessage);
  }

  @Test
  public void messageWithXStreamXML() throws Exception
  {
    XStream xstream = new XStream();
    Message message = createMessage();
    Message resultMessage = createMessage();

    message.getBody().add("value1", "<?xml version=\"1.0\"?>" + xstream.toXML(new Type1("Test1", 1)));
    message.getBody().add(
        "value2",
        "<?xml version=\"1.0\"?>"
            + xstream.toXML(new Type2("Test2", 2, new String[] { "Arr1", "Arr2" }, new Integer[] { 1, 2, 3 },
                new SubType1("SubTest21", 21))));

    MessageFormat messageFormat = messageFormatRepository.getMessageFormat("message-type-1", 1);

    assertNotNull(messageFormat);

    messageFormat.normalizeMessage(message, new MessageBodyCollector(resultMessage), messageFormatRepository);

    checkResultMessage(resultMessage);
  }

  @Test
  public void messageWidhFullXStreamXML() throws Exception
  {
    XStream xstream = new XStream();
    Message message = createMessage();
    Message resultMessage = createMessage();

    message.getBody().add(
        "<?xml version=\"1.0\"?>"
            + xstream.toXML(new MessageType1(new Type1("Test1", 1), new Type2("Test2", 2,
                new String[] { "Arr1", "Arr2" }, new Integer[] { 1, 2, 3 }, new SubType1("SubTest21", 21)))));

    MessageFormat messageFormat = messageFormatRepository.getMessageFormat("message-type-1", 1);

    assertNotNull(messageFormat);

    messageFormat.normalizeMessage(message, new MessageBodyCollector(resultMessage), messageFormatRepository);

    checkResultMessage(resultMessage);
  }

  private void checkResultMessage(Message resultMessage)
  {
    assertNotNull(resultMessage.getBody().get("value1"));
    assertTrue(resultMessage.getBody().get("value1") instanceof NormalizedData);
    assertNotNull(resultMessage.getBody().get("value2"));
    assertTrue(resultMessage.getBody().get("value2") instanceof NormalizedData);

    NormalizedData normValue1 = (NormalizedData) resultMessage.getBody().get("value1");
    NormalizedData normValue2 = (NormalizedData) resultMessage.getBody().get("value2");

    assertEquals(normValue1.get("val1"), "Test1");
    assertEquals(normValue1.get("val2"), 1);

    assertEquals(normValue2.get("val1"), "Test2");
    assertEquals(normValue2.get("val2"), 2);
    assertEquals(normValue2.get("arr1"), Arrays.asList("Arr1", "Arr2"));
    assertEquals(normValue2.get("arr2"), Arrays.asList(1, 2, 3));
    assertNotNull(normValue2.get("sub1"));
    assertEquals(((NormalizedData) normValue2.get("sub1")).get("subVal1"), "SubTest21");
    assertEquals(((NormalizedData) normValue2.get("sub1")).get("subVal2"), 21);
  }

  public static class MessageType1 implements Serializable
  {
    private static final long serialVersionUID = 1L;

    Type1 value1;
    Type2 value2;

    public MessageType1(Type1 value1, Type2 value2)
    {
      this.value1 = value1;
      this.value2 = value2;
    }

    public Type1 getValue1()
    {
      return value1;
    }

    public void setValue1(Type1 value1)
    {
      this.value1 = value1;
    }

    public Type2 getValue2()
    {
      return value2;
    }

    public void setValue2(Type2 value2)
    {
      this.value2 = value2;
    }
  }

  public static class Type1 implements Serializable
  {
    private static final long serialVersionUID = 1L;

    String val1;
    int val2;

    public Type1(String val1, int val2)
    {
      this.val1 = val1;
      this.val2 = val2;
    }

    public String getVal1()
    {
      return val1;
    }

    public void setVal1(String val1)
    {
      this.val1 = val1;
    }

    public int getVal2()
    {
      return val2;
    }

    public void setVal2(int val2)
    {
      this.val2 = val2;
    }
  }

  public static class Type2 implements Serializable
  {
    private static final long serialVersionUID = 1L;
    String val1;
    int val2;
    String[] arr1;
    Integer[] arr2;
    SubType1 sub1;

    public Type2(String val1, int val2, String[] arr1, Integer[] arr2, SubType1 sub1)
    {
      this.val1 = val1;
      this.val2 = val2;
      this.arr1 = arr1;
      this.arr2 = arr2;
      this.sub1 = sub1;
    }

    public String getVal1()
    {
      return val1;
    }

    public void setVal1(String val1)
    {
      this.val1 = val1;
    }

    public int getVal2()
    {
      return val2;
    }

    public void setVal2(int val2)
    {
      this.val2 = val2;
    }

    public String[] getArr1()
    {
      return arr1;
    }

    public void setArr1(String[] arr1)
    {
      this.arr1 = arr1;
    }

    public Integer[] getArr2()
    {
      return arr2;
    }

    public void setArr2(Integer[] arr2)
    {
      this.arr2 = arr2;
    }

    public SubType1 getSub1()
    {
      return sub1;
    }

    public void setSub1(SubType1 sub1)
    {
      this.sub1 = sub1;
    }
  }

  public static class SubType1 implements Serializable
  {
    private static final long serialVersionUID = -1501395507182047487L;
    String subVal1;
    int subVal2;

    public SubType1(String subVal1, int subVal2)
    {
      super();
      this.subVal1 = subVal1;
      this.subVal2 = subVal2;
    }

    public String getSubVal1()
    {
      return subVal1;
    }

    public void setSubVal1(String subVal1)
    {
      this.subVal1 = subVal1;
    }

    public int getSubVal2()
    {
      return subVal2;
    }

    public void setSubVal2(int subVal2)
    {
      this.subVal2 = subVal2;
    }

  }
}
