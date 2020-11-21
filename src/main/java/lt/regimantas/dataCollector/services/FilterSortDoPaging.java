package lt.regimantas.dataCollector.services;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.SearchSpecification;
import lt.regimantas.dataCollector.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterSortDoPaging {

    @Autowired
    OfferRepository offerRepository;

    public List<Offer> search(String searchString, Integer pageSize, Integer pageIndex) {
        Specification<Offer> specs = new SearchSpecification(searchString);
        Sort sort = Sort.by(Sort.Order.asc("company"));
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page page = offerRepository.findAll(specs, pageable);
        return page.getContent();
    }

}
