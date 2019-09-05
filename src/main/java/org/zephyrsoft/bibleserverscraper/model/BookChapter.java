package org.zephyrsoft.bibleserverscraper.model;

public class BookChapter {

	private Book book;
	private int chapter;
	private String nameGerman;
	private String nameEnglish;

	public BookChapter(Book book, int chapter, String nameGerman, String nameEnglish) {
		this.book = book;
		this.chapter = chapter;
		this.nameGerman = nameGerman;
		this.nameEnglish = nameEnglish;
	}

	public Book getBook() {
		return book;
	}

	public int getChapter() {
		return chapter;
	}

	public String getPrintNameGerman() {
		return getNameGerman()
			.replaceAll("^([1-5]\\.)([A-Z])", "$1 $2")
			.replaceAll("([a-z])([0-9]+)$", "$1 $2");
	}

	public String getNameGerman() {
		return nameGerman;
	}

	public String getNameEnglish() {
		return nameEnglish;
	}

}
