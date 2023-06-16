package main.java;//package Ranker;

import main.java.mongoDB;
import opennlp.tools.stemmer.PorterStemmer;
import org.bson.Document;

import javax.print.Doc;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ranker {
    public static HashMap<String,Double>rankurls=new HashMap<String, Double>();

    static mongoDB DataBase;
    String [] QueryWords;
    HashMap<String,String> Snippets;

    String[] tags={"title", //9
            "h1",
            "h2",
            "h3",
            "h4",
            "h5",
            "h6",
            "p", //2
            "body"}; //1

    Ranker() {
        DataBase= new mongoDB();
        Snippets=new HashMap<>();
//        Print_Rank();
    }
    HashMap<String,Double> letsrank(List<Document> Docs,int num,HashMap<String,List<String>>arr,String Query) throws IOException {
        SplitQuery(Query);
        if (num==2) {//query
            rankwithtag(Docs,Query);
//            Calc_Rank_Of_URLs_By_Popularity();
        } else if(num==1) {//phrase
            rankwithtagphrase(arr,Docs);
//            Calc_Rank_Of_URLs_By_Popularity_phrase();
        }
        sortHashMapByValue(rankurls);
        return rankurls;
    }
    void SuggestionsSave(String specificString)
    {
        String filename="C:\\Users\\ahma3\\IdeaProjects\\GoWell-new2\\src\\queries.txt  ";
        try {
            // Open the file in read mode
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            boolean isPresent = false;

            // Read the file line by line and check if the specific string is present
            while ((line = reader.readLine()) != null) {
                if (line.contains(specificString)) {
                    isPresent = true;
                    break;
                }
            }

            reader.close();

            if (isPresent) {
                System.out.println("The string is already in the file.");
            } else {
                // If the string is not present, proceed with insertion
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
                writer.write(specificString);
                writer.newLine();
                writer.close();
                System.out.println("The string has been inserted into the file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //    private String get_Snippet(String body_String,int start_index)
//    {
//        //split the string into words
//        String[] body = body_String.split("\\s+");
//        StringBuffer snippet = new StringBuffer();
//        int i = start_index;
//        StringBuffer temp2 = new StringBuffer(body[start_index]);
//        //reverse the word that we are searching for
//        temp2.reverse();
//        snippet.append(temp2+" ");
//        i--;
//        int counter =0;
//        //get the ten word before the chosen word
//        while (i>=0&&counter<=10)
//        {
//            StringBuffer temp = new StringBuffer(body[i]);
//            temp.reverse();
//            snippet.append(temp + " ");
//            i--;
//            counter++;
//
//        }
//        //until here the snippet is reversed ,so we will reverse it back
//        snippet.reverse();
//        snippet.append(" ");
//        snippet.deleteCharAt(0);//remove space;
//        //here we want to get the ten words after the chosen one
//        //But first we have to check if the word has words after it aslan
//        //So we check if it ends with dot or not
//        //if so we return the snippet without getting the ten words after it
//        i = start_index + 1;
//        if (body[start_index].endsWith("."))
//            return snippet.toString();
//
//        counter=0;
//        //get the words after it
//        while (i <= body.length - 1 &&counter!=10) {
//            snippet.append(body[i] + " ");
//            i++;
//            counter++;
//        }//end of while loop
//
//        if (i <= body.length - 1)
//            snippet.append(body[i] + " ");
//
//System.out.println("the Sinnpets "+snippet.toString());
//        return snippet.toString();
//    }
    String getSnippet(String bodyString, int startIndex) {

        bodyString=bodyString.replaceAll("[^a-zA-Z0-9]", " ");
        String[] words = bodyString.split("\\s+");
        int leftIndex = Math.max(0, startIndex - 10);
        int rightIndex = Math.min(words.length - 1, startIndex + 10);
        StringBuilder snippetBuilder = new StringBuilder();
        for (int i = leftIndex; i <= rightIndex; i++) {
            String word = words[i];
            if (i == startIndex) {
                snippetBuilder.append("<mark>").append(word).append("</mark>");
            } else {
                snippetBuilder.append(word);
            }
            snippetBuilder.append(" ");
        }
        // System.out.println(snippetBuilder.toString().trim());
        return snippetBuilder.toString().trim();
    }



    String GetSnippetsFromContent(String url,Document d) throws IOException {
        String resultString = null;
        String fullhtml = null;
        Document query = new Document("url", url);
        Document result = DataBase.Content.find(query).first();
        if (result != null) {
            fullhtml = result.getString("body");
//            fullhtml=fullhtml.replaceAll("[^a-zA-Z0-9]", " ");
//            System.out.println("============================= "+fullhtml);
            int position=getWordPosition(QueryWords,d,url);

            if(position != -1) {
                resultString=getSnippet(fullhtml, position);
                //System.out.println("URL :"+ url);
                //System.out.println("the position ================ " + position);
            }
        } else {
            //  System.out.println("No document found with url: " + url);
        }

        return resultString;

    }


    public static HashMap<String, Double> sortHashMapByValue(HashMap<String, Double> map) {
        // Create a list of map entries
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());
        System.out.println("sort entered ");

        // Sort the list using a custom comparator
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Create a new LinkedHashMap from the sorted list
        HashMap<String,Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String,Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
//        rankurls.putAll(sortedMap);
        return sortedMap;
    }
    void rankwithtagphrase(HashMap<String,List<String>> FinalURLS,List<Document> Doc) throws IOException {int GetOfSize = -1;

        List<Map.Entry<String, Integer>> elements = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : FinalURLS.entrySet()) {
            // add the element to the list
            // elements.add(entry);
            String url=entry.getKey();
            double rank=0;
            List<String> s=entry.getValue();
            for (int i = 0; i < s.size(); i++) {
                String item = s.get(i);
                int idx=Arrays.asList(tags).indexOf(item);
                if (Doc != null && Doc.size() >0) {
                    GetOfSize = GetSize(QueryWords, item, Doc.get(0));
                    Snippets.put(url,GetSnippetsFromContent(url,Doc.get(0))) ;
                }
                if (GetOfSize !=-1)
                    rank+=((9-idx)*GetOfSize);
                else  rank+=(9-idx);

                System.out.println("The Rank of the url "+ url +" is "+rank+" Size is "+GetOfSize+" with tag "+item);

            }
//            System.out.println("url="+url+"value="+entry.getValue()+"rank="+rank);
            rankurls.put(url,rank);

        }

    }
    //First Position of specific url and tag
    // How many positions
    //Problems
    //1- what if exact word or a meaning

    public int getWordPosition(String []QueryWords ,Document matching,String url) throws IOException {
        // Extract the position of the word from the "DOC" array in the document
        String[] tags={"title","h1","h2","h3","h4","h5","h6","p","body"};

        List<Document> docList = matching.getList("DOC", Document.class);
        int position = -1;
        PorterStemmer porterStemmer=new PorterStemmer();

        for (Document doc :docList) {

            String docString = doc.toString();
            Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            Matcher matcher = pattern.matcher(docString);

            while (matcher.find()) {
//                System.out.println(docString);
//                System.out.println(matcher.group(1));
                String []strings=matcher.group(1).split(",");
                position=Integer.parseInt(strings[0]);
//                System.out.println(position);
                break;
            }
            if(position!=-1)
                break;
        }
        return position;
    }
    public int GetSize (String[] QueryWords ,String tag,Document matching)
    {
        String word= null;
        for (int i=0;i<QueryWords.length;i++)
        {
            String documet=matching.toString();
            if (documet.contains(QueryWords[i]))
            {
                word=QueryWords[i];
            }
        }
        // Extract the position of the word from the "DOC" array in the document
        List<Document> docList = matching.getList("DOC", Document.class);
        int size = -1;
        for (Document doc : docList) {
            if (doc.containsKey(word)) {
                List<Integer> bodyList = doc.get(word, Document.class).getList(tag, Integer.class);
                if (bodyList!=null&&!bodyList.isEmpty()) {
                    size= bodyList.size();
                    break;
                }
            }
        }

        return size;
    }
    //TF =Getsize
    void rankwithtag(List<Document> d,String Query) throws IOException {
        double Occurance=0;
        SplitQuery(Query);
        double rank=0;
        for(Document doc:d){
            String url;
            double IDF= doc.getDouble("IDF");
//        for(Document doc2:d.get("DOC"))
            Object b= doc.get("DOC");
            System.out.println("doc= " +b);

            String []sub=b.toString().split("\\{\\{url=");
            //splitting urls
            for(int i=1;i< sub.length;i++)
            {int res;
                url="";
                rank=0;
                for(int f=0;f<sub[i].length()-1;f++)
                    if(Character.isWhitespace(sub[i].charAt(f+1)))//end of url is comma then space
                        break;
                    else url+=sub[i].charAt(f);

//                System.out.println("url new="+url);
                int index=url.length()+2;
                //removing all except those to get rid of {,[,},]
                String r=sub[i].substring(index,sub[i].length()).replaceAll("[^a-zA-Z0-9/:.%]+", " ");
                //removing numbers only which isn't beside a letter to keep h1,h2,...h6
                String pattern = "(?<![a-zA-Z])\\d+(?![a-zA-Z])";
                r=r.replaceAll(pattern, "");
                String[] used=r.split(" ");
                //looping on tags starting from j=2 as at 0 and 1 aren't tags

                Snippets.put(url,GetSnippetsFromContent(url,doc)) ;
                for(int j=2;j<used.length;j++)
                    if(Arrays.asList(tags).contains(used[j]))
                    {
                        int idx=Arrays.asList(tags).indexOf(used[j]);
                        int SizeoFTags=GetSize(QueryWords,used[j],doc);
                        Occurance+=SizeoFTags;
                        if (SizeoFTags !=-1)
                        {rank+=((9-idx)*SizeoFTags);
                            //  System.out.println("The Rank of the url "+ url +" is "+rank+" Size is "+SizeoFTags+" with tag "+used[j]);
                        }
                    }

                res= (int) getSizeOfDocument(url);
                Occurance= (double) (Occurance/res);
                double IDF_TF=IDF*Occurance;
                rank+=IDF_TF;
                if(rankurls.containsKey(url))
                    rank+=rankurls.get(url);
                rankurls.put(url,rank);
                System.out.println("url="+url+"  rank="+rank);
            }

        }
        System.out.println("done");
//        for(Map.Entry<String, Double> entry : rankurls.entrySet())
//            System.out.println("url="+entry.getKey() + " rank= " + entry.getValue());
    }

    /*
     * Ranker receive documents from Query processor
     * then it arrange them according to some factors there is some of them
     * I think This order makes sense
     * 1- the documents with most number of words of query (I care with the documents which have most of the words) \\
     * 2- the documents with the exact or stemming or synonym edit in indexer | query//
     * 3- place of appearance h1 , body ....etc//
     * 4- Normalize frequency of the word in doc TFN  indexer// not done
     * 5- IDF DF/all number of doc    // low idf cause
     * 5- popularity of doc  pagerank  //
     *
     *
     *
     * function ranker phrase
     *
     * */
    /*
     * menna mohammed phrase searching / TF /IDF
     * menna ahmed update total nums of words in each doc/place of appearance h1 , body
     * sara bisheer
     * check oh type of word/               done
     * popularity of doc  pagerank
     *  number of words of query in each doc done
     * mongodb done
     * Q
     * types of word is ready
     * but when to increase the rank according to it
     *
     * */

    public double getSizeOfDocument(String url)
    {
        Document d=new Document();
        d.append("url",url);
        Document Result =DataBase.NonCopy.find().first();
        int size=Result.getInteger("size");
        return size;

    }
    public String getTitle(String url)
    {
        Document d=new Document();
        d.append("url",url);
        Document Result =DataBase.NonCopy.find().first();
        String title=Result.getString("title");
        return title;

    }

    private void calculate_NumWordsInDoc(List<Document> Docs) {
        // looping on Documents to calculate number of words in each document
        for (Document Doc : Docs) //Doc about some word in query
        {
            ArrayList<Document> urls = (ArrayList<Document>) Doc.get("DOC");
            for (Document url_doc : urls) {
                String url=url_doc.getString("url");
                if(rankurls.get(url)==null)rankurls.put(url,1000.0);
                else
                    //check weight
                    rankurls.put(url,rankurls.get(url)+1000);
            }
        }
        System.out.println("here1");
    }
    // update on the value of IDF (scarcity of word )
    // update state of word upon it's exact or stemmed or synonymous
    private void updateRankerBy_IDF_word_rank(List<Document> Docs ){
        for(Document doc : Docs)
        {
            double rank=rankurls.get(doc.get("URL"));
            String state =doc.getString("State");
            rank *= doc.getDouble("IDF") ;
            if (state.equals("exact")) {
                rank*=10; //TODO::should be changed
            } else if(state.equals("stemmed")) {
                rank*=3;//TODO::should be changed
            }
            else {
                rank*=2;//TODO::should be changed
            }

            rankurls.put(doc.getString("URL"),rank);
        }
    }

    public void Print_Rank() {
        for (Map.Entry<String, Double> entry : rankurls.entrySet()) {
            String key = entry.getKey();
            System.out.println("Url "+key + " has rank : " + entry.getValue());
        }
    }
    private Map<String,Double> CalcpageRank(HashSet<String> urlSet) {
        Map<String,Double>pageRankMap=new HashMap<String, Double>(); //pagerank
        double initialPageRank = 1.0 / urlSet.size();
        for (String url : urlSet) {
            pageRankMap.put(url, initialPageRank);
        }
        // Perform multiple iterations of the PageRank algorithm
        int numIterations = 10;
        for (int i = 0; i < numIterations; i++) {
            // Create a new Map to hold the PageRank scores for the next iteration
            Map<String, Double> newPageRankMap = new HashMap<String, Double>();

            // Calculate the new PageRank score for each URL
            for (String url : urlSet) {
                double newPageRank = 0.0;
                for (String linkingUrl : urlSet) {
                    if (DataBase.linksTo(linkingUrl, url)) {
                        int numOutgoingLinks = DataBase.countOutgoingLinks(linkingUrl);
                        double linkingPageRank = pageRankMap.get(linkingUrl);
                        newPageRank += linkingPageRank / numOutgoingLinks;
                    }
                }
                newPageRankMap.put(url, newPageRank);
                // Replace the old PageRank scores with the new ones
            }
            pageRankMap = newPageRankMap;
        }
        System.out.println("here2");
        return pageRankMap;
    }
    String [] SplitQuery(String Query)
    {
        String[] wordsOfQuery = Query.toLowerCase().split("\\s+");
        QueryWords=wordsOfQuery;
        return wordsOfQuery;
    }
    private HashSet<String> getUrl()
    {
        HashSet<String>urls=new HashSet<>();
        for ( Map.Entry<String, Double> entry : rankurls.entrySet()) //Doc about some word in query
        {
//            System.out.println(entry.getKey());
            urls.add(entry.getKey());
        }
        return urls;
    }

    private void Calc_Rank_Of_URLs_By_Popularity() {
        Map<String,Double> pageRankMap=CalcpageRank(getUrl());
        for ( Map.Entry<String, Double> entry : rankurls.entrySet()) //Doc about some word in query
        {
            String url=entry.getKey();
            entry.setValue(entry.getValue()*pageRankMap.get(url));
        }
    }
    private void Calc_Rank_Of_URLs_By_Popularity_phrase() {
        Map<String,Double> pageRankMap=CalcpageRank(getUrl());
        for ( Map.Entry<String, Double> entry : rankurls.entrySet()) //Doc about some word in query
        {
            String url=entry.getKey();
            entry.setValue(entry.getValue()*pageRankMap.get(url));
        }
    }
    public void printHashMap(HashMap<String, String> map) {
        System.out.println("HashMap:");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }


    public static void main(String[] args) throws IOException {
        Ranker R=new Ranker();
        Phrase_SearchingV2 p=new Phrase_SearchingV2("harnessed for productivity");
        if (p.checkonCheckTags()) {
            System.out.println("Found");
        }else   System.out.println("Not Found");
        p.PhraseSearching();
//        Query_Processor q=new Query_Processor();

        rankurls= R.letsrank(p.matchingDocs,1,p.FinalURLS,"harnessed for productivity");
        R.printHashMap(R.Snippets);


//HashMap<String,Double>rank=new HashMap<String, Double>();
//rank.put("k",1.0);
//rank.put("l",2.0);
//rank.put("f",3.0);
//rank.put("g",4.0);
//rank.put("h",1.0);
//        HashMap<String,Double>s=sortHashMapByValue(rank);

    }


}