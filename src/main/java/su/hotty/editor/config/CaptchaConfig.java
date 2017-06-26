package su.hotty.editor.config;

import com.octo.captcha.service.CaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import com.octo.captcha.engine.image.gimpy.SimpleListImageCaptchaEngine;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import org.apache.commons.collections.FastHashMap;

@Configuration
public class CaptchaConfig {

    @Bean(value = "captchaEngine")
    public SimpleListImageCaptchaEngine getCaptchaEngine() {
        return new SimpleListImageCaptchaEngine();
    }

    @Bean(value = "captchaService")
    public DefaultManageableImageCaptchaService getCaptchaService() {
        return new DefaultManageableImageCaptchaService();
    }

}
