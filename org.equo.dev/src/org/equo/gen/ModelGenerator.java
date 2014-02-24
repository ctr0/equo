package org.equo.gen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.equo.gen.model.MetaDatasource;
import org.equo.gen.model.MetaModule;
import org.equo.gen.model.MetaPeer;
import org.xml.sax.SAXException;

public class ModelGenerator {
	
	public MetaModule generateModule(File file) throws SAXException, IOException {
		return generateModule(new BufferedInputStream(new FileInputStream(file)));
	}
	
	public MetaModule generateModule(InputStream stream) throws SAXException, IOException {
		Parser parser = new Parser();
		MetaModule module = parser.parseModuleSchema(stream);
		generateModule(module);
		return module;
	}
	
	private void generateModule(MetaModule module) throws IOException {
		File outDir = new File(".");
		String baseDir = outDir.getAbsolutePath() + File.separator;
		String entityDir = baseDir + "entity" + File.separator;
		String peerDir = entityDir + "peer" + File.separator;
		new File(peerDir).mkdirs();
		File domainsFile = new File(peerDir + "Domains.java");
		generateDomains(module, domainsFile);
		for (MetaDatasource ds : module.getDatasources()) {
			for (MetaPeer peer : ds.getPeers()) {
				String pkg = module.getPackage();
				generateEntity(pkg, peer, new File(entityDir + peer.getEntity() + ".java"));
				generatePeer(pkg, peer, new File(peerDir + peer.getName() + ".java"));
			}
		}
	}

	private void generateDomains(MetaModule module, File domainsFile) {
		// TODO Auto-generated method stub
		
	}

	private void generateEntity(String pkg, MetaPeer peer, File file) {
		// TODO Auto-generated method stub
		
	}

	private void generatePeer(String pkg, MetaPeer peer, File file) {
		// TODO Auto-generated method stub
		
	}
	
	
}
