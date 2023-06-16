package IndexerJob;

import java.io.File; ///file Data type

//reading file
import java.io.IOException;

//for fetching words
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


//Elements
import org.jsoup.select.Elements;

public class HtmlParsing {
    Document doc;
    HtmlParsing (String str_html) throws IOException
    {
        doc = Jsoup.connect(str_html).get();

//        doc= Jsoup.parse(str_html,"UTF-8");
    }
    public Elements Parse_Tags(String tag)
    {
        Elements paragraphs = doc.select(tag);
        return paragraphs;
    }
}