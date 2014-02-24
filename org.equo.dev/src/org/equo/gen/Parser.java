package org.equo.gen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.equo.gen.model.MetaDatasource;
import org.equo.gen.model.MetaDomain;
import org.equo.gen.model.MetaEntity;
import org.equo.gen.model.MetaField;
import org.equo.gen.model.MetaModule;
import org.equo.gen.model.MetaPeer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {
	
	private Map<String, Schema> schemas = new HashMap<>();
	
	public static class Location {
		
		private String resource;
		private int line;
		private int col;

		public Location(String resource, int lineNumber, int columnNumber) {
			this.resource = resource;
			this.line = lineNumber;
			this.col = columnNumber;
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder(resource);
			b.append(" line: ");
			b.append(line);
			b.append(" col: ");
			b.append(col);
			return b.toString();
		}

	}

	private Schema getSchema(String fileName) {
		Schema schema = schemas.get(fileName);
		if (schema == null) {
			SchemaFactory f = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			try {
				schema = f.newSchema(new File(fileName));
				schemas.put(fileName, schema);
			} catch (SAXException e) {
				throw new RuntimeException("Internal error parsing XML schema " + fileName, e);
			}
		}
		return schema;
	}
	
	private static final Location getLocation(String resource, Locator locator) {
		return new Location(resource, locator.getLineNumber(), locator.getColumnNumber());
	}
	
	private void parse(InputStream in, String schema, Visitor visitor) throws SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		if (schema != null) {
			factory.setValidating(true);
			factory.setSchema(getSchema(schema));
		}
		SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Internal error creating parser", e);
		}
		try {
			parser.parse(in, new VisitorHandler(visitor));
		} catch (IOException e) {
			throw new RuntimeException("IO exception parsing stream", e);
		}
	}
	
	public MetaModule parseModuleSchema(InputStream stream) throws SAXException {
		MetaModule module = new MetaModule();
		parseModuleSchema(module, stream);
		return module;
	}
	
	private void parseModuleSchema(final MetaModule module, InputStream in) throws SAXException {
		parse(in, null, new Visitor() {
			
			@Override
			public Visitor visitElement(String uri, String localName, String qName, Attributes attrs) 
					throws SAXException {
				
				switch (localName) {
				case "module-schema":
					module.setId(getString(attrs, "id"));
					module.setName(getString(attrs, "name"));
					module.setPackage(getString(attrs, "pkg"));
					break;
				case "domain-resource":
					String dr = getString(attrs, "resource");
					if (dr != null) parseDomainSchema(module, dr);
					// else TODO error
					break;
				case "peer-resource":
					String name = getString(attrs, "datasource");
					MetaDatasource d = module.getDatasource(name);
					if (d == null) {
						// Thow error
					}
					String pr = getString(attrs, "resource");
					if (pr != null) parsePeerSchema(d, pr);
					// else TODO error
					break;
				case "datasource":
					final MetaDatasource ds = new MetaDatasource(getString(attrs, "name"));
					module.addDatasource(ds);
					return new Visitor() {
						
						@Override
						public Visitor visitElement(String uri, String localName, String qName,
								Attributes attrs) throws SAXException {
							if (localName.equals("property")) {
								ds.setProperty(getString(attrs, "name"), getString(attrs, "value"));
							}
							return null;
						}
					};
				}
				return this;
			}
		});
	}
	
	private void parseDomainSchema(final MetaModule module, final String resource) throws SAXException {
		parse(getResourceStream(resource), null, new Visitor() {
			
			@Override
			public Visitor visitElement(String uri, String localName, String qName, Attributes attrs) 
					throws SAXException {
				
				switch (localName) {
				case "domain":
					MetaDomain d = new MetaDomain(getString(attrs, "name"), 
							getString(attrs, "type"), getInt(attrs, "legth"), 
							getInt(attrs, "scale"), getBoolean(attrs, "nullable"), 
							getString(attrs, "model"));
					d.setLocation(getLocation(resource, getLocator()));
					module.addDomain(d);
					break;
				case "possible-values":
					
					break;
				case "possible-value":
					
					break; 
				}
				return this;
			}
		});
	}
	
	private void parsePeerSchema(final MetaDatasource ds, final String resource) throws SAXException {
		parse(getResourceStream(resource), null, new Visitor() {
			
			@Override
			public Visitor visitElement(String uri, String localName, String qName, Attributes attrs) 
					throws SAXException {
				
				switch (localName) {
				case "peer":
					final MetaPeer peer = new MetaPeer(getString(attrs, "name"), 
							getString(attrs, "model"), getString(attrs, "entity"));
					peer.setLocation(getLocation(resource, getLocator()));
					ds.addPeer(peer);
					return new Visitor() {

						@Override
						public Visitor visitElement(String uri, String localName, String qName,
								Attributes attrs) throws SAXException {
							
							switch (localName) {
							case "field":
								MetaField field = new MetaField(getString(attrs, "name"), 
										getString(attrs, "domain"), getString(attrs, "model"), 
										getBoolean(attrs, "primary-key"));
								peer.addField(field);
								break;
							case "entity":
								MetaEntity entity = new MetaEntity(getString(attrs, "name"),
										getString(attrs, "model"), getBoolean(attrs, "primary-key"));
								peer.addEntity(entity);
								break;
							}
							return this;
						}
					};
				}
				return this;
			}
		});
	}
	
	protected static String getString(Attributes attrs, String name) {
		return attrs.getValue(null, name);
	}
	
	protected static Integer getInt(Attributes attrs, String name) {
		return Integer.parseInt(attrs.getValue(null, name));
	}
	
	protected static Boolean getBoolean(Attributes attrs, String name) {
		return Boolean.parseBoolean(attrs.getValue(null, name));
	}
	
	protected InputStream getResourceStream(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static abstract class Visitor extends DefaultHandler {
		
		private Locator locator;
		
		public Visitor visitElement(String uri, String localName,
                String qName, Attributes attributes) throws SAXException {
			return this;
		}
		
		public void visitEnd(String uri, String localName, String qName)
				throws SAXException {}
		
		protected Locator getLocator() {
			return locator;
		}
	}

	private static class VisitorHandler extends DefaultHandler {
		
		private static final Visitor trivialHandler = new Visitor() {};
		
		private Locator locator;
		
		private Stack<Visitor> delegates = new Stack<>();
		
		public VisitorHandler(Visitor visitor) {
			delegates.push(visitor);
		}
		
		@Override
		public final void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			Visitor v = delegates.peek().visitElement(uri, localName, qName, attributes);
			if (v == null) delegates.push(trivialHandler);
			else {
				v.locator = locator;
				delegates.push(v); 
			}
		}

		@Override
		public final void endElement(String uri, String localName, String qName)
				throws SAXException {
			delegates.pop().visitEnd(uri, localName, qName);
		}

		@Override
		public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}
		
		public InputSource resolveEntity(String publicId, String systemId)
				throws IOException, SAXException {
			return delegates.peek().resolveEntity(publicId, systemId);
		}

		public void notationDecl(String name, String publicId, String systemId)
				throws SAXException {
			delegates.peek().notationDecl(name, publicId, systemId);
		}

		public void unparsedEntityDecl(String name, String publicId,
				String systemId, String notationName) throws SAXException {
			delegates.peek().unparsedEntityDecl(name, publicId, systemId, notationName);
		}

		public void startDocument() throws SAXException {
			delegates.peek().startDocument();
		}

		public void endDocument() throws SAXException {
			delegates.peek().endDocument();
		}

		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			delegates.peek().startPrefixMapping(prefix, uri);
		}

		public void endPrefixMapping(String prefix) throws SAXException {
			delegates.peek().endPrefixMapping(prefix);
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			delegates.peek().characters(ch, start, length);
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
			delegates.peek().ignorableWhitespace(ch, start, length);
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
			delegates.peek().processingInstruction(target, data);
		}

		public void skippedEntity(String name) throws SAXException {
			delegates.peek().skippedEntity(name);
		}

		public void warning(SAXParseException e) throws SAXException {
			delegates.peek().warning(e);
		}

		public void error(SAXParseException e) throws SAXException {
			delegates.peek().error(e);
		}

		public void fatalError(SAXParseException e) throws SAXException {
			delegates.peek().fatalError(e);
		}
	}
}
