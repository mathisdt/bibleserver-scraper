package org.zephyrsoft.bibleserverscraper.model;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public enum Translation {

	ELB("ELB", BookChapter::getNameGerman),
	LUT("LUT", BookChapter::getNameGerman),
	SLT("SLT", BookChapter::getNameGerman),
	NLB("NLB", BookChapter::getNameGerman),
	KJV("KJV", BookChapter::getNameEnglish);

	private String abbreviation;
	private Function<BookChapter, String> nameGetter;

	private Translation(String abbreviation, Function<BookChapter, String> nameGetter) {
		this.abbreviation = abbreviation;
		this.nameGetter = nameGetter;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String nameOf(BookChapter bookChapter) {
		return nameGetter.apply(bookChapter);
	}

	public static void forEach(Consumer<Translation> action) {
		Stream.of(values()).forEach(action);
	}
}
