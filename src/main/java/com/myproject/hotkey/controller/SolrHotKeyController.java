package com.myproject.hotkey.controller;

import com.myproject.hotkey.pojo.Key;
import com.myproject.hotkey.service.SolrHotKeyService;
import net.sf.json.JSONArray;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/hotkey")
public class SolrHotKeyController {
    @Autowired
    private SolrHotKeyService hotKeyService;

    //向hotkeys core添加用户检索词
    //必须传入key
    @PostMapping(value = "/add")
    public String add(@RequestParam(value = "key", required = true) String key) {
        try {
            hotKeyService.addNewIndex(key, new Date());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据前缀
    // 取出hotkeys core中与用户检索有关的热词
    // 默认空串，空串取出最热的搜索词
    @RequestMapping(value = "/getKeys", method = RequestMethod.POST)
    public String getKeys(@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix) {
        List<Key> keys = null;
        try {
            keys = hotKeyService.getKeys(prefix, new Date());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = JSONArray.fromObject(keys);
        return jsonArray.toString();
    }

    @PostMapping(value = "/getKey")
    public String getKeys(@ModelAttribute Key key, Model model) {
        String keys = getKeys(key.getKw());
        model.addAttribute("keys", keys);
        return keys;
    }

}