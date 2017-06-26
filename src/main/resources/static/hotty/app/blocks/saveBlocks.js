//Для новых блоков генерировать фейковые id УНИКАЛЬНЫЕ для данной страницы.
var generateId = function(min, max){
		var fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
		while($("#"+fakeId).length>0) fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
		return Math.uuidFast();
	},
	saveBlocks = function (container) {
	var result = {
		id: generateId(100000,999999),
		title: $("title").text(),
		path: "",
		style: typeof(container.attr("style"))!= "undefined" ? container.attr("style") : "",
		frontClases: typeof(container.attr("class"))!= "undefined" ? container.attr('class') : [],
		blocks: []
	};
	
	container.find("div").each(function(k,v){
		if(typeof($(v).attr("data-block")) != "undefined"){
			if(typeof($(v).attr("id"))== "undefined" || $(v).attr("id").indexOf("ui-id-") > -1){
				$(v).attr("id",generateId(100000,999999));
			}
			var parentId = $(v).parent().attr("id");
					//$(v).parent().parent().attr("data-block") == "slider" ? $(v).parent().parent().attr("id") : $(v).parent().attr("id");
			//Create json for simple
			var concreteProperties, addingBlock = {
				id: typeof($(v).attr("id"))!= "undefined" ? $(v).attr("id") : generateId(100000,999999),
				name: typeof($(v).attr("name"))!= "undefined" ? $(v).attr("name") : $(v).attr("data-block")+generateId(100000,999999),
				page: 1,
				frontClases: typeof($(v).attr("class"))!= "undefined" ? $(v).attr('class') : [],
				style: typeof($(v).attr("style"))!= "undefined" ? $(v).attr("style") : "",
				parent: parentId, //обработать случай слайдера
				topLevel: $(v).parent().get(0) === container.get(0),
				static: "",
				type: "Block"
			};
			switch ($(v).attr("data-block")) {
				case "accordeon":
					concreteProperties = {
						type: "AccordeonBlock",
						heightStyle: "fill",
						items: []
					};
				break;
				case "static":
					concreteProperties = {
						type: "StaticBlock"
					};
				break
				case "sendmail":
					concreteProperties = {
						type: "SendmailBlock",
						sendTo: "serega2rikov@me.com",
						sendFrom: "sergey.daquisto@gmail.com",
						capchaEnable: true,
					};
				break
				case "menu":
					//how to get nav options?
					var itemStylesStr = JSON.stringify({
							navli : {
				"display" : 'inline',
				'float' : 'left',
				'position':'relative',
			},
							navlia : {
				'display' : 'block',
				'padding':'1px',
				'text-decoration' : 'none',
				//'font-size' : '15px',
				//'color' : '#669999',
			},
							navlia_hover:{
				'display' : 'block',
				//'color' : '#454545',
				'font-size' : '15px',
				'padding' : '1px',
				'text-decoration' : 'none',
				
			},
							navli_hover : {
				"display" : 'inline',
				'float' : 'left',
				'position':'relative',
				'background' : 'red',
				'text-decoration' : 'none'
			}
						});
					concreteProperties = {
						type: "MenuBlock",
						isVertical: false,
						itemStyles: itemStylesStr=="" ? "{}" : itemStylesStr,
						items: []
					};
					//Search childrens (menuItems)
					$(v).find("ul").find("li").each(function(i,j){
						if(typeof($(j).attr("id"))!= "undefined"){
							$(j).attr("id",generateId(100000,999999));
						}
						
						var itemBlock = {
							id: typeof($(j).attr("id"))!= "undefined" ? $(j).attr("id") : generateId(100000,999999),
							name: $(j).find("a").text(),
							frontClases: typeof($(j).attr("class"))!= "undefined" ? $(j).attr('class') : [],
							style: typeof($(j).attr("style"))!= "undefined" ? $(j).attr("style") : "",
							parent: $(j).parent().get(0)==$(v).get(0) ? $(v).attr("id") : $(j).parent().parent().attr("id"),
							toplevel: $(j).parent().get(0)==$(v).children("ul").get(0),
							type: "menuItem",
							link: $(v).find("a").attr("href"),
							priority: $(v).find("ul").length-i
						};
						concreteProperties.items.push(itemBlock);
					});
				break
				case "slider":
					concreteProperties = {
						type: "SliderBlock",
						isVertical: false,
						isArrowsShow: false, //Find .nextArrow and .prevArrow isVisible
						isSliderShow: false,
						scrollContentStyle: $(v).attr("scroll-content-style"),
						scrollContentItemStyle: $(v).attr("scroll-content-item-style")
					};
				break
				case "text":
					concreteProperties = {
						type: "TextBlock",
						text: $(v).html()
					};
				break
				case "video":
					concreteProperties = {
						type: "VideoBlock",
						videoUrl: $(v).find("iframe").attr("src")
					};
				break
				case "image":
					concreteProperties = {
						type: "ImageBlock",
						originalImgPath: $(v).css("background-image")
					};
				break
				default:
			}
			addingBlock = $.extend(addingBlock, concreteProperties);
			result.blocks.push(addingBlock);
		}
	});
	return result;
 };
		