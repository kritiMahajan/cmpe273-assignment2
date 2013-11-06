package edu.sjsu.cmpe.procurement.domain;

public class BookOrder {
	
	private String id;
	private long[] order_book_isbns;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long[] getOrder_book_isbns() {
		return order_book_isbns;
	}
	public void setOrder_book_isbns(long[] order_book_isbns) {
		this.order_book_isbns = order_book_isbns;
	}
	
	
}
