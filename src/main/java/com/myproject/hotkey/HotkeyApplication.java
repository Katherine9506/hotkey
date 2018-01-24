package com.myproject.hotkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;

@SpringBootApplication(exclude = SolrAutoConfiguration.class)
public class HotkeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotkeyApplication.class, args);
    }
}
