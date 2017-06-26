package su.hotty.editor.web;

import org.springframework.beans.factory.annotation.Value;
import su.hotty.editor.config.API;
import su.hotty.editor.domain.SendedMessage;
import su.hotty.editor.domain.Block;
import su.hotty.editor.repository.SendedMessageRepository;
import su.hotty.editor.service.BlockService;
import su.hotty.editor.util.Translit;
import su.hotty.editor.validators.JcaptchaValidator;
import su.hotty.editor.validators.SendedMessageValidator;
import su.hotty.editor.validators.SendmailBlockValidator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping(API.PATH+"/sendmailblocks")
public class SendmailBlockController {

    @Autowired
    BlockService blockService;

    @Autowired
    SendedMessageRepository sendedMessageRepository;

    @Autowired
    org.springframework.mail.javamail.JavaMailSenderImpl mailSender;

    @Autowired
    SendedMessageValidator sendedMessageValidator;

    @Autowired
    JcaptchaValidator jcaptchaValidator;

    @Autowired
    SendmailBlockValidator sendmailBlockValidator;

    @Value("${resources.images.folder}")
    private String imageFolder;

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public ResponseEntity<String> send(MultipartHttpServletRequest request, HttpServletResponse response){

        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");
        JSONObject responseJson = new JSONObject();
        //Вытаскиваем всё из request
        String responseEmail = request.getParameter("responseEmail"),
                targetEmail = request.getParameter("targetEmail"),
                capcha = request.getParameter("j_captcha_response"),
                targetMessage = request.getParameter("targetMessage"),
                subject = request.getParameter("subject");
        String[] files = !request.getParameter("files").isEmpty() ? request.getParameter("files").split(",>") : new String[]{};
        Block parentBlock = blockService.findBlock(request.getParameter("parentBlock").toString());
        if (parentBlock != null) {
            SendedMessage sm = new SendedMessage();
            sm.setTheme(subject);
            sm.setEmail(responseEmail);
            sm.setCaptcha(capcha);
            sm.setTargetMessage(targetMessage);
            sm.setParent(request.getParameter("parentBlock").toString());

            //Валидируем
            BindException errors = new BindException(sm, "SendedMessage");
            sendedMessageValidator.validate(sm, errors);
            HashMap target = new HashMap();
            target.put("sessionId", request.getSession().getId());
            target.put("j_captcha_response", capcha);
            jcaptchaValidator.validate(target, errors);
            responseJson.put("hasErrors", errors.hasErrors());
            if(errors.hasErrors()){
                responseJson.put("errors", prepareValidationResult(errors));
                return new ResponseEntity<String>(responseJson.toString(), h, HttpStatus.OK);
            }

            try {
                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.smtp.starttls.enable", "true");
                mailSender.setJavaMailProperties(props);
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true); // Создание письма
                //helper.setFrom(responseEmail != null && !responseEmail.isEmpty() ? responseEmail : parentBlock.getSendFrom()); TODO: .getSpecialData.get('sendFrom')
                //helper.setTo(targetEmail != null && !targetEmail.isEmpty() ? targetEmail : parentBlock.getSendTo()); TODO: .getSpecialData.get('sendTo')
                helper.setSubject(subject);
                helper.setText(targetMessage);
                if(files.length>0)
                    for (String file : files) {
                        FileSystemResource addingFile = new FileSystemResource(imageFolder+request.getSession().getId()+"/"+Translit.toTranslit(file));
                        helper.addAttachment(file, addingFile); // Вложение
                    }
                mailSender.send(message);
            } catch (MessagingException ex) {
                return new ResponseEntity<>( h, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception exe) {
                exe.printStackTrace();
                return new ResponseEntity<>( h, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            sendedMessageRepository.save(sm);
        }
        return new ResponseEntity<>( "{}", h, HttpStatus.OK);
    }


    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<String> uploadfile(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");
        Iterator<String> itr =  request.getFileNames();
        MultipartFile uploadedFile = request.getFile(itr.next());
        JSONObject fileParams = new JSONObject();
        if (uploadedFile != null) {
            boolean isValid = uploadedFile.getOriginalFilename().toLowerCase().endsWith(".png") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".jpg") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".gif") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".ico") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".jpeg") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".html") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".txt") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".doc") ||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".pdf")||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".rtf")||
                    uploadedFile.getOriginalFilename().toLowerCase().endsWith(".zip");
            if(!isValid){
                fileParams.put("result", false);
                fileParams.put("message", "Данный тип файлов не разрешен для отправки!");
                return new ResponseEntity<String>(fileParams.toString(), h, HttpStatus.OK);
            }
            if(uploadedFile.getSize()>20000000){
                fileParams.put("result", false);
                fileParams.put("message", "Файл слишком большой для отправки по почте! Разрешено отправлять файлы размером не более 20Мб");
                return new ResponseEntity<String>(fileParams.toString(), h, HttpStatus.OK);
            }
            String originalName = uploadedFile.getOriginalFilename().replaceAll("[\\,/,<,>]", ""),
                    translitedFilename = Translit.toTranslit(originalName);
            File file = new File(imageFolder + request.getSession().getId() + "/" + translitedFilename);
            //file.mkdir();
            //file.mkdirs();
            try {
                uploadedFile.transferTo(file);
                try {
                    BufferedImage bufImage = ImageIO.read(file);
                    fileParams.put("filename", originalName);
                } catch (IOException ex) {
                    fileParams.put("result", false);
                    fileParams.put("getsize", true);
                    return new ResponseEntity<String>(fileParams.toString(), h, HttpStatus.OK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>(h, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        fileParams.put("result", true);
        return new ResponseEntity<String>(fileParams.toString(), h, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate")
    public ResponseEntity<String> validate(MultipartHttpServletRequest request, HttpServletResponse response){
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");
        JSONObject responseJson = new JSONObject();
        String from = request.getParameter("from"),
                to = request.getParameter("to");

        Block targetBlock = request.getParameter("blockId").isEmpty() ? new Block() : blockService.findBlock(
                request.getParameter("blockId").toString()
        );
        if(targetBlock == null) targetBlock = new Block();
       /* targetBlock.setSendTo(to);
        targetBlock.setSendFrom(from);
        */
        BindException errors = new BindException(targetBlock, "SendmailBlock");
        sendmailBlockValidator.validate(targetBlock, errors);
        responseJson.put("hasErrors", errors.hasErrors());
        responseJson.put("errors", prepareValidationResult(errors));
        return new ResponseEntity<>( responseJson.toString(), h, HttpStatus.OK);
    }

    private JSONObject prepareValidationResult(BindException errors){
        JSONObject validationErrors = new JSONObject();
        for(FieldError fe : errors.getFieldErrors()) if(!validationErrors.keySet().contains(fe.getField())) validationErrors.put(fe.getField(), fe.getCode());
        return validationErrors;
    }

}