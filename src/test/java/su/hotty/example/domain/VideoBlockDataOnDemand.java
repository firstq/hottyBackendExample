package su.hotty.example.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

@Configurable
@Component
public class VideoBlockDataOnDemand {
	
    private Random rnd = new SecureRandom();
    
    private List<VideoBlock> data;
    
    public VideoBlock getNewTransientVideoBlock(int index) {
        VideoBlock obj = new VideoBlock();
		obj.setName("name_" + index);
        setVideoSource(obj, index);
        setVideoUrl(obj, index);
        return obj;
    }
    
    public void setVideoSource(VideoBlock obj, int index) {
        String videoSource = "videoSource_" + index;
        obj.setVideoSource(videoSource);
    }
    
    public void setVideoUrl(VideoBlock obj, int index) {
        String videoUrl = "videoUrl_" + index;
        obj.setVideoUrl(videoUrl);
    }
    
    public VideoBlock getSpecificVideoBlock(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        VideoBlock obj = data.get(index);
        Long id = obj.getId();
        return VideoBlock.findVideoBlock(id);
    }
    
    public VideoBlock getRandomVideoBlock() {
        init();
        VideoBlock obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return VideoBlock.findVideoBlock(id);
    }
    
    public boolean modifyVideoBlock(VideoBlock obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = VideoBlock.findVideoBlockEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'VideoBlock' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<VideoBlock>();
        for (int i = 0; i < 10; i++) {
            VideoBlock obj = getNewTransientVideoBlock(i);
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
            data.add(obj);
        }
    }
    
}
