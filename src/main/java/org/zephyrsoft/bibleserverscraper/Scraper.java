package org.zephyrsoft.bibleserverscraper;

import static java.util.stream.Collectors.joining;

import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.Translation;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scraper {
	private static final Logger LOG = LoggerFactory.getLogger(Scraper.class);

	public static void main(String args[]) {
		Scraper scraper = new Scraper();
		scraper.scrape();
	}

	// TODO parameter: target directory
	public void scrape() {
		try (WebClient client = new WebClient()) {
			client.getOptions().setCssEnabled(false);
			client.getOptions().setJavaScriptEnabled(false);

			Translation.abbreviations().forEach(translation -> {
				Book.books().flatMap(book -> book.bookChapters()).forEach(bookChapter -> {
					scrapeChapter(client, translation, bookChapter);
				});
			});
		}
	}

	private void scrapeChapter(WebClient client, String translation, String bookChapter) {
		// TODO scrape only if the target file doesn't exist yet
		try {
			String searchUrl = "https://www.bibleserver.com/text/" + translation + "/" + URLEncoder.encode(bookChapter, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);

			handleChapter(translation, bookChapter, page);
		} catch (Exception e) {
			LOG.warn("error on " + bookChapter + " in " + translation, e);
		}
	}

	// TODO write each chapter to a text file, one verse per line (rename or delete if an exception occurs)
	private void handleChapter(String translation, String bookChapter, HtmlPage page) {
		List<DomNode> verses = page.<DomNode>getByXPath("//*[@class='chapter']/*[contains(@class,'verse')]");

		LOG.debug("============= {} / {}", translation, bookChapter);
		for (DomNode verse : verses) {
			String verseString = verse.<DomNode>getByXPath("./text()").stream()
				.map(node -> node.asText())
				.collect(joining(" "))
				.replaceAll(" {2,}", " ")
				.replaceAll("(\\w) ([\\.!\\?,;:])", "$1$2");
			LOG.debug(verseString);
		}
	}

}
