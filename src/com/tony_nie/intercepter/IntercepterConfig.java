package com.tony_nie.intercepter;

import java.io.File; 
import java.io.IOException; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException; 
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class IntercepterConfig { 
	DocumentBuilderFactory builderFactory;
	Document document;
	static final String BLACKLIST = "blacklist";
	static final String WHITELIST = "whitelist";
	static final String PHONENUMBER = "phoneNumber";
	static final String INTERCEPTER = "intercepter";
	private String xmlPath;


	public IntercepterConfig(String xml) {
		xmlPath = xml;
		init(xml);
	}

	public IntercepterConfig() {
		xmlPath = INTERCEPTER + ".xml";
		init(xmlPath);
	}

	private void saveConfig(Document doc, String filePath) {
		TransformerFactory tf = TransformerFactory.newInstance();

		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
			PrintWriter pw = new PrintWriter(new FileOutputStream(filePath));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (Exception e) {
		}
	}

	private void createConfigNode(Document doc) {
		Element root = doc.createElement(xmlPath);
		doc.appendChild(root);

		Element blacklist = doc.createElement(BLACKLIST);
		blacklist.setAttribute("enable", "true");
		root.appendChild(blacklist);

		Element whitelist = doc.createElement(WHITELIST);
		whitelist.setAttribute("enable", "true");
		root.appendChild(whitelist);
	}

	private int createConfigFile(String filePath) {
		DocumentBuilder db;
		Document doc;

		try {
			db = builderFactory.newDocumentBuilder(); 
			doc = db.newDocument();
			createConfigNode(doc);
			saveConfig(doc, filePath);

		} catch (Exception e) {

		}


		return 0;
	}

	private void init(String filePath) {
		builderFactory = DocumentBuilderFactory.newInstance(); 
		File file = new File(filePath);


		if (!file.isFile())
			createConfigFile(filePath);
		try { 
			/* DOM parser instance */
			DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
			/* parse an XML file into a DOM tree  */
			document = builder.parse(new File(filePath)); 
		} catch (ParserConfigurationException e) { 
			e.printStackTrace();  
		} catch (SAXException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
	}

	private int removePhoneNumber(String type, String phone) {
		Element root = document.getDocumentElement();
		NodeList nodelist = root.getElementsByTagName(type);
		Element element = (Element) nodelist.item(0);
		String text = element.getTextContent();

		if (text.indexOf(phone) < 0)
			return -1;

		NodeList childs = element.getElementsByTagName(PHONENUMBER);
		Element removes[] = new Element[childs.getLength()];

		for(int j = 0; j < childs.getLength(); j++) {
			Element child = (Element) childs.item(j);
			if (child.getTextContent().indexOf(phone) >= 0)
				/* element.removeChild(child); */
				removes[j] = child;
			else
				removes[j] = null;
		}

		for(int i = 0; i < removes.length; i++) {
			if (removes[i] != null)
				element.removeChild(removes[i]);
		}

		saveConfig(document, xmlPath);

		return 0;
	}

	public void removeNumberFromBlacklist(String phone) {
		removePhoneNumber(BLACKLIST, phone);
	}

	public void removeNumberFromWhitelist(String phone) {
		removePhoneNumber(WHITELIST, phone);
	}

	private int addPhoneNumber(String type, String phone) {
		Element root = document.getDocumentElement();
		NodeList nodelist = root.getElementsByTagName(type);

		if (nodelist == null || (nodelist.getLength() <= 0)) {
			/* TODO: throw Execption */
		} else {
			Element element = (Element) nodelist.item(0);
			String all_node = element.getTextContent();

			if (all_node.indexOf(phone) >= 0)
				return 0;
			Element new_ = document.createElement(PHONENUMBER);
			/* new_.appendChild(document.createTextNode(phone)); */
			new_.setTextContent(phone);
			element.appendChild(new_);
			saveConfig(document, xmlPath);
		}

		return 0;
	}

	public int addNumber2Blacklist(String phone) {
		return addPhoneNumber(BLACKLIST, phone);
	}

	public int addNumber2WhiteList(String phone) {
		return addPhoneNumber(WHITELIST, phone);
	}

	private void clearList(String type) {
		Element root = document.getDocumentElement();
		NodeList nodelist = root.getElementsByTagName(type);
		Element element = (Element) nodelist.item(0);

		NodeList childs = element.getElementsByTagName(PHONENUMBER);

		while(childs.getLength() > 0) {
			Element child = (Element) childs.item(0);
			element.removeChild(child);
		}

		element.normalize();

		saveConfig(document, xmlPath);
	}

	public void clearBlacklist() {
		clearList(BLACKLIST);
	}

	public String[] getList(String type) {
		Element root = document.getDocumentElement();
		NodeList nodelist = root.getElementsByTagName(type);
		Element element = (Element) nodelist.item(0);

		NodeList childs = element.getElementsByTagName(PHONENUMBER);

		String[] list = new String[childs.getLength()];

		for(int i = 0; i < childs.getLength(); i++) {
			Element child = (Element) childs.item(i);
			list[i] = child.getTextContent();
		}

		return list;
	}

	public String[] getBlacklist() {
		return getList(BLACKLIST);
	}

	public String[] getWhitelist() {
		return getList(WHITELIST);
	}

	public void clearWhitelist() {
		clearList(WHITELIST);
	}

	private boolean isListEnable(String type) {
		Element root = document.getDocumentElement();
		NodeList nodelist = root.getElementsByTagName(type);
		Element element = (Element) nodelist.item(0);

		String attr = element.getAttribute("enable");

		if (attr.equals("true"))
			return true;
		else
			return false;
	}

	public boolean isBlacklistEnable() {
		return isListEnable(BLACKLIST);
	}
	
	public boolean isWhitelistEnable() {
		return isListEnable(WHITELIST);
	}

	private boolean isInList(String type, String phone) {
		/* TODO: */
		return false;
	}
	
	public boolean isInBlacklist(String phone) {
		return isInList(BLACKLIST, phone);
	}
	
	public boolean isInWhitelist(String phone) {
		return isInList(WHITELIST, phone);
	}
	
	public static void main(String[] args) { 
		IntercepterConfig parser = new IntercepterConfig(); 


		if (args[0].equals("add")) {
			if (args[1].equals("black"))
				parser.addNumber2Blacklist(args[2]);
			else
				parser.addNumber2WhiteList(args[2]);
		} else if (args[0].equals("remove")) {
			System.out.println(args[0] + args[1] + args[2]);
			if (args[2].equals("all")) {
				if (args[1].equals("black"))
					parser.clearBlacklist();
				else
					parser.clearWhitelist();
			} else {
				if (args[1].equals("black"))
					parser.removeNumberFromBlacklist(args[2]);
				else
					parser.removeNumberFromWhitelist(args[2]);
			}
		} else if (args[0].equals("print")) {

			String phones[];
			if (args[1].equals("black"))
				phones = parser.getBlacklist();
			else
				phones = parser.getWhitelist();

			for(String phone: phones)
				System.out.println(phone);
		}


		/* NodeList nodeList = rootElement.getElementsByTagName(BLACKLIST); 
		if(nodeList != null) { 
			for (int i = 0 ; i < nodeList.getLength(); i++) 
			{ 
				Element element = (Element)nodeList.item(i); 
				parser.process_element2(element);
			} 
		} */
	} 

}
