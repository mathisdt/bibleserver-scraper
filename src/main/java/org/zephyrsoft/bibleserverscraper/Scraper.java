package org.zephyrsoft.bibleserverscraper;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.BookChapter;
import org.zephyrsoft.bibleserverscraper.model.Translation;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper {
	private static final Logger LOG = LoggerFactory.getLogger(Scraper.class);

	private Random random = new Random();

	public static void main(String args[]) {
		if (args.length < 1 || directoryNotCorrect(args[0])) {
			LOG.error("please provide an existing target directory as first parameter");
		} else {
			Scraper scraper = new Scraper();
			scraper.scrape(args[0]);
		}
	}

	private static boolean directoryNotCorrect(String dir) {
		File directory = new File(dir);
		return !directory.exists() || !directory.isDirectory() || !directory.canWrite();
	}

	public void scrape(String directory) {
		try (WebClient client = new WebClient()) {
			client.getOptions().setCssEnabled(false);
			client.getOptions().setJavaScriptEnabled(false);
			client.getOptions().setHistoryPageCacheLimit(1);

			Translation.abbreviations().forEach(translation -> {
				Book.books().flatMap(book -> book.bookChapters()).forEach(bookChapter -> {
					boolean shouldWait = scrapeChapter(directory, client, translation, bookChapter);
					if (shouldWait) {
						sleepRandomTime();
					}
				});
			});
		}
	}

	private boolean scrapeChapter(String directory, WebClient client, String translation, BookChapter bookChapter) {
		File targetFile = new File(directory + File.separator + translation + "-" +
			bookChapter.getBook().getOrdinal() + "-" + bookChapter.getName() + ".txt");
		if (targetFile.exists()) {
			LOG.debug("not fetching {} in {}, file {} exists", bookChapter.getName(), translation, targetFile);
			return false;
		} else {
			try {
				LOG.debug("fetching {} in {}", bookChapter.getName(), translation);
				String searchUrl = "https://www.bibleserver.com/text/" + URLEncoder.encode(translation, "UTF-8")
					+ "/" + URLEncoder.encode(bookChapter.getName(), "UTF-8");
				Page page = client.getPage(searchUrl);

				if (page.isHtmlPage()) {
					handleChapter(targetFile, (HtmlPage)page);
				} else {
					LOG.warn("result was not HTML: {}", page.getWebResponse().getContentAsString(StandardCharsets.UTF_8));
				}
			} catch (Exception e) {
				LOG.warn("error fetching " + bookChapter.getName() + " in " + translation, e);
			}
			return true;
		}
	}

	private void handleChapter(File targetFile, HtmlPage page) throws IOException {
		List<DomNode> verses = page.<DomNode>getByXPath("//*[@class='chapter']/*[contains(@class,'verse')]");
		if (verses.size() == 0) {
			LOG.warn("no verses found, not writing file to disk");
		} else {
			List<String> versesText = new LinkedList<>();
			for (DomNode verse : verses) {
				String verseString = verse.<DomNode>getByXPath("./text()").stream()
					.map(node -> node.asText())
					.collect(joining(" "))
					.replaceAll(" {2,}", " ")
					.replaceAll("(\\w) ([\\.!\\?,;:])", "$1$2");
				versesText.add(verseString);
			}
			Files.write(targetFile.toPath(), versesText, StandardOpenOption.CREATE_NEW);
		}
	}

	private void sleepRandomTime() {
		try {
			int seconds = random.nextInt(4) + 2;
			LOG.debug("waiting for {} seconds", seconds);
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// do nothing
		}
	}

}
