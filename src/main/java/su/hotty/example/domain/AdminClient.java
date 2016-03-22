package su.hotty.example.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.Calendar;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
public class AdminClient {

	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /**
     */
    @NotNull
    @Size(min = 1)
    private String name;

    /**
     */
    private String lastname;

    /**
     */
    private String fathername;

    /**
     */
    @NotNull
    @Pattern(regexp = "[\\w.]+@[\\w.]+\\.\\w+")
    private String email;

    /**
     */
    @NotNull
    @Size(min = 3)
    private String login;

    /**
     */
    @NotNull
    private String password;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Calendar dateCreate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Calendar dateUpdate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Calendar dateLastVisit;

    /**
     */
    private Boolean enabled;
	
	
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getFathername() {
        return this.fathername;
    }
    
    public void setFathername(String fathername) {
        this.fathername = fathername;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Calendar getDateCreate() {
        return this.dateCreate;
    }
    
    public void setDateCreate(Calendar dateCreate) {
        this.dateCreate = dateCreate;
    }
    
    public Calendar getDateUpdate() {
        return this.dateUpdate;
    }
    
    public void setDateUpdate(Calendar dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
    
    public Calendar getDateLastVisit() {
        return this.dateLastVisit;
    }
    
    public void setDateLastVisit(Calendar dateLastVisit) {
        this.dateLastVisit = dateLastVisit;
    }
    
    public Boolean getEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "lastname", "fathername", "email", "login", "password", "dateCreate", "dateUpdate", "dateLastVisit", "enabled");
    
    public static final EntityManager entityManager() {
        EntityManager em = new AdminClient().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countAdminClients() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AdminClient o", Long.class).getSingleResult();
    }
    
    public static List<AdminClient> findAllAdminClients() {
        return entityManager().createQuery("SELECT o FROM AdminClient o", AdminClient.class).getResultList();
    }
    
    public static List<AdminClient> findAllAdminClients(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AdminClient o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AdminClient.class).getResultList();
    }
    
    public static AdminClient findAdminClient(Long id) {
        if (id == null) return null;
        return entityManager().find(AdminClient.class, id);
    }
    
    public static List<AdminClient> findAdminClientEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AdminClient o", AdminClient.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<AdminClient> findAdminClientEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AdminClient o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AdminClient.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AdminClient attached = findAdminClient(this.id);
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
    public AdminClient merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AdminClient merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
