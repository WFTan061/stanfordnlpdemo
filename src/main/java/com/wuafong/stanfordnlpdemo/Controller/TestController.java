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
            transverseTree(tree);
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

    private void transverseTree(Tree t){
            System.out.println("---");
            System.out.println(t.getSpan());
            try {
                List<Double> annotTest = RNNCoreAnnotations.getPredictionsAsStringList(t);
                for (Double s : annotTest) {
                    System.out.println(s);
                }
            }catch(Exception e){
                System.out.println("Out of bounds or smth.");
            }finally{
                System.out.println("---");
                for (Tree child : t.getChildrenAsList()) {
                    if (child != null) {
                        transverseTree(child);
                    }
                }
            }
        }
}
