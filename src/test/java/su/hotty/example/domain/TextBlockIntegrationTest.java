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
public class TextBlockIntegrationTest {

    @Autowired
    TextBlockDataOnDemand dod;
    
    @Test
    public void testCountTextBlocks() {
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", dod.getRandomTextBlock());
        long count = TextBlock.countTextBlocks();
        Assert.assertTrue("Counter for 'TextBlock' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindTextBlock() {
        TextBlock obj = dod.getRandomTextBlock();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to provide an identifier", id);
        obj = TextBlock.findTextBlock(id);
        Assert.assertNotNull("Find method for 'TextBlock' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TextBlock' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllTextBlocks() {
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", dod.getRandomTextBlock());
        long count = TextBlock.countTextBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'TextBlock', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<TextBlock> result = TextBlock.findAllTextBlocks();
        Assert.assertNotNull("Find all method for 'TextBlock' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TextBlock' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindTextBlockEntries() {
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", dod.getRandomTextBlock());
        long count = TextBlock.countTextBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TextBlock> result = TextBlock.findTextBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TextBlock' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TextBlock' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        TextBlock obj = dod.getRandomTextBlock();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to provide an identifier", id);
        obj = TextBlock.findTextBlock(id);
        Assert.assertNotNull("Find method for 'TextBlock' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTextBlock(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TextBlock' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        TextBlock obj = dod.getRandomTextBlock();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to provide an identifier", id);
        obj = TextBlock.findTextBlock(id);
        boolean modified =  dod.modifyTextBlock(obj);
        Integer currentVersion = obj.getVersion();
        TextBlock merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'TextBlock' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", dod.getRandomTextBlock());
        TextBlock obj = dod.getNewTransientTextBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TextBlock' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'TextBlock' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        TextBlock obj = dod.getRandomTextBlock();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TextBlock' failed to provide an identifier", id);
        obj = TextBlock.findTextBlock(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TextBlock' with identifier '" + id + "'", TextBlock.findTextBlock(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
