/**
 * Created by realmx2000 on 6/24/17.
 */

import java.io.File;
import java.io.PrintWriter;

public class Main {
    final private static String PATH = "/Users/realmx2000/Downloads/Papers";

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        File home = new File(PATH);
        File[] files = home.listFiles();
        int total = files.length;
        int current = 1;
        PrintWriter pw = new PrintWriter(new File("/Users/realmx2000/Documents/ReadGood/results.csv"));
        pw.close();
        PrintWriter pw2 = new PrintWriter(new File("/Users/realmx2000/Documents/ReadGood/results.csv"));
        pw2.write("Subject,Relation,Object,Evidence\n");
        for (File paper : files) {
            System.out.printf("Now parsing file %d of %d.\n", current++, total);
            String location = paper.toString();
            if (location.contains(".txt"))
                parser.parse(paper.toString(), pw2);
            else
                System.out.println("Not a text file. Continuing...");
        }
        pw2.close();
    }
}
