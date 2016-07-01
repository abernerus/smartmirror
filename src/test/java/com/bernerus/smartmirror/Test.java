package com.bernerus.smartmirror;

import com.bernerus.smartmirror.util.YrXmlParser;
import org.apache.commons.io.IOUtils;

import java.io.File;

/**
 * Created by andreas on 30/06/16.
 */
public class Test {

  @org.junit.Test
  public void testXml() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();

    String xml = IOUtils.toString(classLoader.getResourceAsStream("testweather.xml"), "UTF-8");
    YrXmlParser.readYrXml(xml);
  }
}
