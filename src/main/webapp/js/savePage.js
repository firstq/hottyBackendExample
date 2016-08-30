//Для новых блоков генерировать фейковые id УНИКАЛЬНЫЕ для данной страницы.
var generateId = function(min, max){
		var fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
		while($("#"+fakeId).length>0) fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
		return fakeId;
	},
	savePage = function (container) {
	var result = {
		id: typeof(container.attr("id")) == "undefined" ? generateId(100000,999999) : parseInt(container.attr("id")),
		title: $("title").text(),
		path: "",
		style: typeof(container.attr("style"))!= "undefined" ? container.attr("style") : "",
		frontClases: typeof(container.attr("class"))!= "undefined" ? container.attr('class') : "",
		blocks: [],
		delBlocks: []
	};
	
	container.find("div").each(function(k,v){
		if(typeof($(v).attr("type")) != "undefined"){
			if(typeof($(v).attr("id"))== "undefined"){
				$(v).attr("id",generateId(100000,999999));
			}
			var parentId = $(v).parent().attr("id");
			var concreteProperties, addingBlock = {
				id: $(v).attr("id"),
				name: typeof($(v).attr("name"))!= "undefined" ? $(v).attr("name") : $(v).attr("data-block")+$(v).attr("id"),
				page: 1,
				frontClases: typeof($(v).attr("class"))!= "undefined" ? $(v).attr('class') : "",
				style: typeof($(v).attr("style"))!= "undefined" ? $(v).attr("style") : "",
				parent: parentId, //обработать случай слайдера
				topLevel: $(v).parent().get(0) === container.get(0),
				static: "",
				type: "Block",
				isDelete: $(v).get(0).options==undefined ? false : $(v).get(0).options.isDelete,
				restUrl: $(v).get(0).options==undefined ? "/blocks" : $(v).get(0).options.restUrl,
				dependItem: $('li[conected-slider-child="'+$(v).attr("id")+'"]').attr("id") //Двойная связь при привязке к слайдеру
			};
			switch ($(v).attr("type")) {
				case "AccordeonBlock":
					concreteProperties = {
						type: "AccordeonBlock",
						heightStyle: "fill",
						//Сохранять связи
						//items: []
					};
					
				break;
				case "StaticBlock":
					concreteProperties = {
						type: "StaticBlock"
					};
				break
				case "SendmailBlock":
					/*
					 * options.defaultMailTo = to.val();
					options.defaultMailFrom = from.val();
					 */
					concreteProperties = {
						type: "SendmailBlock", //!!
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
					
					concreteProperties = {
						type: "MenuBlock",
						isVertical: false,
						itemStyles: itemStylesStr=="" ? "{}" : itemStylesStr,
						logo: $("a#logotype").find("img").attr("src"),
						items: []
					};
					//Search childrens (menuItems)
					$(v).find("ul").find("li").each(function(i,j){
						if(typeof($(j).attr("id"))== "undefined"){
							$(j).attr("id",generateId(100000,999999));
						}
						var itemBlock = {
							id: typeof($(j).attr("id"))!= "undefined" ? $(j).attr("id") : generateId(100000,999999),
							name: $(j).find("a").text(),
							frontClases: typeof($(j).attr("class"))!= "undefined" ? $(j).attr('class') : "",
							style: typeof($(j).attr("style"))!= "undefined" ? $(j).attr("style") : "",
							parent: $(j).parent().get(0)==$(v).get(0) ? $(v).attr("id") : $(j).parent().parent().attr("id"),
							toplevel: $(j).parent().get(0)==$(v).children("ul").get(0),
							type: "menuItem",
							link: $(v).find("a").attr("href"),
							priority: typeof($(j).attr('priority')) ? $(j).attr('priority') : $(v).find("li").length-i,//!!!
							dependSliderBlock: $(j).attr("conected-slider-child"), //j.options.slideTO Как быть если это новый блок?
							dependSliderBlockType: $("#"+$(j).attr("conected-slider-child")).attr("type")
						};
						concreteProperties.items.push(itemBlock);
					});
				break
				case "SliderBlock":
					
					concreteProperties = {
						special: $(v).get(0).options.isSpecial,
						type: "SliderBlock",
						isVertical: $(v).get(0).options.isVertical,//(typeof($(v).attr("is_vertical")) != "undefined") && $(v).attr("is_vertical")=='true',
						isArrowsShow: $(v).get(0).options.isArrowsShow,//$(v).attr("arrows") == "show", 
						isMouseScrolled: $(v).css("overflow") == "auto",
						isSliderShow: $(v).get(0).options.isSliderFade,
						scrollContentStyle: JSON.stringify($(v).get(0).options.scrollContentStyle),//$(v).attr("scroll-content-style"), //TO DO JSON.strinfity из options
						scrollContentItemStyle: JSON.stringify($(v).get(0).options.scrollContentItemStyle)//$(v).attr("scroll-content-item-style") //TO DO JSON.strinfity из options
					};
				break
				case "TextBlock":
					concreteProperties = {
						type: "TextBlock",
						text: $(v).find('div[contenteditable="true"]').html() //find(div[contenteditable])
					};
				break
				case "VideoBlock":
					
					concreteProperties = {
						type: "VideoBlock",
						videoUrl: $(v).find("iframe").attr("src")
					};
				break
				case "ImageBlock":
					concreteProperties = {
						type: "ImageBlock",
						originalImgPath: $(v).css("background-image")
					};
				break
				default:
					
			}
			addingBlock = $.extend(addingBlock, concreteProperties);
			if(addingBlock.isDelete) result.delBlocks.push(addingBlock);
			result.blocks.push(addingBlock);
		}
	});
	return result;
 };
		