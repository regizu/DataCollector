package lt.regimantas.dataCollector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lt.regimantas.dataCollector.repositories.sitesToParse.Sites;

@AllArgsConstructor
@Getter
public class TestingParams {
    Sites site;
    int reduceTo;
}
