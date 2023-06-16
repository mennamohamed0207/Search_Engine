package main.java;//package Ranker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.mit.jwi.item.*;
import main.java.mongoDB;
import opennlp.tools.stemmer.PorterStemmer;
import org.bson.Document;
//import net.sf.extjwnl.dictionary.DictionaryException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.IStemmer;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.mit.jwi.RAMDictionary;

import javax.print.Doc;
import java.util.List;

public class Query_Processor {
    private PorterStemmer porterStemmer;
    private mongoDB database;
    private  HashSet<String> NonStemmingwords;
    private  HashSet<String>StopWords;
    private  HashSet<String>Meanings;
    String[] tokens;
    private  List<Document> matchingDocs=new ArrayList<>();
    public List<Document> Filtered=new ArrayList<>();
    private  List<String> queryTerms = new ArrayList<>();
    private  List<String> queryTermsMeaning = new ArrayList<>();
    HashSet<String>Searched=new HashSet<>();

    List<String> QueryDBWords=new ArrayList<String>();
    public Query_Processor() {

        porterStemmer=new PorterStemmer();
        database=new mongoDB();
        StopWords=getStopWords();

    }


    private List<String> preprocessQuery(String query) throws IOException {
        HashSet<String>Meanings;
        tokens = query.toLowerCase().split("\\s+");
        //Modification
        SetQueryWordsDB();
        for (String token : tokens) {
            if (!StopWords.contains(token) ) {
                queryTerms.add(token);
                Meanings= (HashSet<String>) synonymize(token);
                for(String word:Meanings){
                    queryTermsMeaning.add(word);
                }
            }
        }
        return queryTerms;
    }
    boolean findWord (String Exactword)
    {
        boolean found =false;
        MongoCollection<Document> collection = database.indexedwords;

        String stemmedWord = porterStemmer.stem(Exactword);
        // Create the query
        Document query = new Document("stemmedword", stemmedWord);

        // Find the document with the matching query
        Document cursor =  collection.find(query).first();
        if (cursor !=null) {
            List<Document> docList = (List<Document>) cursor.get("DOC");
            // Iterate over the results and extract the "Hi" field

            for (Document doc : docList) {
                if (doc.containsKey(Exactword)) {
                    found=true;
                    // System.out.println("Found "+Exactword+" in document with URL: " + doc.getString("url"));

                }
            }
        }

        return found;
    }
    //Function to get the words of the Query that exists in the database for Phrase searching
    //Modification
    public void SetQueryWordsDB()
    {
        for (String token:tokens)
        {
            if (!StopWords.contains(token)&&findWord(token))
            {
                if(QueryDBWords.contains(token))
                { String stemmedToken = porterStemmer.stem(token);
                    MongoCursor<Document> cursor = database.indexedwords.find(new Document("stemmedword", stemmedToken)).iterator();
                    while (cursor.hasNext()) {
                        Document doc = cursor.next();
                        updatestate(doc,token,0);
                        matchingDocs.add(doc);
                    }

                }
                QueryDBWords.add(token);
            }
        }

    }
    private void updatestate(Document doc, String exact, int state) {
        ArrayList<?> urls = (ArrayList<?>) doc.get("DOC");
        for (Object obj : urls) {
            if (obj instanceof Document) {
                Document url = (Document) obj;
                for (String key : url.keySet()) {
                    Object value = url.get(key);
                    if (value instanceof Document) {
                        Document wordDoc = (Document) value;
                        if (state == 0) {
                            if (key.equals(exact)) {

                                wordDoc.append("State", "exact");
                                if (!(Filtered.contains(doc)))
                                {
                                    Filtered.add(doc);
                                    // System.out.println("===========================================");
                                    //System.out.println(doc);
                                    //System.out.println("===========================================");
                                }
                            } else {
                                wordDoc.append("State", "stemmed");
                            }
                        } else {
                            wordDoc.append("State", "synonym");
                        }
                    }
                }
            }
        }
    }

    private List<Document> searchIndex(List<String> queryTerms,int state) {

        for (String term : queryTerms) {
            String stemmedToken = porterStemmer.stem(term);
            if(!Searched.contains(stemmedToken))
            {
                Searched.add(stemmedToken);
                MongoCursor<Document> cursor = database.indexedwords.find(new Document("stemmedword", stemmedToken)).iterator();
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    updatestate(doc,term,state);
                    matchingDocs.add(doc);
                }
            }
        }
        return matchingDocs;
    }

    public List<Document> processQuery(String query) throws IOException {
        preprocessQuery(query);
//        for(String word:queryTerms)
//            System.out.println("queryTerms :: "+word);
//        for(String word:queryTermsMeaning)
//            System.out.println("queryTermsMeaning :: "+word);
        searchIndex(queryTerms,0);//0 indicates that it maybe exact or stemmed
        searchIndex(queryTermsMeaning,2);//2 indicates that it is meaning not exact
        for (int i = 0; i < matchingDocs.size(); i++) {
            //  System.out.println(matchingDocs.get(i) + " ");
        }

        return matchingDocs;
    }
    public void getMeanings(String query)  throws IOException {
        // Construct a RAMDictionary object to hold the WordNet data in memory
        String path = "D:\\WordNet\\dict";
        IDictionary dict = new RAMDictionary(new File(path));
        dict.open();

        // Construct a stemmer object to get the base form of the input word
        IStemmer stemmer = new WordnetStemmer(dict);

        // Look up the input word in WordNet and get its synsets
        String word = query;
        List<String> baseForms = stemmer.findStems(word, POS.NOUN);
        if (!baseForms.isEmpty()) {
            String baseForm = baseForms.get(0);
            IIndexWord indexWord = dict.getIndexWord(baseForm, POS.NOUN);
            if (indexWord != null) {
                List<IWordID> wordIDs = indexWord.getWordIDs();
                for (IWordID wordID : wordIDs) {
                    ISynset synset = dict.getSynset(wordID.getSynsetID());

                    // System.out.println(synset.getGloss());
                }
            }
        }
        // Close the dictionary
        dict.close();

    }
    //Modification
    List<Document> FilterMatching()
    {
        List<Document> newMatching = new ArrayList<>();
        for (Document d:matchingDocs)
        {
            ArrayList<Document> urls = (ArrayList<Document>) d.get("DOC");
            for (Document obj : urls) {

                Document url = (Document) obj;
                for (String key : url.keySet()) {
                    Document value = (Document) url.get(key);
                    Document wordDoc = (Document) value;
                    String State =wordDoc.getString("State");
                    if (State=="exact") {
                        newMatching.add(d);
                    }

                }

            }

        }
        return newMatching;
    }

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
    //    public static String synonymize(String input) throws IOException {
//        // Construct a RAMDictionary object to hold the WordNet data in memory
//        String path = "D:\\WordNet\\dict";
//        IDictionary dict = new RAMDictionary(new File(path));
//        dict.open();
//
//        // Construct a stemmer object to get the base form of the input word
//        IStemmer stemmer = new WordnetStemmer(dict);
//
//        // Look up the input word in WordNet and get its synsets
//        List<String> baseForms = stemmer.findStems(input, POS.NOUN);
//        if (!baseForms.isEmpty()) {
//            String baseForm = baseForms.get(0);
//            IIndexWord indexWord = dict.getIndexWord(baseForm, POS.NOUN);
//            if (indexWord != null) {
//                List<IWordID> wordIDs = indexWord.getWordIDs();
//                for (IWordID wordID : wordIDs) {
//                    ISynset synset = dict.getSynset(wordID.getSynsetID());
//                    List<IWord> words = synset.getWords();
//                    String finalInput = input;
//                    List<IWord> matchingWords = words.stream()
//                            .filter(w -> w.getLemma().equalsIgnoreCase(finalInput) && w.getPOS() == POS.NOUN)
//                            .collect(Collectors.toList());
//                    if (!matchingWords.isEmpty()) {
//                        // Get a random synonym from the synset
//                        String finalInput1 = input;
//                        List<String> synonyms = words.stream()
//                                .filter(w -> !w.getLemma().equalsIgnoreCase(finalInput1))
//                                .map(IWord::getLemma)
//                                .collect(Collectors.toList());
//                        if (!synonyms.isEmpty()) {
//                            String synonym = synonyms.get(new Random().nextInt(synonyms.size()));
//                            input = input.replaceAll("\\b" + input + "\\b", synonym);
//                        }
//                    }
//                }
//            }
//        }
//
//        // Close the dictionary
//        dict.close();
//
//        return input;
//    }
    public static HashSet<String> synonymize(String word) throws IOException {
        HashSet<String> synonyms = new HashSet<String >();
        String path = "D:\\dict";
        IDictionary dict = new edu.mit.jwi.Dictionary(new File(path));
        dict.open();
        IIndexWord idxWord = dict.getIndexWord(word, POS.NOUN);
        if (idxWord != null) {
            for (IWordID wordID : idxWord.getWordIDs()) {
                IWord iword = dict.getWord(wordID);
                ISynset synset = iword.getSynset();
                for (IWord w : synset.getWords()) {
                    synonyms.add(w.getLemma());
                }
            }
        }
        dict.close();
        return synonyms;
    }


    public static void main(String[] args) throws IOException {

        Query_Processor q=new Query_Processor();
        q.processQuery("best coding hackerrank");
        //for get the equivalence of other words for testing ///////////////////////////
        HashSet<String> equi=synonymize("coding");
        String[] synonymsArray = equi.toArray(new String[equi.size()]);

        for (int i = 0; i<synonymsArray.length; i++) {
            System.out.println(synonymsArray[i]);
        }
        //Testing the function of the find Exact word
        // q.findWord("explore");
        //////////////////////////////////////////////////////////////////

    }

}