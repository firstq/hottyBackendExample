package su.hotty.example.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Entity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import javax.persistence.PrimaryKeyJoinColumn;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class VideoBlock extends Block {

    /**
     */
    private String videoUrl;

    /**
     */
    private String videoSource;
	
    public String getVideoUrl() {
        return this.videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getVideoSource() {
        return this.videoSource;
    }
    
    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("videoUrl", "videoSource");
    
    public static long countVideoBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM VideoBlock o", Long.class).getSingleResult();
    }
    
    public static List<VideoBlock> findAllVideoBlocks() {
        return entityManager().createQuery("SELECT o FROM VideoBlock o", VideoBlock.class).getResultList();
    }
    
    public static List<VideoBlock> findAllVideoBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM VideoBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, VideoBlock.class).getResultList();
    }
    
    public static VideoBlock findVideoBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(VideoBlock.class, id);
    }
    
    public static List<VideoBlock> findVideoBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM VideoBlock o", VideoBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<VideoBlock> findVideoBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM VideoBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, VideoBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            VideoBlock attached = findVideoBlock(this.getId());
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public VideoBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        VideoBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static VideoBlock fromJsonToVideoBlock(String json) {
        return new JSONDeserializer<VideoBlock>()
        .use(null, VideoBlock.class).deserialize(json);
    }
    
    public static String toVideoBlockJsonArray(Collection<VideoBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toVideoBlockJsonArray(Collection<VideoBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<VideoBlock> fromJsonArrayToVideoBlocks(String json) {
        return new JSONDeserializer<List<VideoBlock>>()
        .use("values", VideoBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
