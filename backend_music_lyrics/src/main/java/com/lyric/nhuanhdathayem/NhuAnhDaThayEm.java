package com.lyric.nhuanhdathayem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NhuAnhDaThayEm {

    private static final Logger LOGGER = LoggerFactory.getLogger(NhuAnhDaThayEm.class);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m"; // Vibrant Red
    private static final String ANSI_GREEN = "\u001B[32m"; // Vibrant Green
    private static final String ANSI_WHITE = "\u001B[37m"; // White for text
    private static final String[] LINE_COLORS = { ANSI_GREEN, ANSI_RED ,ANSI_WHITE }; // Alternate Green and Red

    private static final Map<Integer, Long> LINE_DELAYS = new HashMap<>();

    static {
        LINE_DELAYS.put(0, 1200L); // "Và một lần cuối"
        LINE_DELAYS.put(1, 1300L); // "Để mình không cần mạnh mẽ"
        LINE_DELAYS.put(2, 3750L); // "Dù sao ta cũng đã yêu nhiều thế !"
        LINE_DELAYS.put(3, 1900L); // "Có rất nhiều điều"
        LINE_DELAYS.put(4, 3000L); // "Mà anh vẫn chưa nói ra"
        LINE_DELAYS.put(5, 2600L); // "Vì lần cuối cùng được nắm tay em bước qua khắp nẻo đường"
        LINE_DELAYS.put(6, 5000L); // "Ngắm hoàng hôn chạm bờ vai em"
        LINE_DELAYS.put(7, 3500L); // "Như khoảnh khắc đầu tiên em đến"
        LINE_DELAYS.put(8, 4000L); // "Anh cất nụ cười người vào trang kỉ niệm"
        LINE_DELAYS.put(9, 3000L); // "Như em vẫn còn bên anh"
    }

    private final String[] lyrics;
    private final SongInfo songInfo;
    private static final String CACHE_FILE = "song_info_cache.txt";

    public NhuAnhDaThayEm(String[] lyrics, String albumUrl) {
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
        String title = "Như Anh Đã Thấy Em";
        String artist = "PhúcXP, Freak D";
        try {
            Document doc = Jsoup.connect(albumUrl).timeout(5000).get();
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
        String color = LINE_COLORS[lineIndex % LINE_COLORS.length];

        for (String word : words) {
            System.out.print(color + word + ANSI_RESET + " ");
            System.out.flush();
            TimeUnit.MILLISECONDS.sleep(wordDelay);
        }
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
            "Và một lần cuối",
            "Để mình không cần mạnh mẽ",
            "Dù sao ta cũng đã yêu nhiều thế !",
            "Có rất nhiều điều",
            "Mà anh vẫn chưa nói ra...",
            "Vì lần cuối cùng được nắm tay em bước qua khắp nẻo đường",
            "Ngắm hoàng hôn chạm bờ vai em",
            "Như khoảnh khắc đầu tiên em đến",
            "Anh cất nụ cười người vào trang kỉ niệm",
            "Như em vẫn còn bên anh..."
        };
        String albumUrl = "https://zingmp3.vn/album/Nhu-Anh-Da-Thay-Em-Single-PhucXP-Freak-D/6B6E88W9.html";

        try {
            NhuAnhDaThayEm printer = new NhuAnhDaThayEm(lyrics, albumUrl);
            SongInfo info = printer.getLyricsInfo();
            System.out.println(ANSI_WHITE + "=== Song Lyrics: " + info.getSongTitle() + " ===" + ANSI_RESET);
            System.out.printf(ANSI_WHITE + "Artist: %s%n" + ANSI_RESET, info.getArtist());
            System.out.printf(ANSI_WHITE + "Total Lines: %d%n%n" + ANSI_RESET, info.getTotalLines());
            System.out.println(ANSI_WHITE + "Starting lyrics display (press Ctrl+C to exit)..." + ANSI_RESET);
            printer.printLyrics();
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error initializing printer: Invalid lyrics provided", e);
            System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
        }
    }
}