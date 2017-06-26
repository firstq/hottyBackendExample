//Для новых блоков генерировать фейковые id УНИКАЛЬНЫЕ для данной страницы.
var savePage = function (container) {
	var result = {
		id: typeof(container.attr("id")) == "undefined" ? Math.uuidFast() : container.attr("id"),
		pageTitle: container.attr("pagetitle"),
		pagePath: container.attr("pagepath"),
		styles: typeof(container.attr("style"))!= "undefined" ? container.attr("style") : "",
		frontClasses: typeof(container.attr("class"))!= "undefined" ? container.attr('class') : "",
		siteId: container.attr("site"),
		blocks: [],
		delBlocks: []
	};
	
	container.find("div").each(function(k,v){
		if(typeof($(v).attr("type")) != "undefined"){
			if(typeof($(v).attr("id"))== "undefined"){
				$(v).attr("id",Math.uuidFast());
			}
			var parentId = $(v).parent().attr("id");
			var concreteProperties, addingBlock = {
				id: $(v).attr("id"),
				name: typeof($(v).attr("name"))!= "undefined" ? $(v).attr("name") : $(v).attr("type")+$(v).attr("id"),
				style: typeof($(v).attr("style"))!= "undefined" ? $(v).attr("style") : "",
				frontClasses: typeof($(v).attr("class"))!= "undefined" ? $(v).attr('class') : "",
				isTop: $(v).parent().get(0) === container.get(0),
				isStatic: false,
				tagType: "div",
				blockType: "Block",
				pageId: result.id,
				parentId: $(v).parent().get(0) === container.get(0) ? null : parentId, //обработать случай слайдера
				isDelete: $(v).get(0).options==undefined ? false : $(v).get(0).options.isDelete,
				restUrl: $(v).get(0).options==undefined ? "/hotty/blocks" : $(v).get(0).options.restUrl,
				dependItem: $('li[conected-slider-child="'+$(v).attr("id")+'"]').attr("id") //Двойная связь при привязке к слайдеру
			};
			switch ($(v).attr("type")) {
				//Это блок видный на каждой страничке и помечается галкой. Исключением будет 404
				/*case "StaticBlock":
					concreteProperties = {
						blockType: "StaticBlock"
					};
				break*/
				case "SendmailBlock":
					/*
					 * options.defaultMailTo = to.val();
					options.defaultMailFrom = from.val();
					 */
					addingBlock.blockType = "SendmailBlock";
					concreteProperties = {
						//blockType: "SendmailBlock", //!!
						sendTo: $(v).get(0).options.defaultMailTo,
						sendFrom: $(v).get(0).options.defaultMailFrom,
						capchaEnable: true,
					};
				break
				case "MenuBlock":
					//how to get nav options?
					
					var itemStylesStr = JSON.stringify({
							navli : $(v).get(0).options.navli,
							navlia : $(v).get(0).options.navlia,
							navlia_hover:$(v).get(0).options.navlia_hover,
							navli_hover : $(v).get(0).options.navli_hover
						});

					addingBlock.blockType = "MenuBlock";
					concreteProperties = {
						//blockType: "MenuBlock",
						isVertical: false,
						itemStyles: itemStylesStr=="" ? "{}" : itemStylesStr,
						logo: $("a#logotype").find("img").attr("src"),
						items: []
					};

					$(v).find("ul").find("li").each(function(i,j){
						if(typeof($(j).attr("id"))== "undefined"){
							$(j).attr("id",Math.uuidFast());
						}
						var itemBlock = {
							id: typeof($(j).attr("id"))!= "undefined" ? $(j).attr("id") : Math.uuidFast(),
							name: $(j).find("a").text(),
							frontClases: typeof($(j).attr("class"))!= "undefined" ? $(j).attr('class') : "",
							style: typeof($(j).attr("style"))!= "undefined" ? $(j).attr("style") : "",
							parent: $(j).parent().get(0)==$(v).get(0) ? $(v).attr("id") : $(j).parent().parent().attr("id"),
							toplevel: $(j).parent().get(0)==$(v).children("ul").get(0),
							type: "MenuItem",
							link: $(j).find("a").attr("href"),
							priority: $(j).attr('priority') != undefined ? $(j).attr('priority') : $(v).find("li").length-i,//!!!
							dependSliderBlock: $(j).attr("conected-slider-child"), //j.options.slideTO Как быть если это новый блок?
							dependSliderBlockType: $("#"+$(j).attr("conected-slider-child")).attr("type")
						};
						concreteProperties.items.push(itemBlock);
					});
				break
				case "SliderBlock":
					addingBlock.blockType = "SliderBlock";
					concreteProperties = {
						special: $(v).get(0).options.isSpecial,
						//blockType: "SliderBlock",
						isVertical: $(v).get(0).options.isVertical,//(typeof($(v).attr("is_vertical")) != "undefined") && $(v).attr("is_vertical")=='true',
						isArrowsShow: $(v).get(0).options.isArrowsShow,//$(v).attr("arrows") == "show", 
						isMouseScrolled: $(v).css("overflow") == "auto",
						isSliderShow: $(v).get(0).options.isSliderFade,
						scrollContentStyle: JSON.stringify($(v).get(0).options.scrollContentStyle),//$(v).attr("scroll-content-style"), //TO DO JSON.strinfity из options
						scrollContentItemStyle: JSON.stringify($(v).get(0).options.scrollContentItemStyle)//$(v).attr("scroll-content-item-style") //TO DO JSON.strinfity из options
					};
				break
				case "TextBlock":
					addingBlock.blockType = "TextBlock";
					concreteProperties = {
						//blockType: "TextBlock",
						text: $(v).find('div[contenteditable="true"]').html() //find(div[contenteditable])
					};
				break
				case "VideoBlock":
					addingBlock.blockType = "VideoBlock";
					concreteProperties = {
						//blockType: "VideoBlock",
						videoUrl: $(v).find("iframe").attr("src")
					};
				break
				case "ImageBlock":
					addingBlock.blockType = "ImageBlock";
					concreteProperties = {
						//blockType: "ImageBlock",
						originalImgPath: $(v).css("background-image")
					};
				break
				default:
					
			}
			//addingBlock = $.extend(addingBlock, concreteProperties);
			addingBlock.specialData = concreteProperties;
			if(addingBlock.isDelete) result.delBlocks.push(addingBlock);
			result.blocks.push(addingBlock);
		}
	});
	return result;
 };
		