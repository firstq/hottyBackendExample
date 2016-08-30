package su.hotty.example.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Entity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class TextBlock extends Block {

    /**
     */
    @Size(max = 2000)
    private String styles;
	
	@Size(max = 10000)
	private String text;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Block> blocks = new ArrayList<Block>();
	
    public String getStyles() {
        return this.styles;
    }
    
    public void setStyles(String styles) {
        this.styles = styles;
    }
    
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
    public List<Block> getBlocks() {
        return this.blocks;
    }
    
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("styles", "blocks");
    
    public static long countTextBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TextBlock o", Long.class).getSingleResult();
    }
    
    public static List<TextBlock> findAllTextBlocks() {
        return entityManager().createQuery("SELECT o FROM TextBlock o", TextBlock.class).getResultList();
    }
    
    public static List<TextBlock> findAllTextBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TextBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TextBlock.class).getResultList();
    }
    
    public static TextBlock findTextBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(TextBlock.class, id);
    }
    
    public static List<TextBlock> findTextBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TextBlock o", TextBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<TextBlock> findTextBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TextBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TextBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TextBlock attached = findTextBlock(this.getId());
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
    public TextBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TextBlock merged = this.entityManager.merge(this);
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
    
    public static TextBlock fromJsonToTextBlock(String json) {
        return new JSONDeserializer<TextBlock>()
        .use(null, TextBlock.class).deserialize(json);
    }
    
    public static String toTextBlockJsonArray(Collection<TextBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toTextBlockJsonArray(Collection<TextBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<TextBlock> fromJsonArrayToTextBlocks(String json) {
        return new JSONDeserializer<List<TextBlock>>()
        .use("values", TextBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
