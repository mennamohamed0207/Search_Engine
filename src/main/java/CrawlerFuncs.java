package main.java;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.jsoup.HttpStatusException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class CrawlerFuncs
{
    static Queue<String>queue=new LinkedList<>();
    private static main.java.mongoDB DB;
    //    public MongoCollection<Document> non;
//    static Queue<String>NonCopy=new LinkedList<>();
    CrawlerFuncs(main.java.mongoDB db)
    {
        DB=db;
    }
    //=========Normalizing Url=================
    static String getNormalizedURL(String s)
    {
        try {
            URL url = new URL(s);
            String scheme = (url.getProtocol()+"://").toLowerCase();
            String host = getHostName(url.getHost());
            String port = getPort(url.getPort());
            String path = normalizePath(url.getPath());
            String query = sortQueryParamerters(url.getQuery());
            String l= scheme+host+port+path+query;
            return l;
        } catch (MalformedURLException e) {
            return null;
        }
    }
    static String normalizePath(String path)
    {
        String p = 	path.replaceAll("//", "/").replace("index.html", "")
                .replace("index.htm", "") //since these filenames are often used by default
                .replace("index.php", "")  // for URLs that point to a directory.
                .replaceAll("\\{", "%7B")// replaces any occurrences of opening or closing curly
                .replaceAll("\\}", "%7D"); // braces ({ and }) with their URL-encoded equivalents (%7B and %7D, respectively),
        // since these characters have special meaning in URL paths and may
        if(p.endsWith("/")) {
            p = p.substring(0, p.length()-1);
        }
        return p;
    }
    //Overall, this function is useful for standardizing and extracting the hostname component of a website URL,
// regardless of whether the URL is specified using a domain name or an IP address.
// It also removes the www subdomain from the hostname, which is a common convention used for many websites.
// https://www.example.com/index.html, the hostname is www.example.com.
    static String getHostName(String host)
    {
        //check if IP not domain name
        if(host.matches("([0-9]+[.]*)+")){
            try {
                host = InetAddress.getByName(host).getHostName();
            } catch (UnknownHostException e) {
                //e.printStackTrace();
            }
        }
        host = host.toLowerCase();
        // remove www
        if(host.length() > 3 && host.startsWith("www")) {
            host = host.substring(host.indexOf('.')+1);
        }
        return host;
    }
    //in the URL http://example.com:8080/index.html, the port number is 8080.
    static String getPort(int port)
    {return (port == 80 || port == -1) ? "" : (":" + String.valueOf(port));}
    ///Overall, this function can be useful for sorting and cleaning up URL
// query parameters before making network requests or parsing the parameters in a web application.
    static String sortQueryParamerters(String query)
    {
        if(query == null || query.equals("")) return "";
        String[] parameters = query.split("&");
        SortedMap<String, String> sortedPar = new TreeMap<String, String>();
        for(String s : parameters) {
            int idxSep = s.indexOf('=');
            if(idxSep == -1) continue;
            String par = s.substring(0, idxSep);
            String val = s.substring(idxSep+1);
            sortedPar.put(par, val);
        }
        String sortedQuery = "";
        for (SortedMap.Entry<String,String> p : sortedPar.entrySet()) {
            if(p.getValue().equals("")) continue;
            if(!sortedQuery.equals("")) sortedQuery+="&";
            sortedQuery += p.getKey()+"="+p.getValue();
        }
        return !sortedQuery.equals("") ? "?"+sortedQuery : "";
    }
    //==========checking that it's an html page=================
    static boolean IsHTML(String url)
    {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            String contentType = conn.getContentType();
//            System.out.println(contentType);
            if (contentType != null && contentType.startsWith("text/html"))
                return true;
            else return false;
        }catch (Exception e){return false;}
    }
    //====== checking that there is no any urls that both refer to same content=============
    public static String[] getWebpageHash(String url) throws Exception {
//        URL website = new URL(url);
//        BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
//        StringBuilder sb = new StringBuilder();
//        String inputLine;
//
//        while ((inputLine = in.readLine()) != null) {
//            sb.append(inputLine);
//        }
//
//        in.close();
//
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(sb.toString().getBytes("UTF-8"));
//        byte[] digest = md.digest();
//
//        StringBuilder hash = new StringBuilder();
//        for (byte b : digest) {
//            hash.append(String.format("%02x", b));
//        }
//
//        return hash.toString();
        String[] s=new String[4];
        String rawHTML = "";
        try {
            URL url1 = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url1.openStream()));
            String line = in.readLine();
            while (line != null)
            {
                if (!line.isEmpty())
                {
                    int middleIndex = (line.length()) / 2;
                    rawHTML+= line.charAt(middleIndex);
                    rawHTML+=line.charAt(0);
                    rawHTML+=line.charAt(line.length()-1);

                }
                // System.out.println(line);
                line = in.readLine();
            }
            in.close();
        } catch (Exception e) {}
        String fullhtml="";
        try {
            String u = url;
            Document doc = Jsoup.connect(u).get();
            fullhtml = doc.text();
            s[3]=doc.title();
        }catch (Exception e){}
        String[]sub=fullhtml.split("\\s+");
        int size=sub.length;
//        return rawHTML;
        s[0]=rawHTML;
        s[1]=fullhtml;
        s[2]=Integer.toString(size);
        return s;
    }
    public static String sourcecode(String url)
    {
        String fullhtml="";
        try {
            String u = url;
            Document doc = Jsoup.connect(u).get();
            fullhtml = doc.html();
        }catch (Exception e){}
//        System.out.println(fullhtml);
        return fullhtml;
    }
    static boolean IsACopy(String st) {
//        String rawHTML = "";
////        System.out.println("rawHTML=" + rawHTML);
//        try {
//            URL url1 = new URL(url);
//            BufferedReader in = new BufferedReader(new InputStreamReader(url1.openStream()));
//            String line = in.readLine();
//            System.out.println(line);
//            while (line != null)
//            {
//                if (!line.isEmpty())
//                {
//                    int middleIndex = (line.length()) / 2;
//                    rawHTML+= line.charAt(middleIndex);
//                    rawHTML+=line.charAt(0);
//                    rawHTML+=line.charAt(line.length()-1);
//
//                }
//                line = in.readLine();
//            }
//            in.close();
//        } catch (Exception e) {}
//        if (NonCopy.contains(rawHTML) || rawHTML.equals(""))
//            return true;
//        else {
//            NonCopy.add(rawHTML);
//            System.out.println("url="+url+" rawHTML="+rawHTML);
//            return false;
//        }
//        try {
//            String u = url;
////            Document doc = Jsoup.connect(u).get();
////            rawHTML = doc.text();
//              rawHTML=getWebpageHash(url);
//        }catch (Exception e){}

        if (DB.Iscontained(st)|| st.equals(""))
            return true;
        else {
            return false;
        }

    }

    public static void IsNormalizedandHTML (String url,String[] st)
    {
        String l=getNormalizedURL(url);
        boolean b1=DB.IsVisited(l);
        if(b1)//visited before
            return;
        boolean b2=IsACopy(st[0]);
        boolean b3=IsHTML(l);
        if(!b2)
        {  DB.InsertNormalized(st[0],l,Integer.parseInt(st[2]),st[1],st[3]);
//       System.out.println("insert in normalized");
        }
        //before we was making that if this function returns true then insert in unvisited
        //but using this way we was inserting urls without being normalized
        if(!b1&&!b2&&b3)
            DB.Insert(DB.UnVisited, l);
    }
    public static void main(String[] args) throws Exception {
        String url = "https://www.nytimes.com/international/";
        sourcecode(url);
//        mongoDB DataBase= new mongoDB();
//        CrawlerFuncs n=new CrawlerFuncs(DataBase);
////        String url1 = "https://www.health.com/";
//        String url1="https://www.woot.com/#";
//        String[]s1= getWebpageHash(url1);
////        String url2 = "https://www.health.com/";
//        String url2="https://www.woot.com/";
//        String[]s2= getWebpageHash(url2);
//        //s1[0],s2[0]-> compact string
//        if(IsNormalizedandHTML(url1,s1))
//        { System.out.println("can insert");DataBase.Insert(DataBase.Visited,url1);}
//        else System.out.println("can't insert");
//        if(IsNormalizedandHTML(url2,s2))
//            System.out.println("can insert");
//        else System.out.println("can't insert");

    }


}