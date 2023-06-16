package main.java;

import main.java.mongoDB;
import opennlp.tools.stemmer.PorterStemmer;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Phrase_Searching {
    private PorterStemmer porterStemmer;//stemmer

    private static HashSet<String> stopWords;
    private static HashSet<String> NonStemmingWords;
    //////////For Phrase Searching Version 2//////////////
    List<String> query_words;
    public HashMap<Integer, Document> id_final;
    List<String> all_content = new ArrayList<>();
    List<String> UrlCorrespoding = new ArrayList<>();
    public HashMap<Integer, Document> id_temp;
    public HashMap<Integer, Document> id_final_not_phrase;
    public HashSet<Document> id_final_phrase; //phrase
    int count;
    String Phrase;
    Query_Processor q;
    private mongoDB database;
    List<Document> matchingDocs = new ArrayList<>();

    public Phrase_Searching(String Phrase) throws IOException {
        database = new mongoDB();
        id_final = new HashMap<Integer, Document>();
        id_temp = new HashMap<Integer, Document>();
        id_final_phrase = new HashSet<Document>();
        id_final_not_phrase = new HashMap<Integer, Document>();
        this.Phrase = Phrase;
        q = new Query_Processor();
        matchingDocs = q.processQuery(Phrase);
        getContent();
        query_words = q.QueryDBWords;
        porterStemmer = new PorterStemmer();
        stopWords = getStopWords();
        NonStemmingWords = getNonStemmingwords();
        this.fillid_final();
        this.PhraseSearch(Phrase);


    }

    void fillid_final() {
        for (int i = 0; i < matchingDocs.size(); i++)
            id_final.put(i, matchingDocs.get(i));
    }

    void getContent() {
        for (int i = 0; i < matchingDocs.size(); i++) {
            List<Document> docList = (List<Document>) matchingDocs.get(i).get("DOC");
            for (Document doc : docList) {
                String url = doc.getString("url");
                Document query = new Document("url", url);
                Document result = database.NonCopy.find(query).first();
                if (result != null) {
                    String content = result.getString("rawHTML");
                    all_content.add(content);
                    UrlCorrespoding.add(url);
                }
            }

        }

    }

    public void PhraseSearch(String original_query) {

        if (matchingDocs.size() >= query_words.size()) {
            handlePhraseMatch(original_query);
        }
        System.out.println("id_final content");
        this.printHashMap(id_final);
        System.out.println("id_final_not_phrase content");
        this.printHashMap(id_final_not_phrase);
        System.out.println("id_final_phrase content");
        for (Document doc : id_final_phrase) {
            System.out.println(doc);
        }

    }


//private void handleNotPhraseMatch(String original_query, int i) {
//    id_final_not_phrase.put(i,id_final.get(i));
//    String current_content=all_content.get(i);
//    String sub=original_query;
//    String arr[] = sub.split(" ");
//
//    for(int j=0;j<arr.length;j++) {
//        int indexOfSubStr = current_content.toLowerCase(Locale.ROOT).indexOf(arr[j].toLowerCase(Locale.ROOT));
//
//        if(indexOfSubStr != -1) {
//            // Found exact word match
//            id_final.get(i).append("first_index", indexOfSubStr);
//            id_final.get(i).append("first_word", arr[j]);
//            id_final_not_phrase.put(i,id_final.get(i));
//            break;
//        } else {
//            // Try stemming
//            String stem = porterStemmer.stem(arr[j]);
//            int len=stem.length()-2;
//            indexOfSubStr=current_content.toLowerCase(Locale.ROOT).indexOf(stem.substring(len).toLowerCase(Locale.ROOT));
//            id_final.get(i).append("first_index", indexOfSubStr);
//            id_final.get(i).append("first_word", arr[j]);
//            id_final_not_phrase.put(i,id_final.get(i));
//        }
//    }
//}

    private void handlePhraseMatch(String original_query) {

        for (int j = 0; j < all_content.size(); j++) {
            String current_content = all_content.get(j);
            if (current_content.toLowerCase(Locale.ROOT).contains(original_query.toLowerCase(Locale.ROOT))) {

                Document d = new Document();
                d.append("URL", UrlCorrespoding.get(j));

                id_final_phrase.add(d);
            }
        }

    }
    //    } else {
//        // Try finding each word in the phrase separately
//        String sub=original_query;
//        String arr1[] = sub.split(" ");
//
//        for(int k=0;k<arr1.length;k++) {
//            int index = current_content.toLowerCase(Locale.ROOT).indexOf(arr1[k].toLowerCase(Locale.ROOT));
//
//            if(index != -1) {
//                // Found exact word match
//                id_final.get(i).append("first_index", index);
//                id_final.get(i).append("first_word", arr1[k]);
//                id_final_not_phrase.put(i,id_final.get(i));
//                break;
//            } else {
//                // Try stemming
//                String stem = porterStemmer.stem(arr1[k]);
//                int len=stem.length()-2;
//                index=current_content.toLowerCase(Locale.ROOT).indexOf(stem.substring(len).toLowerCase(Locale.ROOT));
//
//                id_final.get(i).append("first_index", index);
//                id_final.get(i).append("first_word", arr1[k]);
//                id_final_not_phrase.put(i,id_final.get(i));
//            }
//        }
//    }
//}
    public  void printHashMap(HashMap<Integer, Document> hm) {
        for (Map.Entry<Integer, Document> entry : hm.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }


    private String get_Snippet(String body_String,int start_index)
    {
        //split the string into words
        String[] body = body_String.split("\\s+");
        StringBuffer snippet = new StringBuffer();
        int i = start_index;
        StringBuffer temp2 = new StringBuffer(body[start_index]);
        //reverse the word that we are searching for
        temp2.reverse();
        snippet.append(temp2+" ");
        i--;
        int counter =0;
        //get the ten word before the chosen word
        while (i>=0&&counter<=10)
        {
            StringBuffer temp = new StringBuffer(body[i]);
            temp.reverse();
            snippet.append(temp + " ");
            i--;
            counter++;

        }
        //until here the snippet is reversed ,so we will reverse it back
        snippet.reverse();
        snippet.append(" ");
        snippet.deleteCharAt(0);//remove space;
        //here we want to get the ten words after the chosen one
        //But first we have to check if the word has words after it aslan
        //So we check if it ends with dot or not
        //if so we return the snippet without getting the ten words after it
        i = start_index + 1;
        if (body[start_index].endsWith("."))
            return snippet.toString();

        counter=0;
        //get the words after it
        while (i <= body.length - 1 &&counter!=10) {
            snippet.append(body[i] + " ");
            i++;
            counter++;
        }//end of while loop

        if (i <= body.length - 1)
            snippet.append(body[i] + " ");

        return snippet.toString();
    }

    public static HashSet<String>  getStopWords() {
        HashSet<String> stopw=new HashSet<String>();
        try {
            File websitesFile = new File("F:\\GoWell-new2\\GoWell-new2\\src\\stopwords.txt");
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
            File websitesFile = new File("F:\\GoWell-new2\\GoWell-new2\\src\\NonStemmingwords.txt");
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

    public static void main(String[] args) throws IOException {
        Phrase_Searching p=new Phrase_Searching("Online Coding Tests & Certified Assessments");

    }

}
