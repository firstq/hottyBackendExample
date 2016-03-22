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
public class ImageBlockIntegrationTest {

    @Autowired
    ImageBlockDataOnDemand dod;
    
    @Test
    public void testCountImageBlocks() {
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", dod.getRandomImageBlock());
        long count = ImageBlock.countImageBlocks();
        Assert.assertTrue("Counter for 'ImageBlock' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindImageBlock() {
        ImageBlock obj = dod.getRandomImageBlock();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to provide an identifier", id);
        obj = ImageBlock.findImageBlock(id);
        Assert.assertNotNull("Find method for 'ImageBlock' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ImageBlock' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllImageBlocks() {
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", dod.getRandomImageBlock());
        long count = ImageBlock.countImageBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'ImageBlock', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ImageBlock> result = ImageBlock.findAllImageBlocks();
        Assert.assertNotNull("Find all method for 'ImageBlock' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ImageBlock' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindImageBlockEntries() {
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", dod.getRandomImageBlock());
        long count = ImageBlock.countImageBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ImageBlock> result = ImageBlock.findImageBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ImageBlock' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ImageBlock' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        ImageBlock obj = dod.getRandomImageBlock();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to provide an identifier", id);
        obj = ImageBlock.findImageBlock(id);
        Assert.assertNotNull("Find method for 'ImageBlock' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyImageBlock(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ImageBlock' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        ImageBlock obj = dod.getRandomImageBlock();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to provide an identifier", id);
        obj = ImageBlock.findImageBlock(id);
        boolean modified =  dod.modifyImageBlock(obj);
        Integer currentVersion = obj.getVersion();
        ImageBlock merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ImageBlock' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", dod.getRandomImageBlock());
        ImageBlock obj = dod.getNewTransientImageBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ImageBlock' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ImageBlock' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        ImageBlock obj = dod.getRandomImageBlock();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageBlock' failed to provide an identifier", id);
        obj = ImageBlock.findImageBlock(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ImageBlock' with identifier '" + id + "'", ImageBlock.findImageBlock(id));
    }
    
    @Test
    public void testMarkerMethod() {
    }
}
