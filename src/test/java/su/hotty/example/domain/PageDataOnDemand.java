package su.hotty.example.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

@Configurable
@Component
public class PageDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<Page> data;
    
    public Page getNewTransientPage(int index) {
        Page obj = new Page();
        setDescription(obj, index);
        setPagePath(obj, index);
        setStyles(obj, index);
        setTitle(obj, index);
        return obj;
    }
    
    public void setDescription(Page obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void setPagePath(Page obj, int index) {
        String pagePath = "pagePath_" + index;
        obj.setPagePath(pagePath);
    }
    
    public void setStyles(Page obj, int index) {
        String styles = "styles_" + index;
        if (styles.length() > 900) {
            styles = styles.substring(0, 900);
        }
        obj.setStyles(styles);
    }
    
    public void setTitle(Page obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }
    
    public Page getSpecificPage(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Page obj = data.get(index);
        Long id = obj.getId();
        return Page.findPage(id);
    }
    
    public Page getRandomPage() {
        init();
        Page obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Page.findPage(id);
    }
    
    public boolean modifyPage(Page obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = Page.findPageEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Page' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Page>();
        for (int i = 0; i < 10; i++) {
            Page obj = getNewTransientPage(i);
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
            data.add(obj);
        }
    }
 
}
