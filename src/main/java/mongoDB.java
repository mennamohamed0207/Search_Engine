//package database;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.Iterator;
import java.util.List;
public class mongoDB {
    MongoDatabase db;
    public MongoCollection<Document> k;
    public MongoDatabase get_db() {
        return db;
    }
    public mongoDB(String Database) { //constructor holds data base name

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
            db = Client.getDatabase(Database);

            System.out.println("successfully connected to data base \n");
            //---------Collection Creation
//            db.createCollection("try");
//            db.createCollection("try1");
            k = db.getCollection("try");

            Document document1 = new Document();
            document1.append("name", "Ram");
            document1.append("age", 26);
            document1.append("city", "Hyderabad");
            Document document2 = new Document();
            document2.append("name1", "Ram");
            document2.append("age1", 26);
            document2.append("city1", "Hyderabad");
            db.getCollection("try").insertOne(document1);
            db.getCollection("try").insertOne(document2);
        } catch (Exception e) {
            System.out.println("faild to connect to data base ");
            e.printStackTrace();
        }

    }
    public FindIterable<Document> getAllk() {
        return k.find(new org.bson.Document());
    }




}