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

@Configurable
@Component
public class MenuBlockDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<MenuBlock> data;
    
    public MenuBlock getNewTransientMenuBlock(int index) {
        MenuBlock obj = new MenuBlock();
		obj.setName("name_" + index);
        setIsVertical(obj, index);
        return obj;
    }
    
    public void setIsVertical(MenuBlock obj, int index) {
        Boolean isVertical = Boolean.TRUE;
        obj.setIsVertical(isVertical);
    }
    
    public MenuBlock getSpecificMenuBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MenuBlock obj = data.get(index);
        Long id = obj.getId();
        return MenuBlock.findMenuBlock(id);
    }
    
    public MenuBlock getRandomMenuBlock() {
        init();
        MenuBlock obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MenuBlock.findMenuBlock(id);
    }
    
    public boolean modifyMenuBlock(MenuBlock obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = MenuBlock.findMenuBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MenuBlock' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MenuBlock>();
        for (int i = 0; i < 10; i++) {
            MenuBlock obj = getNewTransientMenuBlock(i);
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
