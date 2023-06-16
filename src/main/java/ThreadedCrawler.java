package main.java;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.*;

public class ThreadedCrawler implements Runnable {
    private  int ID;
    private Thread thread;
    private static mongoDB DataBase;
    CrawlerFuncs cf;
    //7000 not 6000 as for spam remember to check the spam of IDF,TF
    private static final long MAX_NUM_PAGES = 7000;
    static final Object LOCK = new Object();

    HashMap<String,List<String>>InconmingLinks=new HashMap<>();
    HashMap<String,Integer>OutComes=new HashMap<>();

    Integer []count=new Integer [2000];
    String []Url=new String[2000];
    ThreadedCrawler(int id,mongoDB DB)
    {
        ID=id;
        DataBase=DB;
        thread =new Thread(this);
        cf=new CrawlerFuncs(DataBase);
        thread.start();
    }
    @Override
    public void run()
    {
        crawl();
    }

    private void crawl() {

        while (!DataBase.is_Empty(DataBase.UnVisited) && DataBase.Get_Size(DataBase.Visited) <= MAX_NUM_PAGES) {
            Url[ID] = "";
            synchronized (this.LOCK) {
                //Get First Url in UnVisited
                Url[ID] = Get_Url();
                if(Url[ID]==null)
                    continue;
                DataBase.Delete(DataBase.UnVisited, Url[ID] );
                if(DataBase.IsVisited(Url[ID]))
                {
                    continue;
                }
                //remove from unvisited
                DataBase.Insert(DataBase.CurrVisiting, Url[ID] );
            }
            if (!Url[ID].equals("")) {
                RobotsTxtParser parser = new RobotsTxtParser();
                parser.getrobotfile(Url[ID] );
                ///to stop getting urls in each url when we reach the Visited we want
                if (parser.isAllowed(Url[ID] )&&DataBase.Get_Size(DataBase.UnVisited)+DataBase.Get_Size(DataBase.Visited) <= MAX_NUM_PAGES) {
                    //mark as CurrVisiting
                    //get Doc of the Url
                    Document Doc = request(Url[ID] );
                    if (Doc != null) {
                        //parse HTML File to Get Links
                        count[ID]=0;
                        for (Element link : Doc.select("a[href]")) {
                            String []st=new String[3];
                            String newLink = link.absUrl("href");
                            //url[id]  point to -->  newlink
                            DataBase.StoreInComingLinks(newLink,Url[ID]);
                            DataBase.IncOutCome(Url[ID]);
                            try {
                                st=cf.getWebpageHash(newLink);
                            }catch (Exception e){}
                            synchronized (this.LOCK) {
                                count[ID]++;
                                //I changed this part to be able to insert the normalized urls
                                //as before the urls inserted wasn't normalized and that's wrong
                               cf.IsNormalizedandHTML(newLink,st);//not visited or will be visited

//                                {
////                                    System.out.println("youve to insert");
//                                    DataBase.Insert(DataBase.UnVisited, newLink);
//                                }

                            }
                            if(count[ID]==20)
                                break;
                        }
                    }else{
                        DataBase.Delete(DataBase.CurrVisiting, Url[ID] );
                        continue;
                    }

//                System.out.println("INsert in visited");

                    String f=cf.sourcecode(Url[ID]);
                    //add to visited
                    DataBase.InsertVisited(DataBase.Visited, Url[ID],f );
                    //print Visited Info
                    System.out.println("Thread ID # " + ID + "  Link :" + Url[ID] + "  " + Doc.title());
                    //un mark the CurrVisiting
                    DataBase.Delete(DataBase.CurrVisiting, Url[ID] );
                }

                else {
                    System.out.println("IN not alloed to crawel");
                    String f=cf.sourcecode(Url[ID]);
                    DataBase.InsertVisited(DataBase.Visited, Url[ID],f );

                }
            }
        }

    }

    private synchronized  String Get_Url(){
        if(!DataBase.is_Empty(DataBase.UnVisited))
            return DataBase.Get_Urls_From_Coll(DataBase.UnVisited).get(0);
        return null;
    }

    // get document of a url
    private Document request(String Url)  {
        if(Url.contains("pinterest"))
            return  null;
        try {
            Connection con = Jsoup.connect(Url);
            Document Doc = con.get();
            if (con.response().statusCode() == 200) {
                return Doc;
            }
            else {
                return null;
            }
        }

        catch (Exception e)
        {
            return null;
        }
    }
    public Thread Get_thread(){
        return thread;
    }
    private static int GetNumThreadsFromUser(){
        Scanner myObj = new Scanner(System.in);

        // Enter username and press Enter
        System.out.println("Enter Number of Threads to spider your seeds ");
        return Integer.valueOf(myObj.nextLine());
    }

    public static void main(String[] args) throws InterruptedException {

        mongoDB DataBase= new mongoDB();
        int num=GetNumThreadsFromUser();
        ThreadedCrawler [] TC=new ThreadedCrawler[num];
        for (int i=0;i<num;i++)
        {
            TC[i]=new ThreadedCrawler(i,DataBase);
        }
        for (int i=0;i< num;i++)
        {
            TC[i].Get_thread().join();
        }
    }

}