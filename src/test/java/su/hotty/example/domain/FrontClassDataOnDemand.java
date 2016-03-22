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
public class FrontClassDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<FrontClass> data;
    
    public FrontClass getNewTransientFrontClass(int index) {
        FrontClass obj = new FrontClass();
        setName(obj, index);
        return obj;
    }
    
    public void setName(FrontClass obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public FrontClass getSpecificFrontClass(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        FrontClass obj = data.get(index);
        Long id = obj.getId();
        return FrontClass.findFrontClass(id);
    }
    
    public FrontClass getRandomFrontClass() {
        init();
        FrontClass obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return FrontClass.findFrontClass(id);
    }
    
    public boolean modifyFrontClass(FrontClass obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = FrontClass.findFrontClassEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'FrontClass' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<FrontClass>();
        for (int i = 0; i < 10; i++) {
            FrontClass obj = getNewTransientFrontClass(i);
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
