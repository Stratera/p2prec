This readme file outlines procedures for handling the project source and general deployment and administration.

Developer Setup
----------------
1) install java
2) install maven 3.X (latest version)
3) Install eclipse with subversion plugin (I use subclipse) and full m2eclipse plugins
4) install all applicable m2eclipse plugins via the eclipse/maven discovery catalog
5) checkout Code (Developers use trunk and releases go out to branches)
6) Once the code is checked out, delete from Package Explorer (do not check "Delete from disk" checkbox!)
7) re-import project as maven project.  It should set up 4 eclipse projects:
	peerrate
	peerrate_contract
	peerrate_dbmigrations
	peerrateweb
8) install postgreSQL (http://get.enterprisedb.com/postgresql/postgresql-9.6.0-beta4-windows-x64.exe)
	

FAQ
-----
Q: How is the code separated in the project?
A: This is a complicated question with a lot of answer parts.  
   1) All webservice contract definitions will be java beans that are defined in XSDs which are stored
   in the contract project.  This includes the models for webservice APIs and Import/Export routines. 
   2) Any script intended to be run to modify database structure is to go in 
   peerrate_dbmigrations/src/main/resources/db/migration 
   3) inside the web project the bulk of non-generated java code resides in packages based on functions.  
   For example, the entities (database model) are packaged in com.stateratech.dhs.peerate.entity.model.
   The repository interfaces (and concrete classes if there are any) are 
   in com.strateratech.dhs.peerrate.entity.repository.
   The web controllers (rest service endpoints) are in com.strateratech.dhs.peerrate.web.controller.
   
Q: How do we build this project and what is the expected artifacts?
A: 1) Checkout project 
   2) command: mvn clean install deploy:deploy
   		produces 3 jars, 2 of which should go to nexus with each release (peerrate_contract an peerrateweb)		   
   3) cd into cpcipcrest_dbmigrations subproject
   4) mvn clean install -Donejar=true
   		Make sure the resulting jar is attached to the DBA documentation for the release
		
	
	
Q: How do I create a local copy of the DB for me to use for testing?
A: Follow the following steps (assumes you already installed PostgreSQL with valid user for DDL permissions):
	1) configure container with DB credentials in ther container-managed connection pool
	2) deploy war
	3) start app (starting the app causes the db migration plugin to upgrade the database to latest revisions)

Q: Is it possible to install the DB datamodel without running the application?
A: Yes.  The dbmigrations jar is a stand-alone runnable jar that packages the current DDLs and Java db migrations with a 
	interactive admin shell for reviewing and approving said migrations before running.  Simply run the dbmigrations 
	jar (with deps) from the command line with the following command:
		java -jar peerrate_dbmigrations_withdeps-${project.version}
		NOTE: $(project.version) is replaced on the fly by the POM version.  Each release will have it's own 
			migration jar.
	
	
Q: How to I create Unit Test Data using Oracle DB data?
A: Follow the following steps:
   1) In DbTestingExporter.java add @Test annotation on testExportDb().
   2) In applicationContext-test.xml comment the h2 datasource and uncomment oracle datasource.
   3) In peerrateweb/src/test/resources/application.properties 
      Comment - hibernate.dialect=org.hibernate.dialect.H2Dialect
                hibernate.ddlauto.setting=create-drop
	  Uncomment - hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
                  hibernate.ddlauto.setting=validate
   4) Run unit test in DbTestingExporter.java.
...
   
Q: How do I update the version in the project?
A: Assuming your version is 1.2.7 (and you are working on trunk)
   1) cd into the root of the project (/peerrate)
   2) execute "mvn versions:set -DnewVersion=1.2.7-SNAPSHOT"
   NOTE: remember to delete all the tmp files created by the version plugin.
   These files are copies of the original poms and child poms named pom.xml.versionsBackup    
   
Q: How do I configure saml for my developer workstation?
A: each instance of the application is a separate Service Provider (SP).  Each separate SP needs its own copy
	of the saml metadata and that metadata needs to be registered with the IDP.  You can register the SP 
	metadata for _YOUR_INSTANCE_ by downloading the file from {yourserver}/peerrateweb/saml/metadata 
	and uploading the resulting xml file to the IDP through whatever mechanism your IDP provides for metadata.
	NOTE:  Make sure each environment (except prod) has a value for the environmental property
		"environmental_idp_entity_id_suffix".  Examples values listed below (include colons in property vals):
		Env										Sample Value
		---------------------------------------------------------------
		dev										:dev
		matt's local tomcat dev workstation		:mattlocal
		stagingg								:staging
		qa										:qa
	
Q: Isn't saml metadata bidirectional (meaning I have to register the IDP metadata with our applicaiton the 
	same way we register our metadata with them}?  
A: Yes! But in fact, this app is preconfigured to use SSO circle and the metadata for SSO circle is already
	registered in this app.	      
   
				  
