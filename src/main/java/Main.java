package main.java;//package Ranker;

import main.java.Query_Processor;
import main.java.mongoDB;
import org.bson.Document;
import java.util.Scanner;
import java.io.IOException;
import java.util.*;
public class Main
{
    String query;
    public Main(String q)
    {
        query=q;
    }

    public static List<Document> search() throws IOException {
        List<Document> Docs=new ArrayList<>();
        int num;
        List<Document>output=new ArrayList<>();
        HashMap<String,List<String>>arr=new HashMap<>();
        HashMap<String,Double>rankurls=new HashMap<String, Double>();
        Ranker r=new Ranker();
        Scanner scanner = new Scanner(System.in);
        String userInput="";

        mongoDB db=new mongoDB();
        // Ask the user to enter a string
        System.out.print("Please enter a string: ");

        // Read the user's input as a string
        userInput = scanner.nextLine();

        // Print the string entered by the user
        System.out.println("You entered: " + userInput);
        r.SuggestionsSave(userInput);

        // Close the scanner to free up resources
//        scanner.close();
        if (userInput.charAt(0)=='\"'&&userInput.charAt(userInput.length()-1)=='\"'&&!userInput.equals(" ")) {
            System.out.println("The string contains double quotes.");//phrase
            Phrase_SearchingV2 p=new Phrase_SearchingV2(userInput);
            if (p.checkonCheckTags()) {
                System.out.println("Found");
            }else   System.out.println("Not Found");
            p.PhraseSearching();
            arr=p.FinalURLS;
            rankurls= r.letsrank(p.FilteredDocs,1,arr,userInput);
        } else {
            System.out.println("The string does not contain double quotes.");
            Query_Processor q=new Query_Processor();
            Docs= q.processQuery(userInput);
            for (Document doc : Docs) {
                System.out.println(doc.get("URL"));
            }
            rankurls= r.letsrank(Docs,2,arr,userInput);
        }
        System.out.println("hh");
        HashMap<String, Double> map=r.sortHashMapByValue(rankurls);
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            System.out.println("Url for main "+key + " has rank : " + entry.getValue());
            String title= r.getTitle(key);
            String Snippet=r.Snippets.get(key);
//System.out.println("url="+key+"title="+title);
//System.out.println("Snippet="+Snippet);
            Document d1=new Document();
            d1.append("title",title);
            d1.append("URL",key);
            d1.append("paragraph",Snippet);
            output.add(d1);
        }
        return output;

//        r.Print_Rank();
    }




}