package lt.regimantas.dataCollector.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class Text {
    public static String LeaveOnlyLineBreak(String input) {
        final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String output = Jsoup.clean(input, "", Whitelist.none(), outputSettings);
        output = output.replaceAll("(\\s*\n+\\s*){2,}", "\n\n")
        .replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}^\n]", " ");
        return output;
//        \p{L} – to allow all letters from any language
//        \p{N} – for numbers
//        \p{P} – for punctuation
//        \p{Z} – for whitespace separators
//        ^ is for negation, so all these expressions will be whitelisted
    }
}
