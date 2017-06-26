package su.hotty.editor.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import su.hotty.editor.config.API;
import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.Page;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import su.hotty.editor.service.BlockService;
import su.hotty.editor.service.PageService;
import su.hotty.editor.web.PageController;
import org.json.*;


@Controller
@RequestMapping(API.PATH+"/pages")
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    @Autowired
    PageService pageService;

    @Autowired
    BlockService blockService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") String id) {
        Page page = pageService.findPage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (page == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(page.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Page> result = pageService.findAllPages();
        return new ResponseEntity<String>(Page.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        JSONObject pageJson = new JSONObject(json);
        List<Block> blockEtities = new ArrayList<>();
        Page page=null;
        if(pageJson.get("id") != null && !pageJson.get("id").toString().isEmpty()){
            //Try to find
            page = pageService.findPage(pageJson.get("id").toString());
            if(page == null) page = new Page();
            page.setId(pageJson.get("id").toString());
        }
        page.setPageTitle(pageJson.get("pageTitle").toString());
        page.setPagePath(pageJson.get("pagePath").toString());
        page.setStyles(pageJson.get("styles").toString().replaceAll("[1-9]+px dashed", "0px dashed"));
        page.setFrontClasses(pageJson.get("frontClasses").toString());
        page.setSiteId(pageJson.get("siteId").toString());

        //TODO: Blocks TO DO фиксировать создание нового блока!!!
        if(pageJson.get("blocks")!=null && pageJson.get("blocks") instanceof org.json.JSONArray){
            JSONArray blocks = (JSONArray) pageJson.get("blocks");
            Map<String,Block> clientIdRelation = new HashMap<>(blocks.toList().size());
            for (Object jBlock : blocks) {
                //Prepare block entities
                if(jBlock instanceof JSONObject){
                    JSONObject jsonBlock = (JSONObject) jBlock;
                    Block block =  prepareBlock(jsonBlock, page, clientIdRelation);
                    blockEtities.add(block);
                    /*
                    switch(jsonBlock.get("blockType").toString()){
                        case "MenuBlock":
                            JSONObject jsonMenuBlockSpecialData = (JSONObject) jsonBlock.get("specialData");
                            //JSONArray items = (JSONArray) pageJson.get("blocks");
                            if(jsonMenuBlockSpecialData.toMap().get("items")!=null && jsonMenuBlockSpecialData.toMap().get("items") instanceof org.json.JSONArray) {
                                //TODO: Удаление item
                                JSONArray itemsJson = (JSONArray) jsonMenuBlockSpecialData.toMap().get("items");
                                log.info("itemsJson="+itemsJson);
                                //TODO: Вызывать prepareBlock( для каждого item
                                for (Object item : itemsJson) {
                                    JSONObject menuitemBlockJson = (JSONObject) item;
                                    Block menuitemBlock = prepareBlock(menuitemBlockJson, page, clientIdRelation);
                                    blockEtities.add(menuitemBlock);
                                }
                            }
                        break;
                    }
                    */
                }
            }
        }
        pageService.savePage(page, blockEtities);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Page page: Page.fromJsonArrayToPages(json)) {
            pageService.savePage(page);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Page page = Page.fromJsonToPage(json);
        page.setId(id);
        if (pageService.updatePage(page) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") String id) {
        Page page = pageService.findPage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (page == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        pageService.deletePage(page);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

//????
    private Block prepareBlock(JSONObject jsonBlock, Page page, Map<String,Block> clientIdRelation){
        //id генерить фейковые на клиенте и по ним разпознавать родителей
        Block block = null;
        if(jsonBlock.get("id") != null && !jsonBlock.get("id").toString().isEmpty()){
            block = blockService.findBlock(jsonBlock.get("id").toString());
            if(block==null){
                    block = new Block();
                    block.setId(jsonBlock.get("id").toString());
            }
        } else {
            block = new Block();
            jsonBlock.put("id",UUID.randomUUID().toString());
            block.setId(jsonBlock.get("id").toString());
        }

        block.setName(jsonBlock.get("name").toString());
        block.setIsDelete(jsonBlock.toMap().get("isDelete")==null ? false : Boolean.valueOf( jsonBlock.get("isDelete").toString()));
        block.setIsStatic(jsonBlock.get("isStatic")==null ? false : Boolean.valueOf( jsonBlock.get("isStatic").toString()));
        block.setPageId(block.getIsDelete() ? null : page.getId());
        block.setIsTop(Boolean.valueOf(jsonBlock.get("isTop").toString()));
        if(!block.getIsTop() || jsonBlock.get("parentId")!=null) block.setParentId(jsonBlock.get("parentId").toString());
        block.setStyles(jsonBlock.get("style").toString().replaceAll("[1-9]+px dashed", "0px dashed"));
        block.setFrontClasses(jsonBlock.get("frontClasses").toString());
        block.setTagType(jsonBlock.get("tagType").toString());
        block.setBlockType(jsonBlock.get("blockType").toString());
        clientIdRelation.put(jsonBlock.get("id").toString(), block);
        block.setSpecialData((jsonBlock.toMap().get("specialData") == null ? null : ((JSONObject)jsonBlock.get("specialData")).toMap()));
        return block;
    }
    
}