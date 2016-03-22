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
public class SliderBlockIntegrationTest {

    @Autowired
    SliderBlockDataOnDemand dod;
    
    @Test
    public void testCountSliderBlocks() {
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", dod.getRandomSliderBlock());
        long count = SliderBlock.countSliderBlocks();
        Assert.assertTrue("Counter for 'SliderBlock' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindSliderBlock() {
        SliderBlock obj = dod.getRandomSliderBlock();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to provide an identifier", id);
        obj = SliderBlock.findSliderBlock(id);
        Assert.assertNotNull("Find method for 'SliderBlock' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SliderBlock' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllSliderBlocks() {
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", dod.getRandomSliderBlock());
        long count = SliderBlock.countSliderBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'SliderBlock', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SliderBlock> result = SliderBlock.findAllSliderBlocks();
        Assert.assertNotNull("Find all method for 'SliderBlock' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SliderBlock' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindSliderBlockEntries() {
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", dod.getRandomSliderBlock());
        long count = SliderBlock.countSliderBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SliderBlock> result = SliderBlock.findSliderBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SliderBlock' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SliderBlock' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        SliderBlock obj = dod.getRandomSliderBlock();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to provide an identifier", id);
        obj = SliderBlock.findSliderBlock(id);
        Assert.assertNotNull("Find method for 'SliderBlock' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySliderBlock(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'SliderBlock' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        SliderBlock obj = dod.getRandomSliderBlock();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to provide an identifier", id);
        obj = SliderBlock.findSliderBlock(id);
        boolean modified =  dod.modifySliderBlock(obj);
        Integer currentVersion = obj.getVersion();
        SliderBlock merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'SliderBlock' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", dod.getRandomSliderBlock());
        SliderBlock obj = dod.getNewTransientSliderBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'SliderBlock' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'SliderBlock' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        SliderBlock obj = dod.getRandomSliderBlock();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SliderBlock' failed to provide an identifier", id);
        obj = SliderBlock.findSliderBlock(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'SliderBlock' with identifier '" + id + "'", SliderBlock.findSliderBlock(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
