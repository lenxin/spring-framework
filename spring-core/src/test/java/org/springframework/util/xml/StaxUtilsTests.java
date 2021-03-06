package org.springframework.util.xml;

import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StaxUtilsTests {

	@Test
	public void isStaxSourceInvalid() throws Exception {
		assertFalse("A StAX Source", StaxUtils.isStaxSource(new DOMSource()));
		assertFalse("A StAX Source", StaxUtils.isStaxSource(new SAXSource()));
		assertFalse("A StAX Source", StaxUtils.isStaxSource(new StreamSource()));
	}

	@Test
	public void isStaxSource() throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		String expected = "<element/>";
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new StringReader(expected));
		Source source = StaxUtils.createCustomStaxSource(streamReader);

		assertTrue("Not a StAX Source", StaxUtils.isStaxSource(source));
	}

	@Test
	public void isStaxSourceJaxp14() throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		String expected = "<element/>";
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(new StringReader(expected));
		StAXSource source = new StAXSource(streamReader);

		assertTrue("Not a StAX Source", StaxUtils.isStaxSource(source));
	}

	@Test
	public void isStaxResultInvalid() throws Exception {
		assertFalse("A StAX Result", StaxUtils.isStaxResult(new DOMResult()));
		assertFalse("A StAX Result", StaxUtils.isStaxResult(new SAXResult()));
		assertFalse("A StAX Result", StaxUtils.isStaxResult(new StreamResult()));
	}

	@Test
	public void isStaxResult() throws Exception {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(new StringWriter());
		Result result = StaxUtils.createCustomStaxResult(streamWriter);

		assertTrue("Not a StAX Result", StaxUtils.isStaxResult(result));
	}

	@Test
	public void isStaxResultJaxp14() throws Exception {
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(new StringWriter());
		StAXResult result = new StAXResult(streamWriter);

		assertTrue("Not a StAX Result", StaxUtils.isStaxResult(result));
	}

}
