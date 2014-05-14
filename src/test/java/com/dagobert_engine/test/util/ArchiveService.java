package com.dagobert_engine.test.util;

import java.io.File;
import java.nio.file.Paths;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public enum ArchiveService {
	INSTANCE;


	// packages
	private static final String COMMONS_MATH = "org.apache.commons:commons-math:2.0";
	private static final String COMMONS_LANG = "commons-lang:commons-lang:2.6";
	private static final String GSON = "com.google.code.gson:gson:2.2.4";
	private static final String SIMPLE = "com.googlecode.json-simple:json-simple:1.1.1";
	private static final String JTA = "javax.transaction:jta:1.1";
	private static final String COMMONS_CODEC = "commons-codec:commons-codec:1.8";
	
	private static final String XCHANGE_CORE = "com.xeiam.xchange:xchange-core:2.0.0";
	private static final String XCHANGE_KRAKEN = "com.xeiam.xchange:xchange-kraken:2.0.0";
	
	private static final String TEST_WAR = "test.war";
	private static final String BASE_PACKAGE_PATH = "/com/dagobert_engine/";
	
	private static final String CLASSES_DIR = "target/classes";
	
	private static final String WEBINF_DIR = "src/main/webapp/WEB-INF/";
	private static final String BEANS_XML = WEBINF_DIR + "beans.xml";
	private static final String WEB_XML = WEBINF_DIR + "web.xml";
	private static final String FACES_CONFIG_XML = WEBINF_DIR + "faces-config.xml";
	private static final String EJBJAR_XML = WEBINF_DIR + "ejb-jar.xml";
	private static final String JBOSSWEB_XML = WEBINF_DIR + "jboss-web.xml";
	private static final String JBOSSEJB3_XML = WEBINF_DIR + "jboss-ejb3.xml";

	private static final String JBOSS_DEPLOYMENT_STRUCTURE_XML = "src/test/resources/jboss-deployment-structure.xml";
	
	
	private final WebArchive archive = ShrinkWrap.create(WebArchive.class, TEST_WAR);

	/**
	 */
	private ArchiveService() {
		addMavenDeps();
		
		addKlassen();
		
		addWebInf();

		addTestClasses();
		
//		final Path arquillianPath = Paths.get("target/arquillian");
//		try {
//			Files.createDirectories(arquillianPath);
//		}
//		catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		final File warFile = Paths.get(arquillianPath.toString(), "shop.war").toFile();
//		archive.as(ZipExporter.class).exportTo(warFile, true); 
	}
	
	private void addMavenDeps() {
		archive.addAsLibraries(Maven.resolver()  
			    .loadPomFromFile("pom.xml").resolve(COMMONS_MATH, COMMONS_LANG, GSON, SIMPLE, JTA, COMMONS_CODEC, XCHANGE_CORE, XCHANGE_KRAKEN)  
			    .withTransitivity().asFile());
		
	}

	private void addKlassen() {
		final JavaArchive tmp = ShrinkWrap.create(JavaArchive.class);
		tmp.as(ExplodedImporter.class).importDirectory(CLASSES_DIR);
		archive.merge(tmp, "WEB-INF/classes");
	}
	
	private void addWebInf() {
		// Gibt es WEB-INF\beans.xml? Ansonsten als leere Datei dem Web-Archiv hinzufuegen
		final File beansXml = Paths.get(BEANS_XML).toFile();
		if (beansXml.exists()) {
			archive.addAsWebInfResource(beansXml);
		}
		else {
			archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		}
		
		// Gibt es WEB-INF\web.xml fuer Webanwendungen und RESTful WS?
		final File webXml = Paths.get(WEB_XML).toFile();
		if (webXml.exists()) {
			archive.setWebXML(webXml);
		}
		
		// Gibt es WEB-INF\web.xml fuer Webanwendungen?
		final File facesConfigXml = Paths.get(FACES_CONFIG_XML).toFile();
		if (facesConfigXml.exists()) {
			archive.addAsWebInfResource(facesConfigXml);
		}
		
		// Gibt es WEB-INF\jboss-web.xml fuer Security?
		final File jbossWebXml = Paths.get(JBOSSWEB_XML).toFile();
		if (jbossWebXml.exists()) {
			archive.addAsWebInfResource(jbossWebXml);
		}
		
		// Gibt es WEB-INF\ejb-jar.xml fuer EJBs?
		final File ejbJarXml = Paths.get(EJBJAR_XML).toFile();
		if (ejbJarXml.exists()) {
			archive.addAsWebInfResource(ejbJarXml);
		}
		
		// Gibt es WEB-INF\jboss-ejb3.xml fuer Security?
		final File jbossEjb3Xml = Paths.get(JBOSSEJB3_XML).toFile();
		if (jbossEjb3Xml.exists()) {
			archive.addAsWebInfResource(jbossEjb3Xml);
		}

		// Gibt es WEB-INF\jboss-deployment-structure.xml fuer die Erweiterung des Classpath?
		final File jbossDeploymentStructureXml = Paths.get(JBOSS_DEPLOYMENT_STRUCTURE_XML).toFile();
		if (jbossDeploymentStructureXml.exists()) {
			archive.addAsWebInfResource(jbossDeploymentStructureXml);
		}
		else {
			System.out.println(jbossDeploymentStructureXml.toString() + " does not exist");
			// TODO MANIFEST.MF fuer Web-Archiv kann nicht mit ShrinkWrap gesetzt werden
//			archive.addAsManifestResource(new StringAsset("Manifest-Version: 1.0\n"
//			                              + "Dependencies: org.jboss.as.controller-client,org.jboss.dmr,com.google.\n"
//			                              + " guava,org.joda.time,org.slf4j"),
//					                      "MANIFEST.MF");
			
//			archive.setManifest(new StringAsset("Manifest-Version: 1.0\n"
//					            + "Dependencies: org.jboss.as.controller-client,org.jboss.dmr,com.google.\n"
//					            + " guava,org.joda.time,org.slf4j"));
		}
	}

	private void addTestClasses() {
		archive.addClass(AbstractTest.class);
		
		final Filter<ArchivePath> filter = Filters.include(BASE_PACKAGE_PATH + "[\\w-/]*Test\\.class");
		for (Class<?> clazz : Testclasses.getInstance().getTestklassen()) {
			archive.addPackages(false, filter, clazz.getPackage());
		}
	}
	
	public static ArchiveService getInstance() {
		return INSTANCE;
	}
	
	public Archive<?> getArchive() {
		return archive;
	}
}
