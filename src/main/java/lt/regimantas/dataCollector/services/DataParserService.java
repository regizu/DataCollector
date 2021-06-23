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
public class DataParserService {

    public static List<Offer> doParsing(Sites site, int reduceOffersTo){
        if(site.equals(Sites.ALL))
            return OfferHeaderParserService.getAllSitesOffersHeaders(reduceOffersTo);
        else
            return OfferHeaderParserService.getOneSiteOffersHeaders(site, reduceOffersTo);
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
