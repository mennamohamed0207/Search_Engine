package IndexerJob;

import java.io.File;
import java.io.IOException;
import java.util.*;

//Elements
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import main.java.ThreadedCrawler;
import main.java.mongoDB;
import opennlp.tools.dictionary.Index;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Database
//import MongoDBPackage.MongoDB;
import org.bson.Document;
//Stemmer
import opennlp.tools.stemmer.PorterStemmer;
//get from DB
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.String;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;//eq
import com.mongodb.client.model.Updates;//update

public class Indexer {
//    static public String[] tags={"title","h1","h2","h3","h4","h5","h6","p","body"};
    private PorterStemmer porterStemmer;
    private mongoDB database_Index;
    long  numOfDocs;

    private static HashSet<String>NonStemmingwords;
    private static HashSet<String>StopWords;
    public HashSet<String>tagsnames;
    Indexer()
    {
//        database_Index.UnVisited.drop();
//        database_Index.createCollection("myCappedCollection", { capped: true, size: 100000000 })
        NonStemmingwords=new HashSet<>();
        StopWords=new HashSet<>();
        StopWords=getStopWords();
        NonStemmingwords=getNonStemmingwords();
        porterStemmer = new PorterStemmer();
        tagsnames=new HashSet<String>();
        tagsnames.add("h1");
        tagsnames.add("h2");
        tagsnames.add("h3");
        tagsnames.add("h4");
        tagsnames.add("h5");
        tagsnames.add("h6");
        tagsnames.add("p");

        database_Index=new mongoDB();

        numOfDocs=database_Index.Visited.countDocuments();

    }
    //throws IOException as Parsing throws exception
    public void Index(String url,String source_str) throws IOException {
//        if(url.equals("https://mediawiki.org/wiki/Special:MyLanguage/Help:User_contributions"))
//            return;
        StringBuffer body_String=new StringBuffer("");//to store body of this url
        int no_Of_Words=0;//count of words in this url
        int position=0;//position for words to add in the body

        //Parse Source Source file
        org.jsoup.nodes.Document doc= Jsoup.parse(source_str,"UTF-8");
        Elements bodyElements=doc.body().select("*");//select all ("*") tags in the body

        //======================================Main Loop====================================//
        //loop over all elements in the body==>element
        for(Element element:bodyElements)
        {
            if(element.ownText().isEmpty())
                continue;
            //element==> String[]
            String[] word = (element.ownText().toLowerCase().split("\\s+"));//splits the string based on whitespace

            //tag name of the element
            String tag=element.tagName();
            boolean take=false;
            //check if this tag isn't from our desired tags
            if (!tagsnames.contains(tag)) {
                //another chance for the first parent tag  <h1><a>hello</a><h1>
                if(tagsnames.contains(element.parent().tagName()))
                {
                    tag=element.parent().tagName();
                }
                else continue;//not desired tag
            }
            addtoContentTags(url,tag,element.ownText());

            //loop over each word in this String[] ==>has tag (valid one)
            for (int i = 0; i < word.length; i++) {

                body_String.append(word[i]+" ");//filling body
                String search_word=word[i].toLowerCase();//to lower
                search_word=search_word.trim();

                //Check if this word is an important word
                if (!NonStemmingwords.contains(search_word))
                {
                    //preprocessing
                    search_word =search_word.replaceAll("[^a-zA-Z0-9]", " ");
                    String[] subwords = search_word.split("\\s+");//splits the string based on whitespace
                    //further, sub words
                    for (int j = 0; j < subwords.length; j++) {
                        search_word=subwords[j];
                        if (StopWords.contains(search_word)) {
                            //don't add to the indexer
                            no_Of_Words++;
                            continue;
                        }
                        addWordtoDB(search_word, url, tag, no_Of_Words);
                        no_Of_Words++;
//                    position++;
                    }//end of loop Sub words
                } else {
                    //No preprocessing
                    addWordtoDB(search_word, url, tag, no_Of_Words);
                    no_Of_Words++;
//                    position++;
                }
            }//end of loop of words in each element
        }//end of Main loop


        //=====================================Putting body===============================================//
        String body;
        if(body_String.length()==0)
            body=body_String.toString();
        else
            body=body_String.deleteCharAt(body_String.length()-1).toString();//remove last space
//
//        System.out.println(body_String);
//        System.out.println(body);
//        put  this Body in the Crawler collection
        database_Index.InsertContent(url,body);
//        Bson filter=eq("_id",url);
//        Bson update2=Updates.combine(Updates.set("NoOfWords",no_Of_Words),Updates.set("_body",body));
//        database_Index..updateMany(filter, update2);
    }

    //the database
    /*
    {
   "stemmedword": "hello",
   "DF": 2,  //the frequency of the word in documents
   "IDF":5.02158
   "DOC": [
      {
         "url": "http://example.com/doc1.html",
         "Occurance":29
         "Hi": {
            "tag": [2],     //the tag in code the posttion of the word in doc
            "body": [10, 15, 20]
         }
         "State":"exact"
      },
      {
         "url": "http://example.com/doc2.html",
         "Occurance":17
         "hello": {
            "tag": [1],
            "body": [5, 10, 12]
         }
         "State":"exact"
      }
   ]
},
    */

    private void addWordtoDB(String word, String url, String tag, int position) {
        // For stemming the word
        String stemword = porterStemmer.stem(word);

        // finding the word that is with URL
        Bson filter = and(eq("stemmedword", stemword), eq("DOC.url", url));

        // DOC[word][tag]=position //word is attribute in doc and tag is an array in word
        Bson update = Updates.addToSet("DOC.$." + word + "." + tag, position);


        // update the documents that fulfill the condition
        UpdateResult updateResult = database_Index.indexedwords.updateMany(filter, update);

        // check if it is a new word or new url, so the update did nothing
        if (updateResult.getMatchedCount() == 0) {
            // For new url
            // Create the array of tags that carry positions
            Document tagDoc = new Document(tag, Arrays.asList(position));
            // put the tagdocument opposite the word attribute
            Document doc = new Document("url", url).append(word, tagDoc);
            // find the stemmed word that match
            filter = eq("stemmedword", stemword);

            // calculate IDF score
            double idf = calculateIDF(stemword);

            // update multiple things --> push an element in the array of DOC and increase DF by 1
            update = Updates.combine(
                    Updates.push("DOC", doc),
                    Updates.inc("DF", 1),
                    Updates.set("IDF", idf)
            );

            updateResult = database_Index.indexedwords.updateMany(filter, update);

            // For new word
            if (updateResult.getMatchedCount() == 0) {
                tagDoc = new Document(tag, Arrays.asList(position));
                doc = new Document("url", url).append(word, tagDoc);
                Document docElement = new Document(word, doc);

                doc = new Document("stemmedword", stemword)
                        .append("DF", 1)
                        .append("DOC", Arrays.asList(doc))
                        .append("IDF", idf);

                database_Index.indexedwords.insertOne(doc);
            }
        }
    }
    private double calculateIDF(String stemword) {
        long numDocs = database_Index.indexedwords.countDocuments();
        // System.out.println("numDocs = "+numDocs);
        Document wordDoc = database_Index.indexedwords.find(eq("stemmedword", stemword)).first();
        if (wordDoc == null) {
            // Word not found in database, return default IDF value
            return 0.0;
        } else {
            int DF = wordDoc.getInteger("DF")+1;
            double idf = Math.log(numDocs / (double) DF);
            // System.out.println("DF = "+DF);
            return idf;
        }
    }

    public void Index_crawlar() throws IOException {
        FindIterable<Document> itratdoc=database_Index.Visited.find();
        for (Document d:itratdoc)
        {
            System.out.println(d.get("URL").toString());
            Index(d.get("URL").toString(),d.get("html").toString());
            database_Index.Delete(database_Index.Visited,d.get("URL").toString());
        }
    }
    /**
     * {url:"https://www.geeksforgeeks.org/",
     * tags:[{
     *     tag:"h1",
     *     content:"Some Content"
     * }]}
     */

    public static HashSet<String>  getStopWords() {
        HashSet<String> stopw=new HashSet<String>();
        try {
            File websitesFile = new File("C:\\Users\\ahma3\\IdeaProjects\\GoWell-new2\\src\\main\\java\\stopwords.txt");
            Scanner reader = new Scanner(websitesFile);
            while (reader.hasNextLine()) {
                String word = reader.nextLine();
                stopw.add(word);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("An exception occured while reading the stop words!");
            e.printStackTrace();
        }
        return stopw;
    }
    public static HashSet<String> getNonStemmingwords() {
        HashSet<String> impword=new HashSet<String>();
        try {
            File websitesFile = new File("C:\\Users\\ahma3\\IdeaProjects\\GoWell-new2\\src\\main\\java\\NonStemmingWords.txt");
            Scanner reader = new Scanner(websitesFile);
            while (reader.hasNextLine()) {
                String word = reader.nextLine();
                impword.add(word);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("An exception occured while reading the stop words!");
            e.printStackTrace();
        }
        return impword;
    }
    void addtoContentTags(String url,String tag,String content)
    {
        Document newTag = new Document("tag", tag)

                .append("content", content);
        database_Index.TagsContent.updateOne(
                new Document("url", url),
                new Document("$push", new Document("tags", newTag)),
                new UpdateOptions().upsert(true)
        );

    }
    public static void main(String[] args) throws IOException {
//        mongoDB DB1=new mongoDB("DB1");
//        int num=1;
//        List<String> l =  DB1.Get_Urls_From_Coll(DB1.Seeds);
//        ThreadedCrawler[] TC=new main.java.ThreadedCrawler[num];
//        for (int i=0;i<num;i++)
//        {
//            TC[i]=new ThreadedCrawler(i, l.get(i),DB1);
////            System.out.println(l.get(i)+TC[i]);
//        }
//        for (int i=0;i< num;i++)
//        {
//            try {
//                TC[i].Get_thread().join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        String url="https://www.woot.com/";
//        String src="";
//        try {
//            String u = url;
////            System.out.println("kk");
//            org.jsoup.nodes.Document doc = Jsoup.connect(u).timeout(3000).get();
//            Connection con = Jsoup.connect(url).timeout(3000);
//            org.jsoup.nodes.Document Doc = con.get();
//            if (con.response().statusCode() == 200) {
//                src= Doc.html();
//            }
//            else System.out.println("hhh");
//        }catch (Exception e){
//            System.out.println("hi");
//
//
//        }
        Indexer d=new Indexer();
//        d.Index(url,src);
        d.Index_crawlar();
    }

}