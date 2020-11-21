package lt.regimantas.dataCollector.repositories.sitesToParse;

public class SitesFactory {

    public static OfferInterface getSiteFactory(Sites site){
        OfferInterface offerInterface;
        switch (site){
            case CVBANKAS:
                return offerInterface = new CvBankas();
            case CVONLINE:
                return offerInterface = new CvOnline();

            default: return null;
        }
    }
}
