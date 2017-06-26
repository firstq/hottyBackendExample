package su.hotty.editor.web;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.multitype.MultiTypeCaptchaService;
import su.hotty.editor.config.API;
import su.hotty.editor.validators.SendedMessageValidator;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(API.PATH+"/captcha")
public class CaptchaController {
    public static final String CAPTCHA_IMAGE_FORMAT = "jpeg";
    private static final String DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME = "j_captcha_response";

    @Autowired
    private DefaultManageableImageCaptchaService captchaService;


    @Autowired
    SendedMessageValidator sendedMessageValidator;


    @Autowired private ApplicationContext applicationContext;
    //private MultiTypeCaptchaService captchaService;

    @RequestMapping("/captcha.html")
    public void showForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // get the session id that will identify the generated captcha.
            // the same id must be used to validate the response, the session id is a good candidate!

            String captchaId = request.getSession().getId();
            BufferedImage challenge = captchaService.getImageChallengeForID(captchaId, request.getLocale());

            ImageIO.write(challenge, CAPTCHA_IMAGE_FORMAT, jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/"+CAPTCHA_IMAGE_FORMAT);

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

	/*
	EXAMPLE!!
	*/
//	@RequestMapping(method = RequestMethod.POST)
//	public String processForm(@ModelAttribute(FORM_NAME) RegistrationForm form,BindingResult result, SessionStatus status, HttpServletRequest request) {
//		validator.validate(form, result);
//		validateCaptcha(form, result, request.getSession().getId(), "registration.captcha");
//		if (result.hasErrors()) {
//			return "captcha_problem";
//		}
//	}


    protected void validateCaptcha(/*RegistrationForm registrationForm, */MultipartHttpServletRequest request, BindingResult result, String sessionId, String errorCode) {
        // If the captcha field is already rejected
        if (null != result.getFieldError("captcha")) return;
        boolean validCaptcha = false;
        try {
            Iterator<String> itr =  request.getFileNames();
            MultipartFile image = request.getFile(itr.next());
            validCaptcha = captchaService.validateResponseForID(sessionId, request.getParameter("captcha"));
        }
        catch (CaptchaServiceException e) {
            //should not happen, may be thrown if the id is not valid
            //logger.warn("validateCaptcha()", e);
        }
        if (!validCaptcha) {
            result.rejectValue("captcha", errorCode); //Добавление в список ошибок валидации ошибку капчи
        }
    }

    public static void validateCaptcha(HttpServletRequest request, BindException errors, DefaultManageableImageCaptchaService captchaService){

        boolean isResponseCorrect = false;
        String captchaId = request.getSession().getId();
        String response = request.getParameter(DEFAULT_CAPTCHA_RESPONSE_PARAMETER_NAME);

        try {
            if(response != null){
                isResponseCorrect = captchaService.validateResponseForID(captchaId, response);
            }
        } catch (CaptchaServiceException e) {

        }
        if(!isResponseCorrect){
            String objectName = "Captcha";
            String[] codes = {"invalid"};
            Object[] arguments = {};
            String defaultMessage = "Invalid image test entered!";
            ObjectError oe = new ObjectError(objectName, codes, arguments, defaultMessage);
            errors.addError(oe);
        }
    }
}