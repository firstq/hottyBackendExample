package su.hotty.editor.domain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.DomainNameIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.DomainNameDataOnDemand;
import su.hotty.editor.service.DomainNameService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomainNameIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    DomainNameDataOnDemand dod;
    
    @Autowired
    DomainNameService domainNameService;
    
    @Test
    public void testCountAllDomainNames() {
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", dod.getRandomDomainName());
        long count = domainNameService.countAllDomainNames();
        Assert.assertTrue("Counter for 'DomainName' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindDomainName() {
        DomainName obj = dod.getRandomDomainName();
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DomainName' failed to provide an identifier", id);
        obj = domainNameService.findDomainName(id);
        Assert.assertNotNull("Find method for 'DomainName' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'DomainName' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllDomainNames() {
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", dod.getRandomDomainName());
        long count = domainNameService.countAllDomainNames();
        Assert.assertTrue("Too expensive to perform a find all test for 'DomainName', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<DomainName> result = domainNameService.findAllDomainNames();
        Assert.assertNotNull("Find all method for 'DomainName' illegally returned null", result);
        Assert.assertTrue("Find all method for 'DomainName' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindDomainNameEntries() {
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", dod.getRandomDomainName());
        long count = domainNameService.countAllDomainNames();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<DomainName> result = domainNameService.findDomainNameEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'DomainName' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'DomainName' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveDomainName() {
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", dod.getRandomDomainName());
        DomainName obj = dod.getNewTransientDomainName(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'DomainName' failed to provide a new transient entity", obj);
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
        Assert.assertNotNull("Expected 'DomainName' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeleteDomainName() {
        DomainName obj = dod.getRandomDomainName();
        Assert.assertNotNull("Data on demand for 'DomainName' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DomainName' failed to provide an identifier", id);
        obj = domainNameService.findDomainName(id);
        domainNameService.deleteDomainName(obj);
        Assert.assertNull("Failed to remove 'DomainName' with identifier '" + id + "'", domainNameService.findDomainName(id));
    }
    
}