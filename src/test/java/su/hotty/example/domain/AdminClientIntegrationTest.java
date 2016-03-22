package su.hotty.example.domain;

import org.springframework.beans.factory.annotation.Configurable;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class AdminClientIntegrationTest {

    @Autowired
    AdminClientDataOnDemand dod;
    
    @Test
    public void testCountAdminClients() {
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", dod.getRandomAdminClient());
        long count = AdminClient.countAdminClients();
        Assert.assertTrue("Counter for 'AdminClient' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindAdminClient() {
        AdminClient obj = dod.getRandomAdminClient();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to provide an identifier", id);
        obj = AdminClient.findAdminClient(id);
        Assert.assertNotNull("Find method for 'AdminClient' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AdminClient' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllAdminClients() {
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", dod.getRandomAdminClient());
        long count = AdminClient.countAdminClients();
        Assert.assertTrue("Too expensive to perform a find all test for 'AdminClient', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AdminClient> result = AdminClient.findAllAdminClients();
        Assert.assertNotNull("Find all method for 'AdminClient' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AdminClient' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindAdminClientEntries() {
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", dod.getRandomAdminClient());
        long count = AdminClient.countAdminClients();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AdminClient> result = AdminClient.findAdminClientEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AdminClient' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AdminClient' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        AdminClient obj = dod.getRandomAdminClient();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to provide an identifier", id);
        obj = AdminClient.findAdminClient(id);
        Assert.assertNotNull("Find method for 'AdminClient' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAdminClient(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AdminClient' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        AdminClient obj = dod.getRandomAdminClient();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to provide an identifier", id);
        obj = AdminClient.findAdminClient(id);
        boolean modified =  dod.modifyAdminClient(obj);
        Integer currentVersion = obj.getVersion();
        AdminClient merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AdminClient' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", dod.getRandomAdminClient());
        AdminClient obj = dod.getNewTransientAdminClient(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AdminClient' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'AdminClient' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        AdminClient obj = dod.getRandomAdminClient();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdminClient' failed to provide an identifier", id);
        obj = AdminClient.findAdminClient(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AdminClient' with identifier '" + id + "'", AdminClient.findAdminClient(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
