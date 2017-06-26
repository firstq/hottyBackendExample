package su.hotty.editor.domain;
import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.PageDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.Page;
import su.hotty.editor.domain.SiteDataOnDemand;
import su.hotty.editor.service.PageService;

@Configurable
@Component
public class PageDataOnDemand {
    private Random rnd = new SecureRandom();
    
    private List<Page> data;
    
    @Autowired
    SiteDataOnDemand siteDataOnDemand;
    
    @Autowired
    PageService pageService;
    
    public Page getNewTransientPage(int index) {
        Page obj = new Page();
        obj.setId(UUID.randomUUID().toString());
        setDateCreated(obj, index);
        setDateUpdated(obj, index);
        setFrontClasses(obj, index);
        setPageDescription(obj, index);
        setPagePath(obj, index);
        setPageTitle(obj, index);
        setStyles(obj, index);
        return obj;
    }
    
    public void setDateCreated(Page obj, int index) {
        Date dateCreated = new Date();
        obj.setDateCreated(dateCreated);
    }
    
    public void setDateUpdated(Page obj, int index) {
        Date dateUpdated = new Date();
        obj.setDateUpdated(dateUpdated);
    }
    
    public void setFrontClasses(Page obj, int index) {
        String frontClasses = "frontClasses_" + index;
        obj.setFrontClasses(frontClasses);
    }
    
    public void setPageDescription(Page obj, int index) {
        String pageDescription = "pageDescription_" + index;
        obj.setPageDescription(pageDescription);
    }
    
    public void setPagePath(Page obj, int index) {
        String pagePath = "pagePath_" + index;
        obj.setPagePath(pagePath);
    }
    
    public void setPageTitle(Page obj, int index) {
        String pageTitle = "pageTitle_" + index;
        obj.setPageTitle(pageTitle);
    }
    
    public void setStyles(Page obj, int index) {
        String styles = "styles_" + index;
        obj.setStyles(styles);
    }
    
    public Page getSpecificPage(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Page obj = data.get(index);
        String id = obj.getId();
        return pageService.findPage(id);
    }
    
    public Page getRandomPage() {
        init();
        Page obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return pageService.findPage(id);
    }
    
    public boolean modifyPage(Page obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = pageService.findPageEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Page' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Page>();
        for (int i = 0; i < 10; i++) {
            Page obj = getNewTransientPage(i);
            try {
                pageService.savePage(obj);
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