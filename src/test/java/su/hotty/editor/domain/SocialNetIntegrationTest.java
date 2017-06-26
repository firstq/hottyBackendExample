package su.hotty.editor.domain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.SocialNetIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.SocialNetDataOnDemand;
import su.hotty.editor.service.SocialNetService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocialNetIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    SocialNetDataOnDemand dod;
    
    @Autowired
    SocialNetService socialNetService;
    
    @Test
    public void testCountAllSocialNets() {
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", dod.getRandomSocialNet());
        long count = socialNetService.countAllSocialNets();
        Assert.assertTrue("Counter for 'SocialNet' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindSocialNet() {
        SocialNet obj = dod.getRandomSocialNet();
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to provide an identifier", id);
        obj = socialNetService.findSocialNet(id);
        Assert.assertNotNull("Find method for 'SocialNet' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SocialNet' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllSocialNets() {
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", dod.getRandomSocialNet());
        long count = socialNetService.countAllSocialNets();
        Assert.assertTrue("Too expensive to perform a find all test for 'SocialNet', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SocialNet> result = socialNetService.findAllSocialNets();
        Assert.assertNotNull("Find all method for 'SocialNet' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SocialNet' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindSocialNetEntries() {
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", dod.getRandomSocialNet());
        long count = socialNetService.countAllSocialNets();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SocialNet> result = socialNetService.findSocialNetEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SocialNet' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SocialNet' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveSocialNet() {
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", dod.getRandomSocialNet());
        SocialNet obj = dod.getNewTransientSocialNet(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to provide a new transient entity", obj);
        try {
            socialNetService.saveSocialNet(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        Assert.assertNotNull("Expected 'SocialNet' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeleteSocialNet() {
        SocialNet obj = dod.getRandomSocialNet();
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SocialNet' failed to provide an identifier", id);
        obj = socialNetService.findSocialNet(id);
        socialNetService.deleteSocialNet(obj);
        Assert.assertNull("Failed to remove 'SocialNet' with identifier '" + id + "'", socialNetService.findSocialNet(id));
    }
    
}