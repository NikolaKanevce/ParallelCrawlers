import org.jsoup.Jsoup;

import java.io.IOException;

public class Check {
    public static void main(String[] args) throws IOException {
        System.out.println(Jsoup.connect("https://www.flashscore.com/").get().select("a"));
    }
}
