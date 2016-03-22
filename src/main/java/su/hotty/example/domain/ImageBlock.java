package su.hotty.example.domain;

import org.springframework.beans.factory.annotation.Configurable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class ImageBlock extends Block{
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
//    
    /**
     */
    private Boolean isClickable;

    /**
     */
    private String description;

    /**
     */
    private String originalImgPath;
	
    public Boolean getIsClickable() {
        return this.isClickable;
    }
    
    public void setIsClickable(Boolean isClickable) {
        this.isClickable = isClickable;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOriginalImgPath() {
        return this.originalImgPath;
    }
    
    public void setOriginalImgPath(String originalImgPath) {
        this.originalImgPath = originalImgPath;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("isClickable", "description", "originalImgPath");
    
//    public static final EntityManager entityManager() {
//        EntityManager em = new ImageBlock().entityManager;
//        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//        return em;
//    }
    
    public static long countImageBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ImageBlock o", Long.class).getSingleResult();
    }
    
    public static List<ImageBlock> findAllImageBlocks() {
        return entityManager().createQuery("SELECT o FROM ImageBlock o", ImageBlock.class).getResultList();
    }
    
    public static List<ImageBlock> findAllImageBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ImageBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ImageBlock.class).getResultList();
    }
    
    public static ImageBlock findImageBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(ImageBlock.class, id);
    }
    
    public static List<ImageBlock> findImageBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ImageBlock o", ImageBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<ImageBlock> findImageBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ImageBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ImageBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ImageBlock attached = findImageBlock(this.getId());
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
    public ImageBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ImageBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
