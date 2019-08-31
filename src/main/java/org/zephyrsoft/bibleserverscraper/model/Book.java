package org.zephyrsoft.bibleserverscraper.model;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Book {

	_01("1.Mose", 50),
	_02("2.Mose", 40),
	_03("3.Mose", 27),
	_04("4.Mose", 36),
	_05("5.Mose", 34),
	_06("Josua", 24),
	_07("Richter", 21),
	_08("Rut", 4),
	_09("1.Samuel", 31),
	_10("2.Samuel", 24),
	_11("1.Könige", 22),
	_12("2.Könige", 25),
	_13("1.Chronik", 29),
	_14("2.Chronik", 36),
	_15("Esra", 10),
	_16("Nehemia", 13),
	_17("Esther", 10),
	_18("Hiob", 42),
	_19("Psalmen", 150),
	_20("Sprüche", 31),
	_21("Prediger", 12),
	_22("Hoheslied", 8),
	_23("Jesaja", 66),
	_24("Jeremia", 52),
	_25("Klagelieder", 5),
	_26("Hesekiel", 48),
	_27("Daniel", 14),
	_28("Hosea", 14),
	_29("Joel", 4),
	_30("Amos", 9),
	_31("Obadja", 1),
	_32("Jona", 4),
	_33("Micha", 7),
	_34("Nahum", 3),
	_35("Habakuk", 3),
	_36("Zefanja", 3),
	_37("Haggai", 2),
	_38("Sacharja", 14),
	_39("Maleachi", 3),
	_40("Matthäus", 28),
	_41("Markus", 16),
	_42("Lukas", 24),
	_43("Johannes", 21),
	_44("Apostelgeschichte", 28),
	_45("Römer", 16),
	_46("1.Korinther", 16),
	_47("2.Korinther", 13),
	_48("Galater", 6),
	_49("Epheser", 6),
	_50("Philipper", 4),
	_51("Kolosser", 4),
	_52("1.Thessalonicher", 5),
	_53("2.Thessalonicher", 3),
	_54("1.Timotheus", 6),
	_55("2.Timotheus", 4),
	_56("Titus", 3),
	_57("Philemon", 1),
	_58("Hebräer", 13),
	_59("Jakobus", 5),
	_60("1.Petrus", 5),
	_61("2.Petrus", 3),
	_62("1.Johannes", 5),
	_63("2.Johannes", 1),
	_64("3.Johannes", 1),
	_65("Judas", 1),
	_66("Offenbarung", 22);

	public static Stream<Book> books() {
		return Stream.of(values());
	}

	private String name;
	private int chapters;

	private Book(String name, int chapters) {
		this.name = name;
		this.chapters = chapters;
	}

	public Stream<String> bookChapters() {
		if (chapters == 1) {
			return Stream.of(name);
		} else {
			return IntStream.range(1, chapters + 1)
				.mapToObj(chapter -> name + chapter);
		}
	}

}
