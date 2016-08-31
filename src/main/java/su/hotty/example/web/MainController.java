package su.hotty.example.web;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import su.hotty.example.domain.*;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.apache.commons.io.IOUtils;
import su.hotty.example.util.Translit;

@Controller
public class MainController {
	
	private Map<Long,Block> dependSliderBlocks;
	private static Properties propDownload = new Properties();
	
	static Properties getPropDownload() throws FileNotFoundException, IOException {
		if(propDownload.isEmpty()){
			FileInputStream input = new FileInputStream("/opt/conf/download.properties");
			propDownload.load(input);
		}
		return propDownload;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "index";
	}
	
	private Block prepareBlock(JSONObject jsonBlock, Page page, Map<String,Block> clientIdRelation){
		Block block = null;
		if(jsonBlock.get("id") != null && !jsonBlock.get("id").toString().isEmpty()){
			block = findBlock(Long.valueOf(jsonBlock.get("id").toString()), jsonBlock.get("type").toString());
			if(block==null){
				if(jsonBlock.get("dependItem") != null){ 
					block = dependSliderBlocks.get(Long.valueOf(jsonBlock.get("id").toString()));
					if(block==null){
						block = createBlock(jsonBlock.get("type").toString());
						dependSliderBlocks.put(Long.valueOf(jsonBlock.get("id").toString()),block);
					}
				} else block = createBlock(jsonBlock.get("type").toString());
			}
		}
		
		block.setName(jsonBlock.get("name").toString());
		block.setIsDelete(jsonBlock.get("isDelete")==null ? false : Boolean.valueOf( jsonBlock.get("isDelete").toString()));
		block.setPage(block.getIsDelete() ? null : page);
		block.setTopLevel(Boolean.valueOf(jsonBlock.get("topLevel").toString()));
		if(!block.getTopLevel() || jsonBlock.get("parent")!=null) block.setParent(clientIdRelation.get(jsonBlock.get("parent").toString()));
		block.setStyles(jsonBlock.get("style").toString().replaceAll("[1-9]+px dashed", "0px dashed"));
		block.setFrontClases(jsonBlock.get("frontClases").toString());
		
		clientIdRelation.put(jsonBlock.get("id").toString(), block);
		
		switch(jsonBlock.get("type").toString()){
			case "AccordeonBlock":
				((AccordeonBlock)block).setHeightStyle(jsonBlock.get("heightStyle").toString());
				break;
			case "StaticBlock": 
				
				break;
			case "MenuBlock":
				((MenuBlock)block).setIsVertical((Boolean)jsonBlock.get("isVertical"));
				((MenuBlock)block).setItemStyles((String)jsonBlock.get("itemStyles").toString().replaceAll("[1-9]+px dashed", "0px dashed"));
				((MenuBlock)block).setLogo((String)jsonBlock.get("logo"));
				
				if(jsonBlock.get("items")!=null && jsonBlock.get("items") instanceof org.json.simple.JSONArray){
					JSONArray itemsJson = (JSONArray) jsonBlock.get("items");
					Map<String,su.hotty.example.domain.MenuItem> menuIdRelation = new HashMap<>(itemsJson.size());
					List<su.hotty.example.domain.MenuItem> itemsList = new LinkedList<>();
					for (Object item : itemsJson) {
						su.hotty.example.domain.MenuItem menuItem = null;
						JSONObject menuItemJson = (JSONObject)item;
						if(menuItemJson.get("id") != null && !menuItemJson.get("id").toString().isEmpty()){
							menuItem = su.hotty.example.domain.MenuItem.findMenuItem(Long.valueOf(menuItemJson.get("id").toString()));
							if(menuItem==null) 
								menuItem = new su.hotty.example.domain.MenuItem(
										menuItemJson.get("name").toString(),
										(Boolean) menuItemJson.get("toplevel"),
										menuItemJson.get("link").toString(),
										Integer.valueOf(menuItemJson.get("priority").toString()),
										null,
										(MenuBlock)block
									);
						}
						if(!menuItem.getToplevel()) menuItem.setParentItem(menuIdRelation.get(menuItemJson.get("id").toString()));
						menuIdRelation.put(menuItemJson.get("id").toString(), menuItem);
						if(menuItemJson.get("dependSliderBlock") != null){
							Block dependSliderBlock = findBlock(Long.valueOf(menuItemJson.get("dependSliderBlock").toString()), menuItemJson.get("dependSliderBlockType").toString());
							
							if(dependSliderBlock == null){ 
								dependSliderBlock = dependSliderBlocks.get(Long.valueOf(menuItemJson.get("dependSliderBlock").toString()));
								if(dependSliderBlock == null){
									dependSliderBlock = createBlock(menuItemJson.get("dependSliderBlockType").toString()); 
									dependSliderBlock.setName("DependBlockForItem"+menuItemJson.get("id").toString());
									dependSliderBlock.persist();
									dependSliderBlock.flush();
									
									dependSliderBlocks.put(Long.valueOf(menuItemJson.get("dependSliderBlock").toString()), dependSliderBlock);
								}
							}
							menuItem.setDependSliderBlock(dependSliderBlock.getId());
						}
						itemsList.add(menuItem);
						
					}
					((MenuBlock)block).setItems(itemsList);
				}
				break;
			case "SliderBlock":
				((SliderBlock)block).setSpecial((Boolean)jsonBlock.get("special"));
				((SliderBlock)block).setIsVertical((Boolean)jsonBlock.get("isVertical"));
				((SliderBlock)block).setIsArrowsShow((Boolean)jsonBlock.get("isArrowsShow"));
				((SliderBlock)block).setIsSliderShow((Boolean)jsonBlock.get("isSliderShow"));
				((SliderBlock)block).setIsMouseScrolled((Boolean)jsonBlock.get("isMouseScrolled"));
				
				((SliderBlock)block).setScrollContentStyle(jsonBlock.get("scrollContentStyle").toString());
				((SliderBlock)block).setScrollContentItemStyle(jsonBlock.get("scrollContentItemStyle").toString());
				break;
			case "SendmailBlock":
				((SendmailBlock)block).setCapchaEnable((Boolean)jsonBlock.get("capchaEnable"));
				((SendmailBlock)block).setSendTo(jsonBlock.get("sendTo").toString());
				((SendmailBlock)block).setSendFrom(jsonBlock.get("sendFrom").toString());
				break;
			case "TextBlock":
				((TextBlock)block).setText(jsonBlock.get("text").toString());
				break;
			case "VideoBlock":
				((VideoBlock)block).setVideoUrl(jsonBlock.get("videoUrl").toString());
				break;
			case "ImageBlock":
				((ImageBlock)block).setOriginalImgPath(jsonBlock.get("originalImgPath").toString());
				break;
		}
		
		return block;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> save (@RequestBody String json) throws IOException, ParseException{
		dependSliderBlocks = new TreeMap<>();
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject pageJson = (JSONObject)obj;
		List<Block> blockEtities = new ArrayList<>();
		
		Page page=null;
		if(pageJson.get("id") != null && !pageJson.get("id").toString().isEmpty()){
			page = Page.findPage(Long.valueOf(pageJson.get("id").toString()));
			if(page == null) page = new Page();
		}
		
		page.setTitle(pageJson.get("title").toString());
		page.setPagePath(pageJson.get("path").toString());
		page.setStyles(pageJson.get("style").toString().replaceAll("[1-9]+px dashed", "0px dashed"));
		page.setFrontClases(pageJson.get("frontClases").toString());

		if(pageJson.get("blocks")!=null && pageJson.get("blocks") instanceof org.json.simple.JSONArray){
			JSONArray blocks = (JSONArray) pageJson.get("blocks");
			Map<String,Block> clientIdRelation = new HashMap<>(blocks.size());
			for (Object block : blocks) {
				if(block instanceof JSONObject) blockEtities.add(prepareBlock((JSONObject) block, page, clientIdRelation));
			}
		}
		
		if(page.getId() != null){
			page.merge();
		} else page.persist();
		for (Block blockEntity : blockEtities) {
			if(blockEntity.getId() != null){
				blockEntity.merge();
			} else blockEntity.persist();
		}
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	private Block createBlock(String type) {
		switch(type){
			case "AccordeonBlock": return new AccordeonBlock();
			case "StaticBlock": return new StaticBlock();
			case "MenuBlock":  return new MenuBlock();
			case "SliderBlock": return new SliderBlock();
			case "TextBlock": return new TextBlock();
			case "VideoBlock": return new VideoBlock();
			case "ImageBlock": return new ImageBlock();
			case "SendmailBlock": return new SendmailBlock();
		}
		return new Block();
	}
	
	private Block findBlock(Long id, String type) {
		switch(type){
			case "AccordeonBlock": return AccordeonBlock.findAccordeonBlock(id);
			case "StaticBlock": return StaticBlock.findStaticBlock(id);
			case "MenuBlock":  return MenuBlock.findMenuBlock(id);
			case "SliderBlock": return SliderBlock.findSliderBlock(id);
			case "TextBlock": return TextBlock.findTextBlock(id);
			case "VideoBlock": return VideoBlock.findVideoBlock(id);
			case "ImageBlock": return ImageBlock.findImageBlock(id);
			case "SendmailBlock": return SendmailBlock.findSendmailBlock(id);
		}
		return Block.findBlock(id);
	}
	
	@RequestMapping(value = "/resize", method = RequestMethod.POST)
    public ResponseEntity<String> resizeimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject imageParams = new JSONObject();
		String newWidth = request.getParameter("widthn").replaceAll("[^0-9]", ""),
				newHeight = request.getParameter("heightn").replaceAll("[^0-9]", ""),
				filename = request.getParameter("filename");
		
		if(newWidth.isEmpty() || newHeight.isEmpty() || filename.isEmpty()) return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		File file = new File(MainController.getPropDownload().getProperty("webserver.htdocs") + filename);
		try {
			byte[] bytes = resize(file, Integer.valueOf(newWidth), Integer.valueOf(newHeight));
			IOUtils.write(bytes, new FileOutputStream( MainController.getPropDownload().getProperty("webserver.htdocs") + "resizable_"+filename) );
			BufferedImage bufImage = ImageIO.read(new File(MainController.getPropDownload().getProperty("webserver.htdocs") + "resizable_"+filename));
			imageParams.put("width", bufImage.getWidth()+"px");
			imageParams.put("height", bufImage.getHeight()+"px");
			imageParams.put("filename", filename);
			imageParams.put("url","http://" + InetAddress.getLocalHost().getHostAddress() + "/files/" + "resizable_"+file.getName());
		} catch (IOException ex) {
			imageParams.put("result", false);
			return new ResponseEntity<String>(imageParams.toJSONString(), HttpStatus.OK);
		} catch (Exception exep) {
			exep.printStackTrace();
		}
		imageParams.put("result", true);
		return new ResponseEntity<String>(imageParams.toJSONString(), HttpStatus.OK);
	}
	
	public static byte[] resize(File file, int maxWidth, int maxHeight) throws IOException{
		BufferedImage img = ImageIO.read( file );
		Image resized =  img.getScaledInstance( maxWidth, maxHeight, Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(maxWidth, maxHeight, Image.SCALE_REPLICATE);
		buffered.getGraphics().drawImage(resized, 0, 0 , null);
		String formatName = getFormatName( file ) ;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(buffered,formatName,out);
		return out.toByteArray();
	}

	private static String getFormatName(ImageInputStream imageInputStream) {
		try { 
			Iterator iter = ImageIO.getImageReaders(imageInputStream);
			if (!iter.hasNext()) {
				return null;
			}
			ImageReader reader = (ImageReader)iter.next();
			imageInputStream.close();
			return reader.getFormatName();
		} catch (IOException e) {
		}

		return null;
	}

	private static String getFormatName(File file) throws IOException {
		return getFormatName( ImageIO.createImageInputStream(file) );
	}

	private static String getFormatName(InputStream is) throws IOException {
		return getFormatName( ImageIO.createImageInputStream(is) );
	}

    @RequestMapping(value = "/uploading", method = RequestMethod.POST)
    public ResponseEntity<String> uploading(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		Iterator<String> itr =  request.getFileNames();
		MultipartFile image = request.getFile(itr.next());
		JSONObject imageParams = new JSONObject();
		if (image != null) {
            boolean isValidImage = image.getOriginalFilename().toLowerCase().endsWith(".png") ||
					image.getOriginalFilename().toLowerCase().endsWith(".jpg") ||
					image.getOriginalFilename().toLowerCase().endsWith(".gif") ||
					image.getOriginalFilename().toLowerCase().endsWith(".ico") || 
					image.getOriginalFilename().toLowerCase().endsWith(".jpeg");
			if(!isValidImage){
				imageParams.put("result", false);
				return new ResponseEntity<String>(imageParams.toJSONString(), HttpStatus.OK);
			}
			String translitedFilename = Translit.toTranslit(image.getOriginalFilename());
            File file = new File(MainController.getPropDownload().getProperty("webserver.htdocs") + translitedFilename);
            try {
                image.transferTo(file);
                //Путь к статике Апача, dateAdd и size
                imageParams.put("url","http://" + InetAddress.getLocalHost().getHostAddress() + "/files/" + file.getName());
				try {
					BufferedImage bufImage = ImageIO.read(file);
					imageParams.put("width", bufImage.getWidth()+"px");
					imageParams.put("height", bufImage.getHeight()+"px");
					imageParams.put("filename", translitedFilename);
				} catch (IOException ex) {
					imageParams.put("result", false);
					imageParams.put("getsize", true);
					return new ResponseEntity<String>(imageParams.toJSONString(), HttpStatus.OK);
				}
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
		imageParams.put("result", true);
		return new ResponseEntity<String>(imageParams.toJSONString(), HttpStatus.OK);
    }
}
