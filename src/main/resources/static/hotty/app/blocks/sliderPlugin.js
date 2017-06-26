(function( $ ){
    $.fn.sliderPlugin = function(options){
		var 
		refreshScrollBar = function(bar,wrapper){
			var init = function(){
				bar.slider({
					min: 1,
					max: scrollContent.children().length,
					value: 1,
					slide: function( event, ui ) { //TO DO: в будущем настроить использование UI скрола
						if ( scrollContent.width() > self.width() ) {
							scrollContent.css( "margin-left", Math.round(
							ui.value / 100 * ( self.width() - scrollContent.width() )
							) + "px" );
						} else {
							scrollContent.css( "margin-left", 0 );
						}
					},
					change: function( event, ui ) { 
						var fade = options.isSliderFade;
						
						if(fade) scrollContent.fadeOut();
						if ( scrollContent.width() > self.width() || scrollContent.height() > self.height() ) {
							var newPosition, animationParams={};
							/*
								Логика поведения скрола
								при переходе к указанному элементу слайдера, берется его top (left в сулчае вертикального)
								и это значение присваивается в margin scrollContent элемента.
								при абсолютном значении position это видимо даёт сбой и неконтролируемое увеличение margin контейнера
								до непредвид размеров.

							scrollContent.css({
								marginLeft: "0px",
								marginTop: "0px"
							});*/
							if(options.isVertical){
								newPosition = $(scrollContent.children()[bar.slider("value")-1]).position().top;
								animationParams.marginTop = parseInt(scrollContent.css('margin-top'), 10)-newPosition+"px";
								console.log("newPosition="+newPosition);
								console.log("animationParams.marginTop="+animationParams.marginTop);
								console.log("animationParams.scrollTop="+animationParams.scrollTop);
							} else {
								newPosition = $(scrollContent.children()[bar.slider("value")-1]).position().left;
								animationParams.marginLeft = parseInt(scrollContent.css('margin-left'), 10)-newPosition+"px";

							}
							scrollContent.animate(animationParams,300);
							//TODO: Разрулить
							//$(scrollContent.children()[bar.slider("value")-1]).get(0).scrollIntoView();
						} else {
							scrollContent.css( "margin-left", 0 );
							scrollContent.css( "margin-top", 0 );
						}
						if(fade) scrollContent.fadeIn();
					}
				});
			}
			try { 
				bar.slider("destroy");
				init();
			} catch (e){
				init();
			} finally {
				wrapper.append(bar);
				//На исходную
				self.scrollLeft(0);
				self.scrollTop(0);
				scrollContent.css( "margin-left", 0 );
				scrollContent.css( "margin-top", 0 );
			}
		},
		//defaultItemsParamClick = 
		calculateNewWidthForChildrens = function(){
			var itemGrade = options.scrollContentItemStyle.width.includes("%") ? "%" : "px",
					goodWidth = parseInt(options.scrollContentItemStyle.width.replace(/\D+/g,''))*scrollContent.children().length;
			if(itemGrade=="%"){
				var oldItemCount = typeof(options.itemsCount) != "undefined" ? options.itemsCount : Math.round(parseInt(scrollContent.css("width").replace(/\D+/g,'')) / parseInt($(scrollContent.children()[0]).css("width").replace(/\D+/g,''))-0.5),
						targetWithInPercent=null;
				if(options.scrollContentStyle.width.includes("%") && scrollContent.children().length > 0){
					targetWithInPercent = Math.round(parseInt(options.scrollContentStyle.width.replace(/\D+/g,''))/oldItemCount);
				}
				options.scrollContentItemStyle.width = Math.round(87/scrollContent.children().length-0.5) + itemGrade; //(parseInt(options.scrollContentItemStyle.width.toString().replace(/\D+/g,''))
				if(targetWithInPercent != null){
					options.scrollContentStyle.width = Math.round(targetWithInPercent*scrollContent.children().length)+"%";
					scrollContent.css(options.scrollContentStyle);
				}
				
			} else if(scrollContent.width()<goodWidth) {
				options.scrollContentStyle.width = scrollContent.width()+goodWidth;
				scrollContent.css(options.scrollContentStyle);
			}
			options.itemsCount = scrollContent.children().length;
		};
		
		
		var setContentItemStyles = function(){
			self.children("div.scroll-content").children().each(function(){
				$(this).css(options.scrollContentItemStyle);
			});
			
		},
		defaultOptions = {
			isSpecial: false,
			restUrl: "/sliderblocks/",
			isSliderFade: false,
			isArrowsShow: true,
			isVertical: false,
			pasteElementToSlider: function(element, buffer){
				var newId=Math.uuidFast();
				element.attr("id",newId);
				if(typeof(buffer.childrens) != "undefined" && buffer.childrens.length>0) $.each(buffer.childrens, function(k,v){
					//Что если это scrollContent? А скорее всего это он
					var childElement = v.clone.clone().attr("child_level", parseInt(element.attr('child_level'))+1);
					//{showContextmenu: false, borderSize: 0, resizable: false} //для scrollContent
					if(typeof(v.options) != "undefined"){
						v.showContextmenu = false;
						v.dragndropable = false;
						v.borderSize = 0;
						v.resizable = false;
					}
					options.pasteElement(childElement, element, v);
				});
				
				var newOptions = {dragndropable : false,borderSize: 1,resizable:false,thisBlock:element, restUrl: typeof(buffer.options) != "undefined" ? buffer.options.restUrl : "/blocks/"};
				switch (element.attr("type")) {
					case "Block":
						newOptions.popup = $("<div id='simpleBlock"+newId+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
									);
						element.blockPlugin(newOptions);
						break
					case "ImageBlock":
						newOptions.popup = $("<div id='imageBlock"+newId+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
									);
						element.imagePlugin(newOptions);
						break
					case "VideoBlock":
						newOptions.popup = $("<div id='videoBlock"+newId+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
									);
						element.videoPlugin(newOptions);
						break
					case "TextBlock":
						newOptions.savedText = typeof(buffer.options) != "undefined" ? buffer.options.savedText : "New text block";
						newOptions.popup = $("<div id='"+newId+"Popup' title='Параметры'></div>").append(
										$('#textForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
									);
						element.textPlugin(newOptions);
						break
					case "SliderBlock":
						newOptions.borderSize = 2;
						if(typeof(buffer.options) != "undefined"){
							newOptions.scrollPaneStyle = buffer.options.scrollPaneStyle;
							newOptions.scrollBarStyle = buffer.options.scrollBarStyle;
							newOptions.scrollContentStyle = buffer.options.scrollContentStyle;
							newOptions.scrollContentItemStyle = buffer.options.scrollContentItemStyle;
						}
						newOptions.popup = $("<div id='sliderBlock"+newId+"Popup' title='Параметры'></div>").append($('#sliderForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
										);
						element.sliderPlugin(newOptions);
						break
					case "SendmailBlock":
							//newOptions.capchaEnable = buffer.options.capchaEnable;
							newOptions.popup = $("<div id='sendmailBlock"+newId+"Popup' title='Параметры'></div>").append($('#sendmailForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end());
							element.sendmailPlugin(newOptions);
						break
					case "AccordeonBlock":
						newOptions.popup = $("<div id='accordeonBlock"+newId+"Popup' title='Параметры'></div>").append($('#accordeonForm').clone().removeClass('hidden')
										.find('input[name="width"]').prop('disabled', true).end()
										.find('input[name="height"]').prop('disabled', true).end()
										.find('input[name="stretch"]').prop('disabled', true).end()
									);
						newOptions.borderSize = 2;
						element.accordeonPlugin(newOptions);
						break
					case "MenuBlock":
						if(typeof(buffer.options) != "undefined"){
							newOptions.logo = buffer.options.logo;
							newOptions.nav = buffer.options.nav;
							newOptions.navli = buffer.options.navli;
							newOptions.navlia = buffer.options.navlia;
							newOptions.navlia_hover = buffer.options.navlia_hover;
							newOptions.navli_hover = buffer.options.navli_hover;
							newOptions.thisBlock = element;
						}
						newOptions.borderSize = 1;
						element.menuPlugin(newOptions);
						break
					default:
				}

				element.css(options.scrollContentItemStyle);

				scrollContent.append(element);
				
				if(!options.isVertical){
					calculateNewWidthForChildrens();
				}
				///////
				setContentItemStyles();
				refreshScrollBar(scrollbar, scrollBarWrapper);
			},
			thisBlock : $(this),
			resizable : true,
			parentElement:$('<ul style="width:100px;position: absolute;"></ul>'),
            popup: $("<div id='"+options.thisBlock.attr('id')+"Popup' title='Параметры'></div>"),
			colors: $('#simpleColors'),
			scrollPaneStyle: { 
				overflow: "hidden",
			//	width: "99%",
				float: "left", 
			},
			scrollBarStyle: { display: "none" },
			//Сохранять в сущности
			scrollContentStyle: { 
				float: "left" 
			},
			//Сохранять в сущности
			scrollContentItemStyle: {
				width: "300px",
				height: "300px",
				float: "left",
				margin: "10px",
			},
			
			items: [
						{
							data:{name: "Параметры",
							click: function(){
								options.popup.find('form').find('input[name="width"]').val(self.get(0).style.width);
								options.popup.find('form').find('input[name="height"]').val(self.get(0).style.height);
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.find('form').find('textarea[name="classes"]').text(self.attr('class'));
								//TO DO: Загрузить форму данными
								//Тип: ITEM *float left/none или scrollContent width: 99% + СТРЕЛКИ ВВЕРХ и ВНИЗ
//								if(options.isVertical){
//									options.popup.find('form').find('input[name="align"]').val("vertical");
//								} else options.popup.find('form').find('input[name="align"]').val("gorizontal");
								options.popup.find('form').find('input[name="align"]').prop('checked',options.isVertical)
								//Показывать скроллер: scrolPane: OVERFLOW: auto/hidden scroller
								options.popup.find('form').find('input[name="scroller"]').prop('checked', (self.css("overflow") == "auto"));
								//Показывать стрелки:
								options.popup.find('form').find('input[name="arrows"]').prop('checked', options.isArrowsShow);
								
								//Мерцание:
								options.popup.find('form').find('input[name="fade"]').prop('checked', options.isSliderFade);
								
								options.popup.find('form').find('input[name="special"]').prop('checked', options.isSpecial);
								
								//Заполняем таб для детей Ширина: Высота: Растянуть: Отступ: Сверху,Слева, Справа, Снизу
								options.popup.find('form').find('input[name="itemWidth"]').val(options.scrollContentItemStyle.width);
								options.popup.find('form').find('input[name="itemHeight"]').val(options.scrollContentItemStyle.height);
								//MARGIN
								var getMarginFromObject = function(styleObject, marginType){
									switch (marginType){
										case "margin-top": 
											if(typeof(styleObject.marginTop) != "undefined") return styleObject.marginTop; 
										case "margin-right":
											if(typeof(styleObject.marginRight) != "undefined") return styleObject.marginRight;
										case "margin-bottom":
											if(typeof(styleObject.marginBottom) != "undefined") return styleObject.marginBottom; 
										case "margin-left":
											if(typeof(styleObject.marginLeft) != "undefined") return styleObject.marginLeft;
									}
									return styleObject.margin;
								};
								options.popup.find('form').find('input[name="itemTopMargin"]').val(getMarginFromObject(options.scrollContentItemStyle, "margin-top"));
								options.popup.find('form').find('input[name="itemLeftMargin"]').val(getMarginFromObject(options.scrollContentItemStyle, "margin-left"));
								options.popup.find('form').find('input[name="itemRightMargin"]').val(getMarginFromObject(options.scrollContentItemStyle, "margin-right"));
								options.popup.find('form').find('input[name="itemBottomMargin"]').val(getMarginFromObject(options.scrollContentItemStyle, "margin-bottom"));
								
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Добавить блок",
							click: function(){
								//Добавить item, через popup (см. меню)
								if(options.isSpecial){
									var newBlock = $("<div></div>").attr("type","ImageBlock").attr("child_level",parseInt(scrollContent.attr('child_level'))).css("height", self.height()+"px");
									options.pasteElementToSlider(newBlock, {});
								} else {
									var addToSliderForm = $("#addToSliderForm").clone().removeClass('hidden');
									$("<div title='Добавить новый блок'></div>").append(addToSliderForm).dialog({
										autoOpen: false,
										width: 500,
										buttons: [
											{
												text: "Ok",
												click: function() {
													//Добавление блока
													var type = $( this ).find('form').find('input[name="type"]:checked').val(),
															newBlock = $("<div></div>").attr("type",type).attr("child_level",parseInt(scrollContent.attr('child_level'))).css("height", self.height()+"px");
													options.pasteElementToSlider(newBlock, {});
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
								}
								options.parentElement.hide();
							}}
						},
						undefined,
						{
							data:{name: "Вставить",
							click: function(){
								if(typeof(document.body.context_menu_plugin_buffer) != 'undefined'){
									//TO DO: Common function for add and paste block
									var pastedElement = document.body.context_menu_plugin_buffer.clone.clone().attr("child_level",parseInt(scrollContent.attr('child_level')));
									options.pasteElementToSlider(pastedElement, document.body.context_menu_plugin_buffer);	
								}
								options.parentElement.hide();
							}}
						}
					],
					
					droppAble : {
						drop: function(eventb, uib) {
							//Если элемент уже в другом элементе надо ограничить scope его уровнем
							uib.draggable.css("border", "1px dashed #000");
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
							$(this).css("border", "1px solid #27e6ed");
							inside = true;
						},
						out: function(eventb, uib) {
							uib.draggable.css("border", "1px dashed #000");
							$(this).css("border", "1px dashed #000");
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
									
									options.isVertical = options.popup.find('form').find('input[name="align"]').prop('checked');
									
									if(options.isVertical){
										options.scrollContentStyle.width = "99%"
										scrollContent.css(options.scrollContentStyle);
									} else {
										if(self.get(0).style.width.includes("%") && options.scrollContentItemStyle.width.includes("%")){
											options.scrollContentStyle.width = Math.round(99*scrollContent.children().length*1.1)+"%";
										} else options.scrollContentStyle.width = Math.round(parseInt(options.scrollContentItemStyle.width.replace(/\D+/g,''))*scrollContent.children().length)+"px"
										calculateNewWidthForChildrens(); //!!
									}
									
									//Показывать скроллер: scrolPane: OVERFLOW: auto/hidden scroller
									var overflowVal = options.popup.find('form').find('input[name="scroller"]').prop('checked') ? "auto" : "hidden";
									self.css("overflow",overflowVal);
									//Показывать стрелки:
									options.isArrowsShow = options.popup.find('form').find('input[name="arrows"]').prop('checked');
									self.children('a').remove();
									if(options.isArrowsShow) {
										if(options.isVertical){
											prepareArrows(up,down);
										} else {
											prepareArrows(prev,next);
										}
									}
									
									//Мерцание:
									options.isSliderFade = options.popup.find('form').find('input[name="fade"]').prop('checked');
									
									//Заполняем таб для детей Ширина: Высота: Растянуть: Отступ: Сверху,Слева, Справа, Снизу
									options.scrollContentItemStyle.width = options.popup.find('form').find('input[name="itemWidth"]').val();
									options.scrollContentItemStyle.height = options.popup.find('form').find('input[name="itemHeight"]').val();
									if(options.popup.find("form").find('input[name="itemStretch"]').prop("checked")) options.scrollContentItemStyle.width = self.get(0).style.width;
									//MARGIN
									options.scrollContentItemStyle.marginBottom = options.popup.find('form').find('input[name="itemBottomMargin"]').val();
									options.scrollContentItemStyle.marginTop = options.popup.find('form').find('input[name="itemTopMargin"]').val();
									options.scrollContentItemStyle.marginLeft = options.popup.find('form').find('input[name="itemLeftMargin"]').val();
									options.scrollContentItemStyle.marginRight = options.popup.find('form').find('input[name="itemRightMargin"]').val();
									setContentItemStyles();
									refreshScrollBar(scrollbar, scrollBarWrapper);
									
									//Special
									options.isSpecial = options.popup.find('form').find('input[name="special"]').prop('checked');
									if(options.isSpecial){
										//Поиск и удаление всех детей, которые НЕ ImageBlock
										scrollContent.children().each(function(){
											if($(this).attr("type") != "ImageBlock") $(this).remove();
										});
									}
									
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
		var self = $(this), inside=false, files, 
				scrollContent = self.children("div.scroll-content").length == 0 
				? $('<div class="scroll-content"></div>').attr("type","Block").appendTo(self) 
				: self.children("div.scroll-content");
		defaultOptions.scrollContentItemStyle.height = scrollContent.height()+"px";
		options.scrollContentItemStyle = $.extend(defaultOptions.scrollContentItemStyle, options.scrollContentItemStyle);
		options = $.extend(defaultOptions, options);
		
		if(!options.isArrowsShow) options.scrollPaneStyle.overflow = self.css("overflow");
		options = self.css(options.scrollPaneStyle).blockPlugin(options);
		if(options.showContextmenu) self.attr("scroll-content-item-style", JSON.stringify(options.scrollContentItemStyle))
										.attr("scroll-content-style", JSON.stringify(options.scrollContentStyle));
			
	
		scrollContent.css(options.scrollContentStyle);
		scrollContent.blockPlugin({showContextmenu: false, borderSize: 0, resizable: false, dragndropable : false});
		
		//Развешиваем слайдерские стили, вставляем бегунок куда надо и т.д.
		if(options.showContextmenu) setContentItemStyles();
		//Стрелки слайдера 
		var prev = $('<a href="javascript:void(0);" class="prev"></a>'),
			next=$('<a href="javascript:void(0);" class="next"></a>'),
			up = $('<a href="javascript:void(0);" class="up"></a>'),
			down=$('<a href="javascript:void(0);" class="down"></a>');
		
		if(174 >= self.height()) prev.css("margin-top","0px");
		if(174 >= self.height()) next.css("margin-top","0px");
		
		var scrollBarWrapper=$( '<div class="scroll-bar-wrap ui-widget-content ui-corner-bottom" style="border:0;"></div>' ),
			scrollbar = $('<div class="scroll-bar"></div>');
		//для доступа к scrollbar  через options
		options.scrollbar = scrollbar;
		var prepareArrows = function(f,s){
			self.prepend(f).append(s);
			f.hide();
			s.hide();
			self.hover(
				function() {f.fadeIn();s.fadeIn();},
				function() {f.fadeOut();s.fadeOut();}
			);
			s.click(function () {
				scrollbar.slider( "value", scrollbar.slider("value") + 1 );
			});
			f.click(function () {
				scrollbar.slider( "value", scrollbar.slider("value") - 1 );
			});
		}
		
		if(options.isArrowsShow){
			if(options.isVertical){
				prepareArrows(up,down);
			} else prepareArrows(prev,next);
		}
		refreshScrollBar(scrollbar, scrollBarWrapper);
		
		if(options.showContextmenu) scrollContent.children().each(function(){
			if(!$(this).hasClass("ui-resizable-handle") && $(this).get(0).options != undefined){
				console.log($(this).get(0));
				$(this).get(0).options.popup
						.find('input[name="width"]').prop('disabled', true).end()
						.find('input[name="height"]').prop('disabled', true).end()
						.find('input[name="stretch"]').prop('disabled', true).end();
			}
		});
		//или показывать стрелки или скроллер
		options.popup.find('input[name="scroller"]').click(function(){
			if($(this).prop('checked')){
				options.popup.find('input[name="arrows"]').prop('checked',false);
			}
		});
		options.popup.find('input[name="arrows"]').click(function(){
			if($(this).prop('checked')){
				options.popup.find('input[name="scroller"]').prop('checked',false);
			}
		});
		
		options.itemsCount = scrollContent.children().length;
		
	}})(jQuery)