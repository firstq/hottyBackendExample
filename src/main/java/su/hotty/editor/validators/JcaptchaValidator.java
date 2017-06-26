package su.hotty.editor.validators;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import su.hotty.editor.domain.SendedMessage;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
public class JcaptchaValidator implements Validator {

    private static final String DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME = "j_captcha_response";

    @Autowired
    private DefaultManageableImageCaptchaService captchaService;

    @Override
    public boolean supports(Class<?> clazz) {
        return HashMap.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HashMap params = (HashMap) target;
        boolean isResponseCorrect = false;
        String sessionId = (String) params.get("sessionId");
        String compareValue = (String) params.get(DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME);

        try {
            if(compareValue != null){
                isResponseCorrect = captchaService.validateResponseForID(sessionId, compareValue);
            }
        } catch (CaptchaServiceException e) {
            errors.rejectValue("captcha", "Системная ошибка CaptchaServiceException");
        }
        if(!isResponseCorrect) errors.rejectValue("captcha", "Введённое значение не соответствует картинке");
    }

}