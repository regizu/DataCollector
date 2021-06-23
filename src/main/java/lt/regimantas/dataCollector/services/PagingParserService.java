package lt.regimantas.dataCollector.services;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.repositories.sitesToParse.OfferInterface;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import static lt.regimantas.dataCollector.services.OfferHeaderParserService.parseOfferHeaders;

public class PagingParserService {

    public static void goToNextPage(OfferInterface siteFactory, List<Offer> offerList, Document allContainingDocument, int currentPageNum, int reduceTo) throws IOException {
        String pageNode = siteFactory.getNodes().getPagingNode();
        Elements pages = allContainingDocument.select(pageNode);
        if (pages != null) {
            Element nextPage = getNextPage(pages, currentPageNum);
            if (nextPage != null) {
                makeTestReportOrDoNextPageParsing(siteFactory, offerList, nextPage, reduceTo);
            } else {
                if (reduceTo > 0) {
                    makeTestReport(siteFactory, offerList, "Next Page Was NOT Found");
                }
            }
        }
    }

    private static void makeTestReportOrDoNextPageParsing(OfferInterface siteFactory, List<Offer> offerList, Element nextPage, int reduceTo) throws IOException {
        if (reduceTo > 0) {
            makeTestReport(siteFactory, offerList, "Next Page Was FOUND!");
        } else {
            String nextPageUrl = siteFactory.validateUrl(nextPage.attr("href"));
            int nextPageNum = Integer.parseInt(nextPage.text());
            offerList.addAll(parseOfferHeaders(siteFactory, nextPageUrl, nextPageNum, 0));
        }
    }

    private static void makeTestReport(OfferInterface siteFactory, List<Offer> offerList, String message) {
        Offer iFoundNextPage = new Offer();
        iFoundNextPage.setTitle(message);
        iFoundNextPage.setSite(siteFactory.getNodes().getSite().toString());
        offerList.add(iFoundNextPage);
    }

    public static Element getNextPage(Elements pages, int currentPageNum) {
        return pages.stream()
                .filter(e -> e.text().equals(currentPageNum + 1 + ""))
                .findFirst()
                .orElse(null);
    }
}
