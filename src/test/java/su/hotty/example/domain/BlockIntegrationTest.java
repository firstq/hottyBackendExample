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
public class BlockIntegrationTest {

    @Autowired
    BlockDataOnDemand dod;
    
    @Test
    public void testCountBlocks() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = Block.countBlocks();
        Assert.assertTrue("Counter for 'Block' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindBlock() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = Block.findBlock(id);
        Assert.assertNotNull("Find method for 'Block' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Block' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllBlocks() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = Block.countBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'Block', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Block> result = Block.findAllBlocks();
        Assert.assertNotNull("Find all method for 'Block' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Block' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindBlockEntries() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = Block.countBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Block> result = Block.findBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Block' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Block' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testFlush() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = Block.findBlock(id);
        Assert.assertNotNull("Find method for 'Block' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBlock(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Block' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testMergeUpdate() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = Block.findBlock(id);
        boolean modified =  dod.modifyBlock(obj);
        Integer currentVersion = obj.getVersion();
        Block merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Block' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        Block obj = dod.getNewTransientBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Block' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Block' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Block' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testRemove() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = Block.findBlock(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Block' with identifier '" + id + "'", Block.findBlock(id));
    }
    
    @Test
    public void testMarkerMethod() {
		
    }
}
