package org.equo.gen;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.equo.AbstractEntity;
import org.equo.Column;
import org.equo.Domain;
import org.equo.Peer;
import org.equo.PeerTable;
import org.equo.Session;
import org.equo.Table;
import org.equo.Types;
import org.equo.gen.model.Lookup;
import org.equo.gen.model.MetaDatasource;
import org.equo.gen.model.MetaDomain;
import org.equo.gen.model.MetaDomain.PossibleValue;
import org.equo.gen.model.MetaField;
import org.equo.gen.model.MetaModule;
import org.equo.gen.model.MetaPeer;
import org.equo.gen.model.MetadataException;

public class CodeGenerator {

	private File moduleSchema;

	private File outDir;

	private MetaModule module;
	
	public CodeGenerator(File moduleSchema, File outDir) {
		this.moduleSchema = moduleSchema;
		this.outDir = outDir;
	}

	public static void main(String[] args) {
		 String moduleSchema =
		 "C:\\eclipse\\workspace-mads\\gw2db\\xml\\module.xml";
		 String outDir =
		 "C:\\eclipse\\workspace-mads\\gw2db\\src\\ctro\\gw2db";
//		String moduleSchema = "/home/j0rd1/Documents/Projects/workspace-js/guildwars2database/model/module.xml";
//		String outDir = "/home/j0rd1/Documents/Projects/workspace-js/guildwars2database/src/net/orc/gw2";

		try {
			new CodeGenerator(new File(moduleSchema), new File(outDir)).generate();
			System.out.println("Module generated");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generate() throws Exception {
		ModelGenerator generator = new ModelGenerator();
		MetaModule module = generator.generateModule(moduleSchema);
		
		// module.accept(new MeteModuleResolver()); // OTDO resolve

		generate(module);
	}

	private void generate(MetaModule module) throws IOException,
			MetadataException {
		String baseDir = outDir.getAbsolutePath() + File.separator;
		String entityDir = baseDir + "entity" + File.separator;
		String peerDir = entityDir + "peer" + File.separator;
		new File(peerDir).mkdirs();
		File domainsFile = new File(peerDir + "Domains.java");
		generateDomains(module, domainsFile);
		for (MetaDatasource ds : module.getDatasources()) {
			for (MetaPeer peer : ds.getPeers()) {
				String pkg = module.getPackage();
				File peerFile = new File(peerDir + peer.getName() + ".java");
				generatePeer(pkg, peer, peerFile);
//				File viewFile = new File(peerDir + "Peer" + peer.getEntity() + ".java");
//				generateView(pkg, peer, viewFile);
				File entityFile = new File(entityDir + peer.getEntity() + ".java");
				generateEntity(pkg, peer, entityFile);
			}
		}
		
	}

	private void generateDomains(MetaModule module, File file)
			throws IOException {
		JavaWriter w = new JavaWriter(file);

		w.writeStatm("package", module.getPackage() + ".entity.peer");
		w.endLine();
		w.writeStatm("import", Domain.class.getCanonicalName());
		w.writeStatm("import", Types.class.getCanonicalName());
		w.endLine();
		w.writeBlock("public interface Domains");
		w.endLine();
		for (MetaDomain domain : module.getDomains()) {
			String name = toJavaIdentifier(domain.getName());
			w.writeStatm("public static final Domain", name, "= new Domain("
					+ "\"" + name + "\", Types." + domain.getType() + ", "
					+ domain.getLength() + ", " + domain.getScale() + ")");
		}
		w.endLine();
		// PV enums
		for (MetaDomain domain : module.getDomains()) {
			List<PossibleValue> pv = domain.getPossibleValues();
			if (pv.size() > 0) {
				String model = getEnumIdentifier(domain);
				w.endLine();
				w.writeBlock("public static enum", model);
				w.endLine();
				for (int i = 0; i < pv.size(); i++) {
					if (i != 0)
						w.endLine(", ");
					PossibleValue v = pv.get(i);
					w.write(toJavaIdentifier(v.getName()) + "(\"" + v.getValue() + "\")");
				}
				w.endLine(';');
				w.endLine();
				String type = Types.toJavaType(domain.getType());
				w.writeStatm("private", type, "code");
				w.endLine();
				w.writeBlock("private", model + "(" + type, "code)");
				w.writeStatm("this.code = code");
				w.endBlock();
				w.endLine();
				w.writeBlock("public", type, "code()");
				w.writeStatm("return code");
				w.endBlock();
				w.endBlock();
			}
		}
		w.endBlock();
		w.dump(true);
	}

	private int writeField(JavaWriter w, String entity, MetaField field, int fieldCount) throws IOException {
		MetaDomain domain = module.getDomain(field.getDomain());
		String fieldName = field.getName();
		if (fieldName == null) {
			fieldName = domain.getName();
		}
		List<PossibleValue> pv = domain.getPossibleValues();
		String type;
		// TODO constraint code
		// if (pv.size() == 0)
		type = Types.toJavaType(domain.getType());
		// else
		// type = "Domains." + getEnumIdentifier(domain);
		String fieldType = "Field<" + entity + ", " + type + ">";
		w.writeStatm(
				"public static final",
				fieldType,
				fieldName,
				"= new",
				fieldType + "(MT, \"" + fieldName + "\", Domains."
				+ domain.getName() + ", " + fieldCount++ + ")"
		);
		return fieldCount;
	}
	
	private int writeForeignField(JavaWriter w, MetaField field, String localEntity, 
			MetaPeer foreignPeer, int fieldCount) throws IOException {
		
		MetaDomain domain = module.getDomain(field.getDomain());
		if (domain == null) {
			toString();
		}
		String fieldName = field.getName();
		if (fieldName == null) {
			fieldName = domain.getName();
		}
		List<PossibleValue> pv = domain.getPossibleValues();
		String type;
		// TODO constraint code
		// if (pv.size() == 0)
		type = Types.toJavaType(domain.getType());
		// else
		// type = "Domains." + getEnumIdentifier(domain);
		String foreignPeerModel = "Peer" + foreignPeer.getEntity();
		String fieldType = "ForeignField<" + localEntity + ", " + type + ">";
		w.writeStatm(
				"public static final",
				fieldType,
				fieldName,
				"= new",
				fieldType + "(" + fieldCount++ + ", " + foreignPeerModel + "." + fieldName + ")"
		);
		return fieldCount;
	}
	
	private void generateEntity(String basePackage, MetaPeer peer, File file)
			throws IOException {
		JavaWriter w = new JavaWriter(file);

		String name = peer.getName();
		String entity = peer.getEntity();

		w.writeStatm("package", basePackage + ".entity");
		w.endLine();
		w.writeStatm("import", Session.class.getCanonicalName());
		w.writeStatm("import", AbstractEntity.class.getCanonicalName());
//		w.writeStatm("import", DatasourceException.class.getCanonicalName());
		w.endLine();
		w.writeStatm("import", basePackage + ".entity.peer." + name);
		
		w.endLine();
		w.writeBlock("public class", entity, "extends", "Entity<" + entity + '>');
		w.endLine();
		
		w.writeBlock("public static class Peer extends ", name);
		w.endLine();
		w.writeBlock("public Peer(Session session)");
		w.writeStatm("super(session)");
		w.endBlock();
		w.endLine();
		w.endBlock();

		// public Bass(Basses peer)
		w.writeBlock("public", entity + "(Peer peer)");
		w.writeStatm("super(peer)");
		w.endBlock();
		w.endLine();
		
		w.writeBlock("public @Override Peer getPeer()");
		w.writeStatm("return (Peer) super.getPeer()");
		w.endBlock();

//		List<MetaField> fields = peer.getFields();
//		for (MetaField field : fields) {
//			MetaDomain domain = metadata.getDomain(field.getDomain());
//			String model = field.getModel();
//			if (model == null) model = domain.getModel();
//			if (model == null) model = field.getId();
//			String type = Types.toJavaType(domain.getType());
//			w.writeBlock("public", type, "get" + model + "() throws DatasourceException");
//			w.writeStatm("return get(getPeer()." + field.getId() + ')');
//			w.endBlock();
//			w.endLine();
//			w.writeBlock("public void set" + model + "(" + type, "value)");
//			w.writeStatm("set(getPeer()." + field.getId() + ", value)");
//			w.endBlock();
//			w.endLine();
//		}
		
		w.endBlock();
		w.dump(true);
	}
	
	private void generateView(String basePackage, MetaPeer peer, File file)
			throws IOException {
		JavaWriter w = new JavaWriter(file);

		String name = peer.getName();
		String entity = peer.getEntity();
		String model = "Peer" + entity;

		w.writeStatm("package", basePackage + ".entity.peer");
		w.endLine();
		w.writeStatm("import", Session.class.getCanonicalName());
		w.endLine();
		w.writeStatm("import", basePackage + ".entity." + entity);
		
		w.endLine();
		w.writeBlock("public class", model, "extends", name + "<" + entity + ">");
		w.endLine();

		w.writeBlock("private", model + "(Session session)");
		w.writeStatm("super(session)");
		w.endBlock();
		w.endLine();
		
		w.writeBlock("public @Override", entity, "newEntity()");
		w.writeStatm("return new", entity + "(this)");
		w.endBlock();
		
//		generateBaseEntity(w, basePackage, peer);
//		w.endLine();
		
		w.endBlock();
		w.dump(true);
	}

	private void generatePeer(String basePackage, MetaPeer peer, File file)
			throws IOException {
		JavaWriter w = new JavaWriter(file);

		String name = peer.getName();
		String entity = peer.getEntity();
		String model = "Peer" + entity;
		

		w.writeStatm("package", basePackage + ".entity.peer");
		w.endLine();
		w.writeStatm("import", Session.class.getCanonicalName());
		w.writeStatm("import", Peer.class.getCanonicalName());
		w.writeStatm("import", AbstractEntity.class.getCanonicalName());
		w.writeStatm("import", Column.class.getCanonicalName());
		w.writeStatm("import", Table.class.getCanonicalName());
		w.writeStatm("import", PeerTable.class.getCanonicalName());
		w.endLine();
		w.writeStatm("import", basePackage + ".entity." + entity);
		w.writeStatm("import", basePackage + ".entity.peer.Domains");
		
		w.endLine();
		w.writeBlock("public class", name, 
				"extends EntityPeer<" + entity + ">");
		w.endLine();
		
		w.writeStatm("public static final String DS = \"" + "MASTER" + "\"");
		w.writeStatm("public static final String MT = \"" + name + "\"");
		w.endLine();

		// Fields
		int fieldCount = 0;
		List<MetaField> fields = peer.getFields();
		for (MetaField field : fields) {
			fieldCount = writeField(w, entity, field, fieldCount);
		}
		w.endLine();
		
		// Foreign entities
		List<Lookup> lookups = peer.getLookups();
		writeForeignEntities(w, entity, lookups);

		w.writeBlock("protected", name + "(Session session)");
		w.writeStatm("super(MT, session.getDatasource(DS))");
		w.write("setFields(");
		for (int i = 0; i < fields.size(); i++) {
			if (i != 0)
				w.append(", ");
			w.append(fields.get(i).getName());
		}
		w.append(");");
		w.endLine();
		List<MetaField> pkFields = peer.getPrimaryKeyFields();
		w.write("setPrimaryKeyFields(");
		for (int i = 0; i < pkFields.size(); i++) {
			if (i != 0)
				w.append(", ");
			w.append(pkFields.get(i).getName());
		}
		w.append(");");
		w.endLine();
//		initForeignEntities(w, entity, lookups);
		w.endBlock();
		w.endLine();
		
//		w.writeBlock("protected void init(Session session)");
//		w.write("setFields(");
//		for (int i = 0; i < fields.size(); i++) {
//			if (i != 0)
//				w.append(", ");
//			w.append(fields.get(i).getName());
//		}
//		w.append(");");
//		w.endLine();
//		List<MetaField> pkFields = peer.getPrimaryKeyFields();
//		w.write("setPrimaryKeyFields(");
//		for (int i = 0; i < pkFields.size(); i++) {
//			if (i != 0)
//				w.append(", ");
//			w.append(pkFields.get(i).getName());
//		}
//		w.append(");");
//		w.endLine();
//		if (lookups.size() > 0) {
//			w.write("setForeignPeers(");
//			for (int i = 0; i < lookups.size(); i++) {
//				if (i != 0) w.append(", ");
//				w.append(lookups.get(i).getForeignPeer().getName());
//			}
//			w.append(");");
//			w.endLine();
//			w.write("setForeignSegments(");
//			for (int i = 0; i < lookups.size(); i++) {
//				if (i != 0) w.append(", ");
//				w.append("new Field[] {");
//				List<MetaField> localFields = lookups.get(i).getLocalFields();
//				for (int j = 0; j < localFields.size(); j++) {
//					if (j != 0) w.append(", ");
//					w.append(localFields.get(j).getName());
//				}
//				w.append('}');
//			}
//			w.append(");");
//			w.endLine();
//		}
//		List<Detail> details = peer.getDetails();
//		if (details.size() > 0) {
//			for (Detail detail : details) {
//				MetaPeer detailPeer = detail.getForeignPeer();
//				String detailEntity = detailPeer.getEntity();
//				w.writeStatm("Peer" + detailEntity, "peer" + detailEntity, "= session.getPeer(Peer" + detailEntity + ".class)");
//			}
//			w.write("setDetailPeers(");
//			for (int i = 0; i < details.size(); i++) {
//				if (i != 0) w.append(", ");
//				w.append("peer" + details.get(i).getForeignPeer().getEntity());
//			}
//			w.append(");");
//			w.endLine();
//			w.write("setDetailSegments(");
//			for (int i = 0; i < details.size(); i++) {
//				if (i != 0) w.append(", ");
//				w.append("new Field[] {");
//				List<MetaField> foreignFields = details.get(i).getForeignFields();
//				for (int j = 0; j < foreignFields.size(); j++) {
//					if (j != 0) w.append(", ");
//					MetaPeer detailPeer = details.get(i).getForeignPeer();
//					String detailEntity = detailPeer.getEntity();
//					w.append("peer" + detailEntity + "." + foreignFields.get(j).getName());
//				}
//				w.append('}');
//			}
//			w.append(");");
//			w.endLine();
//		}
//		w.endBlock();
//		w.endLine();
		
//		w.writeBlock("public @Override", entity, "newEntity()");
//		w.writeStatm("return new", entity + "((Peer" + entity + ") this)");
//		w.endBlock();
		
//		generateBaseEntity(w, basePackage, peer);
//		w.endLine();
		
		w.endBlock();
		w.dump(true);
	}

	private void writeForeignEntities(JavaWriter w, String localEntity, List<Lookup> lookups) throws IOException {
		Integer index = 0;
		for (Lookup lookup : lookups) {
			MetaPeer foreignPeer = lookup.getForeignPeer();
			String name = foreignPeer.getName(); 
			String alias = name; // TODO alias
			String type = "PeerView<" + localEntity + ", " + localEntity + ">";
			w.writeStatm("public static final", type, alias, "= new", type + "(MT, \"" + alias + "\", ", index.toString() + ")");
			index++;
		}
		w.endLine();
	}
	
	private void initForeignEntities(JavaWriter w, String localEntity, List<Lookup> lookups) throws IOException {
		for (Lookup lookup : lookups) {
			MetaPeer foreignPeer = lookup.getForeignPeer();
			String name = foreignPeer.getName();
			String foreignEntity = foreignPeer.getEntity();
			w.writeStatm(name, "= (" + name + ") session.getPeer(Peer" + foreignEntity + ".class)");
		}
//		w.endLine();
	}

//	private void generateBaseEntity(JavaWriter w, String basePackage, MetaPeer peer) throws IOException {
//
//		String name = toJavaIdentifier(peer.getName());
//		String entity = toJavaIdentifier(peer.getEntity());
//
//		w.writeBlock("public static class BaseEntity extends Entity<" + entity + ">");
//
//		// public Bass(Basses peer)
//		w.writeBlock("protected BaseEntity (Peer" + entity, "peer)");
//		w.writeStatm("super(peer)");
//		w.endBlock();
//		w.endLine();
//		
////		generateBaseEntity(w, peer);
//		
////		w.endLine();
//		
////		generateField(w, peer);
//		
//		w.endBlock();
//		w.endLine();
//		
//		// public Bass newEntity() {
//		w.writeBlock("public @Override", basePackage + ".entity." + entity, "newEntity()");
//		w.writeStatm("return new", basePackage + ".entity." + entity + "(this)");
//		w.endBlock();
//		// // public static Function<Bass> FUNC()
//		// w.writeBlock("public static Function<" + entity + "> FUNC()");
//		// w.writeStatm("return new Function<" + entity + ">()");
//		// w.endBlock();
//		// w.endLine();
//
//		w.endBlock();
//	}
	
//	private void generateField(JavaWriter w, MetaPeer peer) throws IOException {
//		String entity = toJavaIdentifier(peer.getEntity());
//		w.writeBlock("public static class Field<T> extends org.ctro.jsql.Field<" + entity + ", T>");
//		w.endLine();
//		w.writeBlock("private Field(String name, Domain domain, int index)");
//		w.writeStatm("super(Peer.MT, name, domain, index)");
//		w.endBlock();
//		w.endBlock();
//	}

	public String getEnumIdentifier(MetaDomain domain) {
		String model = domain.getModel();
		if (model == null)
			model = domain.getName() + "_PV";
		return toJavaIdentifier(model);
	}

	/**
	 * Converts a string to a Java identifier, encoding unknown characters as
	 * "_"
	 */
	public String toJavaIdentifier(String name) {
		StringBuffer cb = new StringBuffer();

		char ch = name.charAt(0);
		if (Character.isJavaIdentifierStart(ch))
			cb.append(ch);
		else
			cb.append("_");

		for (int i = 1; i < name.length(); i++) {
			ch = name.charAt(i);

			if (Character.isJavaIdentifierPart(ch))
				cb.append(ch);
			else {
				cb.append("_");
			}
		}
		return cb.toString();
	}
}
