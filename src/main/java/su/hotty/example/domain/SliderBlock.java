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
public class SliderBlock extends Block {
	
	/**
     */
    private Boolean special;

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
	
	private String scrollContentStyle;

	private String scrollContentItemStyle;
	
	public String getScrollContentStyle() {
		return scrollContentStyle;
	}

	public void setScrollContentStyle(String scrollContentStyle) {
		this.scrollContentStyle = scrollContentStyle;
	}

	public String getScrollContentItemStyle() {
		return scrollContentItemStyle;
	}

	public void setScrollContentItemStyle(String scrollContentItemStyle) {
		this.scrollContentItemStyle = scrollContentItemStyle;
	}
	
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
    
	public Boolean getSpecial() {
		return special;
	}

	public void setSpecial(Boolean special) {
		this.special = special;
	}
	
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("isVertical", "isMouseScrolled", "isArrowsShow", "isSliderShow");
    
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
	
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static SliderBlock fromJsonToSliderBlock(String json) {
        return new JSONDeserializer<SliderBlock>()
        .use(null, SliderBlock.class).deserialize(json);
    }
    
    public static String toSliderBlockJsonArray(Collection<SliderBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toSliderBlockJsonArray(Collection<SliderBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SliderBlock> fromJsonArrayToSliderBlocks(String json) {
        return new JSONDeserializer<List<SliderBlock>>()
        .use("values", SliderBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
