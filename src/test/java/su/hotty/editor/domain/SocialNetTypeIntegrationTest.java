package su.hotty.editor.domain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.SocialNetTypeIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.SocialNetTypeDataOnDemand;
import su.hotty.editor.service.SocialNetTypeService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocialNetTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    SocialNetTypeDataOnDemand dod;
    
    @Autowired
    SocialNetTypeService socialNetTypeService;
    
    @Test
    public void testCountAllSocialNetTypes() {
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", dod.getRandomSocialNetType());
        long count = socialNetTypeService.countAllSocialNetTypes();
        Assert.assertTrue("Counter for 'SocialNetType' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindSocialNetType() {
        SocialNetType obj = dod.getRandomSocialNetType();
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to provide an identifier", id);
        obj = socialNetTypeService.findSocialNetType(id);
        Assert.assertNotNull("Find method for 'SocialNetType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SocialNetType' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllSocialNetTypes() {
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", dod.getRandomSocialNetType());
        long count = socialNetTypeService.countAllSocialNetTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'SocialNetType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SocialNetType> result = socialNetTypeService.findAllSocialNetTypes();
        Assert.assertNotNull("Find all method for 'SocialNetType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SocialNetType' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindSocialNetTypeEntries() {
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", dod.getRandomSocialNetType());
        long count = socialNetTypeService.countAllSocialNetTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SocialNetType> result = socialNetTypeService.findSocialNetTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SocialNetType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SocialNetType' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveSocialNetType() {
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", dod.getRandomSocialNetType());
        SocialNetType obj = dod.getNewTransientSocialNetType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to provide a new transient entity", obj);
        try {
            socialNetTypeService.saveSocialNetType(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        Assert.assertNotNull("Expected 'SocialNetType' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeleteSocialNetType() {
        SocialNetType obj = dod.getRandomSocialNetType();
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SocialNetType' failed to provide an identifier", id);
        obj = socialNetTypeService.findSocialNetType(id);
        socialNetTypeService.deleteSocialNetType(obj);
        Assert.assertNull("Failed to remove 'SocialNetType' with identifier '" + id + "'", socialNetTypeService.findSocialNetType(id));
    }
    
}