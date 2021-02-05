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

    /*int recurrentCrawling(Elements elements){
        if(MainController.alreadyCrawled.containsAll(elements)){
            return 0;
        }
        else{
            elements.forEach(s-> {
                try {
                    crawledPages.add(Jsoup.connect(s.toString()).get().toString());
                    recurrentCrawling(Jsoup.connect(s.toString()).get().select("a"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return 1;
        }
    }*/

   /* public void addLinks(Set<String> url){
        url.forEach(s -> urlSet.addAll(MainController.getInnerLinks(s)));
    }*/



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


    /*@Override
    public void run() {
        Long start = System.currentTimeMillis();
        this.urlSet.forEach(s -> {
            Document doc;
            try {
                doc = Jsoup.connect(s).get();
                String rawHtml = doc.toString();
                this.crawledPages.add(rawHtml);
                Elements elements = doc.select("a");
                elements.forEach(k -> {
                    if (k.toString().contains("href")) {
                        String url = k.toString()
                                .split("href")[1].split("\"")[1];


                        *//*if (url.startsWith("//")) {
                            url = url.substring(2);
                            if (!url.startsWith("www.")) {
                                url = "https://www." + url;
                            } else {
                                url = "https://" + url;
                            }
                            if (!MainController.isCrawled(url)) {

                                Document document;
                                try {
                                    document = Jsoup.connect(url).get();
                                    String rawHtmlLink = document.toString();
                                    this.crawledPages.add(rawHtmlLink);
                                    System.out.println("YES");
                                    MainController.alreadyCrawled.add(url);
                                } catch (IllegalArgumentException | NullPointerException | IOException e) {
                                    System.out.println("error: " + url);
                                }
                            }
                        }*//*
                        if (!url.startsWith("/")){
                            if(!MainController.isCrawled(url)){
                                Document document;

                                try {
                                    document = Jsoup.connect(url).get();
                                    String rawHtmlLink = document.toString();
                                    this.crawledPages.add(rawHtmlLink);
                                    System.out.println("YES");
                                    MainController.alreadyCrawled.add(url);

                                } catch (IOException e) {
                                    System.out.println("error " + url);
                                }
                            }
                        }
                        else if (url.startsWith("/")){
                            url = s.substring(0, "https://www.rezultati.com".length()) + url;
                            if(!MainController.isCrawled(url)){
                                Document document;

                                try {
                                    document = Jsoup.connect(url).get();
                                    String htmlRawLink = document.toString();
                                    this.crawledPages.add(htmlRawLink);
                                    System.out.println("YES");
                                    MainController.alreadyCrawled.add(url);
                                } catch (IOException e) {
                                    System.out.println("error " + url);
                                }

                            }
                        }
                        //System.out.println(url);
                    }

                });
                System.out.println("YES");
            } catch (IllegalArgumentException | NullPointerException | IOException e) {
                System.out.println("error " + s);
            }
        });

        Long finish = System.currentTimeMillis();
        System.out.println(finish - start);
    }*/


}
