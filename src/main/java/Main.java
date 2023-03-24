//package all;
import java.io.IOException;
import java.util.*;
import java.util.Iterator;
public class Main {
    public static void main(String[] args)
    {

//		System.out.println(x);
//printing a collection
        mongoDB DB1=new mongoDB("DB1");
 Iterator it=DB1.getAllk().iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
