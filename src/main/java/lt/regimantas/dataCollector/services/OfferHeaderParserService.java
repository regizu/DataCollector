package lt.regimantas.dataCollector.services;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.repositories.sitesToParse.OfferInterface;
import lt.regimantas.dataCollector.repositories.sitesToParse.SiteFactory;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfferHeaderParserService {

    public static List<Offer> getOneSiteOffersHeaders(Sites site, int test) {
        return prepareForParsingHeaders(site, test);
    }

    public static List<Offer> getAllSitesOffersHeaders(int test) {
        List<Offer> offerList = new ArrayList<>();
        for (Sites site : Sites.values()) {
            if (!site.equals(Sites.ALL)) {
                offerList.addAll(prepareForParsingHeaders(site, test));
            }
        }
        return offerList;
    }

    public static List<Offer> prepareForParsingHeaders(Sites site, int test) {
        OfferInterface siteFactory = SiteFactory.getSiteFactory(site);
        try {
            return initiateOfferHeaderParsing(test, siteFactory);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Offer>();
        }

    }

    private static List<Offer> initiateOfferHeaderParsing(int test, OfferInterface siteFactory) throws IOException {
        List<Offer> offerList = parseOfferHeaders(
                siteFactory,
                siteFactory.getNodes().getUrl(),
                siteFactory.getNodes().getInicialPage(),
                test
        );
        return offerList;
    }

    public static List<Offer> parseOfferHeaders(OfferInterface siteFactory, String address, int currentPageNum, int reduceTo) throws IOException {
        String containingBlockNode = siteFactory.getNodes().getContainingBlock();
        List<Offer> offerList = new ArrayList<>();

        Document allContainingDocument = Jsoup.connect(address).userAgent("Chrome").get();
        Elements elements = allContainingDocument.select(containingBlockNode);

        for (Element element : elements) {
            offerList.add(siteFactory.getOffer(element, siteFactory.getNodes()));
        }

        if (reduceTo > 0) {
            offerList = offerList.stream().limit(reduceTo).collect(Collectors.toList());
        }

        PagingParserService.goToNextPage(siteFactory, offerList, allContainingDocument, currentPageNum, reduceTo);
        return offerList;
    }

}
