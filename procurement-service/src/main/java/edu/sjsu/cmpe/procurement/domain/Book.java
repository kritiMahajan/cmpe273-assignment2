package edu.sjsu.cmpe.procurement.domain;


public class Book {
    private long isbn;
    private String title;

    // add more fields here
    private String category;
    private String coverimage;
    
    @Override
	public String toString() {
		return "{"+getIsbn()+"}:{\""+getTitle()+"\"}:{\""+getCategory().toLowerCase()+"\"}:{\""+getCoverimage()+"\"}";
	}

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCoverimage() {
		return coverimage;
	}

	public void setCoverimage(String coverimage) {
		this.coverimage = coverimage;
	}

	/**
     * @return the isbn
     */
    public long getIsbn() {
	return isbn;
    }

    /**
     * @param isbn
     *            the isbn to set
     */
    public void setIsbn(long isbn) {
	this.isbn = isbn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }
}
