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
public class TextBlockDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<TextBlock> data;
    
    public TextBlock getNewTransientTextBlock(int index) {
        TextBlock obj = new TextBlock();
		obj.setName("name_" + index);
        setStyles(obj, index);
        return obj;
    }
    
    public void setStyles(TextBlock obj, int index) {
        String styles = "styles_" + index;
        if (styles.length() > 2000) {
            styles = styles.substring(0, 2000);
        }
        obj.setStyles(styles);
    }
    
    public TextBlock getSpecificTextBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        TextBlock obj = data.get(index);
        Long id = obj.getId();
        return TextBlock.findTextBlock(id);
    }
    
    public TextBlock getRandomTextBlock() {
        init();
        TextBlock obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return TextBlock.findTextBlock(id);
    }
    
    public boolean modifyTextBlock(TextBlock obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = TextBlock.findTextBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'TextBlock' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<TextBlock>();
        for (int i = 0; i < 10; i++) {
            TextBlock obj = getNewTransientTextBlock(i);
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
