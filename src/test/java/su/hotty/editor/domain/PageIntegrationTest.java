package su.hotty.editor.domain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.PageIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.PageDataOnDemand;
import su.hotty.editor.service.PageService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PageIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    PageDataOnDemand dod;
    
    @Autowired
    PageService pageService;
    
    @Test
    public void testCountAllPages() {
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", dod.getRandomPage());
        long count = pageService.countAllPages();
        Assert.assertTrue("Counter for 'Page' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindPage() {
        Page obj = dod.getRandomPage();
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Page' failed to provide an identifier", id);
        obj = pageService.findPage(id);
        Assert.assertNotNull("Find method for 'Page' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Page' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllPages() {
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", dod.getRandomPage());
        long count = pageService.countAllPages();
        Assert.assertTrue("Too expensive to perform a find all test for 'Page', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Page> result = pageService.findAllPages();
        Assert.assertNotNull("Find all method for 'Page' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Page' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindPageEntries() {
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", dod.getRandomPage());
        long count = pageService.countAllPages();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Page> result = pageService.findPageEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Page' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Page' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSavePage() {
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", dod.getRandomPage());
        Page obj = dod.getNewTransientPage(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Page' failed to provide a new transient entity", obj);
        try {
            pageService.savePage(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        Assert.assertNotNull("Expected 'Page' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeletePage() {
        Page obj = dod.getRandomPage();
        Assert.assertNotNull("Data on demand for 'Page' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Page' failed to provide an identifier", id);
        obj = pageService.findPage(id);
        pageService.deletePage(obj);
        Assert.assertNull("Failed to remove 'Page' with identifier '" + id + "'", pageService.findPage(id));
    }
    
}