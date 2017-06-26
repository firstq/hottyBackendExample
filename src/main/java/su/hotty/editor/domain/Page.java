package su.hotty.editor.domain;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.ManyToOne;
import su.hotty.editor.domain.Page;
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
public class Page {

    /**
     */
    @NotNull
    private String pagePath;

    /**
     */
    @NotNull
    private String pageTitle;

    /**
     */
    @NotNull
    private String pageDescription;

    /**
     */
    @NotNull
    private String styles;

    /**
     */
    @NotNull
    private String frontClasses;

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

    private String siteId;
    
    public String getPagePath() {
        return this.pagePath;
    }
    
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
    
    public String getPageTitle() {
        return this.pageTitle;
    }
    
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    
    public String getPageDescription() {
        return this.pageDescription;
    }
    
    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }
    
    public String getStyles() {
        return this.styles;
    }
    
    public void setStyles(String styles) {
        this.styles = styles;
    }
    
    public String getFrontClasses() {
        return this.frontClasses;
    }
    
    public void setFrontClasses(String frontClasses) {
        this.frontClasses = frontClasses;
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
    
    public String getSiteId() {
        return this.siteId;
    }
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Page fromJsonToPage(String json) {
        return new JSONDeserializer<Page>()
        .use(null, Page.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Page> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Page> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Page> fromJsonArrayToPages(String json) {
        return new JSONDeserializer<List<Page>>()
        .use("values", Page.class).deserialize(json);
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

    @Transient
    private List<Block> blocks = new ArrayList<Block>();

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}