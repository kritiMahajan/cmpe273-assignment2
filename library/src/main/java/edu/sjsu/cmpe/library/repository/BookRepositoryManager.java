/**
 * 
 */
package edu.sjsu.cmpe.library.repository;

import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.Status;

/**
 * @author Kriti
 *
 */
public class BookRepositoryManager {
	
	private static BookRepositoryInterface bookRepository;
	
	public static void setBookRepository(BookRepositoryInterface bookRepo){
		bookRepository = bookRepo;
	}
	
	public static void updateRepository(Book book){
		Book bookFromRepo = bookRepository.getBookByISBN(book.getIsbn());
		if(bookFromRepo == null){
			((BookRepository)bookRepository).saveBookIfAbsent(book);
		}else{
			bookFromRepo.setStatus(Status.available);
		}	
	}

}
