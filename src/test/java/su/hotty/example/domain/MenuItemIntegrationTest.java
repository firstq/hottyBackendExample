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
public class MenuItemIntegrationTest {

    @Autowired
    MenuItemDataOnDemand dod;
    
    @Test
    public void testCountMenuItems() {
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", dod.getRandomMenuItem());
        long count = MenuItem.countMenuItems();
        Assert.assertTrue("Counter for 'MenuItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindMenuItem() {
        MenuItem obj = dod.getRandomMenuItem();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to provide an identifier", id);
        obj = MenuItem.findMenuItem(id);
        Assert.assertNotNull("Find method for 'MenuItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MenuItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllMenuItems() {
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", dod.getRandomMenuItem());
        long count = MenuItem.countMenuItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'MenuItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MenuItem> result = MenuItem.findAllMenuItems();
        Assert.assertNotNull("Find all method for 'MenuItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MenuItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindMenuItemEntries() {
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", dod.getRandomMenuItem());
        long count = MenuItem.countMenuItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MenuItem> result = MenuItem.findMenuItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'MenuItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MenuItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        MenuItem obj = dod.getRandomMenuItem();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to provide an identifier", id);
        obj = MenuItem.findMenuItem(id);
        Assert.assertNotNull("Find method for 'MenuItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMenuItem(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'MenuItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        MenuItem obj = dod.getRandomMenuItem();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to provide an identifier", id);
        obj = MenuItem.findMenuItem(id);
        boolean modified =  dod.modifyMenuItem(obj);
        Integer currentVersion = obj.getVersion();
        MenuItem merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MenuItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", dod.getRandomMenuItem());
        MenuItem obj = dod.getNewTransientMenuItem(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MenuItem' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'MenuItem' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        MenuItem obj = dod.getRandomMenuItem();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MenuItem' failed to provide an identifier", id);
        obj = MenuItem.findMenuItem(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'MenuItem' with identifier '" + id + "'", MenuItem.findMenuItem(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
