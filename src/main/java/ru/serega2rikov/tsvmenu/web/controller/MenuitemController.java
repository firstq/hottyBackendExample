package ru.serega2rikov.tsvmenu.web.controller;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.serega2rikov.tsvmenu.domain.Menuitem;
import ru.serega2rikov.tsvmenu.domain.Menuitems;
import ru.serega2rikov.tsvmenu.service.MenuitemService;

@Controller
@RequestMapping(value="/menu")
public class MenuitemController {
	
	
	final Logger logger = LoggerFactory.getLogger(MenuitemController.class);
	
	@Autowired
	private MenuitemService menuitemService;

	
	@RequestMapping(value = "/listdata", method = RequestMethod.GET)
	@ResponseBody
	public Menuitems listData() {
		return new Menuitems(menuitemService.findAll());
	}	

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Menuitem findMenuitemById(@PathVariable Long id) {
		return menuitemService.findById(id);
	}
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	@ResponseBody
	public Menuitem create(@RequestBody @Valid Menuitem menuitem) {
		logger.info("Creating menuitem: " + menuitem);
		menuitemService.save(menuitem);
		logger.info("Menuitem created successfully with info: " + menuitem);
		return menuitem;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public void update(@RequestBody Menuitem menuitem, @PathVariable Long id) {
		logger.info("Updating menuitem: " + menuitem);
		menuitemService.save(menuitem);
		logger.info("Menuitem updated successfully with info: " + menuitem);
	}	

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable Long id) {
		logger.info("Deleting menuitem with id: " + id);
		Menuitem menuitem = menuitemService.findById(id);
		menuitemService.delete(menuitem);
		logger.info("Menuitem deleted successfully");
	}	
	
}
