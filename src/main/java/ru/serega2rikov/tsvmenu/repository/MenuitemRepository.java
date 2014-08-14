package ru.serega2rikov.tsvmenu.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.serega2rikov.tsvmenu.domain.Menuitem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuitemRepository  extends CrudRepository<Menuitem, Long> {
	
}
