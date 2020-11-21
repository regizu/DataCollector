package lt.regimantas.dataCollector.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;

import javax.persistence.*;

@Entity
@Table(name = "offer_storage")
@NoArgsConstructor
@Getter
@Setter
public class OfferStorage extends OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public OfferStorage(String title, String description, String company, String city, String money, String money_for_sort, String url, String img, int status, String site) {
        super(title, description, company, city, money, money_for_sort, url, img, status, site);
    }

}
