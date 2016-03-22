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
public class MenuItemDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<MenuItem> data;
    
    @Autowired
    MenuBlockDataOnDemand menuBlockDataOnDemand;
    
    public MenuItem getNewTransientMenuItem(int index) {
        MenuItem obj = new MenuItem();
        setLink(obj, index);
        setName(obj, index);
        setParentItem(obj, index);
        setPriority(obj, index);
        setToplevel(obj, index);
        return obj;
    }
    
    public void setLink(MenuItem obj, int index) {
        String link = "link_" + index;
        obj.setLink(link);
    }
    
    public void setName(MenuItem obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setParentItem(MenuItem obj, int index) {
        MenuItem parentItem = obj;
        obj.setParentItem(parentItem);
    }
    
    public void setPriority(MenuItem obj, int index) {
        int priority = index;
        obj.setPriority(priority);
    }
    
    public void setToplevel(MenuItem obj, int index) {
        Boolean toplevel = Boolean.TRUE;
        obj.setToplevel(toplevel);
    }
    
    public MenuItem getSpecificMenuItem(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MenuItem obj = data.get(index);
        Long id = obj.getId();
        return MenuItem.findMenuItem(id);
    }
    
    public MenuItem getRandomMenuItem() {
        init();
        MenuItem obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MenuItem.findMenuItem(id);
    }
    
    public boolean modifyMenuItem(MenuItem obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = MenuItem.findMenuItemEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MenuItem' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MenuItem>();
        for (int i = 0; i < 10; i++) {
            MenuItem obj = getNewTransientMenuItem(i);
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
