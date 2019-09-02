package org.zephyrsoft.bibleserverscraper.model;

public class BookChapter {

	private Book book;
	private String nameGerman;
	private String nameEnglish;

	public BookChapter(Book book, String nameGerman, String nameEnglish) {
		this.book = book;
		this.nameGerman = nameGerman;
		this.nameEnglish = nameEnglish;
	}

	public Book getBook() {
		return book;
	}

	public String getNameGerman() {
		return nameGerman;
	}

	public String getNameEnglish() {
		return nameEnglish;
	}

}
