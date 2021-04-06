package lt.regimantas.dataCollector.services;


import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.ParserNodes;
import lt.regimantas.dataCollector.repositories.sitesToParse.OfferInterface;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;
import lt.regimantas.dataCollector.repositories.sitesToParse.SiteFactory;
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

    public static List<Offer> doParsing(Sites site, int test){
        if(site.equals(Sites.ALL))
            return getAllSitesOffersHeaders(test);
        else
            return getOneSiteOffersHeaders(site, test);
    }

    public static List<Offer> getOneSiteOffersHeaders(Sites site, int test){
        return prepareForParsingHeaders(site, test);
    }

    public static List<Offer> getAllSitesOffersHeaders(int test){
        List<Offer> offerList = new ArrayList<>();
        for (Sites s : Sites.values()) {
            if (!s.equals(Sites.ALL)) {
                offerList.addAll(prepareForParsingHeaders(s, test));
            }
        }
        return offerList;
    }

    public static List<Offer> prepareForParsingHeaders(Sites site, int test) {
        OfferInterface siteFactory = SiteFactory.getSiteFactory(site);
        try {
            List<Offer> offerList = parseOfferHeaders(
                            siteFactory.getNodes().getInicialPage(),
                            siteFactory.getNodes().getUrl(),
                            siteFactory,
                            test
                    );
            return offerList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Offer>();
        }

    }

    public static List<Offer> parseOfferHeaders(int pg, String address, OfferInterface siteFactory, int test) throws IOException {
        ParserNodes pNodes = siteFactory.getNodes();
        List<Offer> offerList = new ArrayList<>();

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
                    iFoundNextPage.setSite(siteFactory.getNodes().getSite().toString());
                    offerList.add(iFoundNextPage);
                } else {
                    String nextPageUrl = siteFactory.validateUrl(nextPage.attr("href"));
                    offerList.addAll(parseOfferHeaders((pg + 1), nextPageUrl, siteFactory, 0));
                }
            } else {
                if (test > 0) {
                    Offer iFoundNextPage = new Offer();
                    iFoundNextPage.setTitle("Next Page Was NOT Found");
                    iFoundNextPage.setSite(siteFactory.getNodes().getSite().toString());
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
