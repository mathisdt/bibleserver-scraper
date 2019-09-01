package org.zephyrsoft.bibleserverscraper.model;

public class BookChapter {

	private Book book;
	private String name;

	public BookChapter(Book book, String name) {
		this.book = book;
		this.name = name;
	}

	public Book getBook() {
		return book;
	}

	public String getName() {
		return name;
	}

}
