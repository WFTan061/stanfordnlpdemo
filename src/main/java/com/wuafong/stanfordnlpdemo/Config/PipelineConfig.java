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
        props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP();
        return pipeline;
    }
}
