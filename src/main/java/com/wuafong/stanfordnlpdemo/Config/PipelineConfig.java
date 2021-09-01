package com.wuafong.stanfordnlpdemo.Config;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class PipelineConfig {
    @Bean
    StanfordCoreNLP stanfordCoreNLP(){
        Properties props = new Properties();
        props.setProperty("annotators","tokenize,ssplit,pos,parse,lemma,ner,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        return pipeline;
    }
}
