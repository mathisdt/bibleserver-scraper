package org.zephyrsoft.bibleserverscraper.model;

import java.util.stream.Stream;

public enum Translation {

	ELB("ELB"),
	LUT("LUT"),
	SLT("SLT"),
	NLB("NLB"),
	KJV("KJV");

	private String abbreviation;

	private Translation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public static Stream<String> abbreviations() {
		return Stream.of(values())
			.map(t -> t.abbreviation);
	}

}
