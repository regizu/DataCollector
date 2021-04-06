package lt.regimantas.dataCollector.repositories.sitesToParse;

public class SiteFactory {

    public static OfferInterface getSiteFactory(Sites site){
        switch (site){
            case CVBANKAS:
                return new CvBankas();
            case CVONLINE:
                return new CvOnline();

            default: return null;
        }
    }
}
