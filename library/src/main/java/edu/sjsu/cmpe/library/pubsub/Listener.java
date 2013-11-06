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
package edu.sjsu.cmpe.library.pubsub;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.Status;
import edu.sjsu.cmpe.library.repository.BookRepositoryManager;

public class Listener {
	
	private LibraryServiceConfiguration config;
	
	public Listener(LibraryServiceConfiguration configuration){
		config = configuration;
	}
	
	public void listenForMessages(){
		String user = config.getApolloUser();
		String password = config.getApolloPassword();
		String host = config.getApolloHost();
		int port = Integer.parseInt(config.getApolloPort());
		String destination = config.getStompTopicName();
		
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();
		factory.setBrokerURI("tcp://" + host + ":" + port);
		
		try{
			Connection connection = factory.createConnection(user, password);
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = new StompJmsDestination(destination);
	
			MessageConsumer consumer = session.createConsumer(dest);
			System.currentTimeMillis();
			System.out.println("Waiting for messages...");
			while(true) {
			    Message msg = consumer.receive();
			    if( msg instanceof  TextMessage ) {
					String body = ((TextMessage) msg).getText();
					
					updateBookRepository(getBook(body));
					
					if( "SHUTDOWN".equals(body)) {
					    break;
				}
					
				System.out.println("Received message = " + body);
	
			    } else if (msg instanceof StompJmsMessage) {
					StompJmsMessage smsg = ((StompJmsMessage) msg);
					String body = smsg.getFrame().contentAsString();
					
					updateBookRepository(getBook(body));
					
					if ("SHUTDOWN".equals(body)) {
					    break;
					}
					System.out.println("Received message = " + body);
		
			    } else {
			    	System.out.println("Unexpected message type: "+msg.getClass());
			    }
			}
			connection.close();
		} catch(JMSException e){
			e.printStackTrace();
		}
	}
	
	private void updateBookRepository(Book book){
		BookRepositoryManager.updateRepository(book);
	}
	
	private Book getBook(String bookStr){
		bookStr = bookStr.replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
		String[] bookAttributes = bookStr.split(":");
		Book book = new Book();
		book.setIsbn(Long.parseLong(bookAttributes[0]));
		book.setTitle(bookAttributes[1].replaceAll("\"", ""));
		book.setCategory(bookAttributes[2]);
		try {
			book.setCoverimage(new URL(bookAttributes[3]+":"+bookAttributes[4]));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		book.setStatus(Status.available);
		return book;
	}

}