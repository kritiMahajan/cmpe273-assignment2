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
package edu.sjsu.cmpe.procurement.point2point;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;

public class Consumer {

	private static ProcurementServiceConfiguration psConfig;
	
	public static void setProcurementServiceConfiguration(ProcurementServiceConfiguration psConfig){
		Consumer.psConfig = psConfig;
	}
	
	public List<String> getMessagesFromQueue(){
		
		String user = psConfig.getApolloUser();
    	String password = psConfig.getApolloPassword();
    	String host = psConfig.getApolloHost();
    	int port = Integer.parseInt(psConfig.getApolloPort());
    	String queue = psConfig.getStompQueueName();
    	String destination = psConfig.getStompQueueName();

		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);

		List<String> messages = new ArrayList<String>();
		
		try{
			Connection connection = factory.createConnection(user, password);
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = new StompJmsDestination(destination);
	
			MessageConsumer consumer = session.createConsumer(dest);
			System.out.println("Waiting for messages from " + queue + "...");
			
			long waitUntil = 5000; // wait for 5 sec
			while(true) {
			    Message msg = consumer.receive(waitUntil);
			    if( msg instanceof  TextMessage ) {
			           String body = ((TextMessage) msg).getText();
			           System.out.println("Received message = " + body);
			           messages.add(body);
			    } else if (msg == null) {
			          System.out.println("No new messages. Exiting due to timeout - " + waitUntil / 1000 + " sec");
			          break;
			    } else {
			         System.out.println("Unexpected message type: " + msg.getClass());
			    }
			} // end while loop
			connection.close();
			System.out.println("Done");
		}catch(JMSException e){
			e.printStackTrace();
		}
		
		return messages;
	}
}