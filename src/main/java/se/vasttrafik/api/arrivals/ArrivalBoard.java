//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.18 at 03:30:56 PM CEST 
//


package se.vasttrafik.api.arrivals;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element ref="{http://www.vasttrafik.se/api/arrivals}Arrival"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.vasttrafik.se/api/arrivals}attlist.ArrivalBoard"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "arrival"
})
@XmlRootElement(name = "ArrivalBoard")
public class ArrivalBoard {

    @XmlElement(name = "Arrival")
    protected List<Arrival> arrival;
    @XmlAttribute(name = "error")
    protected String error;
    @XmlAttribute(name = "servertime")
    @XmlSchemaType(name = "anySimpleType")
    protected String servertime;
    @XmlAttribute(name = "serverdate")
    @XmlSchemaType(name = "anySimpleType")
    protected String serverdate;

    /**
     * Gets the value of the arrival property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arrival property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArrival().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Arrival }
     * 
     * 
     */
    public List<Arrival> getArrival() {
        if (arrival == null) {
            arrival = new ArrayList<Arrival>();
        }
        return this.arrival;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setError(String value) {
        this.error = value;
    }

    /**
     * Gets the value of the servertime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServertime() {
        return servertime;
    }

    /**
     * Sets the value of the servertime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServertime(String value) {
        this.servertime = value;
    }

    /**
     * Gets the value of the serverdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerdate() {
        return serverdate;
    }

    /**
     * Sets the value of the serverdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerdate(String value) {
        this.serverdate = value;
    }

}
