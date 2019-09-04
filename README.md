# bibleserver-scraper

This tool can package content from bibleserver.com nicely:
1. as [MySword Bible](https://www.mysword.info) modules
2. as side-by-side, verse-by-verse translation comparison

**Please respect the copyright and don't publish bible texts anywhere without consent of the publishers!**

## Usage

Start the released JAR with an (empty) directory as parameter, e.g.

`java -jar bibleserver-scraper-1.0.0-SNAPSHOT.jar /home/username/bibles`

It will show you on the standard output what it does. If all goes well, it will create five MySword bible modules
(ELB, LUT, SLT, NLB and KJV) and some LaTeX source files for the translation comparison (more below).

## MySword Bible modules

You have to copy the resulting files to your device, either by mailing them to yourself or via Android Debug Bridge:

`adb push *.mybible /storage/emulated/0/mysword/bibles`

## side-by-side, verse-by-verse translation comparison

The created files have to be compiled to a PDF using [LaTeX](https://www.latex-project.org). Please use a LaTeX distribution of your choice.
