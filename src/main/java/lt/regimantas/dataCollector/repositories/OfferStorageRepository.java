package lt.regimantas.dataCollector.repositories;

import lt.regimantas.dataCollector.model.Offer;
import lt.regimantas.dataCollector.model.OfferStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferStorageRepository extends JpaRepository<OfferStorage, Long>, JpaSpecificationExecutor<OfferStorage> {
    @Query(value = "SELECT s.* FROM offer_storage s" +
            "where NOT EXISTS (select t.company from offer_temp t where " +
            "s.city = t.city and " +
            "s.company = t.company and " +
            "s.money = t.money and" +
            " and s.site = t.site " +
            "and s.title = t.title)",
            nativeQuery = true
    )
    List<OfferStorage> findNoteExistedAnymore();
}
