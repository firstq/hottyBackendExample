package su.hotty.example.domain;
import org.springframework.beans.factory.annotation.Configurable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class AdminClientDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<AdminClient> data;
    
    public AdminClient getNewTransientAdminClient(int index) {
        AdminClient obj = new AdminClient();
        setDateCreate(obj, index);
        setDateLastVisit(obj, index);
        setDateUpdate(obj, index);
        setEmail(obj, index);
        setEnabled(obj, index);
        setFathername(obj, index);
        setLastname(obj, index);
        setLogin(obj, index);
        setName(obj, index);
        setPassword(obj, index);
        return obj;
    }
    
    public void setDateCreate(AdminClient obj, int index) {
        Calendar dateCreate = Calendar.getInstance();
        obj.setDateCreate(dateCreate);
    }
    
    public void setDateLastVisit(AdminClient obj, int index) {
        Calendar dateLastVisit = Calendar.getInstance();
        obj.setDateLastVisit(dateLastVisit);
    }
    
    public void setDateUpdate(AdminClient obj, int index) {
        Calendar dateUpdate = Calendar.getInstance();
        obj.setDateUpdate(dateUpdate);
    }
    
    public void setEmail(AdminClient obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void setEnabled(AdminClient obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void setFathername(AdminClient obj, int index) {
        String fathername = "fathername_" + index;
        obj.setFathername(fathername);
    }
    
    public void setLastname(AdminClient obj, int index) {
        String lastname = "lastname_" + index;
        obj.setLastname(lastname);
    }
    
    public void setLogin(AdminClient obj, int index) {
        String login = "login_" + index;
        obj.setLogin(login);
    }
    
    public void setName(AdminClient obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void setPassword(AdminClient obj, int index) {
        String password = "password_" + index;
        obj.setPassword(password);
    }
    
    public AdminClient getSpecificAdminClient(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        AdminClient obj = data.get(index);
        Long id = obj.getId();
        return AdminClient.findAdminClient(id);
    }
    
    public AdminClient getRandomAdminClient() {
        init();
        AdminClient obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return AdminClient.findAdminClient(id);
    }
    
    public boolean modifyAdminClient(AdminClient obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = AdminClient.findAdminClientEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'AdminClient' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<AdminClient>();
        for (int i = 0; i < 10; i++) {
            AdminClient obj = getNewTransientAdminClient(i);
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
