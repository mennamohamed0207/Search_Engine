package main.java;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.*;
import static com.mongodb.client.model.Filters.eq;

import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class mongoDB {
    //    static mongoDB myDatabase;
    static MongoDatabase db;
    public static MongoCollection<Document>Visited;
    public static MongoCollection<Document>UnVisited;
    public static MongoCollection<Document>CurrVisiting;
    public static MongoCollection<Document>NonCopy;
    public static MongoCollection<Document>PageRank;
    public static MongoCollection<Document>indexedwords;

    public static MongoCollection<Document>TagsContent;
    public static MongoCollection<Document>Content;
    public mongoDB() { //constructor holds data base nam
        try {
            //---------DataBase Connection
            //data base connnection string
            String uri ;
            if(System.getenv("DB_URI")==null)
                uri="mongodb://localhost:27017"; //database connection string
            else
                uri=System.getenv("DB_URI");
            ConnectionString connection_string = new ConnectionString(uri);
            MongoClientSettings settings =
                    MongoClientSettings.builder().applyConnectionString(connection_string).retryWrites(true).build();
            //connect to server
            MongoClient Client = MongoClients.create(settings);
            // Create the data base
            db = Client.getDatabase("GO");
            System.out.println("successfully connected to data base \n");
            //---------Collection Creation
            Visited=db.getCollection("Visited");
            UnVisited=db.getCollection("UnVisited");
            CurrVisiting=db.getCollection("CurrVisiting");
            NonCopy= db.getCollection("NonCopy");
            indexedwords=db.getCollection("indexedwords");
            PageRank=db.getCollection("PageRank");
            TagsContent=db.getCollection("TagsContent");
            Content=db.getCollection("Content");
            if(is_Empty((Visited)))
            {//if indexer finished, then crawler starts from scartch
                UnVisited.drop();
            }
            if (is_Empty(UnVisited)) {
                InsertSeed();
            }
            Clear_Current_Visiting();

        } catch (Exception e) {
            System.out.println("faild to connect to data base ");
            e.printStackTrace();
        }
    }
//    public static mongoDB getDataBase(){
//        return  myDatabase;
//    }

    public  static void  Clear_Current_Visiting(){
        if(!is_Empty(CurrVisiting))
        {
            List<String>L=Get_Urls_From_Coll(CurrVisiting);
            for(int i=0;i<L.size();i++)
            {
                Insert(UnVisited,L.get(i));
                Delete(CurrVisiting,L.get(i));
            }
        }
    }
   public void InsertContent(String url,String body)
    {
        Document d = new Document();
        d.append("url",url);
        d.append("body", body);
        Content.insertOne(d);
    }
    public static void InsertSeed()
    {

        Document document12 = new Document();
        document12.append("Name", "CNN");
        document12.append("URL", "https://edition.cnn.com/");
//        db.getCollection("UnVisited").insertOne(document12);

        Document document1 = new Document();
        document1.append("Name", "Wikipedia");
        document1.append("URL", "https://www.wikipedia.org/");
//        db.getCollection("UnVisited").insertOne(document1);

        Document document4 = new Document();
        document4.append("Name", "BBC News");
        document4.append("URL", "https://www.bbc.com/news");
//        db.getCollection("UnVisited").insertOne(document4);


        Document document6 = new Document();
        document6.append("Name", "Geeks for Geeks");
        document6.append("URL", "https://www.geeksforgeeks.org/");
//        db.getCollection("UnVisited").insertOne(document6);

        Document document18 = new Document();
        document18.append("Name", "Hackerrank");
        document18.append("URL", "https://www.hackerrank.com/");
//        db.getCollection("UnVisited").insertOne(document18);

        Document document2 = new Document();
        document2.append("Name", "Education");
        document2.append("URL", "https://www.education.com/");
//        db.getCollection("UnVisited").insertOne(document2);

        Document document3 = new Document();
        document3.append("Name", "Github");
        document3.append("URL", "https://github.com/");
//        db.getCollection("UnVisited").insertOne(document3);

        Document document5 = new Document();
        document5.append("Name", "Free Code Camp");
        document5.append("URL", " https://www.freecodecamp.org/");
//        db.getCollection("UnVisited").insertOne(document5);


        Document document7 = new Document();
        document7.append("Name", "Coursera");
        document7.append("URL", "https://www.coursera.org/");
//        db.getCollection("UnVisited").insertOne(document7);

        Document document8 = new Document();
        document8.append("Name", "Khan Academy");
        document8.append("URL", "https://www.khanacademy.org/");
//        db.getCollection("UnVisited").insertOne(document8);

        Document document9 = new Document();
        document9.append("Name", "Stackoverflow");
        document9.append("URL", "https://stackoverflow.com/");
//        db.getCollection("UnVisited").insertOne(document9);

        Document document10 = new Document();
        document10.append("Name", "Mozilla");
        document10.append("URL", "https://developer.mozilla.org/en-US/");
//        db.getCollection("UnVisited").insertOne(document10);

        Document document11 = new Document();
        document11.append("Name", "W3Schools");
        document11.append("URL", "https://www.w3schools.com/");
//        db.getCollection("UnVisited").insertOne(document11);


        Document document13 = new Document();
        document13.append("Name", "woot");
        document13.append("URL", "https://www.woot.com/");
//        db.getCollection("UnVisited").insertOne(document13);

        Document document14 = new Document();
        document14.append("Name", "health");
        document14.append("URL", "https://www.health.com/");
//        db.getCollection("UnVisited").insertOne(document14);

        Document document15 = new Document();
        document15.append("Name", "imdb");
        document15.append("URL", "https://www.imdb.com/");
//        db.getCollection("UnVisited").insertOne(document15);

        Document document16 = new Document();
        document16.append("Name", "quora");
        document16.append("URL", "https://www.quora.com/");
//        db.getCollection("UnVisited").insertOne(document16);

        Document document17 = new Document();
        document17.append("Name", "allrecipes");
        document17.append("URL", "https://www.allrecipes.com/\n");
//        db.getCollection("UnVisited").insertOne(document17);

//using insertmany as it's faster than insertone
        List<Document> doc= new ArrayList<>();
        doc.add(document1);
        doc.add(document2);
        doc.add(document3);
        doc.add(document4);
        doc.add(document5);
        doc.add(document6);
        doc.add(document7);
        doc.add(document8);
        doc.add(document9);
        doc.add(document10);
        doc.add(document12);
        doc.add(document13);
        doc.add(document14);
        doc.add(document15);
        doc.add(document16);
        doc.add(document17);
        // adding the links to the database
        UnVisited.insertMany(doc);


    }
    public static void Insert(MongoCollection<Document>Col,String Url){
        Document d = new Document();
        d.append("URL", Url);
//        if(Col==Visited)
//        {
//            d.append("P",1);
//        }
        Col.insertOne(d);

    }
    public static void InsertVisited(MongoCollection<Document>Col,String Url,String content){
        Document d = new Document();
        d.append("URL", Url);
        d.append("html", content);
//        if(Col==Visited)
//        {
//            d.append("P",1);
//        }
        Col.insertOne(d);

    }
    public void InsertNormalized(String rawHTML,String url,int size,String fullhtml,String title){
        Document d = new Document();
        d.append("rawHTML", rawHTML);
        d.append("url",url);
        d.append("size",size);
        d.append("fullhtml",fullhtml);
        d.append("title",title);
        NonCopy.insertOne(d);

    }
    public Boolean Iscontained(String rawHTML) {
        return (NonCopy.countDocuments(Filters.eq("rawHTML",rawHTML)) > 0);
    }
    public int countOutgoingLinks(String url){
        FindIterable<Document> results = PageRank.find(Filters.eq("URL", url));
        for (Document doc : results) {
            return (Integer) doc.get("OutC");
        }
        return 0;
    }
    public Boolean linksTo(String LinkingUrl,String url)
    {
        FindIterable<Document> results = PageRank.find(Filters.eq("URL", url));
        for (Document doc : results) {
            ArrayList<String>Linking=(ArrayList<String>) doc.get("INCome");
            if(Linking!=null)
                return Linking.contains(LinkingUrl);
        }
        return false;
    }
    public ArrayList<String>  GetLinkToSet(String url){

        FindIterable<Document> results = PageRank.find(Filters.eq("URL", url));
        for (Document doc : results) {
            ArrayList<String> links = (ArrayList<String> )doc.get("INCome");
            return links;
        }
        return null;
    }
    public void IncPriority(String Url){
        // create the update operation to modify the selected document(s)
        Bson filter = Filters.eq("URL", Url);
        // create the update operation to modify the selected document(s)
        Bson update = Updates.inc("P", 1);
        // update a single document that matches the filter
        UpdateResult result = Visited.updateOne(filter, update);
    }
    public void IncOutCome(String Url){
        if (PageRank.countDocuments(Filters.eq("URL", Url)) <= 0) {
            Document Doc =new Document();
            Doc.append("URL",Url);
            Doc.append("OutC",1);
            PageRank.insertOne(Doc);
        } else {
            // create the update operation to modify the selected document(s)
            Bson filter = Filters.eq("URL", Url);
            // create the update operation to modify the selected document(s)
            Bson update = Updates.inc("OutC", 1);
            // update a single document that matches the filter
            UpdateResult result = PageRank.updateOne(filter, update);
        }
    }
    public void StoreInComingLinks(String link , String income) {
        if(link.equals(income))return;
        if (PageRank.countDocuments(Filters.eq("URL", link)) <= 0) {
            Document Doc =new Document();
            Doc.append("URL",link);
            List<String>incomes=new ArrayList<>();
            incomes.add(income);
            Doc.append("INCome",incomes);
            PageRank.insertOne(Doc);
        } else {
            // create the update operation to modify the selected document(s)
            Bson filter = Filters.eq("URL", link);
            // create the update operation to modify the selected document(s)

            Bson update = Updates.push("INCome", income);
            // update a single document that matches the filter
            UpdateResult result = PageRank.updateOne(filter, update);
        }
    }
    public static void Delete(MongoCollection<Document>Col,String Url) {
        Col.deleteOne(Filters.eq("URL", Url));
    }
    public static Boolean is_Empty(MongoCollection<Document>Col) {
        if(Col.countDocuments()>0)
            return false;
        return true;

    }
    public static long Get_Size(MongoCollection<Document> Col) {
        return Col.countDocuments();
    }
    public Boolean IsVisited(String Url) {
        return (Visited.countDocuments(Filters.eq("URL",Url)) > 0)||
                (UnVisited.countDocuments(Filters.eq("URL",Url)) > 0)||
                (CurrVisiting.countDocuments(Filters.eq("URL",Url))>0);
    }
    public static List<String> Get_Urls_From_Coll( MongoCollection<Document> col) {
        List<String> fieldValues = new ArrayList<>();
        MongoCursor<Document> cursor = col.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String fieldValue = doc.getString("URL");
                if (fieldValue != null) {
                    fieldValues.add(fieldValue);
                }
            }
        } finally {
            cursor.close();
        }
        return fieldValues;
    }
    public String Extract_URL()
    {
        Bson sorts = Sorts.descending("p");
        MongoCursor<Document> cursor = UnVisited.find().sort(sorts).iterator();
        Document Doc= cursor.next();
        return Doc.getString("Url");
    }
}