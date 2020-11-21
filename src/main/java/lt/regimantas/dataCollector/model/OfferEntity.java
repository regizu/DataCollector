package lt.regimantas.dataCollector.model;


import lombok.Data;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class OfferEntity {
    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String company;

    @Column
    private String city;

    @Column
    private String money;

    @Column
    private String money_for_sort;

    @Column
    private String url;

    @Column
    private String img;

    @Column(length = 1)
    private int status;

    private String site;

    public OfferEntity() {
    }

    public OfferEntity(String title, String description, String company, String city, String money, String money_for_sort, String url, String img, int status, String site) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.city = city;
        this.money = money;
        this.money_for_sort = money_for_sort;
        this.url = url;
        this.img = img;
        this.status = status;
        this.site = site;
    }
}
