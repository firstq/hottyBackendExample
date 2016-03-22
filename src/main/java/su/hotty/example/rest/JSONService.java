package su.hotty.example.rest;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.example.domain.*;

@Path("/service")
@Transactional
public class JSONService {
	
	@GET
	@Path("/blocks")
	@Produces(MediaType.APPLICATION_JSON)
	public List<? extends Block> getBlocks() {
		List<? extends Block> blocks = Block.findAllBlocks();
		return blocks;
	}
	
	@GET
	@Path("/image/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageBlock getImage(@PathParam("id") Long id) {
		return ImageBlock.findImageBlock(id);
	}
	
	@GET
	@Path("/video/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public VideoBlock getVideo(@PathParam("id") Long id) {
		return VideoBlock.findVideoBlock(id);
	}
	
	@GET
	@Path("/text/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TextBlock getText(@PathParam("id") Long id) {
		return TextBlock.findTextBlock(id);
	}
	
	@GET
	@Path("/menu/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MenuBlock getMenu(@PathParam("id") Long id) {
		MenuBlock mb = MenuBlock.findMenuBlock(id);
		System.out.println("mb="+mb);
		return mb;
	}
	
	@GET
	@Path("/slider/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SliderBlock getSlider(@PathParam("id") Long id) {
		return SliderBlock.findSliderBlock(id);
	}
	
	@GET
	@Path("/menuitems/{menuId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MenuItem> getMenuitems(@PathParam("menuId") Long menuId) {
		List<MenuItem> its = MenuItem.findAllMenuItemsByParentBlockId(menuId);
		for (MenuItem it : its) {
			System.out.println("ITEM="+it);
		}
		return its;
	}
	
	@GET
	@Path("/menuitem/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MenuItem getMenuitem(@PathParam("menuId") Long id) {
		return MenuItem.findMenuItem(id);
	}
}