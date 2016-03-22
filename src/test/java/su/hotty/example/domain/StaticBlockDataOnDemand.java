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
public class StaticBlockDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<StaticBlock> data;
    
    public StaticBlock getNewTransientStaticBlock(int index) {
        StaticBlock obj = new StaticBlock();
		obj.setName("name_" + index);
        return obj;
    }
    
    public StaticBlock getSpecificStaticBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        StaticBlock obj = data.get(index);
        Long id = obj.getId();
        return StaticBlock.findStaticBlock(id);
    }
    
    public StaticBlock getRandomStaticBlock() {
        init();
        StaticBlock obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return StaticBlock.findStaticBlock(id);
    }
    
    public boolean modifyStaticBlock(StaticBlock obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = StaticBlock.findStaticBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'StaticBlock' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<StaticBlock>();
        for (int i = 0; i < 10; i++) {
            StaticBlock obj = getNewTransientStaticBlock(i);
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
