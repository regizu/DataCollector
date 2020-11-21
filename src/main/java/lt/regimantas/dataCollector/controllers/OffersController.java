package lt.regimantas.dataCollector.controllers;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.OfferStorage;
import lt.regimantas.dataCollector.model.TestingParams;
import lt.regimantas.dataCollector.repositories.OfferRepository;
import lt.regimantas.dataCollector.repositories.OfferStorageRepository;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;
import lt.regimantas.dataCollector.services.DataParser;
import lt.regimantas.dataCollector.services.FilterSortDoPaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class OffersController extends ApiRestController {
    @Autowired
    OfferRepository offerRepository;

    @Autowired
    OfferStorageRepository offerStorageRepository;

    @Autowired
    FilterSortDoPaging filterSortDoPaging;


    @GetMapping("/offers")
    List<Offer> getOffers() {
        List<Offer> offersList = (List<Offer>) offerRepository.findAll();

        return offersList;
    }

    @GetMapping("/writetodb/{site}")
    void saveToDb(@PathVariable Sites site) throws IOException {
        List<Offer> offerList = DataParser.whatToParse(site, 0);
            if (offerList != null) {
                offerRepository.saveAll(offerList);

            }
    }

    @PostMapping("/test")
    List<Offer> test(@RequestBody TestingParams testingParams) throws IOException {
        List<Offer> offerList = DataParser.whatToParse(testingParams.getSite(), testingParams.getTimes());
            return offerList;
    }

    @GetMapping("/delete_old")
    List<OfferStorage> showOld(){
        return offerStorageRepository.findNoteExistedAnymore();
    }

    @GetMapping("/filtered")
    List<Offer> filteredSortedPaged(@RequestParam("search") String search) {
        List<Offer> filteredSortedPagedList = filterSortDoPaging.search(search, 5, 0);
        return filteredSortedPagedList;
    }
}
