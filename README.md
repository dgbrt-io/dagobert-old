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
							###########      \     ..      /       ############
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
 * Reading MtGox personal portfolio data (balances, wallets, order history, ...)
 * Polling MtGox every five seconds for current prices
 * Calculate basic empiric data for each period: Avg, Min, Max, Standard Deviation, Quantiles
 * Strategy interface: You can implement your own trading strategy
 * JSON REST API
 * Seam 3 support (CDI extension)
 
Not implemented yet
----------------------------------------------------------

Legend:
 **[S]** = Short-term priority, **[L]** = Long-term priority, **[U]** = Uncertain, **[C]** = Certain

 * **[S][C] Graphical UI**. Either internal UI with technologies JSF or a Node.js client for this engine. There is no decision yet.
 
 * **[S][C] JPA support**. Was already implemented but removed, because it is not needed for the functionality at the moment.
 
 * **[S][C] E-Mail notifications** 
 
 * **[S][C] Chart generation** Candle charts (see for reference: http://bitcoin.clarkmoody.com/), Volume charts
 
 * **[L][C] Block chain analysis** Analysing market participants by their market transactions

 * **[L][C] Benchmarking against various markets**
 
 * **[L][C] Universal interface** for 3rd party API connectors to other trading platforms like Kraken or BTC China
 
 * **[L][U] Strategy editor **
 
 * **[L][U] OSGI support**


Platform
--------------
Dagobert is based on Java Enterprise Edition 6. (http://www.oracle.com/technetwork/java/javaee/tech/index.html)
I'm using JBoss Enterprise Application Platform 6.1.0 to run this app.

Special thanks
--------------
Adv0r, whose project gave me a great start with the MtGox API.
https://github.com/adv0r/mtgox-api-v2-java

Nitrous, because of the great API overview
https://bitbucket.org/nitrous/mtgox-api/overview



Requirements (minimum)
----------------------

 * Maven 3
 * Any Java EE 6 application server. I recommend JBoss EAP 6.1.0 Final (or AS 7.2.0) http://www.jboss.org/jbossas/downloads/
 
 
Requirements (recommended)
--------------------------
 
 * Eclipse Kepler for Java EE
 * Eclipse Plugin "JBoss Tools"
 * JBoss EAP 6.1.0 Final http://www.jboss.org/jbossas/downloads/
 
 
Building
-----------------

    git clone https://github.com/MitchK/dagobert.git
    cd dagobert
    mvn clean package
    
Configuration
---------------------

In order to run the app, you have to do the following steps:
 1. Edit src/main/resources/META-INF/seam-beans.example.xml and save it as seam-beans.xml
   => You can enable c:tradingEnabled to enable the trading mechanism. I recommend to turn it off for debugging purposes.
   => Set your MtGox credentials c:mtGoxPublicKey and c:mtGoxPrivateKey. The api access must have the trading permission to trade.
 2. Edit src/main/webapp/WEB-INF/beans.xml. In here, you can add your personal trading implementation (It must implement com.dagobert_engine.trading.service.Strategy and has to be annotated with CDI's @Alternative). If you just want to test Dagobert without having any Strategy implementation yet, just comment out my CustomStrategy. It's not the best solution, I will make a better solution for the future.

    
Deploying to JBoss AS
---------------------

    mvn clean package jboss-as:deploy
    
Running JUnit Tests
-------------------

    mvn clean test -Parq-jbossas-managed

