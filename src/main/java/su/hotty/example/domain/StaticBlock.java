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
public class StaticBlock extends Block {
	
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");
    
    public static long countStaticBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StaticBlock o", Long.class).getSingleResult();
    }
    
    public static List<StaticBlock> findAllStaticBlocks() {
        return entityManager().createQuery("SELECT o FROM StaticBlock o", StaticBlock.class).getResultList();
    }
    
    public static List<StaticBlock> findAllStaticBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StaticBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StaticBlock.class).getResultList();
    }
    
    public static StaticBlock findStaticBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(StaticBlock.class, id);
    }
    
    public static List<StaticBlock> findStaticBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StaticBlock o", StaticBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<StaticBlock> findStaticBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StaticBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StaticBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            StaticBlock attached = findStaticBlock(this.getId());
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
    public StaticBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        StaticBlock merged = this.entityManager.merge(this);
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
    
    public static StaticBlock fromJsonToStaticBlock(String json) {
        return new JSONDeserializer<StaticBlock>()
        .use(null, StaticBlock.class).deserialize(json);
    }
    
    public static String toStaticBlockJsonArray(Collection<StaticBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toStaticBlockJsonArray(Collection<StaticBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<StaticBlock> fromJsonArrayToStaticBlocks(String json) {
        return new JSONDeserializer<List<StaticBlock>>()
        .use("values", StaticBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
