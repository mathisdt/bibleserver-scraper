package org.zephyrsoft.bibleserverscraper.model;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Book {

	_01("1.Mose", "Genesis", 50),
	_02("2.Mose", "Exodus", 40),
	_03("3.Mose", "Leviticus", 27),
	_04("4.Mose", "Numbers", 36),
	_05("5.Mose", "Deuteronomy", 34),
	_06("Josua", "Joshua", 24),
	_07("Richter", "Judges", 21),
	_08("Rut", "Ruth", 4),
	_09("1.Samuel", "1 Samuel", 31),
	_10("2.Samuel", "2 Samuel", 24),
	_11("1.Könige", "1 Kings", 22),
	_12("2.Könige", "2 Kings", 25),
	_13("1.Chronik", "1 Chronicles", 29),
	_14("2.Chronik", "2 Chronicles", 36),
	_15("Esra", "Ezra", 10),
	_16("Nehemia", "Nehemiah", 13),
	_17("Esther", "Esther", 10),
	_18("Hiob", "Job", 42),
	_19("Psalmen", "Psalms", 150),
	_20("Sprüche", "Proverbs", 31),
	_21("Prediger", "Ecclesiastes", 12),
	_22("Hoheslied", "Song of Solomon", 8),
	_23("Jesaja", "Isaiah", 66),
	_24("Jeremia", "Jeremiah", 52),
	_25("Klagelieder", "Lamentations", 5),
	_26("Hesekiel", "Ezekiel", 48),
	_27("Daniel", "Daniel", 14),
	_28("Hosea", "Hosea", 14),
	_29("Joel", "Joel", 4),
	_30("Amos", "Amos", 9),
	_31("Obadja", "Obadiah", 1),
	_32("Jona", "Jonah", 4),
	_33("Micha", "Micah", 7),
	_34("Nahum", "Nahum", 3),
	_35("Habakuk", "Habbakuk", 3),
	_36("Zefanja", "Zephaniah", 3),
	_37("Haggai", "Haggai", 2),
	_38("Sacharja", "Zechariah", 14),
	_39("Maleachi", "Malachi", 3),
	_40("Matthäus", "Matthew", 28),
	_41("Markus", "Mark", 16),
	_42("Lukas", "Luke", 24),
	_43("Johannes", "John", 21),
	_44("Apostelgeschichte", "Acts", 28),
	_45("Römer", "Romans", 16),
	_46("1.Korinther", "1 Corinthians", 16),
	_47("2.Korinther", "2 Corinthians", 13),
	_48("Galater", "Galatians", 6),
	_49("Epheser", "Ephesians", 6),
	_50("Philipper", "Philippians", 4),
	_51("Kolosser", "Colossians", 4),
	_52("1.Thessalonicher", "1 Thessalonians", 5),
	_53("2.Thessalonicher", "2 Thessalonians", 3),
	_54("1.Timotheus", "1 Timothy", 6),
	_55("2.Timotheus", "2 Timothy", 4),
	_56("Titus", "Titus", 3),
	_57("Philemon", "Philemon", 1),
	_58("Hebräer", "Hebrews", 13),
	_59("Jakobus", "James", 5),
	_60("1.Petrus", "1 Peter", 5),
	_61("2.Petrus", "2 Peter", 3),
	_62("1.Johannes", "1 John", 5),
	_63("2.Johannes", "2 John", 1),
	_64("3.Johannes", "3 John", 1),
	_65("Judas", "Jude", 1),
	_66("Offenbarung", "Revelation", 22);

	public static Stream<Book> books() {
		return Stream.of(values());
	}

	private String nameGerman;
	private String nameEnglish;
	private int chapters;

	private Book(String nameGerman, String nameEnglish, int chapters) {
		this.nameGerman = nameGerman;
		this.nameEnglish = nameEnglish;
		this.chapters = chapters;
	}

	public String getOrdinal() {
		return name().replace("_", "");
	}

	public Stream<BookChapter> bookChapters() {
		if (chapters == 1) {
			return Stream.of(new BookChapter(this, nameGerman, nameEnglish));
		} else {
			return IntStream.range(1, chapters + 1)
				.mapToObj(chapter -> new BookChapter(this, nameGerman + chapter, nameEnglish + chapter));
		}
	}

}
