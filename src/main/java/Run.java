package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Run {
    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(1000);
        runTomCat();
        Thread.sleep(1000);
        openLocalHost();
    }



    public static void runTomCat() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "cmd /c start startup.bat",
                    null,
                    new File("C:\\Users\\ahma3\\IdeaProjects\\apache-tomcat-9.0.75-windows-x64\\apache-tomcat-9.0.75\\bin"));
        } catch (IOException e) {
            System.out.println("Cannot run tomcat");
        }
    }

    public static void openLocalHost() {
        try {
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome http://localhost:8080/SearchEngine.html"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
