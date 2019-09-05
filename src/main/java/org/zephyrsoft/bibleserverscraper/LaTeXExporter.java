package org.zephyrsoft.bibleserverscraper;

import static java.util.stream.Collectors.toMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zephyrsoft.bibleserverscraper.model.Book;
import org.zephyrsoft.bibleserverscraper.model.BookChapter;
import org.zephyrsoft.bibleserverscraper.model.Translation;

public class LaTeXExporter {
	private static final Logger LOG = LoggerFactory.getLogger(LaTeXExporter.class);

	public void export(String rawDirectory, String latexDirectory) {
		String filename = latexDirectory + File.separator + "side-by-side.tex";
		if (new File(filename).exists()) {
			LOG.debug("not writing, file {} exists", filename);
		} else {
			try (BufferedWriter out = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
				appendHeader(out);

				for (Book book : Book.values()) {
					appendBookHeader(out, book);
					book.bookChapters().forEach(convertExceptionsInConsumer(bookChapter -> {
						appendChapterHeader(out, bookChapter);
						appendChapterContent(out, bookChapter, rawDirectory);
						appendChapterFooter(out);
					}));
					appendBookFooter(out);
					LOG.debug("exported {}", book.getNameGerman());
				}

				appendFooter(out);
			} catch (Exception e) {
				new File(filename).delete();
				LOG.warn("error while writing, deleted partly-finished file " + filename, e);
			}
		}
	}

	private void appendHeader(BufferedWriter out) throws IOException {
		out.append("\\documentclass{article}\n")
			.append("\\usepackage[T1]{fontenc}\n")
			.append("\\usepackage[utf8]{inputenc}\n")
			.append("\\usepackage[paperheight=21cm,paperwidth=29.7cm,margin=0.5cm,")
			.append("headheight=0cm,footskip=1.3cm,includehead,includefoot]{geometry}\n")
			.append("\\usepackage{graphicx}\n")
			.append("\\usepackage{tabu}\n")
			.append("\\usepackage{longtable}\n")
			.append("\\usepackage[plainpages=false,pdfpagelabels,colorlinks=true,")
			.append("linkcolor=black,anchorcolor=black]{hyperref}\n")
			.append("\\usepackage{setspace}")
			.append("\\DeclareUnicodeCharacter{00B4}{'}\n")
			.append("\\DeclareUnicodeCharacter{2006}{ }\n")
			.append("\\DeclareUnicodeCharacter{05D0}{Alef}\n")
			.append("\\DeclareUnicodeCharacter{05D1}{Bet}\n")
			.append("\\DeclareUnicodeCharacter{05D2}{Gimel}\n")
			.append("\\DeclareUnicodeCharacter{05D3}{Dalet}\n")
			.append("\\DeclareUnicodeCharacter{05D4}{He}\n")
			.append("\\DeclareUnicodeCharacter{05D5}{Vav}\n")
			.append("\\DeclareUnicodeCharacter{05D6}{Zayin}\n")
			.append("\\DeclareUnicodeCharacter{05D7}{Het}\n")
			.append("\\DeclareUnicodeCharacter{05D8}{Tet}\n")
			.append("\\DeclareUnicodeCharacter{05D9}{Yod}\n")
			.append("\\DeclareUnicodeCharacter{05DA}{Kaf}\n")
			.append("\\DeclareUnicodeCharacter{05DB}{Kaf}\n")
			.append("\\DeclareUnicodeCharacter{05DC}{Lamed}\n")
			.append("\\DeclareUnicodeCharacter{05DD}{Mem}\n")
			.append("\\DeclareUnicodeCharacter{05DE}{Mem}\n")
			.append("\\DeclareUnicodeCharacter{05DF}{Nun}\n")
			.append("\\DeclareUnicodeCharacter{05E0}{Nun}\n")
			.append("\\DeclareUnicodeCharacter{05E1}{Samekh}\n")
			.append("\\DeclareUnicodeCharacter{05E2}{Ayin}\n")
			.append("\\DeclareUnicodeCharacter{05E3}{Pe}\n")
			.append("\\DeclareUnicodeCharacter{05E4}{Pe}\n")
			.append("\\DeclareUnicodeCharacter{05E5}{Tsadi}\n")
			.append("\\DeclareUnicodeCharacter{05E6}{Tsadi}\n")
			.append("\\DeclareUnicodeCharacter{05E7}{Qof}\n")
			.append("\\DeclareUnicodeCharacter{05E8}{Resh}\n")
			.append("\\DeclareUnicodeCharacter{05D9}{Shin}\n")
			.append("\\DeclareUnicodeCharacter{FB2A}{Shin}\n")
			.append("\\DeclareUnicodeCharacter{FB2B}{Shin}\n")
			.append("\\DeclareUnicodeCharacter{FB2C}{Shin}\n")
			.append("\\DeclareUnicodeCharacter{FB2D}{Shin}\n")
			.append("\\DeclareUnicodeCharacter{05EA}{Tav}\n")
			.append("\\renewcommand{\\contentsname}{Inhalt}\n")
			.append("\\makeatletter\n")
			.append("\\renewcommand*\\l@section{\\@dottedtocline{1}{1.5em}{2.3em}}\n")
			.append("\\makeatother\n")
			.append("\\begin{document}\n")
			.append("\\setcounter{secnumdepth}{-2}\n")
			.append("\\setcounter{tocdepth}{1}\n")
			.append("\\renewcommand{\\arraystretch}{1.5}\n")
			.append("\\doublespacing\n")
			.append("\\tableofcontents\n")
			.append("\\singlespacing\n")
			.append("\\newpage\n");
	}

	private void appendBookHeader(BufferedWriter out, Book book) throws IOException {
		out.append("\\section{")
			.append(book.getPrintNameGerman())
			.append("}\n");
	}

	private void appendChapterHeader(BufferedWriter out, BookChapter bookChapter) throws IOException {
		out.append("\\subsection{")
			.append(bookChapter.getPrintNameGerman())
			.append("}\n\n")
			.append("\\begin{longtabu}{ p{1cm} | *{")
			.append(String.valueOf(Translation.values().length))
			.append("}{X|} }\n");
		Translation.forEach(convertExceptionsInConsumer(translation -> {
			out.append(" & ").append(translation.getName());
		}));
		out.append(" \\\\ \\hline\n\\endhead\n");
	}

	private void appendChapterContent(BufferedWriter out, BookChapter bookChapter, String rawDirectory)
		throws IOException {
		Map<Translation, List<String>> translationToVerses = Translation.stream().collect(toMap(
			translation -> translation,
			convertExceptionsInFunction(translation -> loadChapterContent(translation, bookChapter, rawDirectory))));
		int i = 0;
		while (oneTranslationHasEntry(i, translationToVerses)) {
			out.append(String.valueOf(i + 1))
				.append("\n");

			for (Translation translation : Translation.values()) {
				out.append("&")
					.append(translationToVerses.get(translation).size() > i
						? translationToVerses.get(translation).get(i)
						: "")
					.append("\n");
			}

			out.append("\\\\\n");
			i++;
		}
	}

	private boolean oneTranslationHasEntry(int i, Map<Translation, List<String>> translationToVerses) {
		return translationToVerses.values().stream().anyMatch(list -> list.size() > i);
	}

	private List<String> loadChapterContent(Translation translation, BookChapter bookChapter, String rawDirectory)
		throws IOException {
		File file = new File(rawDirectory + File.separator + translation.fileNameOf(bookChapter));
		return Files.readAllLines(file.toPath());
	}

	private void appendChapterFooter(BufferedWriter out) throws IOException {
		out.append("\\end{longtabu}\n");
	}

	private void appendBookFooter(BufferedWriter out) throws IOException {
		out.append("\\newpage\n");
	}

	private void appendFooter(BufferedWriter out) throws IOException {
		out.append("\n\\end{document}");
	}

	@FunctionalInterface
	private interface CheckedConsumer<T> {
		void apply(T t) throws Exception;
	}

	@FunctionalInterface
	private interface CheckedFunction<T, R> {
		R apply(T t) throws Exception;
	}

	private <T> Consumer<T> convertExceptionsInConsumer(CheckedConsumer<T> checkedConsumer) {
		return t -> {
			try {
				checkedConsumer.apply(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	private <T, R> Function<T, R> convertExceptionsInFunction(CheckedFunction<T, R> checkedFunction) {
		return t -> {
			try {
				return checkedFunction.apply(t);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}
