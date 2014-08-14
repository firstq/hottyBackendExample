package ru.serega2rikov.tsvmenu.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.serega2rikov.tsvmenu.domain.Menuitem;
import ru.serega2rikov.tsvmenu.repository.MenuitemRepository;
import ru.serega2rikov.tsvmenu.service.MenuitemService;
import com.google.common.collect.Lists;

@Service("menuitemService")
@Repository
@Transactional
public class MenuitemServiceImpl implements MenuitemService{

	@Autowired
	private MenuitemRepository menuitemRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Menuitem> findAll() {
		return Lists.newArrayList(menuitemRepository.findAll());
	}

	@Override
	@Transactional(readOnly=true)
	public Menuitem findById(Long id) {
		Menuitem m = menuitemRepository.findOne(id);
		return menuitemRepository.findOne(id);
	}

	@Override
	public Menuitem save(Menuitem menuitem) {
		return menuitemRepository.save(menuitem);
	}

	@Override
	public void delete(Menuitem menuitem) {
		menuitemRepository.delete(menuitem);
	}
	
}
