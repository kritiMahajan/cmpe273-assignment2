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
package edu.sjsu.cmpe.procurement.pubsub;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;
import edu.sjsu.cmpe.procurement.domain.Book;

public class Publisher {
	
	private static ProcurementServiceConfiguration config;
	private static Connection connection;
	private static MessageProducer producerA, producerB;
	private static Session session;
	
	public static void setProcurementServiceConfiguration(ProcurementServiceConfiguration psConfig){
		config = psConfig;
	}
	
	public void publishBook(Book book){
		String user = config.getApolloUser();
		String password = config.getApolloPassword();
		String host = config.getApolloHost();
		int port = Integer.parseInt(config.getApolloPort());
		
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		
		try{
			connection = factory.createConnection(user, password);
			connection.start(); 
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination destinationA = new StompJmsDestination(config.getStompTopicNameA());
			producerA = session.createProducer(destinationA);
			producerA.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			Destination destinationB = new StompJmsDestination(config.getStompTopicNameB());
			producerB = session.createProducer(destinationB);
			producerB.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			String data = book.toString();
			TextMessage msg = session.createTextMessage(data);
			msg.setLongProperty("id", System.currentTimeMillis());

			if("computer".equalsIgnoreCase(book.getCategory()))
				producerB.send(msg);

			producerA.send(msg);
			
			connection.close();
		}catch(JMSException e){
			e.printStackTrace();
		}
	}
	
	protected void finalize(){
		try {
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
