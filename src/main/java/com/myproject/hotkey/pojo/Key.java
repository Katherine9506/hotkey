package com.myproject.hotkey.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

public class Key {

    @Field
    private String id;
    @Field
    private String kw;
    private List<String> pinyin;
    private List<String> abbre;
    @Field
    private Integer kwfreq;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public List<String> getPinyin() {
        return pinyin;
    }

    public void setPinyin(List<String> pinyin) {
        this.pinyin = pinyin;
    }

    public List<String> getAbbre() {
        return abbre;
    }

    public void setAbbre(List<String> abbre) {
        this.abbre = abbre;
    }

    public Integer getKwfreq() {
        return kwfreq;
    }

    public void setKwfreq(Integer kwfreq) {
        this.kwfreq = kwfreq;
    }

    public Key() {
    }

    @Override
    public String toString() {
        return "Key{" +
                "id='" + id + '\'' +
                ", kw='" + kw + '\'' +
                ", pinyin=" + pinyin +
                ", abbre=" + abbre +
                ", kwfreq=" + kwfreq +
                '}';
    }
}
