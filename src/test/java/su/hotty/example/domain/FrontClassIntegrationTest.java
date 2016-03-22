package su.hotty.example.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.junit.Test;
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
public class FrontClassIntegrationTest {

    @Autowired
    FrontClassDataOnDemand dod;
    
    @Test
    public void testCountFrontClasses() {
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", dod.getRandomFrontClass());
        long count = FrontClass.countFrontClasses();
        Assert.assertTrue("Counter for 'FrontClass' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindFrontClass() {
        FrontClass obj = dod.getRandomFrontClass();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to provide an identifier", id);
        obj = FrontClass.findFrontClass(id);
        Assert.assertNotNull("Find method for 'FrontClass' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FrontClass' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllFrontClasses() {
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", dod.getRandomFrontClass());
        long count = FrontClass.countFrontClasses();
        Assert.assertTrue("Too expensive to perform a find all test for 'FrontClass', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FrontClass> result = FrontClass.findAllFrontClasses();
        Assert.assertNotNull("Find all method for 'FrontClass' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FrontClass' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindFrontClassEntries() {
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", dod.getRandomFrontClass());
        long count = FrontClass.countFrontClasses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FrontClass> result = FrontClass.findFrontClassEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'FrontClass' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FrontClass' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        FrontClass obj = dod.getRandomFrontClass();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to provide an identifier", id);
        obj = FrontClass.findFrontClass(id);
        Assert.assertNotNull("Find method for 'FrontClass' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFrontClass(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'FrontClass' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        FrontClass obj = dod.getRandomFrontClass();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to provide an identifier", id);
        obj = FrontClass.findFrontClass(id);
        boolean modified =  dod.modifyFrontClass(obj);
        Integer currentVersion = obj.getVersion();
        FrontClass merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FrontClass' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", dod.getRandomFrontClass());
        FrontClass obj = dod.getNewTransientFrontClass(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FrontClass' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'FrontClass' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        FrontClass obj = dod.getRandomFrontClass();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FrontClass' failed to provide an identifier", id);
        obj = FrontClass.findFrontClass(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'FrontClass' with identifier '" + id + "'", FrontClass.findFrontClass(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
