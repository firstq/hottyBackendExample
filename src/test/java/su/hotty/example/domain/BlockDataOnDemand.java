package su.hotty.example.domain;
import org.springframework.beans.factory.annotation.Configurable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class BlockDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<Block> data;
    
    @Autowired
    PageDataOnDemand pageDataOnDemand;
    
    @Autowired
    StaticBlockDataOnDemand staticBlockDataOnDemand;
    
    public Block getNewTransientBlock(int index) {
        Block obj = new Block();
        setName(obj, index);
        setParent(obj, index);
        setStyles(obj, index);
        setTopLevel(obj, index);
        return obj;
    }
    
    public void setName(Block obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setParent(Block obj, int index) {
        Block parent = obj;
        obj.setParent(parent);
    }
    
    public void setStyles(Block obj, int index) {
        String styles = "styles_" + index;
        if (styles.length() > 900) {
            styles = styles.substring(0, 900);
        }
        obj.setStyles(styles);
    }
    
    public void setTopLevel(Block obj, int index) {
        Boolean topLevel = Boolean.TRUE;
        obj.setTopLevel(topLevel);
    }
    
    public Block getSpecificBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Block obj = data.get(index);
        Long id = obj.getId();
        return Block.findBlock(id);
    }
    
    public Block getRandomBlock() {
        init();
        Block obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Block.findBlock(id);
    }
    
    public boolean modifyBlock(Block obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = Block.findBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Block' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Block>();
        for (int i = 0; i < 10; i++) {
            Block obj = getNewTransientBlock(i);
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
