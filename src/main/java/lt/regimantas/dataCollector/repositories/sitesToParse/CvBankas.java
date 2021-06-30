package lt.regimantas.dataCollector.repositories.sitesToParse;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.ParserNodes;
import lt.regimantas.dataCollector.services.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CvBankas implements OfferInterface {

    public static ParserNodes pNodes = new ParserNodes(
            Sites.CVBANKAS,
            ".list_article",
            "https://www.cvbankas.lt/?miestas=Vilnius&padalinys%5B%5D=76&keyw=java",
            ".list_h3",
            "[itemprop=\"description\"]",
            ".dib",
            ".list_city",
            ".salary_amount",
            ".list_a",
            ".list_logo_c img",
            ".pages_ul_inner a",
            1
    );

    @Override
    public ParserNodes getNodes() {
        return pNodes;
    }

    @Override
    public String getDescription(Offer offer, String cssQuery) {
        Document docIn = null;
        try {
            docIn = Jsoup.connect(offer.getUrl()).userAgent("Chrome").get();
            String description = Text.LeaveOnlyLineBreak(docIn.select(cssQuery).html());
            return description;
        } catch (IOException e) {
            e.printStackTrace();
            return "IO Error extracting description";
        }
    }
    @Override
    public String getMoneyForSort(Element el, String cssQuery) {
        Pattern p = Pattern.compile("(\\D*)(\\d+)(\\D*)(\\d*)(.*)");
        Matcher m = p.matcher(el.select(cssQuery).text());
        return m.find() ?
                m.group(2) + (
                        !m.group(4).equals("") ? "-" + m.group(4) : ""
                ) : "";
    }
}
