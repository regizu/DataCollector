package lt.regimantas.dataCollector.repositories.sitesToParse;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.ParserNodes;
import lt.regimantas.dataCollector.services.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;

import java.io.IOException;

public class CvOnline implements OfferInterface {

    public static ParserNodes pNodes = new ParserNodes(
            Sites.CVONLINE,
            ".vacancies-list__item",
            "https://cvonline.lt/lt/search?limit=20&offset=0&categories%5B0%5D=INFORMATION_TECHNOLOGY&keywords%5B0%5D=java&towns%5B0%5D=540&isHourlySalary=false&isRemoteWork=false",
            ".vacancy-item__title",
            "#page-main-content",
            ".vacancy-item__info-main a",
            ".vacancy-item__locations",
            ".vacancy-item__salary-label",
            ".vacancy-item__content a",
            ".vacancy-item__logo img",
            "#page-pagination a",
            0
    );

    @Override
    public ParserNodes getNodes() {
        return pNodes;
    }

    @Override
    public String getUrl(Element el, String cssQuery) {
        return "https:"+el.select(cssQuery).attr("href");
    }

    @Override
    public String getImg(Element el, String cssQuery) {
        return "https:" + el.select(cssQuery).attr("src");
    }

    @Override
    public String getDescription(Offer offer, String cssQuery) {
        Document docIn = null;
        String description = "";
        try {

            docIn = Jsoup.connect(offer.getUrl()).userAgent("Chrome").get();
            String iframeSrc = docIn.select("#page-main-content iframe:not(#videoframe)").attr("src");
            if(!iframeSrc.equals("")){                   //IF INFORMATION PLACED TO IFRAME
                iframeSrc = this.validateUrl(iframeSrc);
                //TODO get html with javascript injected texts.

               Document iframeIn = Jsoup.connect(iframeSrc).userAgent("Chrome").get();
                description = "IFRAME: " + Text.LeaveOnlyLineBreak(iframeIn.body().html());
            }else{
                String isContent = docIn.select(cssQuery).html();  //PRIMARY / DEFAULT SELECTOR
                if(isContent.equals("")){
                    //TODO get html with javascript injected texts.

                    isContent = docIn.select("main").html();       // SECONDARY SELECTOR
                }
                if(!isContent.equals("")){
                    description = Text.LeaveOnlyLineBreak(docIn.select(cssQuery).html());
                } else {
                    description = "UNKNOWN DESCRIPTION METHOD!";            //UNKNOWN SELECTOR
                }
            }
            return description;
        } catch (IOException e) {
            e.printStackTrace();
            return "IO Error extracting description";
        }
    }

    @Override
    public String validateUrl(String rawUrl) {
        return "https://www.cvonline.lt" + rawUrl;
    }
}
