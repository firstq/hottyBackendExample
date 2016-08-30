(function( $ ){
    $.fn.blockPlugin = function(options){
		
		var clones = [];
		var self=$(this),
				inside = false;
		var setIsDeleted = function(element){
			
			if(typeof(element.attr("type")) != "undefined" && typeof(element.get(0).options) != "undefined"){
				element.get(0).options.isDelete = true;
				element.hide();
				element.children("div").each(function(k,v){
					setIsDeleted($(v));
				});
			}
		}
		//Refactor
		var defaultItemsParamClick = function(){
								options.popup.find('form').find('input[name="width"]').val(self.get(0).style.width);
								options.popup.find('form').find('input[name="height"]').val(self.css('height'));
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								
								options.popup.find('form').find('textarea[name="classes"]').text(self.attr('class'));
								
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}, 
			defaultItemsCopyClick = function(){
								var clone = self.clone().attr('id', '');
								document.body.context_menu_plugin_buffer={
									clone:clone, 
									options: options,
									childrens: options.prepareChildrensForCopy(self)
								}; 
								clone.children().remove();
								options.parentElement.hide();
							},
			defaultItemsPasteClick = function(){
								if(typeof(document.body.context_menu_plugin_buffer) != 'undefined'){
									var pastedElement = document.body.context_menu_plugin_buffer.clone.clone();
									options.pasteElement(pastedElement, self, document.body.context_menu_plugin_buffer);
								}
								options.parentElement.hide();
							},
			defaultItemsDeleteClick = function(){
								options.makeNewTopForLastOwnerChildrens(self);
								if(typeof(options.dependBlock) != "undefined"){
									options.dependBlock.get(0).options.parentElement.remove();
									options.dependBlock.get(0).options.popup.remove();
									options.dependBlock.remove();
								}
								//self.remove();
								options.isDelete = true;
								self.hide();
								//И всех детей
								self.children("div").each(function(k,v){
									setIsDeleted($(v));
								});
								options.parentElement.remove();
								options.popup.remove();
							},
			defaultItems = [
						{
							data:{name: "Параметры",
							click: defaultItemsParamClick}
						},
						{
							data:{name: "Добавить блок",
							click: function(){
								//Сначала реализовать функцию вставки, потом применить здесь вставляя НОВЫЙ элемент
								
								//Добавить item, через popup (см. меню)
								$("<div title='Добавить новый блок'></div>").append($("#addToSliderForm").clone().removeClass('hidden')).dialog({
									autoOpen: false,
									width: 500,
									buttons: [
										{
											text: "Ok",
											click: function() {
												//Добавление блока
												var type = $( this ).find('form').find('input[name="type"]:checked').val(),
														newId=generateId(100000,999999),
														newBlock = $("<div></div>").attr("type",type).css("height", self.height()-10+"px").css("width", self.width()-10+"px");
														options.pasteElement(newBlock, self, {});
												$( this ).remove();
											}
										},
										{
											text: "Выход",
											click: function() {
												$( this ).remove();
											}
										}
									]			
								}).dialog("open");
								options.parentElement.hide();

							}}
						},
						{
							data:{name: "Копировать",
							click: defaultItemsCopyClick}
						},
						{
							data:{name: "Вставить",
							click: defaultItemsPasteClick}
						},
						{
							data:{name: "Удалить",
							click: defaultItemsDeleteClick}
						}
					];
		options.items = $.extend(defaultItems, options.items); 
	
        var defaultOptions = {
			restUrl: "/blocks/",
			prepareChildrensForCopy: function(clone){
				var childrens = [];
				clone.children().each(function(){
					if(typeof($(this).attr("type"))!="undefined"){
						var childClone = $(this).clone().attr('id', '');
						childrens.push({
							clone: childClone,
							options: this.options,
							childrens: options.prepareChildrensForCopy($(this))
						});
						var notClearTypes = ["TextBlock", "VideoBlock"];
						if(!notClearTypes.includes($(this).attr("type"))) childClone.children().remove(); //Не очищать если это видео, текст, меню
					}
					
				});
				return childrens;
			},
			pasteElement: function(pastedElement, futureParent, buffer){
				var idn=options.generateId(100000,999999), top=options.makeNewTopForCurent(0,futureParent), child_level=parseInt(futureParent.attr('child_level'));
				pastedElement.attr('id', idn)
					.css("top",top+"px")
					.css("left","0px").attr("parent-id",futureParent.attr("id")).attr("child_level",++child_level);
				
				if(typeof(buffer.options) == "undefined" || typeof(buffer.options.pushAfter) == "undefined"){	
					pastedElement.appendTo(futureParent);
				} else if(buffer.options.pushAfter == null){
					futureParent.prepend(pastedElement);
				} else {
					buffer.options.pushAfter.after(pastedElement);
				}
				
				if(typeof(buffer.childrens) != "undefined" && buffer.childrens.length>0) $.each(buffer.childrens, function(k,v){
					var childElement = v.clone.clone();
					options.pasteElement(childElement, pastedElement, v);
				});
				var newOpt = {};
				newOpt.thisBlock = pastedElement;
				if(typeof(buffer.options) != "undefined"){
					newOpt.showContextmenu = buffer.options.showContextmenu;
					newOpt.dragndropable = buffer.options.dragndropable;
					newOpt.borderSize = buffer.options.borderSize;
					newOpt.resizable = buffer.options.resizable;
					newOpt.restUrl = buffer.options.restUrl;
				}
				switch (pastedElement.attr("type")) { //TO DO ДОбавлять меню?
					case "ImageBlock":
						newOpt.popup = $("<div id='imageBlock"+idn+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden'));
						pastedElement.imagePlugin(newOpt);
					break
					case "VideoBlock":
						newOpt.popup = $("<div id='videoBlock"+idn+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden'));
						pastedElement.videoPlugin(newOpt);
					break
					case "MenuBlock":
						newOpt.popup = $("<div id='menuBlock"+idn+"Popup' title='Параметры'></div>").append($('#menuForm').clone().removeClass('hidden'));
						pastedElement.menuPlugin(newOpt);
					break
					case "TextBlock":
						newOpt.savedText = typeof(buffer.options) != "undefined" ? buffer.options.savedText : "New text block";
						newOpt.popup = $("<div id='textBlock"+idn+"Popup' title='Параметры'></div>").append($('#textForm').clone().removeClass('hidden'));
						pastedElement.textPlugin(newOpt);
					break
					case "SliderBlock":
						if(typeof(buffer.options) != "undefined"){
							newOpt.scrollPaneStyle = buffer.options.scrollPaneStyle;
							newOpt.scrollBarStyle = buffer.options.scrollBarStyle;
							newOpt.scrollContentStyle = buffer.options.scrollContentStyle;
							newOpt.scrollContentItemStyle = buffer.options.scrollContentItemStyle;
						}
						newOpt.popup = $("<div id='sliderBlock"+idn+"Popup' title='Параметры'></div>").append($('#sliderForm').clone().removeClass('hidden'));
						newOpt.borderSize = 2;
						pastedElement.sliderPlugin(newOpt);
					break
					case "SendmailBlock":
						//newOpt.capchaEnable = buffer.options.capchaEnable;
						newOpt.popup = $("<div id='sendmailBlock"+idn+"Popup' title='Параметры'></div>").append($('#sendmailForm').clone().removeClass('hidden'));
						newOpt.borderSize = 2;
						pastedElement.sendmailPlugin(newOpt);
					break
					case "AccordeonBlock":
						newOpt.popup = $("<div id='accordeonBlock"+idn+"Popup' title='Параметры'></div>").append($('#accordeonForm').clone().removeClass('hidden'));
						newOpt.borderSize = 2;
						pastedElement.accordeonPlugin(newOpt);
					break
					case "MenuBlock":
						if(typeof(buffer.options) != "undefined"){
							newOpt.logo = buffer.options.logo;
							newOpt.nav = buffer.options.nav;
							newOpt.navli = buffer.options.navli;
							newOpt.navlia = buffer.options.navlia;
							newOpt.navlia_hover = buffer.options.navlia_hover;
							newOpt.navli_hover = buffer.options.navli_hover;
							newOpt.thisBlock = pastedElement;
						}
						newOpt.borderSize = 1;
						pastedElement.menuPlugin(newOpt);
					break
					default:
						newOpt.popup = $("<div id='simpleBlock"+idn+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden'));
						pastedElement.blockPlugin(newOpt);
				}
				if(typeof(buffer.options) != "undefined" && typeof(buffer.options.dependBlock) != "undefined") pastedElement.get(0).options.dependBlock = buffer.options.dependBlock;
			},
			generateId: function(min, max){
				//TO DO: Выгружать все id блоков и сравнивать с ними (В редакторе это можно себе позволить)
				var fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
				while($("#"+fakeId).length>0) fakeId = Math.floor(Math.random() * (max - min + 1)) + min;
				return fakeId;
			},
			itemsParamClick: defaultItemsParamClick,
			itemsCopyClick: defaultItemsCopyClick,
			itemsPasteClick: defaultItemsPasteClick,
			itemsDeleteClick: defaultItemsDeleteClick,
			makeNewTopForCurent: function(top, receiver){
				receiver.children().each(function(){
										if(typeof($(this).attr('id')) !== 'undefined' && $(this).css('float') !== 'right'){
											top=top-parseInt($(this).css("height").match(/\d+/));
										}
									});
				return 0;
			},
			makeNewTopForLastOwnerChildrens: function(movable){
				var isAfterMovable = false, newTop = parseInt(movable.css('height').match(/\d+/));
				movable.parent().children().each(function(){
					//Изменять только те, которые идут после целевого
					if(movable.get(0) === $(this).get(0)) isAfterMovable = true;
					if(typeof($(this).attr('id')) !== 'undefined' && isAfterMovable){
						$(this).css('top', '+='+newTop+"px");
					}
				});
				return 0;
			},
			showContextmenu: true,
			borderSize: 1,
			contextmenu: function(e){
				$(document.body).click();
				e.preventDefault();
				var offset = $(this).offset();
				var x = e.pageX;
				var y = e.pageY;
				options.parentElement.css("top",y+"px").css("left", x+"px").css("z-index","100500").toggle();
				if(options.parentElement.is(':visible')){
					self.css("border", options.borderSize+"px dashed #27e6ed");
				} else {
					self.css("border", options.borderSize+"px dashed #000");
				}
				e.stopPropagation();
			},
			dragndropable : true,
			resizable : true,
			popupWidth: 600,
			parentElement:$('<ul style="width:100px;position: absolute;"></ul>'),
            popup: $("<div id='"+self.attr('id')+"Popup' title='Параметры'></div>"),
			colors: $('#simpleColors'),
			items: defaultItems,
			dragAble : {
				drag: function() {
					if(inside){
						$(this).css("border", options.borderSize+"px solid #27e6ed");
					} else $(this).css("border", options.borderSize+"px dashed #27e6ed");
				},
				start: function() {
					$(this).css("border", options.borderSize+"px dashed #27e6ed");
				},
				stop: function() {
					$(this).css("border", options.borderSize+"px dashed #000");
				},
				containment: "parent", 
				scope: "block"
			},
			droppAble : {
				drop: function(eventb, uib) {
					//Если элемент уже в другом элементе надо ограничить scope его уровнем
					//this - элемент в который вставляем
					//uib.draggable.get(0) - элемент который вставляется
					uib.draggable.css("border", options.borderSize+"px dashed #000");
					var child_level = parseInt(uib.draggable.attr('child_level'));
					if($(this).attr("child_level") >= child_level && $(this).attr("id") !== uib.draggable.attr('parent-id')) {
						child_level++;
						var newDraggable = uib.draggable.attr("parent-id",$(this).attr("id")), top=options.makeNewTopForCurent(0,$(this));
						options.makeNewTopForLastOwnerChildrens(newDraggable);
						newDraggable.draggable(options.dragAble)
							.resizable({animateDuration: "fast"})
							.droppable(options.droppAble)
							.css("top",top+"px")
							.css("left","0px")
							.resizable({animateDuration: "fast"})
							//.blockPlugin({popup: $("<div id='"+uib.draggable.attr('id')+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden'))})
							.attr('child_level',child_level)
							.appendTo($(this));
						}
				},
				over: function(eventb, uib) {
//							uib.draggable.css("border", "1px solid #27e6ed");
					$(this).css("border", options.borderSize+"px solid #27e6ed");
					inside = true;
				},
				out: function(eventb, uib) {
					uib.draggable.css("border", options.borderSize+"px dashed #000");
					$(this).css("border", options.borderSize+"px dashed #000");
					inside = false;
				},
				tolerance: "fit",
				scope: "block"
			},
			popupOptions : {
				autoOpen: false,
				width: options.popupWidth+"px",
				buttons: [
					{
						text: "Ok",
						click: function() {
							var width = $( this ).find('form').find('input[name="width"]').val();

							if(width == '100%' || (width.indexOf('%') > -1 && parseInt(width.match(/\d+/)) > 100) 
									|| $( this ).find('form').find('input[name="stretch"]').is(':checked')){
								$( this ).find('form').find('input[name="width"]').val("100%");
								width = "100%"
								self.css("left", "0px").css('width', width);
							} else {
								self.css('width', width);
							}
							var height = $( this ).find('form').find('input[name="height"]').val();
							if(height == '100%' || (height.indexOf('%') > -1 && parseInt(height.match(/\d+/)) > 100)){
								height = '100%';
								self.css('height', height).css("top", "0px");
							} else self.css('height', height);

							var stick = $( this ).find('form').find('input[name="stick"]:checked').val();
							if(stick === 'top' || stick === 'left') self.removeClass('align_center').removeClass('align_right').css('position','relative').css(stick,'0px');
							if(stick === 'top'){
								self.prependTo(self.parent());
							}
							if(stick === 'right') self.addClass('align_right').css('left','0px');
							if(stick === 'center') self.addClass('align_center');

							//And colour
							if(typeof(options.popup.find('#showColors').css('background')) != 'undefined' && typeof(options.popup.find('#showColors').css('background')) != 'none'){
								self.css('background',options.popup.find('#showColors').css('background'));
							} else self.removeProp('background')

							$( this ).dialog( "close" );
						}
					},
					{
						text: "Выход",
						click: function() {
							$( this ).dialog( "close" );
						}
					}
				]
			}
        };
		options.popupOptions = $.extend(defaultOptions.popupOptions, options.popupOptions);
		options = $.extend(defaultOptions, options);
		
		if(options.dragndropable) self.draggable(options.dragAble).droppable(options.droppAble);
		
		options.popup.appendTo($(document.body)).dialog(options.popupOptions).find('.block_colors').click(function(e){
				options.colors.attr('checkerid', $(this).attr('id')).dialog('open');
				
			});
			//options.popup.css("top","0px").css("left","0px").css("z-index",100);
		$.each(options.items, function(k,v){
			
			
			if(v != null){
				options.parentElement.append($('<li>'+v.data.name+'</li>').click(v.data.click));
			} 
		});
		options.parentElement.menu();
		options.parentElement.appendTo($(document.body)).hide()
		
		if(options.showContextmenu) this.bind("contextmenu", options.contextmenu);
		
		$(document.body).mousedown(function(event){
			if ($(event.target).closest(options.parentElement).length === 0) {
				options.parentElement.hide();
				self.css("border", options.borderSize+"px dashed #000");
			}
		});
		
		
		if(options.colors !== "Not used"){
			options.colors.removeClass('hidden').dialog({autoOpen: false,width: 500,height: 400,
			buttons: [
						{
							text: "Очистить цвет",
							click: function() {
								options.popup.find('#'+options.colors.attr('checkerid')).css('background','none');
								$( this ).dialog( "close" );
							}
						},
						{
							text: "Отмена",
							click: function() {
								$( this ).dialog( "close" );
							}
						},

					]
				}).find('td').click(function(){
				options.popup.find('#'+options.colors.attr('checkerid')).css('background',$(this).attr('bgcolor'));
				options.colors.dialog('close');
			});
		}
		if(options.popup.find(".nav-tabs a").length>0){
			options.popup.find(".nav-tabs a").each(function(){
				$(this).attr("href", $(this).attr("href")+self.attr("id"));
			});
			options.popup.find(".tab-pane").each(function(){
				$(this).attr("id", $(this).attr("id")+self.attr("id"));
			});
			options.popup.find(".nav-tabs a").click(function(e){
				e.preventDefault();
				$(this).tab('show');
			  });
		}
		if(options.resizable) self.resizable({animateDuration: "fast"});
		self.css("border", options.borderSize+"px dashed #000");
		this.get(0).options = options; //Не надёжно!!!
		return options;
	}
})(jQuery)
	