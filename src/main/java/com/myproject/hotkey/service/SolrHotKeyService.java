package com.myproject.hotkey.service;

import com.myproject.hotkey.pojo.Key;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface SolrHotKeyService {

    /**
     * 向索引库添加新的用户输入词项
     *
     * @return
     */
    boolean addNewIndex(String key, Date date) throws IOException, SolrServerException;

    /**
     * 根据前缀prefix查找相关keys
     *
     * @param prefix 前缀
     * @return keys集合
     */
    List<Key> getKeys(String prefix, Date date) throws IOException, SolrServerException;
}
