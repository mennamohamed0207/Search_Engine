package main.java;
import main.java.mongoDB;
import opennlp.tools.stemmer.PorterStemmer;
import org.bson.Document;


import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//=======================================Very Important Notes================================================//
/*
* To run the phrase searching you have to do like following
*         Phrase_SearchingV2 p=new Phrase_SearchingV2("The phrase");
*  if (p.checkonCheckTags()) {
            System.out.println("Found");
        }else   System.out.println("Not Found");
        p.PhraseSearching();
        * Result will be in FinalURLS
            p.printHashMap(p.FinalURLS);
 * */
/*
* To run the Bonus OR AND
* Note that the contructor is empty
* *         Phrase_SearchingV2 p=new Phrase_SearchingV2();

*  List<String> input =new ArrayList<>();
            input.add(0,"Harnessed collaboration");
            input.add(1,"hackerrank");

        List<HashMap<String,List<String>>>output =p.OR_Experssion(input);
        System.out.println("OR output ");
        p.printListOfMaps(output);
        List<HashMap<String,List<String>>>Andoutput=p.AND_Experssion(input);
        System.out.println("AND output ");
        p.printListOfMaps(Andoutput);
* */






/*1-First We got the matching document that is the Exact Word ---->Function in QueryProcessor ------->DONE
2- check if all the words are in the document looping on all urls and content like before and check from the number of query words --->DONE
3-get the tags of the first word of the Query and put them in the array
4-loop on them if there is a word does not have this tag continue with another tag of the array but if the word has this tag increment counter
5- check on the counter if equal to the number of the query word - number of stopwords in query
if yes:
All query words in the same tag so put this url in anything and the tag with it Hashmap<url,tag>
if no so there is no results
6-if the query words in the same tag and i have that tag
get the content of the tag from url loop and check if the words contained in it ordered and away from them specific number of words
if yes this is finally our phrase
 */
public class Phrase_SearchingV2 {
    private mongoDB database;
    String Phrase;
    private PorterStemmer porterStemmer;//stemmer

    private static HashSet<String> stopWords;
    List<String> query_words;
    Query_Processor q;
    List<Document> matchingDocs = new ArrayList<>();
    List<Document> FilteredDocs = new ArrayList<>();
    List<String> all_content = new ArrayList<>();
    List<String> UrlCorrespoding = new ArrayList<>();
    List<String> PossibleMatchingURLs = new ArrayList<>();
    HashMap<String,List<String>> urlwithtags;
    public HashMap<String,List<String>> FinalURLS ;

    public Phrase_SearchingV2(String Phrase) throws IOException {
        database = new mongoDB();
        FinalURLS=new HashMap<>();
        this.Phrase=Phrase;
        q = new Query_Processor();
        //Query Words without Stop Words
        query_words = q.QueryDBWords;

        q.processQuery(Phrase);
        matchingDocs=q.Filtered;
        getContent();
        checkifURLHaveAllWords();
        urlwithtags=new HashMap<>();
    }
    public Phrase_SearchingV2() {

    }
    void getContent() {
        for (int i = 0; i < matchingDocs.size(); i++) {
            List<Document> docList = (List<Document>) matchingDocs.get(i).get("DOC");
            for (Document doc : docList) {
                String url = doc.getString("url");
                Document query = new Document("url", url);
//                Document result = database.NonCopy.find(query).first();
//                if (result != null&&!UrlCorrespoding.contains(url)) {
//                    String content = result.getString("fullhtml");
//                    all_content.add(content);
                UrlCorrespoding.add(url);


//                }
            }

        }
    }

    void checkifURLHaveAllWords()
    {
        for (int j=0;j<UrlCorrespoding.size();j++)
        {
//            int i=0;
//          while(i< query_words.size()&&all_content.get(j).toLowerCase(Locale.ROOT).contains(query_words.get(i).toLowerCase(Locale.ROOT)))
//          {
//              i++;
//
//          }
//          if (i==query_words.size()&&
            if (!PossibleMatchingURLs.contains(UrlCorrespoding.get(j)))
            {
                PossibleMatchingURLs.add(UrlCorrespoding.get(j));
                // System.out.println("URL  is "+UrlCorrespoding.get(j));


            }
        }
    }
    //From the document given get the tags of the word
    List<String> GetTags(Document doc,String URL)
    {
        String url;
        List<String> Tags=new ArrayList<>();
        System.out.println("doc="+doc.get("DOC"));
        Object b= doc.get("DOC");
        String []sub=b.toString().split("\\{\\{url=");
        //splitting urls
        String URLModified=URL+",";
        for(int i=1;i< sub.length;i++) {
            if (sub[i].contains(URLModified) ) {
                url = "";

                for (int f = 0; f < sub[i].length() - 1; f++)

                    if (Character.isWhitespace(sub[i].charAt(f + 1)))//end of url is comma then space
                        break;
                    else url += sub[i].charAt(f);

                System.out.println("url new=" + url);
                int index = url.length() + 2;
                //removing all except those to get rid of {,[,},]
                String r = sub[i].substring(index, sub[i].length()).replaceAll("[^a-zA-Z0-9/:.%]+", " ");
                //removing numbers only which isn't beside a letter to keep h1,h2,...h6
                String pattern = "(?<![a-zA-Z])\\d+(?![a-zA-Z])";
                r = r.replaceAll(pattern, "");
                String[] used = r.split(" ");
                String[] tags = {"title", "h1", "h2", "h3", "h4", "h5", "h6", "p", "body"};
                //looping on tags starting from j=2 as at 0 and 1 aren't tags
                for (int j = 2; j < used.length; j++)
                    if (Arrays.asList(tags).contains(used[j])) {
                        int idx = Arrays.asList(tags).indexOf(used[j]);
                        //rank+=(9-idx);

                        Tags.add(used[j]);
                    }

            }
        }
        return Tags;
    }
    //get the specific tag that the phrase in
    void Check_onTag()
    {
        this.checkifURLHaveAllWords();


        for(int i=0;i< PossibleMatchingURLs.size();i++) {
            List<String> tagsthathaveps=new ArrayList<>();
            List<String> FirstWordTag=GetTags(matchingDocs.get(0),PossibleMatchingURLs.get(i));
            for (int j=0;j<FirstWordTag.size();j++)
            {int matchingWords = 1;
                for (int k=1;k< query_words.size();k++)
                {

                    List<String> tagsofWord=GetTags(matchingDocs.get(k),PossibleMatchingURLs.get(i));
                    if (!tagsofWord.contains(FirstWordTag.get(j)))
                    {
                        break;
                    }else matchingWords++;

                }
                if (matchingWords==query_words.size())
                {
                    tagsthathaveps.add(FirstWordTag.get(j));
                    urlwithtags.put(PossibleMatchingURLs.get(i), tagsthathaveps);

                }
            }
        }

    }
    //Now i got the tag and i have the words time to search of content of the tags
//    List<String> getLinksPhraseSearching()
//    {
//        for (int i=0;i< urlwithtags.size();i++)
//        {
//
//        }
//    }
    boolean checkonCheckTags()
    {
        this.Check_onTag();
        if (urlwithtags.size()!=0)
            return true;
        else return false;
    }
    List<String> getContent (String url, String tag)
    {
        Document query = new Document("url", url).append("tags.tag", tag);
        List<Document> documents = database.TagsContent.find(query).into(new ArrayList<>());

        List<String> contentList = new ArrayList<>();
        for (Document document : documents) {
            List<Document> tags = (List<Document>) document.get("tags");
            for (Document t : tags) {
                if (t.getString("tag").equals(tag)) {
                    String content = t.getString("content");
                    contentList.add(content);
                    break;
                }
            }
        }
        return contentList;
    }

    void PhraseSearching()
    {
        //get the content of  eachtag in urlswithContent
        //loop on the content and check for every content for ALL words to exist in it
        //get the position of the words and make sure that they are ordered ascendingly and get the difference of the words
        //i do not know how can i make it or to specify a threshold -->
        // i think that i can loop on the words and get the difference between every consecuative words
        for (Map.Entry<String, List<String>> entry : urlwithtags.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            List<String> TagsPerLink=new ArrayList<>();
            //System.out.println("Key: " + key);
            for (String value : values) {
                String content = getContent(key,value).toString();
                int counter=0;
                for (int i=1;i<query_words.size();i++)
                {
                    int minWords = 0;
                    int maxWords = 5;
                    //\\b(word1(\\W+\\w+){0,3}(?=\\W|$))(?:\\W+(\\w+\\W+){0,3}word2)?\\b
                    //\\W+(?!\\.|\\?|\\s*$\\w+)*\\W+{0,5}
                    content=content.replaceAll("[^a-zA-Z0-9/:]+", " ");

                    String patternString ="\\b" + query_words.get(i-1)+ "\\W+(?:\\w+\\W+){0,5}"  +query_words.get(i) + "\\b";
                    Pattern pattern = Pattern.compile(patternString,Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(content);
//                   System.out.println("=========="+matcher.find());
                    //current_content.toLowerCase(Locale.ROOT).contains(original_query.toLowerCase(Locale.ROOT))
                    if (content.toLowerCase(Locale.ROOT).contains(query_words.get(i).toLowerCase(Locale.ROOT))&&matcher.find())
                    {
                        counter++;
                        //  System.out.println("count is "+counter);

                    }
                }
                if (counter ==query_words.size()-1)
                {
                    // System.out.println("============================ "+ content+"the url"+key);
                    TagsPerLink.add(value);

                }
            }
            if (TagsPerLink.size() !=0)
                FinalURLS.put(key,TagsPerLink);
        }
    }
    //Bonus OR Experssion return links of all phrases
    public List<HashMap<String,List<String>>> OR_Experssion(List<String> Inputs) throws IOException {
        List<HashMap<String,List<String>>>outputs =new ArrayList<>();
        for (int i=0;i<Inputs.size();i++)
        {
            Phrase_SearchingV2 p=new Phrase_SearchingV2(Inputs.get(i));
            p.checkonCheckTags();
            p.PhraseSearching();
            if (!outputs.contains(p.FinalURLS))
                outputs.add(p.FinalURLS);

        }
        return outputs;
    }
    //0 -->Link0,Link1,Link3
    //1 --> Link0,Link10,Link3
    public List<HashMap<String,List<String>>> AND_Experssion(List<String> inputs) throws IOException {
        List<HashMap<String, List<String>>> listOfMaps = this.OR_Experssion(inputs);

        if (listOfMaps.isEmpty()) {
            System.out.println("The input list is empty.");
            return null;
            // add any other handling you need for an empty list
        } else {
            // Step 1: Create a HashSet to store the common keys
            HashSet<String> commonKeys = new HashSet<String>();

            // Step 2: Loop through the first HashMap and add all its keys to the HashSet
            HashMap<String, List<String>> firstMap = listOfMaps.get(0);
            commonKeys.addAll(firstMap.keySet());

            // Step 3: Loop through the remaining HashMaps and remove any key that is not present in the HashSet
            for (int i = 1; i < listOfMaps.size(); i++) {
                HashMap<String, List<String>> map = listOfMaps.get(i);
                commonKeys.retainAll(map.keySet());
            }

            // Step 4: Loop through the List of HashMaps and keep only the ones that have the common keys
            List<HashMap<String, List<String>>> resultMap = new ArrayList<HashMap<String, List<String>>>();
            for (HashMap<String, List<String>> map : listOfMaps) {
                if (!commonKeys.isEmpty()&&map.keySet().containsAll(commonKeys)) {
                    resultMap.add(map);
                }
            }

            // Step 5: The resultMap now contains only the HashMaps that have the intersection of keys
            System.out.println(resultMap);
            return resultMap;
        }
    }
    public void printListOfMaps(List<HashMap<String, List<String>>> listOfMaps) {
        for (HashMap<String, List<String>> map : listOfMaps) {
            System.out.println("HashMap:");
            for (String key : map.keySet()) {
                System.out.println("\tKey: " + key);
                System.out.println("\tValues:");
                for (String value : map.get(key)) {
                    System.out.println("\t\t" + value);
                }
            }
        }
    }
    public void printHashMap(HashMap<String, List<String>> map) {
        System.out.println("HashMap:");
        for (String key : map.keySet()) {
            System.out.println("\tKey: " + key);
            System.out.println("\tValues:");
            for (String value : map.get(key)) {
                System.out.println("\t\t" + value);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Phrase_SearchingV2 p=new Phrase_SearchingV2();
        if (p.checkonCheckTags()) {
            System.out.println("Found");
        }else   System.out.println("Not Found");
        p.PhraseSearching();

        System.out.println("the urls of the links ");
        p.printHashMap(p.FinalURLS);

        List<String> input =new ArrayList<>();
        input.add(0,"Harnessed collaboration");
        input.add(1,"hackerrank");

        List<HashMap<String,List<String>>>output =p.OR_Experssion(input);
        System.out.println("OR output ");
        p.printListOfMaps(output);
        List<HashMap<String,List<String>>>Andoutput=p.AND_Experssion(input);
        System.out.println("AND output ");
        p.printListOfMaps(Andoutput);
    }

}
