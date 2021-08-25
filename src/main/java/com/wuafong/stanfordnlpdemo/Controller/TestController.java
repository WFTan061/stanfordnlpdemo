package com.wuafong.stanfordnlpdemo.Controller;

import com.wuafong.stanfordnlpdemo.dto.TestDTO;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.CoreNLPProtos;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
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

@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private StanfordCoreNLP pipeline;
    @PostMapping
    private ResponseEntity<?> testingStuff(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody TestDTO test){
        CoreDocument doc = new CoreDocument(test.getSentence());
        pipeline.annotate(doc);
        System.out.println("---");
        System.out.println("entities found");
        for (CoreEntityMention em : doc.entityMentions())
            System.out.println("\tdetected entity: \t"+em.text()+"\t"+em.entityType());
        System.out.println("---");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
