package su.hotty.editor.util;

import java.util.*;
import su.hotty.editor.domain.*;
import su.hotty.editor.repository.*;
import su.hotty.editor.service.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;

public class FrontHelper {

    @Autowired
    BlockRepository blockRepository;

    public static String buildMenuTree(List<Map<String,Object>> menu, boolean isMenu, JSONObject itemStyles) {
        StringBuffer result = new StringBuffer(menu.size());
//        List<MenuItem> childrens = null;
        result.append(isMenu
                ? "<ul id=\"nav\" class=\"align_center nav navbar-nav navbar-collapse collapse out\" style='font-weight: normal; margin-top: 10px;'>"
                : "<ul>");
        for (Map<String,Object> m : menu) {
            // onmouseover='$.extend(this.style,itemStyles.getOrDefault("navli", "{}")' onmouseout='$.extend(this.style,itemStyles.getOrDefault("navli_hover", "{}")'
            result.append("<li id='" + m.get("id") + "' "
                    + "csc='" + m.get("dependSliderBlock") + "' "
                    + "navlihover='" + itemStyles.get("navli_hover") + "' "
                    + "navli='" + itemStyles.get("navli") + "' "
                    + "conected-slider-child='" + m.get("dependSliderBlock") + "'>"
                    + "<a href='" + m.get("link") + "'"
                    + "navliahover='" + itemStyles.get("navlia_hover") + "' "
                    + "navlia='" + itemStyles.get("navlia") + "' >" + m.get("name") + "</a>");
 /*           childrens = MenuItem.findMenuItemsByParent(m);
            //childrens = menuItemRepository.findAllByParentItem(m);
            if (!childrens.isEmpty() && childrens.size() > 0)
                result.append(FrontHelper.buildMenuTree(childrens, false, itemStyles));*/
            result.append("</li>");
        }
        result.append("</ul>");
        return result.toString();
    }


    public static String buildPage(List<Block> blocks) {
        StringBuilder result = new StringBuilder();
    //    List<MenuItem> childrens = null;
        for (Block block : blocks) {
            String classes = block.getFrontClasses(), innerText = "", helpAttributes = "";
            switch (block.getBlockType()) {
                case "StaticBlock":
                    classes += " static-front";
                    break;
                case "MenuBlock":
                    classes += " menu-front";
                    List<Map<String,Object>> menu = (List<Map<String,Object>>)block.getSpecialData().get("items");
                    Map<String,Object> menuBlock = block.getSpecialData();
                    //TO DO If it contains logo (block.getLogo().isEmpty())
                    if (menuBlock.get("logo") != null && !menuBlock.get("logo").toString().isEmpty())
                        innerText = "<div class=\"navbar-header menu-helper\">"
                                + "<button type=\"button\" class=\"navbar-toggle menu-helper-button\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">"
                                + "<span class=\"sr-only\">Toggle navigation</span>"
                                + "<span class=\"icon-bar\"></span>"
                                + "<span class=\"icon-bar\"></span>"
                                + "<span class=\"icon-bar\"></span>"
                                + "</button><a class=\"navbar-brand menu-helper-logo\" href=\"/\" id=\"logotype\"><img alt=\"\" class=\"b-logo b-logo_index png\" src=\" " + menuBlock.get("logo") + " \"/></a></div>";

                    innerText += FrontHelper.buildMenuTree(menu, true, new JSONObject(menuBlock.get("itemStyles").toString()));
                    break;
                case "SliderBlock":
                    classes += " slider-front";
                    helpAttributes = "arrows='"
                            + block.getSpecialData().get("isArrowsShow")
                            + "' fade='" + block.getSpecialData().get("isSliderShow") + "' vertical='"
                            + block.getSpecialData().get("isVertical") + "'"
                            + "' special='" + block.getSpecialData().get("Special") + "'";
                    break;
                case "TextBlock":
                    classes += " text-front";
                    innerText += block.getSpecialData().get("text");
                    break;
                case "VideoBlock":
                    classes += " video-front";
                    //width=\"101%\" height=\"400px\"
                    innerText = "<iframe width=\"101%\" src=\"" + block.getSpecialData().get("videoUrl") + "\" frameborder=\"0\"></iframe>";
                    break;
                case "ImageBlock":
                    classes += " image-front";
                    break;
                case "SendmailBlock":
                    classes += " sendmail-front";
                    //Содержимое добавлять на клиенте в light версии
                    break;

            }
            String tagStr = "<div class='" + classes + " ' style=' " + block.getStyles() + "' id='" + block.getId() + "' " + helpAttributes + ">" + innerText;
            if (block.getChildren() != null && block.getChildren().size() > 0) tagStr += buildPage(block.getChildren());
            result.append(tagStr);
            result.append("</div>");
        }
        return result.toString();
    }

}