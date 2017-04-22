package com.bernerus.smartmirror.util;

import com.bernerus.smartmirror.dto.yr.YrPrecipitation;
import com.bernerus.smartmirror.dto.yr.YrWeather;
import com.bernerus.smartmirror.dto.yr.YrWeatherData;
import com.bernerus.smartmirror.dto.yr.YrWeatherSymbol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

/**
 * Created by andreas on 30/06/16.
 */
public final class YrXmlParser {
  private YrXmlParser() {
    //Utility Class
  }

  public static YrWeather readYrXml(final String xml) {
    try {
      InputSource inputXml = new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8")));

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputXml);

      //optional, but recommended
      //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
      doc.getDocumentElement().normalize();

      //Time element is where all weather information is stored per time interval.
      NodeList nList = doc.getElementsByTagName("time");

      YrWeather weather = new YrWeather();
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          LocalDateTime from = LocalDateTime.parse(eElement.getAttribute("from"));
          LocalDateTime to = LocalDateTime.parse(eElement.getAttribute("to"));
          YrWeatherData weatherData = new YrWeatherData(from, to);

          Element symbolElement = (Element) eElement.getElementsByTagName("symbol").item(0);
          weatherData.setSymbol(new YrWeatherSymbol(symbolElement.getAttribute("name"), Integer.parseInt(symbolElement.getAttribute("number")), symbolElement.getAttribute("var")));

          Element precipitationElement = (Element) eElement.getElementsByTagName("precipitation").item(0);
          weatherData.setPrecipitation(new YrPrecipitation(
            precipitationElement.getAttribute("value"),
            precipitationElement.getAttribute("minvalue"),
            precipitationElement.getAttribute("maxvalue"))
          );

          Element temperatureElement = (Element) eElement.getElementsByTagName("temperature").item(0);
          weatherData.setTemperature(temperatureElement.getAttribute("value"));

          weather.getWeatherDatas().add(weatherData);
        }
      }
      return weather;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
