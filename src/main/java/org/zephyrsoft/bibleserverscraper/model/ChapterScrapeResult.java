package org.zephyrsoft.bibleserverscraper.model;

public class ChapterScrapeResult {

	private boolean shouldWait;
	private boolean wasSuccessful;

	public ChapterScrapeResult(boolean shouldWait, boolean wasSuccessful) {
		this.shouldWait = shouldWait;
		this.wasSuccessful = wasSuccessful;
	}

	public boolean shouldWait() {
		return shouldWait;
	}

	public boolean wasSuccessful() {
		return wasSuccessful;
	}

}
