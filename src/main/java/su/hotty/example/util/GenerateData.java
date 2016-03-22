package su.hotty.example.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.example.domain.Block;
import su.hotty.example.domain.ImageBlock;
import su.hotty.example.domain.MenuBlock;
import su.hotty.example.domain.MenuItem;
import su.hotty.example.domain.SliderBlock;
import su.hotty.example.domain.StaticBlock;
import su.hotty.example.domain.TextBlock;
import su.hotty.example.domain.VideoBlock;

@Configurable
@Transactional
public class GenerateData {
	
	private static List<? extends Block> data;
	
	static void createContent(){
		List<Block> createdBlocks = new ArrayList<>();
		List<MenuItem> items = new ArrayList<MenuItem>();
        for (int i = 0; i < 10; i++) {
			items.clear();
			//TO DO: Add diferent blocks
            Block obj;
			switch (i) {
				case 1: obj = new ImageBlock(); 
						((ImageBlock)obj).setIsClickable(Boolean.TRUE);
						break;
				case 2: obj = new VideoBlock(); 
						((VideoBlock)obj).setVideoUrl("https://youtu.be/fzWSBaAYtWs");
						break;
				case 3: obj = new TextBlock(); break;
				case 4: obj = new StaticBlock(); break;
				case 5: obj = new MenuBlock(); 
						MenuItem mainitem = new MenuItem( 0, "menu_item", Boolean.TRUE, "page", 100500, null, (MenuBlock)obj);
						items.add(mainitem);
						for (int j = 0; j < 10; j++) {
							items.add(new MenuItem( 0, "menu_item_"+j, Boolean.FALSE, "page"+j, j, null, (MenuBlock)obj));
						}
						
						break;
				case 6: obj = new SliderBlock(); break;
				default: obj = new Block(); break;
			}
			
			
			obj.setName("name_"+i);
			obj.setStyles("position: absolute;");
			obj.setTopLevel(true);
			
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
			System.out.println("BEFORE ITEMS FOREACH");
			MenuItem saveditem=null;
			for (MenuItem item : items) {
				System.out.println("item="+item);
				item.setParentItem(saveditem);
				item.persist();
				item.flush();
				saveditem = item;
			}
            createdBlocks.add(obj);
        }
		
		data = createdBlocks;
	}
	
	public static void main(String [] args){
		// Injecting dependencies into application
        ConfigurableApplicationContext applicationContext = 
                new ClassPathXmlApplicationContext("classpath*:/META-INF/spring/applicationContext*.xml");
        applicationContext.registerShutdownHook();
		if(data == null || data.isEmpty()) createContent();
	}
	
}
