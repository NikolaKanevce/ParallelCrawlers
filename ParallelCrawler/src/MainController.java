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
    private static Set<String> linksToCrawl;
    private static Set<String> alreadyCrawled;
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


    private void assignSetToCrawler(Set<String> urls, Crawler crawler) {
        crawler.setUrlSet(urls);
    }

    private void getUrls(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\n");
        while (scanner.hasNextLine()) {
            String url = scanner.nextLine();

            if (url.startsWith("http")) {
                linksToCrawl.add(url);
                alreadyCrawled.add(url);
            }

        }

    }

    public static void main(String[] args) throws IOException {

        MainController mainController = new MainController();

        mainController.getUrls("D:\\Arhiva\\Nikola Kanevce\\Desktop\\largeUrls.txt");
        mainController.divideTheSet(500);

        for (int j = 0; j < mainController.setsOfUrls.size(); j++) {
            List<Set<String>> list = new ArrayList<>(mainController.setsOfUrls);
            Crawler crawler = new Crawler();
            mainController.assignSetToCrawler(list.get(j), crawler);
            mainController.addCrawler(crawler);
        }

        mainController.crawlers.forEach(s -> {
            Thread crawlerThread = new Thread(s);
            crawlerThread.start();
        });

    }
}
