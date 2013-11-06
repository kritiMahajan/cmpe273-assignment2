package edu.sjsu.cmpe.procurement;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.procurement.domain.Book;
import edu.sjsu.cmpe.procurement.domain.BookArrivals;
import edu.sjsu.cmpe.procurement.point2point.Consumer;
import edu.sjsu.cmpe.procurement.pubsub.Publisher;

@Every("300s")
public class ProcurementJobs extends Job{

	private Consumer consumer = new Consumer();
	private Publisher publisher = new Publisher();
	private static Client client;
	
	private static final String STUDENT_ID_LAST_FIVE = "05348";
	private static final String PUBLISHER_ENDPOINT = "http://54.215.210.214:9000/orders";

	
	
	@Override
	public void doJob() {
		System.out.println("***************Procurement Jobs******************");
		List<String> msgs = consumer.getMessagesFromQueue(); //library-a:1234
		System.out.println("Messages Count " + msgs.size() );
		if(msgs.size() > 0){
			List<String> isbns = getIsbnsFromMessages(msgs);
			String bookOrderJson = createBookOrder(isbns);
			postBookOrder(bookOrderJson);
		}
		
		BookArrivals bookArrivals = getBookArrivals();
		for(Book book : bookArrivals.getShipped_books())
			publisher.publishBook(book);
	}
	
	private BookArrivals getBookArrivals() {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);

		ClientResponse response = client.resource(PUBLISHER_ENDPOINT+"/"+STUDENT_ID_LAST_FIVE)
				.accept("application/json").get(ClientResponse.class);
 
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
 
		BookArrivals output = response.getEntity(BookArrivals.class);
 
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		return output;
	}

	private void postBookOrder(String bookOrderJson) {
		try{
			client = Client.create();
			ClientResponse response = client.resource(PUBLISHER_ENDPOINT).type("application/json")
					   .post(ClientResponse.class, bookOrderJson);
			 
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String createBookOrder(List<String> isbns) {
		StringBuilder body = new StringBuilder();
		body.append("{\"id\" : \"" + STUDENT_ID_LAST_FIVE + "\", ");
		body.append("\"order_book_isbns\" : [");
		for(String isbn : isbns)
			body.append(isbn).append(",");
		body.deleteCharAt(body.length() - 1);
		body.append("]}");
		System.out.println(body.toString());
		
		return body.toString();
	}

	private List<String> getIsbnsFromMessages(List<String> messages){
		List<String> isbns = new ArrayList<String>();
		for(String message : messages){
			String[] parts = message.split(":");
			isbns.add(parts[1]);
		}
		
		return isbns;
	}

}
