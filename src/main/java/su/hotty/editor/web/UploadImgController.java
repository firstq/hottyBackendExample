package su.hotty.editor.web;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.UUID;
import su.hotty.editor.config.API;


@Controller
public class UploadImgController {

    private static final Logger log = LoggerFactory.getLogger(UploadImgController.class.getSimpleName());

    @Value("${resources.images.folder}")
    private String imageFolder;

    @Value("${resources.images.web}")
    private String webFolder;

    @GetMapping(API.APP + "/app/size")
    public ResponseEntity<String> sizeImg(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");
        String filename = request.getParameter("filename");

        if (filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf('/'));
        }

        File file = new File(imageFolder + filename);

        JSONObject imageParams = new JSONObject();

        try {
            BufferedImage bufImage = ImageIO.read(new File(imageFolder + filename));
            imageParams.put("width", bufImage.getWidth() + "px");
            imageParams.put("height", bufImage.getHeight() + "px");
            imageParams.put("filename", filename);
            imageParams.put("url", webFolder + file.getName());
            //Имея Ш и В вычисляем пропорции и в случае несовпадения с 3х2 создаём сообщение
            int wxh = bufImage.getWidth() % bufImage.getHeight();
            log.debug("wxh=" + wxh); //Должен быть половина от Высоты
            if (wxh == 0 || (bufImage.getHeight() / wxh) != 2) {
                imageParams.put("message", "Данная картинка не соответствует формату 3х2 и в приложении ТехноФон будет отображаться не полностью!");
            } else imageParams.put("message", "");

        } catch (IOException ex) {
            imageParams.put("result", false);
            return new ResponseEntity<>(imageParams.toString(), h, HttpStatus.OK);
        } catch (Exception exep) {
            exep.printStackTrace();
        }
        imageParams.put("result", true);
        return new ResponseEntity<>(imageParams.toString(), h, HttpStatus.OK);
    }

    @PostMapping(API.APP + "/app/uploadimg")
    public ResponseEntity<String> uploadImg(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
        //TO DO: The field imageFile exceeds its maximum permitted size of 1048576 bytes.
        // Headers initialization
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/html;charset=UTF-8");

        Iterator<String> fileNames = request.getFileNames();
        MultipartFile image = request.getFile(fileNames.next());
        JSONObject imageParams = new JSONObject();
        if (image != null) {
            boolean isValidImage = image.getOriginalFilename().toLowerCase().endsWith(".png") ||
                    image.getOriginalFilename().toLowerCase().endsWith(".jpg") ||
                    image.getOriginalFilename().toLowerCase().endsWith(".gif") ||
                    image.getOriginalFilename().toLowerCase().endsWith(".ico") ||
                    image.getOriginalFilename().toLowerCase().endsWith(".jpeg") ||
                    image.getOriginalFilename().toLowerCase().endsWith(".svg");

            if (!isValidImage) {
                imageParams.put("result", false);
                return new ResponseEntity<>(imageParams.toString(), h, HttpStatus.OK);
            }

            String ext = com.google.common.io.Files.getFileExtension(image.getOriginalFilename());
            String filename = String.format("%s.%s", UUID.randomUUID().toString(), ext.toLowerCase());
            String fileFullPath = imageFolder + filename;

            File file = new File(fileFullPath);
            try {
                image.transferTo(file);
                //Путь к статике Апача, dateAdd и size
                imageParams.put("url", webFolder + file.getName());
                try {
                    BufferedImage bufImage = ImageIO.read(file);
                    //bufImage.
                    imageParams.put("width", bufImage.getWidth() + "px");
                    imageParams.put("height", bufImage.getHeight() + "px");
                    imageParams.put("filename", filename);

                } catch (IOException ex) {
                    imageParams.put("result", false);
                    imageParams.put("getsize", true);
                    return new ResponseEntity<>(imageParams.toString(), h, HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("Can't upload image.", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        imageParams.put("result", true);
        return new ResponseEntity<>(imageParams.toString(), h, HttpStatus.OK);
    }

    @PostMapping(API.APP + "/app/resize")
    public ResponseEntity<String> resizeImg(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String newWidth = request.getParameter("widthn").replaceAll("[^0-9]", "");
        String newHeight = request.getParameter("heightn").replaceAll("[^0-9]", "");
        String filename = request.getParameter("filename");

        if (filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf('/'));
        }

        if (newWidth.isEmpty() || newHeight.isEmpty() || filename.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        File file = new File(imageFolder + filename);

        JSONObject imageParams = new JSONObject();

        try {
            byte[] bytes = resize(file, Integer.valueOf(newWidth), Integer.valueOf(newHeight));
            IOUtils.write(bytes, new FileOutputStream(imageFolder + "resizable_" + filename));
            BufferedImage bufImage = ImageIO.read(new File(imageFolder + "resizable_" + filename));
            imageParams.put("width", bufImage.getWidth() + "px");
            imageParams.put("height", bufImage.getHeight() + "px");
            imageParams.put("filename", filename);
            imageParams.put("url", webFolder + "resizable_" + file.getName());
        } catch (IOException ex) {
            imageParams.put("result", false);
            //imageParams.put("getsize", true);
            return new ResponseEntity<>(imageParams.toString(), HttpStatus.OK);
        } catch (Exception exep) {
            exep.printStackTrace();
        }
        imageParams.put("result", true);
        return new ResponseEntity<>(imageParams.toString(), HttpStatus.OK);
    }

    public static byte[] resize(File file, int maxWidth, int maxHeight) throws IOException {
        BufferedImage img = ImageIO.read(file);
        Image resized = img.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
        BufferedImage buffered = new BufferedImage(maxWidth, maxHeight, Image.SCALE_REPLICATE);
        buffered.getGraphics().drawImage(resized, 0, 0, null);
        String formatName = getFormatName(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(buffered, formatName, out);
        return out.toByteArray();
    }

    private static String getFormatName(ImageInputStream iis) {
        try {
            Iterator imageReaders = ImageIO.getImageReaders(iis);
            if (!imageReaders.hasNext()) {
                return null;
            }

            ImageReader reader = (ImageReader) imageReaders.next();

            iis.close();

            return reader.getFormatName();
        } catch (IOException e) {
            log.error("Can't iterate thru image readers.");
            // do not rethrow?
        }
        return null;
    }

    private static String getFormatName(File file) throws IOException {
        return getFormatName(ImageIO.createImageInputStream(file));
    }

    private static String getFormatName(InputStream is) throws IOException {
        return getFormatName(ImageIO.createImageInputStream(is));
    }
}