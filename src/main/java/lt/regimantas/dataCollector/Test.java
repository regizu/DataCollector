package lt.regimantas.dataCollector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.io.IOException;
import java.text.Normalizer;

public class Test {
    public static void main(String[] args) {
        String input = "abc'de\nfg/h\\ij'fg\"";
        System.out.println(input.replaceAll("\\p{C}?!\n", " "));

    }


}
