package ru.serega2rikov.tsvmenu.domain;

import java.io.Serializable;
import java.util.List;

public class Menuitems implements Serializable {

	private List<Menuitem> menuitems;

	public Menuitems() {
	}
	
	public Menuitems(List<Menuitem> menuitems) {
		this.menuitems = menuitems;
	}
	
	public List<Menuitem> getMenuitems() {
		return menuitems;
	}

	public void setMenuitems(List<Menuitem> menuitems) {
		this.menuitems = menuitems;
	}
	 
}
