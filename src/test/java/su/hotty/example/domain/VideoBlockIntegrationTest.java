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
public class VideoBlockIntegrationTest {

    @Autowired
    VideoBlockDataOnDemand dod;
    
    @Test
    public void testCountVideoBlocks() {
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", dod.getRandomVideoBlock());
        long count = VideoBlock.countVideoBlocks();
        Assert.assertTrue("Counter for 'VideoBlock' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindVideoBlock() {
        VideoBlock obj = dod.getRandomVideoBlock();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to provide an identifier", id);
        obj = VideoBlock.findVideoBlock(id);
        Assert.assertNotNull("Find method for 'VideoBlock' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'VideoBlock' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllVideoBlocks() {
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", dod.getRandomVideoBlock());
        long count = VideoBlock.countVideoBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'VideoBlock', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<VideoBlock> result = VideoBlock.findAllVideoBlocks();
        Assert.assertNotNull("Find all method for 'VideoBlock' illegally returned null", result);
        Assert.assertTrue("Find all method for 'VideoBlock' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindVideoBlockEntries() {
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", dod.getRandomVideoBlock());
        long count = VideoBlock.countVideoBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<VideoBlock> result = VideoBlock.findVideoBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'VideoBlock' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'VideoBlock' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        VideoBlock obj = dod.getRandomVideoBlock();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to provide an identifier", id);
        obj = VideoBlock.findVideoBlock(id);
        Assert.assertNotNull("Find method for 'VideoBlock' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyVideoBlock(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'VideoBlock' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        VideoBlock obj = dod.getRandomVideoBlock();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to provide an identifier", id);
        obj = VideoBlock.findVideoBlock(id);
        boolean modified =  dod.modifyVideoBlock(obj);
        Integer currentVersion = obj.getVersion();
        VideoBlock merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'VideoBlock' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", dod.getRandomVideoBlock());
        VideoBlock obj = dod.getNewTransientVideoBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'VideoBlock' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'VideoBlock' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        VideoBlock obj = dod.getRandomVideoBlock();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'VideoBlock' failed to provide an identifier", id);
        obj = VideoBlock.findVideoBlock(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'VideoBlock' with identifier '" + id + "'", VideoBlock.findVideoBlock(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
