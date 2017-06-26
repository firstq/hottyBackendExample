package su.hotty.editor.domain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.BlockIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.BlockDataOnDemand;
import su.hotty.editor.service.BlockService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlockIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    BlockDataOnDemand dod;
    
    @Autowired
    BlockService blockService;
    
    @Test
    public void testCountAllBlocks() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = blockService.countAllBlocks();
        Assert.assertTrue("Counter for 'Block' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindBlock() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = blockService.findBlock(id);
        Assert.assertNotNull("Find method for 'Block' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Block' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllBlocks() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = blockService.countAllBlocks();
        Assert.assertTrue("Too expensive to perform a find all test for 'Block', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Block> result = blockService.findAllBlocks();
        Assert.assertNotNull("Find all method for 'Block' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Block' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindBlockEntries() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        long count = blockService.countAllBlocks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Block> result = blockService.findBlockEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Block' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Block' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveBlock() {
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", dod.getRandomBlock());
        Block obj = dod.getNewTransientBlock(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Block' failed to provide a new transient entity", obj);
        try {
            blockService.saveBlock(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        Assert.assertNotNull("Expected 'Block' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeleteBlock() {
        Block obj = dod.getRandomBlock();
        Assert.assertNotNull("Data on demand for 'Block' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Block' failed to provide an identifier", id);
        obj = blockService.findBlock(id);
        blockService.deleteBlock(obj);
        Assert.assertNull("Failed to remove 'Block' with identifier '" + id + "'", blockService.findBlock(id));
    }
    
}