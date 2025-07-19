package com.lyric.anhvui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnhVui {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnhVui.class);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static final Map<Integer, Long> LINE_DELAYS = new HashMap<>();

    static {
        LINE_DELAYS.put(0, 1200L); // "Anh vui"
        LINE_DELAYS.put(1, 1300L); // "sao nước mắt cứ tuôn trào"
        LINE_DELAYS.put(2, 3750L); // "Chẳng phải như thế quá tốt hay sao"
        LINE_DELAYS.put(3, 1900L); // "Anh ta đáng giá nhường nào"
        LINE_DELAYS.put(4, 3500L); // "Ngược lại nhìn anh trông chẳng ra sao"
        LINE_DELAYS.put(5, 2600L); // "Cũng đúng thôi"
        LINE_DELAYS.put(6, 5000L); // "Anh làm gì xứng đáng với em"
    }

    private final String[] lyrics;
    private final SongInfo songInfo;
    private static final String CACHE_FILE = "song_info_cache.txt";

    public AnhVui(String[] lyrics, String albumUrl) {
        if (lyrics == null || lyrics.length == 0) {
            throw new IllegalArgumentException("Lyrics array cannot be null or empty");
        }
        this.lyrics = lyrics.clone();
        this.songInfo = loadCachedSongInfo(albumUrl);
    }

    private SongInfo loadCachedSongInfo(String albumUrl) {
        File cacheFile = new File(CACHE_FILE);
        if (cacheFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {
                String cachedUrl = reader.readLine();
                if (albumUrl.equals(cachedUrl)) {
                    String title = reader.readLine();
                    String artist = reader.readLine();
                    return new SongInfo(lyrics.length, title, artist);
                }
            } catch (IOException e) {
                LOGGER.warn("Failed to read cache file: {}", CACHE_FILE, e);
            }
        }
        SongInfo info = fetchSongInfo(albumUrl);
        cacheSongInfo(albumUrl, info);
        return info;
    }

    private void cacheSongInfo(String albumUrl, SongInfo info) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CACHE_FILE))) {
            writer.write(albumUrl);
            writer.newLine();
            writer.write(info.getSongTitle());
            writer.newLine();
            writer.write(info.getArtist());
            writer.newLine();
        } catch (IOException e) {
            LOGGER.warn("Failed to cache song info for URL: {}", albumUrl, e);
        }
    }

    private SongInfo fetchSongInfo(String albumUrl) {
        String title = "Cảm ơn vì em ngỏ lời mời"; // Fallback title
        String artist = "Phạm Kỳ"; // Fallback artist
        try {
            Document doc = Jsoup.connect(albumUrl).timeout(5000).get(); // Increased timeout for reliability
            title = doc.select(".info-top-play h1.txt-primary").text();
            artist = doc.select(".info-top-play h2 a").text();
        } catch (Exception e) {
            LOGGER.warn("Failed to fetch song info from {}, using fallback values", albumUrl, e);
        }
        return new SongInfo(lyrics.length, title, artist);
    }

    public void printLyrics() {
        clearScreen();
        try {
            for (int i = 0; i < lyrics.length; i++) {
                printLine(lyrics[i], i);
                System.out.println();
            }
     
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Lyrics printing interrupted", e);
            System.out.println(ANSI_RED + "Interrupted!" + ANSI_RESET);
        }
    }

    private void printLine(String line, int lineIndex) throws InterruptedException {
        String[] words = line.trim().split("\\s+");
        long lineDelay = LINE_DELAYS.getOrDefault(lineIndex, 3500L);
        long wordDelay = lineDelay / Math.max(words.length, 1);

        for (String word : words) {
            System.out.print(getColoredWord(word, line) + " ");
            System.out.flush();
            TimeUnit.MILLISECONDS.sleep(wordDelay);
        }
    }

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(
            Arrays.asList("cảm ơn", "hạnh phúc", "vui", "tự hào", "xinh"));
    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(
            Arrays.asList("nước mắt", "nghẹn ngào", "chẳng ra sao", "làm gì xứng đáng"));

    private String getColoredWord(String word, String line) {
        String lowerLine = line.toLowerCase();
        if (POSITIVE_WORDS.stream().anyMatch(lowerLine::contains)) {
            return ANSI_BLUE + word + ANSI_RESET;
        }
        if (NEGATIVE_WORDS.stream().anyMatch(lowerLine::contains)) {
            return ANSI_RED + word + ANSI_RESET;
        }
        return word;
    }

    private void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to clear screen", e);
        }
    }

    public static final class SongInfo {

        private final int totalLines;
        private final String songTitle;
        private final String artist;

        public SongInfo(int totalLines, String songTitle, String artist) {
            this.totalLines = totalLines;
            this.songTitle = songTitle;
            this.artist = artist;
        }

        public int getTotalLines() {
            return totalLines;
        }

        public String getSongTitle() {
            return songTitle;
        }

        public String getArtist() {
            return artist;
        }
    }

    public SongInfo getLyricsInfo() {
        return songInfo;
    }

    public static void main(String[] args) {
        String[] lyrics = {
            "Anh vui ",
            "sao nước mắt cứ tuôn trào",
            "Chẳng phải như thế quá tốt hay sao",
            "Anh ta đáng giá nhường nào ",
            "Ngược lại nhìn anh trông chẳng ra sao",
            "Cũng đúng thôi",
            "Anh làm gì xứng đáng với em..."
        };
        String albumUrl = "https://zingmp3.vn/album/ANH-VUI-Single-Pham-Ky/6BDIEE7A.html";

        try {
            AnhVui printer = new AnhVui(lyrics, albumUrl);
            SongInfo info = printer.getLyricsInfo();
            System.out.println(ANSI_GREEN + "=== Song Lyrics: " + info.getSongTitle() + " ===" + ANSI_RESET);
            System.out.printf(ANSI_CYAN + "Artist: %s%n" + ANSI_RESET, info.getArtist());
            System.out.printf(ANSI_CYAN + "Total Lines: %d%n%n" + ANSI_RESET, info.getTotalLines());
            System.out.println(ANSI_YELLOW + "Starting lyrics display (press Ctrl+C to exit)..." + ANSI_RESET);
            printer.printLyrics();
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error initializing printer: Invalid lyrics provided", e);
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }
    }
}
