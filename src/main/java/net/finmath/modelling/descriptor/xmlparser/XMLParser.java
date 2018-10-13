package net.finmath.modelling.descriptor.xmlparser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import net.finmath.modelling.ProductDescriptor;

/**
 * Interface for XML parsers creating a product descriptor from an XML file.
 *
 * @author Christian Fries
 * @author Roland Bachl
 */
public interface XMLParser {

	ProductDescriptor getProductDescriptor(File file) throws SAXException, IOException, ParserConfigurationException;

}
