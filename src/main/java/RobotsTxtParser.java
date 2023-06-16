package main.java;
//to use that class
//1. make an object of the class like --> RobotsTxtParser parser = new RobotsTxtParser();
//2. calll the function getrobotfile to  read the file of robot.txt like -->
//3. check if it is allowed or not using the function isAllowed like -->
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RobotsTxtParser {
    private URL visiting;
    private String result;
    private boolean disallowed;

    public void getrobotfile(String link) {
        visiting = null;
        result = null;
        disallowed = false;
        try {
            visiting = new URL(link);
            URL robotsTxtUrl = new URL(visiting.getProtocol() + "://" + visiting.getHost() + "/robots.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(robotsTxtUrl.openStream()));
            Scanner scanner = new Scanner(reader).useDelimiter("\\A");
            result = scanner.hasNext() ? scanner.next() : "";
            if (result.contains("User-agent: *")&& result.contains("Disallow")) {
                disallowed = true;
                result = result.replace("Disallow", "");
            }
            // System.out.println("Robots.txt file contents: " + result);
        } catch (MalformedURLException e) {
            // System.err.println("Error: Invalid URL: " + e.getMessage());
        } catch (Exception e) {
            //  System.err.println("Error retrieving robots.txt file: " + e.getMessage());
        }
    }

    public boolean isAllowed(String url) {
        // If there is no robots.txt file, allow all URLs
        if (result == null) {
            return true;
        }
        // If the robots.txt file does not contain any rules for this URL, allow it
        if (!disallowed) {
            return true;
        }
        String regex = result.replaceAll("\\*", ".*");
        regex = ".*" + regex + ".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return !matcher.matches();
    }


//    public static void main(String[] args) throws IOException {
//        RobotsTxtParser parser = new RobotsTxtParser();
//        //test for the one which does not have robot.txt
//        parser.getrobotfile("http://www.moma.org/robots.txt");
//        System.out.println("Robots.txt file contents: " + parser.result);
//        System.out.println("Is http://www.moma.org/robots.txt " + parser.isAllowed("http://www.moma.org/robots.txt"));
//
//        parser.getrobotfile("https://www.google.com/");
//        System.out.println("Robots.txt file contents: " + parser.result);
//        System.out.println("Is https://www.google.com/ " + parser.isAllowed("https://www.google.com/"));
//    }

}