
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
Dagobert is a algorithmic trading engine for the J2EE platform. It comes with no decision-making algorithms, because I don't want to publish mine, as you might see. 
It currently it is implemented only for BTC and the MtGox API V2 (https://en.bitcoin.it/wiki/MtGox/API/HTTP/v2), but there are also others planned.

This project is planned as an evolutionary long-termin project.

Why "Dagobert"?
=========================
Dagobert Duck is the german name (as I am a German) of Walt Disney's Scrooge McDuck. I'm not a comic reader, but I did not find any better name. Don't get me wrong, it's the best name.

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

