import com.sun.tools.javac.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Crawler implements Runnable {
    private Set<String> urlSet;
    private Set<String> crawledPages;
    Lock lock;

    Crawler() {
        this.urlSet = new TreeSet<>();
        this.crawledPages = new HashSet<>();
        this.lock = new ReentrantLock();
    }


    void setUrlSet(Set<String> urlSet) {
        this.urlSet = urlSet;
    }

    Document crawlWebSite(String url) throws IOException {
        Document document;
        document = Jsoup.connect(url).get();
        this.crawledPages.add(document.toString());
        return document;
    }


    @Override
    public void run() {

        Long start = System.currentTimeMillis();
        this.urlSet.forEach(s -> {
            try {
                this.crawlWebSite(s);
                System.out.println("YES");
            } catch (IllegalArgumentException | NullPointerException | IOException e) {
                System.out.println("ERROR: " + s);
            }
        });
        Long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


}
