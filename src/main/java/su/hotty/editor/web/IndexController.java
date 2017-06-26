package su.hotty.editor.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.hotty.editor.config.API;
import su.hotty.editor.domain.Page;
import su.hotty.editor.service.BlockService;
import su.hotty.editor.service.PageService;
import su.hotty.editor.util.FrontHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(API.APP)
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class.getSimpleName());

    private static final String REDIRECT_TO_INDEX_HTML = "redirect:/hotty/app/index.html";

    @Autowired
    PageService pageService;

    @Autowired
    BlockService blockService;

    @GetMapping()
    public String index(HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("hello","<div><ul><li>Test</li><li>Test</li><li>Test</li></ul></div>");
        Page page = pageService.findEagerPage("88D2A1BE-A2D7-4895-9F5B-CC4B74551CC9");
        if(page==null) return "404";
        String htmltree = FrontHelper.buildPage(page.getBlocks());
        Map<String,String> htmlPage = new HashMap<>();


        htmlPage.put("bodystyles", page.getStyles());
        htmlPage.put("bodyclasses", page.getFrontClasses());
        htmlPage.put("htmltree", htmltree);
        //pages.put(1, htmlPage);
        model.addAttribute("page", htmlPage);
        return "index";
        //return REDIRECT_TO_INDEX_HTML;
    }

    @GetMapping("/app")
    public String appIndex(Model model) {
        log.debug("appIndex redirect");
        return REDIRECT_TO_INDEX_HTML;
    }

    @RequestMapping("/app/login")
    public String login(Model model) {
        log.debug("login");
        return "login";
    }

    @RequestMapping("/app/logout")
    public String logout(Model model) {
        log.debug("logout");
        return "login";
    }

}