package org.zephyrsoft.bibleserverscraper;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter {
	private static final Logger LOG = LoggerFactory.getLogger(Starter.class);

	public static void main(String args[]) {
		if (args.length < 1 || directoryNotCorrect(args[0])) {
			LOG.error("please provide an existing target directory as first parameter");
		} else {
			Scraper scraper = new Scraper();
			String mainDirectory = args[0];
			String rawDirectory = subdir(mainDirectory, "raw");
			String mySwordDirectory = subdir(mainDirectory, "mysword");
			String latexDirectory = subdir(mainDirectory, "latex");

			scraper.scrape(rawDirectory);

			// TODO create SQLite3 database in mySwordDirectory for each translation
//			sqlite> .schema bible
//			CREATE TABLE IF NOT EXISTS "Bible" ("Book" INT,"Chapter" INT,"Verse" INT,"Scripture" TEXT);
//			CREATE UNIQUE INDEX "bible_key" ON "Bible" ("Book" ASC, "Chapter" ASC, "Verse" ASC);
//			sqlite> select * from bible limit 1;
//			1|1|1|Im Anfang schuf Gott die Himmel und die Erde.
//			sqlite> .schema details
//			CREATE TABLE IF NOT EXISTS "Details" ("Description" NVARCHAR(255),"Abbreviation" NVARCHAR(50),"Comments" TEXT,"Version" TEXT, "VersionDate" DATETIME, "PublishDate" DATETIME,"RightToLeft" BOOL,"OT" BOOL,"NT" BOOL,"Strong" BOOL);
//			sqlite> select * from details;
//			Elberfelder Übersetzung von 1871|Elb1871|Elberfelder Übersetzung von 1871.|1.0|2009-06-15 00:00:00||0|1|1|0

			// TODO create A4-Landscape LaTeX sources for each verse, comparing the translations, in latexDirectory

		}
	}

	private static boolean directoryNotCorrect(String dir) {
		File directory = new File(dir);
		return !directory.exists() || !directory.isDirectory() || !directory.canWrite();
	}

	private static String subdir(String mainDirectory, String subDirectory) {
		String subdirName = mainDirectory + File.separator + subDirectory;
		File subdir = new File(subdirName);
		subdir.mkdirs();
		return subdirName;
	}

}
