package su.hotty.editor.domain;
import org.springframework.beans.factory.annotation.Configurable;
import su.hotty.editor.domain.SocialNetDataOnDemand;
import java.security.SecureRandom;
import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import su.hotty.editor.domain.BlockDataOnDemand;
import su.hotty.editor.domain.SocialNet;
import su.hotty.editor.domain.SocialNetTypeDataOnDemand;
import su.hotty.editor.service.SocialNetService;

@Configurable
@Component
public class SocialNetDataOnDemand {

    private Random rnd = new SecureRandom();
    
    private List<SocialNet> data;
    
    @Autowired
    BlockDataOnDemand blockDataOnDemand;
    
    @Autowired
    SocialNetTypeDataOnDemand socialNetTypeDataOnDemand;
    
    @Autowired
    SocialNetService socialNetService;
    
    public SocialNet getNewTransientSocialNet(int index) {
        SocialNet obj = new SocialNet();
        obj.setId(UUID.randomUUID().toString());
        setAuthKey(obj, index);
        setClientComunity(obj, index);
        setClientNetIcon(obj, index);
        setClientPage(obj, index);
        setClientId(obj, index);
        setDateCreated(obj, index);
        setDateUpdated(obj, index);
        setNetName(obj, index);
        return obj;
    }
    
    public void setAuthKey(SocialNet obj, int index) {
        String authKey = "authKey_" + index;
        obj.setAuthKey(authKey);
    }
    
    public void setClientComunity(SocialNet obj, int index) {
        String clientComunity = "clientComunity_" + index;
        obj.setClientComunity(clientComunity);
    }
    
    public void setClientNetIcon(SocialNet obj, int index) {
        String clientNetIcon = "clientNetIcon_" + index;
        obj.setClientNetIcon(clientNetIcon);
    }
    
    public void setClientPage(SocialNet obj, int index) {
        String clientPage = "clientPage_" + index;
        obj.setClientPage(clientPage);
    }
    
    public void setClientId(SocialNet obj, int index) {
        Long clientId = new Integer(index).longValue();
        obj.setClientId(clientId);
    }
    
    public void setDateCreated(SocialNet obj, int index) {
        Date dateCreated = new Date();
        obj.setDateCreated(dateCreated);
    }
    
    public void setDateUpdated(SocialNet obj, int index) {
        Date dateUpdated = new Date();
        obj.setDateUpdated(dateUpdated);
    }
    
    public void setNetName(SocialNet obj, int index) {
        String netName = "netName_" + index;
        obj.setNetName(netName);
    }
    
    public SocialNet getSpecificSocialNet(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        SocialNet obj = data.get(index);
        String id = obj.getId();
        return socialNetService.findSocialNet(id);
    }
    
    public SocialNet getRandomSocialNet() {
        init();
        SocialNet obj = data.get(rnd.nextInt(data.size()));
        String id = obj.getId();
        return socialNetService.findSocialNet(id);
    }
    
    public boolean modifySocialNet(SocialNet obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = socialNetService.findSocialNetEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'SocialNet' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<SocialNet>();
        for (int i = 0; i < 10; i++) {
            SocialNet obj = getNewTransientSocialNet(i);
            try {
                socialNetService.saveSocialNet(obj);
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