package com.myproject.hotkey.service.impl;

import com.myproject.hotkey.pojo.Key;
import com.myproject.hotkey.service.SolrHotKeyService;
import com.myproject.hotkey.util.PinyinUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SolrHotKeyServiceImpl implements SolrHotKeyService {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private SimpleDateFormat sdf;

    /**
     * 向索引库添加新的用户输入词项
     *
     * @return
     */
    @Override
    public boolean addNewIndex(String key, Date date) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        String id = sdf.format(date);
        Map<String, Integer> map = new HashMap<>();
        map.put("inc", 1);
        String pingYin = PinyinUtils.getPingYin(key);
        String alpha = PinyinUtils.getAlpha(key);

        doc.addField("id", id);
        doc.addField("kw", key);
        doc.addField("kwfreq", map);
        doc.addField("abbre", alpha);
        doc.addField("pinyin", pingYin);
        solrClient.add(doc);
        solrClient.commit();

        return true;
    }

    /**
     * 根据前缀prefix查找相关keys,查找后检查是否存在，若存在，则将kwfreq增1；若不存在，则添加搜索词项。
     *
     * @param prefix 前缀
     * @return keys集合
     */
    @Override
    public List<Key> getKeys(String prefix, Date date) throws IOException, SolrServerException {
        SolrQuery solrQuery = null;
        QueryResponse response = null;
        List<Key> keys = null;
        if (prefix.trim().equals("")) {
            solrQuery = getSuggestQuery(5);
            response = solrClient.query(solrQuery, SolrRequest.METHOD.POST);
            keys = response.getBeans(Key.class);
        } else {
            /* 前缀查询 */
            solrQuery = getPrefixQuery(prefix, 5);
            response = solrClient.query(solrQuery, SolrRequest.METHOD.POST);
            keys = response.getBeans(Key.class);
            if (keys.size() < 5) {
                Map<String, Key> keyMap = list2Map(keys);
                solrQuery = getPrefixQuery("*" + prefix, 5);
                response = solrClient.query(solrQuery);
                List<Key> keys1 = response.getBeans(Key.class);
                for (int i = 0; i < keys1.size(); i++) {
                    if (!keyMap.containsKey(keys1.get(i).getKw())) {
                        keys.add(keys1.get(i));
                    }
                }
            }

        }

        updateIndex(prefix, date);
        return keys;
    }

    public Map<String, Key> list2Map(List<Key> keyList) {
        Map<String, Key> keymap = new HashMap<>();
        for (Key key : keyList) {
            keymap.put(key.getKw(), key);
        }
        return keymap;
    }


    /**
     * 当用户输入搜索词项时，更新索引库中的数据，有则将kwfreq增1，无则添加新搜索词
     *
     * @param key 用户输入的搜索词项
     * @return
     */
    private boolean updateIndex(String key, Date date) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(key);
        QueryResponse response = solrClient.query(solrQuery);
        List<Key> keyList = response.getBeans(Key.class);
        if (keyList.size() < 1) {
            addNewIndex(key, date);
        } else {
            System.out.println(keyList.size());
            String id = keyList.get(0).getId();
            SolrInputDocument doc = new SolrInputDocument();
            Map<String, Integer> map = new HashMap<>();
            map.put("inc", 1);

            doc.addField("id", id);
            doc.addField("kwfreq", map);
            solrClient.add(doc);
            solrClient.commit();
        }
        return true;
    }


    /**
     * 前缀查询构造
     *
     * @param prefix 前缀
     * @param limit  返回的提示词项数量
     * @return
     */
    private SolrQuery getPrefixQuery(String prefix, Integer limit) {
        SolrQuery solrQuery = new SolrQuery();
        //q=prefix*
        solrQuery.setQuery(prefix + "*");
        //设置回显数据
        solrQuery.addField("id");
        solrQuery.addField("kw");
        solrQuery.addField("kwfreq");
        solrQuery.addSort("kwfreq", SolrQuery.ORDER.desc);
        solrQuery.setStart(0);
        solrQuery.setRows(limit);
        return solrQuery;
    }

    private SolrQuery getSuggestQuery(Integer limit) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("*:*");
        //设置回显数据
        solrQuery.addField("id");
        solrQuery.addField("kw");
        solrQuery.addField("kwfreq");
        solrQuery.addSort("kwfreq", SolrQuery.ORDER.desc);
        solrQuery.setStart(0);
        solrQuery.setRows(limit);
        return solrQuery;

    }
}



















