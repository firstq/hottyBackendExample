package su.hotty.editor.domain;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import su.hotty.editor.domain.Site;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Persistent
public class Site {

    /**
     */
    @NotNull
    private String name;

    /**
     */
    @NotNull
    private String keydomain;

    /**
     */
    private String scripts;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Date dateCreated;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Date dateUpdated;

    /**
     */
    @NotNull
    private Long clientId;

    /**
     */
    private Boolean isExample;

    private Boolean isPublished;

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean published) {
        isPublished = published;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getKeydomain() {
        return this.keydomain;
    }

    public void setKeydomain(String keydomain) {
        this.keydomain = keydomain;
    }

    public String getScripts() {
        return this.scripts;
    }
    
    public void setScripts(String scripts) {
        this.scripts = scripts;
    }
    
    public Date getDateCreated() {
        return this.dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date getDateUpdated() {
        return this.dateUpdated;
    }
    
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    public Long getClientId() {
        return this.clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Boolean getIsExample() {
        return this.isExample;
    }
    
    public void setIsExample(Boolean isExample) {
        this.isExample = isExample;
    }

    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").deepSerialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Site fromJsonToSite(String json) {
        return new JSONDeserializer<Site>()
        .use(null, Site.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Site> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Site> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Site> fromJsonArrayToSites(String json) {
        return new JSONDeserializer<List<Site>>()
        .use("values", Site.class).deserialize(json);
    }

    @Id
    private String id;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    private List<Page> pages = new ArrayList<>();

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

}