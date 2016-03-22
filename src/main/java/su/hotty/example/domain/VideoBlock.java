package su.hotty.example.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class VideoBlock extends Block{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Long id;
//    
//    @Version
//    @Column(name = "version")
//    private Integer version;
//    
//    public Long getId() {
//        return this.id;
//    }
//    
//    public void setId(Long id) {
//        this.id = id;
//    }
//    
//    public Integer getVersion() {
//        return this.version;
//    }
//    
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
    
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
    
//    public static final EntityManager entityManager() {
//        EntityManager em = new VideoBlock().entityManager;
//        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//        return em;
//    }
    
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
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
