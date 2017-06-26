package su.hotty.editor.config;

import su.hotty.editor.config.API;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 */
@Configuration
public class WebStaticConfig extends WebMvcConfigurerAdapter {

    @Value("${resources.images.folder}")
    private String imageFolder;

    @Value("${resources.video.folder}")
    private String videoFolder;

    @Value("${resources.files.folder}")
    private String filesFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(API.APP + "/content/img/**").addResourceLocations("file:" + imageFolder);
        registry.addResourceHandler(API.APP + "/content/video/**").addResourceLocations("file:" + videoFolder);
        registry.addResourceHandler(API.APP + "/content/files/**").addResourceLocations("file:" + filesFolder);
        super.addResourceHandlers(registry);
    }

}