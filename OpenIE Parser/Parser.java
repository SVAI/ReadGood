import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Parser {

    private String subject = "";
    private String object = "";
    private String relation = "";

    //Filter lists
    private List<String> SUBJECTS = new ArrayList<String>();
    private List<String> RELATIONS = new ArrayList<String>();

    public Parser() throws IOException {
        SUBJECTS = Files.readAllLines(Paths.get("/Users/realmx2000/IdeaProjects/OpenIE_Parser/src/subjects.txt"));
        RELATIONS = Files.readAllLines(Paths.get("/Users/realmx2000/IdeaProjects/OpenIE_Parser/src/relations.txt"));
    }

    public void parse(String path, PrintWriter pw) throws IOException {
        // Create a CoreNLP document
        Scanner scan = new Scanner(new File(path));
        scan.useDelimiter("\\.|\\Z");
        String segment = "";
        while (scan.hasNext()) {
            segment = "";
            for (int i = 0; (i < 10) && scan.hasNext(); i++) {
                segment += scan.next();
            }
            Document doc = new Document(segment);
            // Iterate over the sentences in the document
            for (Sentence sent : doc.sentences()) {
                // Iterate over the triples in the sentence
                // If triple fits, append to csv
                for (RelationTriple triple : sent.openieTriples())
                    if (filter(triple))
                        writeFile(pw, path);
            }
        }
    }

    private boolean filter(RelationTriple triple) {
        subject = triple.subjectLemmaGloss().toLowerCase().replace(",", "");
        object = triple.objectLemmaGloss().toLowerCase().replace(",", "");
        relation = triple.relationLemmaGloss().toLowerCase().replace(",", "");
        boolean flag = false;
        if (subject.length() > 100 || object.length() > 100 || relation.length() > 100) return false;
        for (String desired : SUBJECTS) {
            if (subject.contains(desired.toLowerCase()) || object.contains(desired.toLowerCase())) {
                flag = true;
                break;
            }
        }
        if (!flag) return false;
        for (String desired : RELATIONS)
            if (relation.contains(desired.toLowerCase()) && flag) return true;
        return false;
    }

    private void writeFile(PrintWriter pw, String path) throws FileNotFoundException {

        StringBuilder sb = new StringBuilder();
        sb.append(subject);
        sb.append(",");
        sb.append(relation);
        sb.append(",");
        sb.append(object);
        sb.append(",");
        sb.append(path.substring(path.lastIndexOf("/") + 1));
        sb.append('\n');

        pw.write(sb.toString());
        pw.flush();
    }
}