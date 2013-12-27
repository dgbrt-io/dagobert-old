<<<<<<< HEAD
dagobert_engine: Assortment of technologies including Arquillian
========================
Author: Pete Muir
Level: Intermediate
Technologies: CDI, JSF, JPA, EJB, JPA, JAX-RS, BV
Summary: An example that incorporates multiple technologies
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java. 

There is a tutorial for this quickstart in the [Getting Started Developing Applications Guide](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/guide/dagobert_engine/).

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/dagobert_engine.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/dagobert_engine/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
=======

                                 DAGOBERT TRADING ENGINE 0.1-ALPHA
                                   (c) 2013-2014 Michael Kunzmann
                                        www.michaelkunzmann.com
								  mail (at) michaelkunzmann [dot] com
								  
								  	      License: Apache 2.0
                             http://www.apache.org/licenses/LICENSE-2.0.html
                             
                             
                               - - - - - - - - - - - - - - - - - - - - - - 
                             	   
                             	    ! ! ! ! ! W A R N I N G ! ! ! ! !
                             	    Dagobert is STILL under Development. 
                             	  Features might not have been tested yet.
                             	          Usage at your OWN risk.
                             	          
                               - - - - - - - - - - - - - - - - - - - - - - 
								  
								  
About Dagobert			  
=========================
Dagobert is a algorithmic trading engine for the J2EE platform. It comes without decision-making algorithms, because I don't want to publish mine, as you might see. 
It is currently only implemented for BTC and the MtGox API V2 (https://en.bitcoin.it/wiki/MtGox/API/HTTP/v2), but there are also others planned.

This project should be an evolutionary long-termin project.

Why "Dagobert"?
=========================
Dagobert Duck is the german name (as I am a German) of Walt Disney's Scrooge McDuck. I'm not a comic reader, but I did not find any better name. Don't get me wrong, it's the best name imho :)

Features
=========================

Implemented
-----------
 * Reading personal portfolio data (balances, wallets, order history, ...)
 * Polling MtGox every five seconds for current prices
 * Categorizing the rates into two periods of variable length. (current period and last period)
 * Calculate basic empiric data for each period: Avg, Min, Max, Standard Deviation, Quantiles
 * Strategy interface: You can implement your own trading strategy
 * HTTP REST interface: Currently XML supported, JSON is following.
 
Not implemented yet
----------------------------------------------------------
 * Strategy editor
 * Benchmarking
 * OSGI support
 * Dynamic HTML5/JS UI, for smartphone, tablet and desktop
 * Chart generation
 * JSON support for REST
 * SOAP support (XML only)
 * Blockchain analysis
 * More assets like shares, derivatives, bonds and currencies.
 * Universal interface for 3rd party API connectors to other trading platforms, not just BTC platforms
 * JPA support: I recently was using Hibernate before, but deleted it because the current architecure does not need JPA yet.

And much more...

Platform
--------------
Dagobert is based on Java Enterprise Edition 6. (http://www.oracle.com/technetwork/java/javaee/tech/index.html)
I'm using JBoss Enterprise Application Platform 6.1.0 to run this app.

Special thanks
--------------
Special thanks to adv0r, whose project gave me a great start with the MtGox API.
https://github.com/adv0r/mtgox-api-v2-java


Getting started
-----------------

Requirements (minimum)
 * Maven 3
 * Any Java EE 6 application server. I recommend JBoss AS 7.2.0 (or EAP 6.1.0)
 
 Requirements (recommended)
 * Eclipse Kepler for Java EE
 * Eclipse Plugin "JBoss Tools"
 * JBoss EAP 6.1.0
 

 1. Clone with git and import it into the IDE of your choice.
 2. Go to src/main/resources/com/dagobert_engine and create a file named settings.properties (you can use default.properties) with your API keys. Please be aware that your keys are permitted for all the rights. 
 3. To create a custom strategy, implement com.dagobert_engine.trading.service.Strategy with the CDI annotation @Alternative and add it as alternative to beans.xml
 4. Build the app with maven and deploy it to your server.

>>>>>>> branch 'master' of https://github.com/MitchK/dagobert.git
