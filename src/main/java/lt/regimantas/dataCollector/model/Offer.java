package lt.regimantas.dataCollector.model;


import lombok.*;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;

import javax.persistence.*;

@Entity
@Table(name = "offer_temp")
@NoArgsConstructor
@Getter
@Setter
public class Offer extends OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_temp;

    public Offer(String title, String description, String company, String city, String money, String money_for_sort, String url, String img, int status, String site) {
        super(title, description, company, city, money, money_for_sort, url, img, status, site);
    }

}

