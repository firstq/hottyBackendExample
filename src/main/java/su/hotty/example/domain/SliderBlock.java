package su.hotty.example.domain;

import java.util.List;
import javax.persistence.EntityManager;
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
public class SliderBlock extends Block{
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
    private Boolean isVertical;

    /**
     */
    private Boolean isMouseScrolled;

    /**
     */
    private Boolean isArrowsShow;

    /**
     */
    private Boolean isSliderShow;
	
    public Boolean getIsVertical() {
        return this.isVertical;
    }
    
    public void setIsVertical(Boolean isVertical) {
        this.isVertical = isVertical;
    }
    
    public Boolean getIsMouseScrolled() {
        return this.isMouseScrolled;
    }
    
    public void setIsMouseScrolled(Boolean isMouseScrolled) {
        this.isMouseScrolled = isMouseScrolled;
    }
    
    public Boolean getIsArrowsShow() {
        return this.isArrowsShow;
    }
    
    public void setIsArrowsShow(Boolean isArrowsShow) {
        this.isArrowsShow = isArrowsShow;
    }
    
    public Boolean getIsSliderShow() {
        return this.isSliderShow;
    }
    
    public void setIsSliderShow(Boolean isSliderShow) {
        this.isSliderShow = isSliderShow;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("isVertical", "isMouseScrolled", "isArrowsShow", "isSliderShow");
    
//    public static final EntityManager entityManager() {
//        EntityManager em = new SliderBlock().entityManager;
//        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//        return em;
//    }
    
    public static long countSliderBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SliderBlock o", Long.class).getSingleResult();
    }
    
    public static List<SliderBlock> findAllSliderBlocks() {
        return entityManager().createQuery("SELECT o FROM SliderBlock o", SliderBlock.class).getResultList();
    }
    
    public static List<SliderBlock> findAllSliderBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SliderBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SliderBlock.class).getResultList();
    }
    
    public static SliderBlock findSliderBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(SliderBlock.class, id);
    }
    
    public static List<SliderBlock> findSliderBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SliderBlock o", SliderBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<SliderBlock> findSliderBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SliderBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SliderBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SliderBlock attached = findSliderBlock(this.getId());
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
    public SliderBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SliderBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
