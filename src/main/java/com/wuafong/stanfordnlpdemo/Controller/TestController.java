package com.wuafong.stanfordnlpdemo.Controller;

import com.wuafong.stanfordnlpdemo.dto.TestDTO;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.IntPair;
import org.ejml.simple.SimpleMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private StanfordCoreNLP pipeline;

    private static String[] positivity = {"Very negative" , "Negative" , "Neutral", "Positive", "Very Positive"};
    @PostMapping
    private ResponseEntity<?> testingStuff(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody TestDTO test){
        CoreDocument doc = new CoreDocument(test.getSentence());
        Annotation sentimentTest = new Annotation(test.getSentence());
        System.out.println("test Annotation object: " + sentimentTest.toString());
        pipeline.annotate(doc);
        pipeline.annotate(sentimentTest);
        for(CoreMap sentence: sentimentTest.get(CoreAnnotations.SentencesAnnotation.class)){
            //root of tree.
            final Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            transverseTree(tree, test.getSentence());
//            for(Tree testTree: tree.getChildrenAsList()){
//                List<Double> annotTest = RNNCoreAnnotations.getPredictionsAsStringList(testTree);
//                System.out.println("---");
//                System.out.println(testTree.getSpan());
//                for(Double s:annotTest){
//                    System.out.println(s);
//                }
//                System.out.println("---");
//            }
//            List<Double> annotations = RNNCoreAnnotations.getPredictionsAsStringList(tree);
//            System.out.println("---");
//            System.out.println(tree.getSpan());
//            System.out.print("sentiment index : ");
//            for(Double s: annotations){
//                System.out.println(s + ", ");
//            }
//            System.out.println("---");
        }
        System.out.println("---");
        System.out.println("entities found");

        for (CoreEntityMention em : doc.entityMentions())
            System.out.println("\tdetected entity: \t"+em.text()+"\t"+em.entityType());
        for(CoreSentence sentence : doc.sentences()){
            System.out.println(sentence.toString() + " : " +sentence.sentiment());
        }
        System.out.println("---");
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * Tranversing :
     * Start 0... to length-1.
     * Span 0,0 represents start to start
     * @param t
     */
    private void transverseTree(Tree t,String sentence){
            System.out.println("---");

            String[] indivWords = sentence.split("\\s+");
            IntPair testPair = t.getSpan();
            StringBuilder buildSentence = new StringBuilder();
            for(int j = testPair.getSource(); j <= testPair.getTarget(); j++){
                buildSentence.append(indivWords[j]);
                buildSentence.append(" ");
            }
            System.out.println("Sentiment Test : "  + buildSentence.toString());
            System.out.println(t.getSpan() + " : " + t.getClass());

            try {
                List<Double> annotTest = RNNCoreAnnotations.getPredictionsAsStringList(t);
                int i = 0;
                for (Double s : annotTest) {
                    System.out.println(positivity[i] + " : " + s);
                    i++;
                }
            }catch(Exception e){
                System.out.println("Out of bounds or smth.");
            }finally{
                System.out.println("---");
                for (Tree child : t.getChildrenAsList()) {
                    if (child != null) {
                        transverseTree(child,sentence);
                    }
                }
            }
        }
}
