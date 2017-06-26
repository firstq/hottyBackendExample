package su.hotty.editor.domain;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import su.hotty.editor.HottyServerApplication;
import su.hotty.editor.domain.SiteIntegrationTest;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import su.hotty.editor.domain.SiteDataOnDemand;
import su.hotty.editor.service.SiteService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {HottyServerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SiteIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    SiteDataOnDemand dod;
    
    @Autowired
    SiteService siteService;
    
    @Test
    public void testCountAllSites() {
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", dod.getRandomSite());
        long count = siteService.countAllSites();
        Assert.assertTrue("Counter for 'Site' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void testFindSite() {
        Site obj = dod.getRandomSite();
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Site' failed to provide an identifier", id);
        obj = siteService.findSite(id);
        Assert.assertNotNull("Find method for 'Site' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Site' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void testFindAllSites() {
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", dod.getRandomSite());
        long count = siteService.countAllSites();
        Assert.assertTrue("Too expensive to perform a find all test for 'Site', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Site> result = siteService.findAllSites();
        Assert.assertNotNull("Find all method for 'Site' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Site' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void testFindSiteEntries() {
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", dod.getRandomSite());
        long count = siteService.countAllSites();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Site> result = siteService.findSiteEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Site' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Site' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void testSaveSite() {
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", dod.getRandomSite());
        Site obj = dod.getNewTransientSite(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Site' failed to provide a new transient entity", obj);
        try {
            siteService.saveSite(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        Assert.assertNotNull("Expected 'Site' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void testDeleteSite() {
        Site obj = dod.getRandomSite();
        Assert.assertNotNull("Data on demand for 'Site' failed to initialize correctly", obj);
        String id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Site' failed to provide an identifier", id);
        obj = siteService.findSite(id);
        siteService.deleteSite(obj);
        Assert.assertNull("Failed to remove 'Site' with identifier '" + id + "'", siteService.findSite(id));
    }
    
}