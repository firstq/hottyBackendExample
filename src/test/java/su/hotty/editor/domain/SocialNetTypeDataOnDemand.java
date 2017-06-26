package su.hotty.editor.domain;

import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.SocialNetTypeDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.SocialNetType;
import su.hotty.editor.service.SocialNetTypeService;

@Configurable
@Component
public class SocialNetTypeDataOnDemand {
    private Random rnd = new SecureRandom();
    
    private List<SocialNetType> data;
    
    @Autowired
    SocialNetTypeService socialNetTypeService;
    
    public SocialNetType getNewTransientSocialNetType(int index) {
        SocialNetType obj = new SocialNetType();
        obj.setId(UUID.randomUUID().toString());
        setDefaultIcon(obj, index);
        setDomainUrl(obj, index);
        setIsApproved(obj, index);
        setName(obj, index);
        return obj;
    }
    
    public void setDefaultIcon(SocialNetType obj, int index) {
        String defaultIcon = "defaultIcon_" + index;
        obj.setDefaultIcon(defaultIcon);
    }
    
    public void setDomainUrl(SocialNetType obj, int index) {
        String domainUrl = "domainUrl_" + index;
        obj.setDomainUrl(domainUrl);
    }
    
    public void setIsApproved(SocialNetType obj, int index) {
        Boolean isApproved = Boolean.TRUE;
        obj.setIsApproved(isApproved);
    }
    
    public void setName(SocialNetType obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public SocialNetType getSpecificSocialNetType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        SocialNetType obj = data.get(index);
        String id = obj.getId();
        return socialNetTypeService.findSocialNetType(id);
    }
    
    public SocialNetType getRandomSocialNetType() {
        init();
        SocialNetType obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return socialNetTypeService.findSocialNetType(id);
    }
    
    public boolean modifySocialNetType(SocialNetType obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = socialNetTypeService.findSocialNetTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'SocialNetType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<SocialNetType>();
        for (int i = 0; i < 10; i++) {
            SocialNetType obj = getNewTransientSocialNetType(i);
            try {
                socialNetTypeService.saveSocialNetType(obj);
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