# bibleserver-scraper

This tool can package content from bibleserver.com nicely:
1. as [MySword Bible](https://www.mysword.info) modules
2. as side-by-side, verse-by-verse translation comparison

The latter comes with a bonus feature you can't buy in any print bible (or at least I couldn't find one):
no headings! Very useful if you want to read the bible without being influenced beforehand about what to expect.

The output can also be used by [bible-bot](https://codeberg.org/mathisdt/bible-bot/) to display and send bible verses.

**Please respect the copyright and don't publish bible texts anywhere without consent of the publishers!**

## Usage

Start the released JAR using Java 11 or above with an (empty) directory as parameter, e.g.

`java -jar bibleserver-scraper-1.0.0-SNAPSHOT.jar /home/username/bibles`

It will show you on the standard output what it does. If all goes well, it will create four MySword bible modules
(ELB, LUT, NLB and KJV) and a LaTeX source file for the translation comparison (more below).

## MySword Bible modules

You have to copy the resulting files to your device, either by mailing them to yourself or via [Android Debug Bridge](https://developer.android.com/studio/command-line/adb):

`adb push *.mybible /storage/emulated/0/mysword/bibles`

## side-by-side, verse-by-verse translation comparison

The created file has to be compiled to a PDF using [LaTeX](https://www.latex-project.org).
Please use a LaTeX distribution of your choice which supports these packages (you may have to install them additionally):
inputenc, fontenc, geometry, setspace, graphicx, hyperref, tabu, longtable

Compilation (e.g. with `pdflatex side-by-side.tex`) has to happen **twice** for the table of contents to have the right page numbers.

## License

This project is licensed under GPL v3. If you submit or contribute changes, these are automatically licensed
under GPL v3 as well. If you don't want that, please don't submit the contribution (e.g. pull request)!
