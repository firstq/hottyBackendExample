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
public class SliderBlockDataOnDemand {

    private Random rnd = new SecureRandom();
    
    private List<SliderBlock> data;
    
    public SliderBlock getNewTransientSliderBlock(int index) {
        SliderBlock obj = new SliderBlock();
		obj.setName("name_" + index);
        setIsArrowsShow(obj, index);
        setIsMouseScrolled(obj, index);
        setIsSliderShow(obj, index);
        setIsVertical(obj, index);
        return obj;
    }
    
    public void setIsArrowsShow(SliderBlock obj, int index) {
        Boolean isArrowsShow = Boolean.TRUE;
        obj.setIsArrowsShow(isArrowsShow);
    }
    
    public void setIsMouseScrolled(SliderBlock obj, int index) {
        Boolean isMouseScrolled = Boolean.TRUE;
        obj.setIsMouseScrolled(isMouseScrolled);
    }
    
    public void setIsSliderShow(SliderBlock obj, int index) {
        Boolean isSliderShow = Boolean.TRUE;
        obj.setIsSliderShow(isSliderShow);
    }
    
    public void setIsVertical(SliderBlock obj, int index) {
        Boolean isVertical = Boolean.TRUE;
        obj.setIsVertical(isVertical);
    }
    
    public SliderBlock getSpecificSliderBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        SliderBlock obj = data.get(index);
        Long id = obj.getId();
        return SliderBlock.findSliderBlock(id);
    }
    
    public SliderBlock getRandomSliderBlock() {
        init();
        SliderBlock obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return SliderBlock.findSliderBlock(id);
    }
    
    public boolean modifySliderBlock(SliderBlock obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = SliderBlock.findSliderBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'SliderBlock' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<SliderBlock>();
        for (int i = 0; i < 10; i++) {
            SliderBlock obj = getNewTransientSliderBlock(i);
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
