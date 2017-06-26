package su.hotty.editor.domain;
import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.DomainNameDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.DomainName;
import su.hotty.editor.domain.SiteDataOnDemand;
import su.hotty.editor.service.DomainNameService;

@Configurable
@Component
public class DomainNameDataOnDemand {
    private Random rnd = new SecureRandom();
    
    private List<DomainName> data;
    
    @Autowired
    SiteDataOnDemand siteDataOnDemand;
    
    @Autowired
    DomainNameService domainNameService;
    
    public DomainName getNewTransientDomainName(int index) {
        DomainName obj = new DomainName();
        obj.setId(UUID.randomUUID().toString());
        setClientId(obj, index);
        setDomainName(obj, index);
        return obj;
    }
    
    public void setClientId(DomainName obj, int index) {
        Long clientId = new Integer(index).longValue();
        obj.setClientId(clientId);
    }
    
    public void setDomainName(DomainName obj, int index) {
        String domainName = "domainName_" + index;
        obj.setDomainName(domainName);
    }
    
    public DomainName getSpecificDomainName(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        DomainName obj = data.get(index);
        String id = obj.getId();
        return domainNameService.findDomainName(id);
    }
    
    public DomainName getRandomDomainName() {
        init();
        DomainName obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return domainNameService.findDomainName(id);
    }
    
    public boolean modifyDomainName(DomainName obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = domainNameService.findDomainNameEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'DomainName' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<DomainName>();
        for (int i = 0; i < 10; i++) {
            DomainName obj = getNewTransientDomainName(i);
            try {
                domainNameService.saveDomainName(obj);
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