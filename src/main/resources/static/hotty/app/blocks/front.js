$(function() {
    console.log("$(document).ready");
    //Sliderspecial
    $(".slider-front").each(function(k,v){
        //Навес специального функционала
        console.log(v);
        $(v).sliderPlugin({isSpecial: $(v).attr("special")=="true",isSliderFade: $(v).attr("fade")=="true",isArrowsShow: $(v).attr("arrows")=="true",isVertical: $(v).attr("vertical")=="true",borderSize: 0,resizable: false,showContextmenu: false,dragndropable : false, thisBlock: $(v)});
        if($(v).attr("special")=="true"){

        }
    });
    //Menu
    $(".menu-front").each(function(k,v){

        var itemStylesObj = {};
        $(v).find("ul").find("li").each(function(i,j){

            itemStylesObj.navli = JSON.parse($(j).attr("navli"));
            itemStylesObj.navlia = JSON.parse($(j).children('a').attr("navlia"));
            itemStylesObj.navlia_hover = JSON.parse($(j).children('a').attr("navliahover"));
            itemStylesObj.navli_hover = JSON.parse($(j).attr("navlihover"));
        });
        //To DO: Передавать isVertical
        $(v).menuPlugin($.extend({borderSize: 0,resizable: false,showContextmenu: false,dragndropable : false, thisBlock:$(v)
        }, itemStylesObj));
        $(v).css("height","auto");
    });

    $(".sendmail-front").each(function(k,v){
        $(v).sendmailPlugin({borderSize: 0,resizable: false,showContextmenu: false,dragndropable : false, thisBlock:$(v)});
    });
});
