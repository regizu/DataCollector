package lt.regimantas.dataCollector.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParserNodes {
    private Sites site;
private String containingBlock;
private String url;
private String titleNode;
private String descriptionNode;
private String companyNode;
private String cityNode;
private String moneyNode;
private String urlNode;
private String imgNode;
private String pagingNode;
private int inicialPage;
}

