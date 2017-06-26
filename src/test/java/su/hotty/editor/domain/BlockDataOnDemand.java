package su.hotty.editor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.BlockDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.PageDataOnDemand;
import su.hotty.editor.service.BlockService;

@Configurable
@Component
public class BlockDataOnDemand {
    private Random rnd = new SecureRandom();
    
    private List<Block> data;
    
    @Autowired
    PageDataOnDemand pageDataOnDemand;
    
    @Autowired
    BlockService blockService;
    
    public Block getNewTransientBlock(int index) {
        Block obj = new Block();
        obj.setId(UUID.randomUUID().toString());

        obj.getSpecialData().put("text","Text "+index);
        obj.getSpecialData().put("boolValue", (index % 2)>0);
        obj.getSpecialData().put("intValue", index);
        obj.getSpecialData().put("listValue", obj.getSpecialData().keySet());

        setBlockType(obj, index);
        setDateCreated(obj, index);
        setDateUpdated(obj, index);
        setFrontClasses(obj, index);
        setIsStatic(obj, index);
        setIsTop(obj, index);
        setName(obj, index);
        setParent(obj, index);
        setStyles(obj, index);
        setTagType(obj, index);
        return obj;
    }
    
    public void setBlockType(Block obj, int index) {
        String blockType = "blockType_" + index;
        obj.setBlockType(blockType);
    }
    
    public void setDateCreated(Block obj, int index) {
        Date dateCreated = new Date();
        obj.setDateCreated(dateCreated);
    }
    
    public void setDateUpdated(Block obj, int index) {
        Date dateUpdated = new Date();
        obj.setDateUpdated(dateUpdated);
    }
    
    public void setFrontClasses(Block obj, int index) {
        String frontClasses = "frontClasses_" + index;
        obj.setFrontClasses(frontClasses);
    }
    
    public void setIsStatic(Block obj, int index) {
        Boolean isStatic = Boolean.TRUE;
        obj.setIsStatic(isStatic);
    }
    
    public void setIsTop(Block obj, int index) {
        Boolean isTop = Boolean.TRUE;
        obj.setIsTop(isTop);
    }
    
    public void setName(Block obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setParent(Block obj, int index) {
        Block parent = obj;
  //      obj.setParent(parent);
    }
    
    public void setStyles(Block obj, int index) {
        String styles = "styles_" + index;
        obj.setStyles(styles);
    }
    
    public void setTagType(Block obj, int index) {
        String tagType = "tagType_" + index;
        obj.setTagType(tagType);
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
        String id = obj.getId();
        return blockService.findBlock(id);
    }
    
    public Block getRandomBlock() {
        init();
        Block obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return blockService.findBlock(id);
    }
    
    public boolean modifyBlock(Block obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = blockService.findBlockEntries(from, to);
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
                blockService.saveBlock(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            data.add(obj);
        }
    }
    
}