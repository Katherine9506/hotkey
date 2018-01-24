package com.myproject.hotkey.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class BeanConfig {

    @Value("${solrUrl}")
    private String solrUrl;

    @Bean
    public SolrClient solrClient() {
        SolrClient solrClient = new HttpSolrClient.Builder(this.solrUrl).withSocketTimeout(6000).withConnectionTimeout(1000).build();
        return solrClient;
    }

    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

}
