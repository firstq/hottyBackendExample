package su.hotty.editor.domain;
import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.SiteDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.Site;
import su.hotty.editor.service.SiteService;

@Configurable
@Component
public class SiteDataOnDemand {
    private Random rnd = new SecureRandom();
    
    private List<Site> data;
    
    @Autowired
    SiteService siteService;
    
    public Site getNewTransientSite(int index) {
        Site obj = new Site();
        obj.setId(UUID.randomUUID().toString());
        setClientId(obj, index);
        setDateCreated(obj, index);
        setDateUpdated(obj, index);
        setIsExample(obj, index);
        setName(obj, index);
        setKeydomain(obj, index);
        setScripts(obj, index);
        return obj;
    }
    
    public void setClientId(Site obj, int index) {
        Long clientId = new Integer(index).longValue();
        obj.setClientId(clientId);
    }
    
    public void setDateCreated(Site obj, int index) {
        Date dateCreated = new Date();
        obj.setDateCreated(dateCreated);
    }
    
    public void setDateUpdated(Site obj, int index) {
        Date dateUpdated = new Date();
        obj.setDateUpdated(dateUpdated);
    }
    
    public void setIsExample(Site obj, int index) {
        Boolean isExample = Boolean.TRUE;
        obj.setIsExample(isExample);
    }
    
    public void setName(Site obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

    public void setKeydomain(Site obj, int index) {
        String keydomain = "keydomain_" + index;
        obj.setKeydomain(keydomain);
    }
    
    public void setScripts(Site obj, int index) {
        String scripts = "scripts_" + index;
        obj.setScripts(scripts);
    }
    
    public Site getSpecificSite(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Site obj = data.get(index);
        String id = obj.getId();
        return siteService.findSite(id);
    }
    
    public Site getRandomSite() {
        init();
        Site obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return siteService.findSite(id);
    }
    
    public boolean modifySite(Site obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = siteService.findSiteEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Site' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Site>();
        for (int i = 0; i < 10; i++) {
            Site obj = getNewTransientSite(i);
            try {
                siteService.saveSite(obj);
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