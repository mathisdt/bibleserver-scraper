package org.zephyrsoft.bibleserverscraper;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.BookChapter;
import org.zephyrsoft.bibleserverscraper.model.Translation;

public class MySwordExporter {
	private static final Logger LOG = LoggerFactory.getLogger(MySwordExporter.class);

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public void export(String rawDirectory, String myswordDirectory) {
		Translation.forEach(translation -> {
			String filename = sqliteFileName(myswordDirectory, translation);
			try (Connection connection = open(filename)) {
				if (connection==null) {
					LOG.debug("not writing {}, file {} exists", translation.getAbbreviation(), sqliteFileName(myswordDirectory, translation));
				} else {
					init(connection, translation);
					writeContent(connection, translation, rawDirectory);
				}
			} catch (Exception e) {
				new File(filename).delete();
				LOG.warn("error writing " + translation.getAbbreviation() + ", deleted partly-finished file " + filename, e);
			}
		});
	}

	private Connection open(String filename) throws SQLException {
		File file = new File(filename);
		if (file.exists()) {
			return null;
		}
		return DriverManager.getConnection("jdbc:sqlite:" + filename);
	}

	private String sqliteFileName(String directory, Translation translation) {
		return directory + File.separator + translation.getAbbreviation() + ".bbl.mybible";
	}

	private void init(Connection connection, Translation translation) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.addBatch(
				"CREATE TABLE \"Bible\" (\"Book\" INT,\"Chapter\" INT,\"Verse\" INT,\"Scripture\" TEXT);");
			statement.addBatch(
				"CREATE UNIQUE INDEX \"bible_key\" ON \"Bible\" (\"Book\" ASC, \"Chapter\" ASC, \"Verse\" ASC);");
			statement.addBatch(
				"CREATE TABLE \"Details\" (\"Description\" NVARCHAR(255),\"Abbreviation\" NVARCHAR(50),\"Comments\" TEXT,\"Version\" TEXT, \"VersionDate\" DATETIME, "
					+ "\"PublishDate\" DATETIME,\"RightToLeft\" BOOL,\"OT\" BOOL,\"NT\" BOOL,\"Strong\" BOOL);");
			statement.addBatch(
				"INSERT INTO \"Details\" (\"Description\", \"Abbreviation\", \"Comments\", \"Version\", \"VersionDate\", "
					+ "\"RightToLeft\", \"OT\", \"NT\", \"Strong\") "
					+ "VALUES "
					+ "(\"" + translation.getName() + "\", \"" + translation.getAbbreviation() + "\", \""
					+ translation.getName() + "\", \"1.0\", \"" + LocalDate.now().format(dateFormatter)
					+ "\", 0, 1, 1, 0)");
			statement.executeBatch();
		}
	}

	private void writeContent(Connection connection, Translation translation, String rawDirectory)
		throws SQLException, IOException {
		try (PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO \"Bible\" (\"Book\",\"Chapter\",\"Verse\",\"Scripture\") VALUES (?,?,?,?);")) {
			for (Book book : Book.values()) {
				for (BookChapter bookChapter : book.bookChapters().collect(toList())) {
					File versesFile = new File(rawDirectory + File.separator + translation.fileNameOf(bookChapter));
					List<String> verses = Files.readAllLines(versesFile.toPath(), StandardCharsets.UTF_8);

					int verseNumber = 0;
					for (String verse : verses) {
						if (verse == null || verse.trim().equals("")) {
							continue;
						}
						verseNumber++;
						statement.setInt(1, bookChapter.getBook().getOrdinalNumber());
						statement.setInt(2, bookChapter.getChapter());
						statement.setInt(3, verseNumber);
						statement.setString(4, verse);
						statement.addBatch();
					}

				}
				statement.executeBatch();
				LOG.debug("wrote {}, {}", translation.getAbbreviation(), book.getNameGerman());
			}
		}
	}

}
