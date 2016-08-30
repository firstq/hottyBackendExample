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
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class AccordeonBlock extends Block{

    /**
     */
    @Size(max = 10)
    private String heightStyle;
	
    public String getHeightStyle() {
        return this.heightStyle;
    }
    
    public void setHeightStyle(String heightStyle) {
        this.heightStyle = heightStyle;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("heightStyle");
    
    public static long countAccordeonBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AccordeonBlock o", Long.class).getSingleResult();
    }
    
    public static List<AccordeonBlock> findAllAccordeonBlocks() {
        return entityManager().createQuery("SELECT o FROM AccordeonBlock o", AccordeonBlock.class).getResultList();
    }
    
    public static List<AccordeonBlock> findAllAccordeonBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AccordeonBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AccordeonBlock.class).getResultList();
    }
    
    public static AccordeonBlock findAccordeonBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(AccordeonBlock.class, id);
    }
    
    public static List<AccordeonBlock> findAccordeonBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AccordeonBlock o", AccordeonBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<AccordeonBlock> findAccordeonBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AccordeonBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AccordeonBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AccordeonBlock attached = findAccordeonBlock(this.getId());
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
    public AccordeonBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AccordeonBlock merged = this.entityManager.merge(this);
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
    
    public static AccordeonBlock fromJsonToAccordeonBlock(String json) {
        return new JSONDeserializer<AccordeonBlock>()
        .use(null, AccordeonBlock.class).deserialize(json);
    }
    
    public static String toAccordeonBlockJsonArray(Collection<AccordeonBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toAccordeonBlockJsonArray(Collection<AccordeonBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<AccordeonBlock> fromJsonArrayToAccordeonBlocks(String json) {
        return new JSONDeserializer<List<AccordeonBlock>>()
        .use("values", AccordeonBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
