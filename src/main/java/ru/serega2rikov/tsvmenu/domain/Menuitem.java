package ru.serega2rikov.tsvmenu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "menuitem")
public class Menuitem  implements Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	 @Column(name = "id")
	private Long id;
	
	@NotNull
	@Column(name = "name")
    private String name;

	@Column(name = "toplevel")
	private Boolean toplevel;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "priority")
	private int priority;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.REFRESH , mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Menuitem> childrens = new ArrayList<Menuitem>();
	
	@JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Menuitem parent;

	public Menuitem() {
	}
	
	public Menuitem(Long id) {
        this.id = id;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isToplevel() {
		return toplevel;
	}

	public void setToplevel(Boolean toplevel) {
		this.toplevel = toplevel;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<Menuitem> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Menuitem> childrens) {
		this.childrens = childrens;
	}

	public Menuitem getParent() {
		return parent;
	}

	public void setParent(Menuitem parent) {
		this.parent = parent;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String toString() {		
		return "Menuitem - Id: " + id + ", Name: " + name; 
	}		
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menuitem)) {
            return false;
        }
        Menuitem other = (Menuitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
