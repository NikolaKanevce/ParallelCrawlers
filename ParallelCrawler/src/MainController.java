import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {
    private List<Crawler> crawlers;
    static Set<String> linksToCrawl;
    static Set<String> alreadyCrawled;
    private Set<Set<String>> setsOfUrls;

    private MainController() {
        this.crawlers = new ArrayList<>();
        linksToCrawl = new TreeSet<>();
        alreadyCrawled = linksToCrawl;
        this.setsOfUrls = new HashSet<>();
    }


    private void addCrawler(Crawler crawler) {
        this.crawlers.add(crawler);
    }

    private void divideTheSet(int numberOfCrawlers) {
        List<String> urls = new ArrayList<>(linksToCrawl);
        for (int i = 0; i < numberOfCrawlers; i++) {
            if (i == numberOfCrawlers - 1) {
                Set<String> set = new TreeSet<>(urls.subList(i * (urls.size() / numberOfCrawlers), urls.size()));
                this.setsOfUrls.add(set);
            } else {
                Set<String> set = new TreeSet<>(urls.subList(i * (urls.size() / numberOfCrawlers), (i + 1) * (urls.size() / numberOfCrawlers)));
                this.setsOfUrls.add(set);
            }
        }
    }

    /*static boolean isCrawled(String url) {
        return alreadyCrawled.contains(url);
    }
*/

    private void assignSetToCrawler(Set<String> urls, Crawler crawler) {
        crawler.setUrlSet(urls);
    }

    private void getUrls(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\n");
        while(scanner.hasNextLine()){
            String url = scanner.nextLine();

            if(url.startsWith("http")){
                linksToCrawl.add(url);
                alreadyCrawled.add(url);
            }

        }
        //linksToCrawl.add(file);
    }
/*
    public static void addLinkToCrawl(Elements elements) {
        Set<String> links = elements.stream().map(Node::toString).collect(Collectors.toSet());
        linksToCrawl.addAll(links);
    }

    public static Set<String> getInnerLinks(String websiteUrl) {
        Set<String> urlSet = new HashSet<>();
        Document doc;
        try {
            doc = Jsoup.connect(websiteUrl).get();
            Elements elements = doc.select("a");
            elements.forEach(k -> {
                if (k.toString().contains("href")) {
                    String url = k.toString()
                            .split("href")[1].split("\"")[1];

                    if (url.startsWith("/")) {
                        url = "https://www.rezultati.com" + url;
                    } else if (url.equals("#")) {

                    }
                    urlSet.add(url);
//                    linksToCrawl.add(url);
//                    alreadyCrawled.add(url);
                }
            });
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            //e.printStackTrace();
        }

        linksToCrawl.remove("#");
        return urlSet;

    }

    public static Set<String> getInnerLinks2(String websiteUrl) {
        Set<String> urlSet = new HashSet<>();
        Document doc;
        try {
            doc = Jsoup.connect(websiteUrl).get();
            Elements elements = doc.select("a");
            elements.forEach(k -> {
                if (k.toString().contains("href")) {
                    String url = k.toString()
                            .split("href")[1].split("\"")[1];

                    if (url.startsWith("/")) {
                        url = "https://www.flashscore.com" + url;
                    } else if (url.equals("#")) {

                    }
                    urlSet.add(url);
//                    linksToCrawl.add(url);
//                    alreadyCrawled.add(url);
                }
            });
        } catch (IllegalArgumentException | NullPointerException | IOException e) {
            //e.printStackTrace();
        }

        linksToCrawl.remove("#");
        return urlSet;

    }*/

    /*public static void getEveryLink(String webSiteUrl) throws IOException {
        Set<String> urls = getInnerLinks(webSiteUrl);
        Set<String> finalSet = new HashSet<>(urls);

        while (finalSet.size() != alreadyCrawled.size()) {
            urls.forEach(s -> {
                finalSet.addAll(getInnerLinks(s));
            });
            urls.addAll(finalSet);
        }
    }*/


/*
    public static void setLinksToCrawl(Set<String> linksToCrawl) {
        MainController.linksToCrawl = linksToCrawl;
    }
*/


    public static void main(String[] args) throws IOException {

        MainController mainController = new MainController();

//        Set<String> set = getInnerLinks("https://www.rezultati.com");
//        Set<String> stringSet = new HashSet<>(set);
//
//        set.forEach(s-> {
//            stringSet.addAll(getInnerLinks(s));
//        });
//
//        setLinksToCrawl(stringSet);
//        System.out.println(stringSet.size());
        mainController.getUrls("D:\\Arhiva\\Nikola Kanevce\\Desktop\\largeUrls.txt");
        mainController.divideTheSet(500);

        for (int j = 0; j < mainController.setsOfUrls.size(); j++){
            List<Set<String>> list = new ArrayList<>(mainController.setsOfUrls);
            Crawler crawler = new Crawler();
            mainController.assignSetToCrawler(list.get(j), crawler);
            mainController.addCrawler(crawler);
        }

        mainController.crawlers.forEach(s->{
            Thread crawlerThread = new Thread(s);
            crawlerThread.start();
        });

    }
}
