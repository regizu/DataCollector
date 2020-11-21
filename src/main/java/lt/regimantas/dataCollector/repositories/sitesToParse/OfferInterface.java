package lt.regimantas.dataCollector.repositories.sitesToParse;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.ParserNodes;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface OfferInterface {
    ParserNodes getNodes();

    default Offer getOffer(Element el, ParserNodes pNodes) {
        return new Offer(
                getTitle(el, pNodes.getTitleNode()),
                "",
                getCompany(el, pNodes.getCompanyNode()),
                getCity(el, pNodes.getCityNode()),
                getMoney(el, pNodes.getMoneyNode()),
                getMoneyForSort(el, pNodes.getMoneyNode()),
                getUrl(el, pNodes.getUrlNode()),
                getImg(el, pNodes.getImgNode()),
                getStatus(),
                getSourceName(pNodes.getSite())
        );
    }

    default String getTitle(Element el, String cssQuery) {
        return el.select(cssQuery).text();
    }

    String getDescription(Offer offer, String cssQuery);

    default String getCompany(Element el, String cssQuery) {
        return el.select(cssQuery).text();
    }

    default String getCity(Element el, String cssQuery) {
        return el.select(cssQuery).text();
    }

    default String getMoney(Element el, String cssQuery) {
        return el.select(cssQuery).text();
    }

    default String getMoneyForSort(Element el, String cssQuery) {
        Pattern p = Pattern.compile("(\\D*)(\\d+\\D?\\d*)(\\D*)(\\d*)(.*)");
        Matcher m = p.matcher(el.select(cssQuery).text());
        return m.find() ?
                m.group(2).split("\\D")[0] + (
                    !m.group(4).equals("") ? "-" + m.group(4) : ""
                ) : "";
    }

    default String getUrl(Element el, String cssQuery) {
        return el.select(cssQuery).attr("href");
    }

    default String getImg(Element el, String cssQuery) {
        return el.select(cssQuery).attr("src");
    }

    default int getStatus() {
        return 0;
    }

    default String getSourceName(Sites sourceName) {
        return sourceName.toString();
    }

    default String validateUrl(String rawUrl){
        return rawUrl;
    }
}
