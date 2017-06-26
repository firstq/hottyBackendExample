package su.hotty.editor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.service.*;


@Component
public class CatalogDataCreator {

    private static final Logger log = LoggerFactory.getLogger(CatalogDataCreator.class);

    @Autowired
    private SocialNetTypeService socialNetTypeService;


    @Autowired
    private BlockService blockService;


    @Autowired
    private PageService pageService;


    @Autowired
    private SiteService siteService;


    public CatalogDataCreator() {
    }

    public void populateSocialNetTypes() {
        blockService.deleteAllBlocks();
        pageService.deleteAllPages();
        siteService.deleteAllSites();
        log.debug("Social Net Types populated.");
    }

}