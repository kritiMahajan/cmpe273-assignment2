/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sjsu.cmpe.library.point2point;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import com.yammer.dropwizard.jersey.params.LongParam;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;

public class Producer {
    
	LibraryServiceConfiguration libConfig;
	
	public Producer(LibraryServiceConfiguration libConfig){
		this.libConfig = libConfig;
	}
	
    public void sendMessage(LongParam isbn){
    	String user = libConfig.getApolloUser();
    	String password = libConfig.getApolloPassword();
    	String host = libConfig.getApolloHost();
    	int port = Integer.parseInt(libConfig.getApolloPort());
    	String queue = libConfig.getStompQueueName();
    	String destination = libConfig.getStompQueueName();

    	StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
    	factory.setBrokerURI("tcp://" + host + ":" + port);

    	try{
	    	Connection connection = factory.createConnection(user, password);
	    	connection.start();
	    	Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    	Destination dest = new StompJmsDestination(destination);
	    	MessageProducer producer = session.createProducer(dest);
	    	producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	
	    	System.out.println("Sending book order to " + queue + "...");
	    	String data = libConfig.getLibraryName()+":"+isbn;
	    	TextMessage msg = session.createTextMessage(data);
	    	msg.setLongProperty("id", System.currentTimeMillis());
	    	producer.send(msg);
	
	    	//producer.send(session.createTextMessage("SHUTDOWN"));
	    	connection.close();
    	} catch(Exception e){
    		e.printStackTrace();
    	}
    }

}
