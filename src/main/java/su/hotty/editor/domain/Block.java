package su.hotty.editor.domain;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.*;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.ManyToOne;
import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.Page;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Persistent
public class Block {

    /**
     */
    @NotNull
    private String name;

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

    /**
     */
    private Boolean isTop;

    /**
     */
    private Boolean isStatic;

    /**
     */
    @NotNull
    private String tagType;

    /**
     */
    @NotNull
    private String blockType;

    private String pageId;

    private String containedPageId;

    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public Boolean getIsTop() {
        return this.isTop;
    }
    
    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }
    
    public Boolean getIsStatic() {
        return this.isStatic;
    }
    
    public void setIsStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public String getTagType() {
        return this.tagType;
    }
    
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
    
    public String getBlockType() {
        return this.blockType;
    }
    
    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }
    
    public String getPageId() {
        return this.pageId;
    }
    
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
    
    public String getContainedPageId() {
        return this.containedPageId;
    }
    
    public void setContainedPageId(String containedPage) {
        this.containedPageId = containedPageId;
    }

    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").deepSerialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").deepSerialize(this);
    }
    
    public static Block fromJsonToBlock(String json) {
        return new JSONDeserializer<Block>()
        .use(null, Block.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Block> collection) {
        return new JSONSerializer()
        .exclude("*.class").deepSerialize(collection);
    }
    
    public static String toJsonArray(Collection<Block> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").deepSerialize(collection);
    }
    
    public static Collection<Block> fromJsonArrayToBlocks(String json) {
        return new JSONDeserializer<List<Block>>()
        .use("values", Block.class).deserialize(json);
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

    /*
    !!! @Transient private Boolean isDelete; - ??? указывается на клиенте для обозначения удалённого блока, на сервере при наличии этого флага блок удаляется
        Специальные данные блока:
        ImageBlock: Boolean isClickable, String description, String originalImgPath
        MenuBlock: String itemStyles, String logo, Boolean isVertical, !Лучше убрать List<MenuItem> т.к. планируется хранить блоки типа MenuitemBlock! List<MenuItem> items = new ArrayList<MenuItem>();
        MenuitemBlock: String name, Boolean toplevel, String link, String dependSliderBlockId, int priority, String parentItemId - для расфасовки на клиенте
        SendmailBlock: String sendTo, String sendFrom, Boolean capchaEnable
        SliderBlock: Boolean special, Boolean isVertical, Boolean isMouseScrolled, Boolean isArrowsShow, Boolean isSliderShow, String scrollContentStyle, String scrollContentItemStyle
        TextBlock: String styles, String text
        VideoBlock: String videoUrl, String videoSource
     */

    private Map<String,Object> specialData = new HashMap<>();

    public Map<String, Object> getSpecialData() {
        return specialData;
    }

    public void setSpecialData(Map<String, Object> specialData) {
        this.specialData = specialData;
    }

    @Transient
    private Boolean isDelete;

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean delete) {
        isDelete = delete;
    }

    @Transient
    private List<Block> children = new ArrayList<>();

    public List<Block> getChildren() {
        return children;
    }

    public void setChildren(List<Block> children) {
        this.children = children;
    }
}