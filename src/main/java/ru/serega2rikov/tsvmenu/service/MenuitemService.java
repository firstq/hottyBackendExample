package ru.serega2rikov.tsvmenu.service;

import java.util.List;
import ru.serega2rikov.tsvmenu.domain.Menuitem;
import org.springframework.data.repository.query.Param;

public interface MenuitemService {
	
	public List<Menuitem> findAll();
	
	public Menuitem findById(Long id);
	
	public Menuitem save(Menuitem menuitem);
	
	public void delete(Menuitem menuitem);
	
}
