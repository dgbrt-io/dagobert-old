Old implementation. New implementation here: https://github.com/thundrcloud/dagobert


							###################################################
							###################################################
							###########                            ############
							###########        -----------         ############
							###########        \         /         ############
							###########         )       (          ############
							###########       =============        ############
							###########      /  /  \ /  \ \        ############
							###########      |  |()| |()|  |       ############
							###########      |  \__/ \__/  |       ############
							###########   ###\     ..      /###    ############
							###########  ####(=============)####   ############
							###########   ####            ####     ############
							###########                            ############
							###########                            ############
							###########   D  A  G  O  B  E  R  T   ############
							###########       Trading Engine       ############
							###########                            ############
							###################################################
							###################################################


                                   DAGOBERT TRADING ENGINE 0.1-ALPHA
                                     (c) 2013-2014 Michael Kunzmann
                                        www.michaelkunzmann.com
								   mail (at) michaelkunzmann [dot] com
								  
								  	      License: Apache 2.0
                              http://www.apache.org/licenses/LICENSE-2.0.html
                             
                             
                               - - - - - - - - - - - - - - - - - - - - - - 
                             	   
                             	    ! ! ! ! ! W A R N I N G ! ! ! ! !
                             	    Dagobert has no stable release yet!
                              Some features might not have been tested yet.
                             	          Usage at your OWN risk.
                             	          
                               - - - - - - - - - - - - - - - - - - - - - - 
								  
Donations
=========
If you are trying/using Dagobert, please donate to keep this project alive and supported. 
I don't expect to get enough donations buy me a new laptop from donations. 
I expect to get some positive feedback about the work and the usage.

You don't have to spent much, every small amount will make me smile :) I don't do it for the money. If I would, I would not make it open.

Donations can be sent to 1BvBCN5dcUXbC4Vp7v5fPXg8LPNChcheYh
								  
About Dagobert			  
=========================
Dagobert is a algorithmic trading engine for the J2EE platform. It comes without any decision-making algorithms, because I don't want to publish mine, as you might see. 
It is currently only implemented for BTC and the MtGox API V2 (https://en.bitcoin.it/wiki/MtGox/API/HTTP/v2), but there are also others planned.

This project is part of my future thesis for my Bachelor in Business Information Systems at the University of Applied Sciences, Karlsruhe.

Why "Dagobert"?
=========================
Dagobert Duck is the german name (as I am a German) of Walt Disney's Scrooge McDuck. I'm not a comic reader, but I did not find any better name. Don't get me wrong, it's the best name imho :)

Features
=========================

Implemented
-----------
 * NEW: Port to Java EE 7 due to near future WebSocket support
 * Reading MtGox personal portfolio data (balances, wallets, order history, ...)
 * Polling MtGox every five seconds for current prices
 * Calculate basic empiric data for each period: Avg, Min, Max, Standard Deviation, Quantiles
 * Strategy interface: You can implement your own trading strategy
 * JSON REST API
 * ~~Seam 3 support (CDI extension)~~ Seam 3 support was dropped, we will use DeltaSpike in the future.
 
 
Not implemented yet
----------------------------------------------------------

Legend:
 **[S]** = Short-term priority, **[L]** = Long-term priority, **[U]** = Uncertain, **[C]** = Certain
 
 * **[S][C] WebSocket support**
 
 * **[S][C] ~~Moving from Seam 3 to Apache Deltaspike, as the the Seam 3 project has been stopped.~~ Stopped using Seam 3 due to some bugs with JBoss Solder and CDI 1.1. Currently DeltaSpike is not needed, but commented out in pom.xml**

 * **[S][C] Dynamic graphical UI using Angular.js**
 
 * **[S][C] Chart generation** with D3.js
 
 * **[S][C] JPA support**. Was already implemented but removed, because it is not needed for the functionality at the moment.
 
 * **[L][C] E-Mail notifications** 

 * **[L][C] Block chain analysis** Analyzing market participants by their market transactions

 * **[L][C] Benchmarking against various markets**
 
 * **[L][C] Universal interface** for 3rd party API connectors to other trading platforms like Kraken or BTC China
 
 * **[L][U] Strategy editor**
 
 * **[L][U] OSGI support**


Platform
--------------
Dagobert is based on Java Enterprise Edition 7. (http://www.oracle.com/technetwork/java/javaee/tech/index.html)
I'm using WildFly 8.0 (former JBoss AS) to run this app.

Special thanks
--------------
Adv0r, whose project gave me a great start with the MtGox API.
https://github.com/adv0r/mtgox-api-v2-java

Nitrous, because of the great API overview
https://bitbucket.org/nitrous/mtgox-api/overview



Requirements (minimum)
----------------------

 * Maven 3
 * Any Java EE 7 application server. I recommend WildFly 8.0 (former JBoss AS) http://www.jboss.org/jbossas/downloads/
 
 
Requirements (recommended)
--------------------------
 
 * Eclipse Kepler for Java EE
 * Eclipse Plugin "JBoss Tools"
 * WildFly 8.0 (former JBoss AS) http://www.jboss.org/jbossas/downloads/
 
 
Building
-----------------

    git clone https://github.com/MitchK/dagobert.git
    cd dagobert
    mvn clean package
    
Configuration
---------------------

In order to run the app, you have to do the following steps:
 1. Edit src/main/resources/META-INF/*.properties.example files to your needs and and save it as *.properties (replace * with the file name)
   => You can enable tradingEnabled to enable the trading mechanism. I recommend to turn it off by default and enable it on runtime.
   => Set your MtGox credentials mtGoxPublicKey and mtGoxPrivateKey. The api access must have the trading permission to trade.
 2. Edit src/main/webapp/WEB-INF/beans.xml. In here, you can add your personal trading implementation (It must implement com.dagobert_engine.trading.service.Strategy and has to be annotated with CDI's @Alternative). If you just want to test Dagobert without having any Strategy implementation yet, just comment out my CustomStrategy. It's not the best solution, I will make a better solution for the future.

    
Deploying to WildFly 8.0
---------------------

    mvn clean package wildfly:deploy
    
Running JUnit Tests
-------------------

    mvn clean test -Parq-wildfly-managed

