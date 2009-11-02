package de.objectcode.soatools.mfm.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import de.objectcode.soatools.mfm.api.BooleanType;
import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.DateType;
import de.objectcode.soatools.mfm.api.IntegerType;
import de.objectcode.soatools.mfm.api.LongType;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.StringType;
import de.objectcode.soatools.mfm.api.TypeRef;
import de.objectcode.soatools.mfm.io.XMLIO;


public class GenSchema
{
  public static void main(String[] args)
  {
    try {
      JAXBContext context = JAXBContext.newInstance(MessageFormat.class, StringType.class, IntegerType.class, LongType.class,
          TypeRef.class, DateType.class, BooleanType.class, ComponentType.class, MessageFormat.class,
          MessageFormatModel.class);

      final StringWriter out = new StringWriter();
      context.generateSchema(new SchemaOutputResolver() {

        @Override
        public Result createOutput(String arg0, String arg1) throws IOException
        {
          System.out.println(arg0 + " " + arg1);
          StreamResult result = new StreamResult(out);
          result.setSystemId(arg0);
          return result;
        }

      });
      System.out.println(out.toString());

      MessageFormatModel messageFormatModel = new MessageFormatModel();
      messageFormatModel.setTypes(new ArrayList<ComponentType>());
      ComponentType type = new ComponentType();
      type.setName("test1");
      type.setVersion(1);
      messageFormatModel.getTypes().add(type);
      type.setComponents(new HashSet<Component>());
      Component component = new Component();
      component.setName("val1");
      component.setType(new StringType());
      type.getComponents().add(component);

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      XMLIO.INSTANCE.write(messageFormatModel, bos);
      System.out.println(bos.toString("UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
