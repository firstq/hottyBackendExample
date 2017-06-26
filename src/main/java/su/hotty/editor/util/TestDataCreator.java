package su.hotty.editor.util;

import su.hotty.editor.domain.*;
import su.hotty.editor.repository.*;
import su.hotty.editor.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import su.hotty.editor.service.SocialNetService;
import su.hotty.editor.service.SocialNetTypeService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataCreator {

    private static final Logger log = LoggerFactory.getLogger(TestDataCreator.class);

    @Autowired
    private SocialNetService socialNetService;

    @Value("${resources.images.folder}")
    private String imageFolder;

    @Value("${rss.url}")
    private String rssUrl;

    public void populateSocialNet() {

        log.debug("SocialNet populated.");
    }

}
