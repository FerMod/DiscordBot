package discordbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import discordbot.stattrack.UserStats;
import discordbot.stattrack.UserStats.Stat;
import sx.blah.discord.Discord4J;

public class XMLWriter {

	private File file;
	private String rootElement;
	private Map<Long, UserStats> elementsMap;
	
	public static void main(String[] args) {
		new XMLWriter();
	}

	public XMLWriter() {
		file = new File("stats.xml");
		rootElement = "users";
		elementsMap = Collections.synchronizedMap(new HashMap<>());
		createFile();
		//writeXML(filePath, rootElement, elementsMap);
	}

	public void update(String stat, UserStats userStats){
//		try {
//			createFile();
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document doc = builder.parse(file);
//			XPathFactory xPathfactory = XPathFactory.newInstance();
//			XPath xpath = xPathfactory.newXPath();
//			XPathExpression expr = xpath.compile("//Type[@id=\"" + userStats.getUser().getLongID() + "\"]");
//			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//			for (int i = 0; i < nodeList.getLength()-1; i++) {
//				if(nodeList.item(i).getLocalName().equals("stat")) {
//					nodeList.item(i).setNodeValue(String.valueOf(userStats.getStatsMap().get(stat)));
//				}
//			}
			
//			XMLInputFactory inFactory = XMLInputFactory.newInstance();
//			XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(file));
//
//			XMLOutputFactory factory = XMLOutputFactory.newInstance();
//			XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(file));
//			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//			while (eventReader.hasNext()) {
//				XMLEvent event = eventReader.nextEvent();
//				writer.add(event);
//				if (event.getEventType() == XMLEvent.START_ELEMENT && event.asStartElement().isAttribute() && event.asStartElement().getName().toString().equals("id")) {
////					if () {
////						writer.
////						writer.add(eventFactory.createStartElement("", null, "index"));
////						writer.add(eventFactory.createEndElement("", null, "index"));
////					}
//				}
//			}
//			writer.close();
			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (XPathExpressionException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		}
	}

	public void createFile() {
		try {
			
			file.delete(); //TODO TEMPORAL
			
			if(file.createNewFile()) {
				FileOutputStream fileOutputStream = new FileOutputStream(file, false); 
				StringWriter stringWriter = new StringWriter();

				XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();			
				XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

				xmlStreamWriter.writeStartDocument();
				xmlStreamWriter.writeStartElement("users");

				for(Long key : elementsMap.keySet()){
					xmlStreamWriter.writeStartElement("user");			
					xmlStreamWriter.writeAttribute("id", String.valueOf(key));

					for(Stat stat : elementsMap.get(key).getStatsMap().keySet()){
						xmlStreamWriter.writeStartElement(stat.toString().toLowerCase());			
						xmlStreamWriter.writeCharacters(String.valueOf(elementsMap.get(key).getStatsMap().get(stat)));
						xmlStreamWriter.writeEndElement();
					}

					xmlStreamWriter.writeEndElement();
				}

				xmlStreamWriter.writeEndElement();
				xmlStreamWriter.writeEndDocument();

				xmlStreamWriter.flush();
				xmlStreamWriter.close();

				String xmlString = stringWriter.getBuffer().toString();
				stringWriter.close();

				try {					
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
					transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(fileOutputStream));
					Discord4J.LOGGER.debug(getFormatedXml(xmlString));
				} catch (TransformerException e) {
					e.printStackTrace();
				} finally {
					fileOutputStream.close();
				}
			}

		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getFormatedXml(String xmlString) throws XMLStreamException, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		Writer out = new StringWriter();		
		transformer.transform(new StreamSource(new StringReader(xmlString)), new StreamResult(out));
		return out.toString();
	}

	private static void createNode(XMLEventWriter eventWriter, String element, String value) throws XMLStreamException {

		XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
		XMLEvent end = xmlEventFactory.createDTD("\n");
		XMLEvent tab = xmlEventFactory.createDTD("\t");

		//Create Start node
		StartElement startElement = xmlEventFactory.createStartElement("", "", element);
		eventWriter.add(tab);
		eventWriter.add(startElement);

		//Create Content
		Characters characters = xmlEventFactory.createCharacters(value);
		eventWriter.add(characters);

		// Create End node
		EndElement endElement = xmlEventFactory.createEndElement("", "", element);
		eventWriter.add(endElement);
		eventWriter.add(end);

	}

}
