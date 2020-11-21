package lt.regimantas.dataCollector.services;


import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.ParserNodes;
import lt.regimantas.dataCollector.repositories.sitesToParse.OfferInterface;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;
import lt.regimantas.dataCollector.repositories.sitesToParse.SitesFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataParser {

    public static List<Offer> whatToParse(Sites site, int test) {
        List<Offer> offerList = new ArrayList<>();
        if (site.equals(Sites.ALL)) {
            for (Sites s : Sites.values()) {
                if (!s.equals(Sites.ALL)) {
                    offerList.addAll(parseOffers(s, test));
                }
            }
        } else {
            offerList.addAll(parseOffers(site, test));
        }
        return offerList;
    }

    public static List<Offer> parseOffers(Sites site, int test) {
        OfferInterface siteFactory = SitesFactory.getSiteFactory(site);
        try {
            List<Offer> offerList = extractDescriptions(
                    parseOfferHeaders(
                            siteFactory.getNodes().getInicialPage(),
                            siteFactory.getNodes().getUrl(),
                            siteFactory,
                            test
                    ),
                    siteFactory
            );
            return offerList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<Offer> parseOfferHeaders(int pg, String address, OfferInterface siteFactory, int test) throws IOException {
        ParserNodes pNodes = siteFactory.getNodes();
        List<Offer> offerList = new ArrayList<>();

//        File url = new File(address);
//        Document doc = Jsoup.parse(url, "utf-8");
        Document doc = Jsoup.connect(address).userAgent("Chrome").get();
        Elements elements = doc.select(pNodes.getContainingBlock());

        for (Element el : elements) {
            offerList.add(siteFactory.getOffer(el, siteFactory.getNodes()));
        }

        if (test > 0) {
            offerList = offerList.stream().limit(test).collect(Collectors.toList());
        }

//-------------- GO THRU PAGES ------------
        Elements pages = doc.select(pNodes.getPagingNode());
        if (pages != null) {
            Element nextPage = pages.stream()
                    .filter(e -> Integer.parseInt(e.text()) == (pg + 1))
                    .findFirst()
                    .orElse(null);
            if (nextPage != null) {
                if (test > 0) {
                    Offer iFoundNextPage = new Offer();
                    iFoundNextPage.setTitle("Next Page Was FOUND!");
                    offerList.add(iFoundNextPage);
                } else {
                    String nextPageUrl = siteFactory.validateUrl(nextPage.attr("href"));
                    offerList.addAll(parseOfferHeaders((pg + 1), nextPageUrl, siteFactory, 0));
                }
            } else {
                if (test > 0) {
                    Offer iFoundNextPage = new Offer();
                    iFoundNextPage.setTitle("Next Page Was NOT Found");
                    offerList.add(iFoundNextPage);
                }
            }
        }
        return offerList;
    }

    public static List<Offer> extractDescriptions(List<Offer> offerList, OfferInterface siteFactory) {
        for (Offer offer : offerList) {
            if (offer.getUrl() != null) {
                offer.setDescription(siteFactory.getDescription(offer, siteFactory.getNodes().getDescriptionNode()));
            }
        }
        return offerList;
    }

}
