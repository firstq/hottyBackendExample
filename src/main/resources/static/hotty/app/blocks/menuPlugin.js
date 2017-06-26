(function( $ ){
    $.fn.menuPlugin = function(options){

		var setSlideTo = function(preparedElement, currentSlider, childIndex){
			preparedElement.unbind( "click" ).attr("conected-slider-child","");
			preparedElement.attr("conected-slider-child",$(currentSlider.children().children()[childIndex-1]).attr("id"));
			preparedElement.click(function(){
				//Заставить при клике скролить к нужному элементу
				currentSlider.get(0).options.scrollbar.slider( "value", childIndex);
			});
			
												
		}
        var defaultOptions = {
			restUrl: "/menublocks/",
			showContextmenu: true,
			sliders: $(".scroll-pane"),
			menuHelper: $('<div class="navbar-header menu-helper"></div>'),
			menuHelperButton: $('<button type="button" class="navbar-toggle menu-helper-button" data-toggle="collapse" data-target=".navbar-collapse">'+
				'<span class="sr-only">Toggle navigation</span>'+
				'<span class="icon-bar"></span>'+
				'<span class="icon-bar"></span>'+
				'<span class="icon-bar"></span>'+
			  '</button>'),
			menuHelperLogo: $('<a class="navbar-brand menu-helper-logo" href="/" id="logotype"></a>'),
			
			menuItemsContainer: $('<ul class="align_center"> </ul>'),
			
			setFontStyles : function(element){
								//Push hover-style
//								element.attr("hover-style",JSON.stringify(options.navli_hover));
//								element.find('a').attr("hover-style",JSON.stringify(options.navlia_hover));
								element.find('a').css(options.navlia);
								$.each(element.children('ul').children('li'), function(k,v){options.setFontStyles($(v))});
							},
			appendByPriority : function(targetParent, element, isTop){
				if(targetParent.children('ul').length === 0){
					targetParent.append($("<ul></ul>").css(options.navul));
				}
				
				var added = false;
				$.each(targetParent.children('ul').children('li'), function(k,v){
					//Чем выше приоритет, тем раньше ставим пункт меню относительно других
					if(parseInt($(v).attr('priority').toString().replace(/\D+/g,''))<parseInt(element.attr('priority').toString().replace(/\D+/g,''))){
						if(element.next().length != 0 && element.next()[0].localName == "br") element.next().remove();
						$(v).before(element);
						if(!isTop) element.after("<br/>");
						added=true;
						return false;
					}
				});
				if(!added){
					if(element.next().length != 0 && element.next()[0].localName == "br") element.next().remove();
					targetParent.children('ul').append(element);
					if(!isTop) element.after("<br/>");
				}
			},
			activateElements : function(menuElement){
				var v = {
					id : menuElement.attr("id"),
					name : menuElement.children('a').text(),
					link : menuElement.children('a').attr("href"),
					priority : menuElement.attr('priority'),
					toplevel : menuElement.parent().hasClass('align_center')
				};
				options.prepareElement(menuElement,v);
				$.each(menuElement.children('ul').children('li'), function(k,v){ options.activateElements($(v)) });
			},
			prepareElement : function(elem,v){
				var preparedElement = elem;
				
				if((typeof(v.dependSliderBlock)!="undefined" && v.dependSliderBlock != null) 
				|| (typeof(preparedElement.attr("conected-slider-child"))!="undefined" && preparedElement.attr("conected-slider-child")!='null' && preparedElement.attr("conected-slider-child")!="") ){
					var sliderChild = $('#'+(v.dependSliderBlock || preparedElement.attr("conected-slider-child") || preparedElement.attr("csc"))),//csc
						currentSlider = sliderChild.parent().parent();
					var index = -1;
					sliderChild.parent().children("div").each(function(k,v){
						if(v==sliderChild.get(0)) index=k;
					});
					setSlideTo(preparedElement, currentSlider, index+1);
				}
					
				
						preparedElement.attr('id', v.id ).attr('priority', v.priority)
							.css(options.navli).css("border", options.borderSize+"px dashed #000")
							.hover(
								function(){ 
									$(this).removeAttr("style").css(options.navli_hover).css("border", options.borderSize+"px solid #27e6ed").children('a').css(options.navlia_hover);
									options.setNormalMargin($(this));
									$(this).children('ul').each(function(){ $(this).show() });
								}, 
								function(){
									$(this).removeAttr("style").css(options.navli).css("border", options.borderSize+"px dashed #000").children('a').css(options.navlia);
									options.setNormalMargin($(this));
									$(this).children('ul').each(function(){ $(this).hide() });
								})
							.blockPlugin({
								borderSize: options.borderSize,
								showContextmenu: options.showContextmenu,
								dragndropable : options.dragndropable,
								colors: "Not used",
								popup: $("<div id='menuItemBlock"+v.id+"Popup' title='Параметры'></div>")
										.append($('#menuItemForm')
													.clone()
													.removeClass('hidden')
													.find('input[name="scroll"]').click(function(){
														var scrollCheckbox = $(this),
															commonParent = scrollCheckbox.parent().parent().parent();
														commonParent.find('input[name="link"]').prop("disabled",scrollCheckbox.prop("checked"));
														commonParent.find('.scroll-visible').toggle();
													}).end().find('select[name="menuItemSlider"]').change( //TO DO: Перенести в ok попапа + загрузка лого
															function(){
																//0. Наполнение опциями menuItemSliderChild
																var currentSliderScrollContent = $($("#"+$(this).val()).children("div")[0]),
																	menuItemSliderChild = $(this).parent().parent().parent().find('select[name="menuItemSliderChild"]');

																menuItemSliderChild.unbind( "change" ).children().remove();

																menuItemSliderChild.append($('<option></option>'));
																currentSliderScrollContent.children().each(function(k,v){
																	var newOption = $('<option>'+(++k)+'</option>').attr('value',$(v).attr("id"));
																	menuItemSliderChild.append(newOption);
																});


															}
														).end()
												),
								popupOptions : {
									autoOpen: false,
									width: '500px',
									buttons: [
										{
											text: "Ok",
											click: function() {
												var currentElement = $('#'+v.id ),
												name = $('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="name"]').val(),
												link = $('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="link"]').val(),
												priority = $('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="priority"]').val().toString().replace(/\D+/g,'');
												
												currentElement.children('a').text(name);
												currentElement.children('a').attr('href',link);
												currentElement.attr("priority", priority);
												//Top
												if($('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="istop"]').is(':checked')) {
													if(currentElement.children('ul').length != 1){
														menu.children('ul').css(options.isVertical ? options.vertnav : options.nav);
														options.appendByPriority(menu,currentElement, true);
													} else{
														menu.children('ul').css(options.isVertical ? options.vertnav : options.nav); //.append(currentElement.children('ul').css(options.navul).parent()); //Вложенному элементу установить обычный ul
														currentElement.children('ul').css(options.navul);
														options.appendByPriority(menu,currentElement, true);
													}
												} else {
													//Переносим по адресу родителя
													var selectedIndex = $('#menuItemBlock'+v.id +'Popup').find('form').find('select[name="menuItemParent"]').prop('selectedIndex');
													var selected = $('#menuItemBlock'+v.id +'Popup').find('form').find('select[name="menuItemParent"]').children()[selectedIndex];
													
													//Может не быть ul
													if(menu.find("#"+$(selected).attr('value')).children('ul').length == 0){
														menu.find("#"+$(selected).attr('value')).append($("<ul></ul>").css(options.navul));
													}
													options.appendByPriority(menu.find("#"+$(selected).attr('value')),currentElement, false);
												}
												options.setNormalMargin(currentElement.removeAttr("style").css(options.navli_hover).css("border", "1px solid #27e6ed"));
												
												//Навес функционала в случае выбора скрола. Если не выбран скрол, то ничего не надо
												if($('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="scroll"]').is(':checked')){
													var menuItemSlider = $('#menuItemBlock'+v.id +'Popup').find('form').find('select[name="menuItemSlider"]'),
														menuItemSliderChild = $('#menuItemBlock'+v.id +'Popup').find('form').find('select[name="menuItemSliderChild"]'),
														currentSlider = $("#"+menuItemSlider.val());
											

													setSlideTo(currentElement, currentSlider, menuItemSliderChild.prop('selectedIndex'));
													currentElement.children('a').attr('href',"javascript:void(0);");
												}
//												preparedElement.unbind( "click" ).attr("conected-slider-child","");
//												if($('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="scroll"]').is(':checked')){
//													preparedElement.attr("conected-slider-child",menuItemSliderChild.val());
//													preparedElement.click(function(){
//														//Заставить при клике скролить к нужному элементу
//														currentSlider.get(0).options.scrollbar.slider( "value", parseInt(menuItemSliderChild.prop('selectedIndex')));
//													});
//												}
												
												$('#menuItemBlock'+v.id +'Popup').dialog( "close" );
											}
										},
										{
											text: "Выход",
											click: function() {
												$('#menuItemBlock'+v.id +'Popup').dialog( "close" );
											}
										}
									]
								},
								resizable : false,
								dragAble : {},
								droppAble : {}, 
								items: [{
										data:{name: "Изменить",
										click: function(){
											var currentDialogBlock = $('#menuItemBlock'+v.id +'Popup'),
													priorityInput = currentDialogBlock.find('form').find('input[name="priority"]');
											currentDialogBlock.find('form').find('input[name="name"]').val($('#'+v.id ).children('a').text());
											var href = $('#'+v.id).children('a').attr('href') == "javascript:void(0);" ? "" : $('#'+v.id).children('a').attr('href')
											currentDialogBlock.find('form').find('input[name="link"]').val(href);
											priorityInput.val($('#'+v.id).attr('priority'));
											
											priorityInput.get(0).onkeypress = function(e) {
													e = e || event;

													if (e.ctrlKey || e.altKey || e.metaKey) return;

													var chr = getChar(e);

													if (chr == null) return;

													if (chr < '0' || chr > '9') {
													  return false;
													}
												  }
											//Выбрать если верхний уровень
											currentDialogBlock.find('form').find('input[name="istop"]').attr('checked', 
												$('#'+v.id ).parent().hasClass('align_center'));
											//Заполнить select и выбрать родителя
											var menuItemParent = currentDialogBlock.find('form').find('select[name="menuItemParent"]');
											menuItemParent.html('');
											menu.find('li').each(function (){
												//Искл. самое себя
												if($(this).children('a').text()!=$('#'+v.id ).children('a').text()){
													var newOption = $('<option>'+$(this).children('a').text()+'</option>').attr('value',$(this).attr('id'));
													if($('#'+v.id ).parent().parent().get(0) === $(this).get(0)) newOption.attr('selected',true);
													menuItemParent.append(newOption);
												}
											});
											
											//Проверять attr("conected-slider-child"); на undefined и на пустоту. 
											//И в скучае заполнености отмечать чекбокс, показывать селекты и в селектах устанавливать выбранные значения
											var managedItem = $('#'+v.id), selectedSlider=null, selectedSliderChild=null;
											
											if(typeof(managedItem.attr('conected-slider-child')) != "undefined" 
													&& managedItem.attr('conected-slider-child') != ""){
												currentDialogBlock.find('form').find('input[name="link"]').prop("disabled",true);
												$('#menuItemBlock'+v.id +'Popup').find('form').find('input[name="scroll"]').prop("checked",true);
												$('#menuItemBlock'+v.id +'Popup').find('form').find('.scroll-visible').show();
												selectedSliderChild =$("#"+managedItem.attr('conected-slider-child'));
												selectedSlider = selectedSliderChild.parent().parent();
											} else currentDialogBlock.find('form').find('input[name="link"]').prop("disabled",false);

											currentDialogBlock.find('select[name="menuItemSlider"]').children().remove();
											currentDialogBlock.find('select[name="menuItemSlider"]').append($('<option></option>'));
											$('div[type="SliderBlock"]').each(function(k,val){
												var newOption = $('<option>'+$(val).attr("id")+'</option>')
														.attr('value',$(val).attr("id"))
														.attr('selected',(selectedSlider != null && $(val).attr("id") == selectedSlider.attr("id")));
												
												currentDialogBlock.find('select[name="menuItemSlider"]').append(newOption);
											});
											currentDialogBlock.find('select[name="menuItemSliderChild"]').children().remove();
											currentDialogBlock.find('select[name="menuItemSliderChild"]').append($('<option></option>'));
											if(selectedSliderChild != null){
												selectedSliderChild.parent().children().each(function(k,val){
													var newOption = $('<option>'+(++k)+'</option>')
															.attr('value',$(val).attr("id"))
															.attr('selected',($(val).attr("id") == selectedSliderChild.attr("id")));
													currentDialogBlock.find('select[name="menuItemSliderChild"]').append(newOption);
												});
											}
											
											$('#menuItemBlock'+v.id +'Popup').dialog( "open" );
											
										}},
										},
										{
										data:{name: "Удалить",
										click: function(){
											var removedElement = $("#"+v.id );
											if(removedElement.next().length != 0 && removedElement.next()[0].localName == "br") removedElement.next().remove();
											removedElement.remove();
											$(this).parent().hide();
										}}
									}, null, null, null]
							});
					if(preparedElement.children('a').length != 0){
						preparedElement.children('a').attr('href', v.link).text(v.name).css(options.navlia);
					} else preparedElement.append($('<a>').attr('href', v.link).text(v.name).css(options.navlia));

					return preparedElement;
			},
			topMaxWidth : 1,
			isVertical : false,
            url: "/hotty/api/v1/blocks/menuitems/", //"menuitems/",
			vertnav: {
				'width' : '2%',
				'font-weight' : 'normal',
			},
			nav: {
				'width' : '97%',
				'font-weight' : 'normal',
			},
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
				//'background' : '#333',
				'text-decoration' : 'none'
			},
			navul : {
				'list-style' : 'none',
				//'position' : 'absolute',
				'opacity':'1',
				'display' : 'none'
			},
			setNormalMargin : function(element){
				var maxWidth = 0;
				$.each(element.parent().children('li'),function(k,v){
					$(v).show();
					if(maxWidth<parseInt($(v).css("width").toString().replace(/\D+/g,''))) maxWidth = parseInt($(v).css("width").toString().replace(/\D+/g,''));
				});
				if(maxWidth !== 0) element.parent().children('li').show().each(function(){
					if(options.isVertical){
						$(this).css("width", maxWidth+"px");
						 $(this).parent().css("width", maxWidth+"px");
					} else if(!$(this).parent().hasClass('align_center')) {
						$(this).css("width", maxWidth+"px");
					}
				});

				if(element.parent().hasClass('align_center')){
					if(options.isVertical){
						element.parent().css("margin", "0").css("width", maxWidth+"px");//*parseFloat(element.css("font-size"))/2.8 SET maxWidth to menuItemsContainer
					} 
					//else element.parent().css("margin", "auto");

					element.css("margin-left",'0px');
				} else {
					if(element.parent().parent().css("margin-left") == '0px' && !options.isVertical){
						var secondLevelTop = parseFloat(element.parent().parent().css("font-size"))+5;
						element.css("margin-left",'-35px').parent().css('top' , secondLevelTop+"px"); //!!! Parameter from font-size
					} else{ 
						//!!!! 40 px - разница при 0 if(parentWidth<40) left=0; if()
						var left= parseInt(element.parent().parent().css("width").toString().replace(/\D+/g,''));
						element.css("margin-left",left-40+"px").parent().css('top' , '1px');
					}
				}

				$.each(element.children('ul').children('li'), function(k,v){options.setNormalMargin($(v))});
			}
		},	
        options = $.extend(defaultOptions, options);
        var onlyDigits = function(e) {
			e = e || event;

			if (e.ctrlKey || e.altKey || e.metaKey) return;

			var chr = getChar(e);

			if (chr == null) return;

			if (chr < '0' || chr > '9') {
			  return false;
			}
		  };
		var blockOptions = {
					thisBlock: $(this),
					resizable:true,
					i:0,
					popupWidth: 600,
					parentElement:$('<ul style="width:100px;position: absolute;"></ul>'),
					popup: $("<div id='menuBlock"+options.index+"Popup' title='Параметры'></div>").append($('#menuForm').clone().removeClass('hidden')),
					dropable: {
						drop: function(eventb, uib) { return; },
						over: function(eventb, uib) { return; },
						out: function(eventb, uib) { return; },
					},
					items: [
						{
							data:{name: "Параметры",
							click: function(){
								blockOptions.popup.find('form').find('input[name="width"]').val(blockOptions.thisBlock.get(0).style.width);
								blockOptions.popup.find('form').find('input[name="height"]').val(blockOptions.thisBlock.css('height'));
								blockOptions.popup.find('form').find('#showColors').css('background',blockOptions.thisBlock.css('background'));
								blockOptions.popup.find('input[name="isvertical"]').prop('checked', options.isVertical);
								blockOptions.popup.find('form').find('textarea[name="classes"]').text(blockOptions.thisBlock.attr('class'));

								blockOptions.popup.find('#menuTextColor').css('background',options.navlia.color);
								blockOptions.popup.find('#menuBackgroundColor').css('background',options.navlia.background);
								blockOptions.popup.find('#menuHoverTextColor').css('background',options.navlia_hover.color);
								blockOptions.popup.find('#menuHoverBackgroundColor').css('background',options.navlia_hover.background);

								blockOptions.popup.find('input[name="shadow"]').prop('checked', blockOptions.thisBlock.children('ul').children('li').children('a').css('text-shadow')!=='none');
								blockOptions.popup.find('select[name="menuFontFamilySelector"]').val(blockOptions.thisBlock.children('ul').children('li').children('a').css('font-family'));
								blockOptions.popup.find('#setBold').css('background-color', 
								blockOptions.thisBlock.children('ul').children('li').children('a').css('font-weight')==='bold' ? 'gray' : 'none');
								blockOptions.popup.find('#setItalic').css('background-color',
								blockOptions.thisBlock.children('ul').children('li').children('a').css('font-style')==='italic' ? 'gray' : 'none');
								if(options.thisBlock.children("ul").children().length != 0){
									blockOptions.popup.find('form').find('input[name="size"]').val(parseInt(blockOptions.thisBlock.children('ul').children('li').children('a').css('font-size').replace(/\D+/g,'') ));
									blockOptions.popup.find('form').find('input[name="marginright"]').val(parseInt(blockOptions.thisBlock.children('ul').children('li').css('margin-right').replace(/\D+/g,'') ));	
								}
								//TODO: Параметры ЛОГО
								if(options.menuHelperLogo.children("img").length > 0){
									var imagePreView = blockOptions.popup.find("#imagePreView"), url=options.menuHelperLogo.children("img").attr("src");
									imagePreView.children().remove();
									imagePreView.append($('<img src="'+url+'" width="'+297+'px" alt="'+url+'" />'));
								}
								blockOptions.popup.dialog( "open" );
								blockOptions.parentElement.hide();
							}}
						},
						null,
						{
							data:{name: "Копировать",
							click: function(){
								//TO DO: Реализовать копирование
								//Исключить повторение копий
								var clone = blockOptions.thisBlock.clone(); //Клонируем
								//blockOptions.i++;
								var idn = Math.uuidFast();
								var prepareMenuBlockClone = function(menuBlockClone){
									menuBlockClone.children('div').each(function(){ if(!$(this).hasClass("navbar-header")) $(this).remove(); }); //Удаляем все Div
									menuBlockClone.children('ul').children('li').each(function(){ //Перебираем все li
										$(this).attr("id",Math.uuidFast()); //генерим каждому пункту меню новый id
										prepareMenuBlockClone($(this));
									});
								};
								prepareMenuBlockClone(clone);
								clone.attr('id', idn);
								document.body.context_menu_plugin_buffer={
									clone:clone,
									options: options,
								};

								blockOptions.parentElement.hide();
							}}
						},
						{
							data:{name: "Добавить пункт",
							click: function(){ //TODO: Навешивать управление слайдером
								menuItemParent.html('');
								blockOptions.thisBlock.find('li').each(function (){
									var newOption = $('<option>'+$(this).children('a').text()+'</option>').attr('value',$(this).attr('id'));
									menuItemParent.append(newOption);
								});
								if(menu.children('ul').children().length == 0){
									addPopup.find('input[name="istop"]').prop("checked",true);
								}
								addPopup.dialog( "open" );
								blockOptions.parentElement.hide();
							}}
						},
						{
							data:{name: "Удалить",
							click: function(){
								
								//blockOptions.thisBlock.remove();
								blockOptions.thisBlock.get(0).options.isDelete = true;
								blockOptions.thisBlock.hide();
								blockOptions.parentElement.remove();
								blockOptions.popup.remove();
							}}
						},
					],	
					popupOptions : {
						autoOpen: false,
						width: 600,
						buttons: [
							{
								text: "Ok",
								click: function() {
									var width = $( this ).find('form').find('input[name="width"]').val();

									if(width == '100%' || (width.indexOf('%') > -1 && parseInt(width.match(/\d+/)) > 100) 
											|| $( this ).find('form').find('input[name="stretch"]').is(':checked')){
										$( this ).find('form').find('input[name="width"]').val("100%");
										width = "100%"
										blockOptions.thisBlock.css("left", "0px").css('width', width);
									} else {
										blockOptions.thisBlock.css('width', width);
									}
									var height = $( this ).find('form').find('input[name="height"]').val();
									if(height == '100%' || (height.indexOf('%') > -1 && parseInt(height.match(/\d+/)) > 100)){
										height = '100%';
										blockOptions.thisBlock.css('height', height).css("top", "0px");
									} else blockOptions.thisBlock.css('height', height);

									options.isVertical = $( this ).find('form').find('input[name="isvertical"]').is(':checked');
									if(options.isVertical) {
										blockOptions.thisBlock.children('ul').css(options.vertnav);
									} else blockOptions.thisBlock.children('ul').css(options.nav); //menuItemsContainer
									
									if($( this ).find('form').find('input[name="shadow"]').is(':checked')){
										options.navlia.textShadow = '1px 1px 1px rgba(0,0,0,0.75)';
										options.navlia_hover.textShadow = '1px 1px 1px rgba(0,0,0,0.75)';
									} else {
										options.navlia.textShadow = 'none';
										options.navlia_hover.textShadow = 'none';
									}
									//Menu item colours
									options.navlia.color = $( this ).find('form').find('#menuTextColor').css('background');
									options.navlia.background = $( this ).find('form').find('#menuBackgroundColor').css('background');
									options.navlia_hover.color = $( this ).find('form').find('#menuHoverTextColor').css('background');
									options.navlia_hover.background = $( this ).find('form').find('#menuHoverBackgroundColor').css('background');
									
									//font-style: normal | italic | oblique | inherit
									//font-family
									//font-weight
									//font-size
									options.navlia.fontSize = $( this ).find('form').find('input[name="size"]').val()+'px';
									options.navlia.fontFamily = $( this ).find('form').find('select[name="menuFontFamilySelector"]').val();
									options.navlia.fontWeight = $( this ).find('form').find('#setBold').css("background-color")=='rgb(128, 128, 128)' ? 'bold' : 'normal';
									options.navlia.fontStyle = $( this ).find('form').find('#setItalic').css("background-color")=='rgb(128, 128, 128)' ? 'italic' : 'normal';
									options.navli.marginRight = $( this ).find('form').find('input[name="marginright"]').val()+'px';
									
									options.navlia_hover.fontSize = $( this ).find('form').find('input[name="size"]').val()+'px';
									options.navlia_hover.fontFamily = $( this ).find('form').find('select[name="menuFontFamilySelector"]').val();
									options.navlia_hover.fontWeight = $( this ).find('form').find('#setBold').css("background-color")=='rgb(128, 128, 128)' ? 'bold' : 'normal';
									options.navlia_hover.fontStyle = $( this ).find('form').find('#setItalic').css("background-color")=='rgb(128, 128, 128)' ? 'italic' : 'normal';
									options.navli_hover.marginRight = $( this ).find('form').find('input[name="marginright"]').val()+'px';
									
									$.each(blockOptions.thisBlock.children('ul').children('li'), function(k,v){ options.setNormalMargin($(v))});
									$.each(blockOptions.thisBlock.children('ul').children('li'), function(k,v){ options.setFontStyles($(v))});

									var stick = $( this ).find('form').find('input[name="stick"]:checked').val();
									if(stick === 'top' || stick === 'left') blockOptions.thisBlock.removeClass('align_center').removeClass('align_right').css('position','relative').css(stick,'0px');
									if(stick === 'top'){
										blockOptions.thisBlock.prependTo(blockOptions.thisBlock.parent());
									}
									if(stick === 'right') blockOptions.thisBlock.addClass('align_right').css('left','0px');
									if(stick === 'center') blockOptions.thisBlock.addClass('align_center');

									//And colour
									if(typeof(blockOptions.popup.find('#showColors').css('background')) != 'undefined' && typeof(blockOptions.popup.find('#showColors').css('background')) != 'none'){
										blockOptions.thisBlock.css('background',blockOptions.popup.find('#showColors').css('background'));
									} else blockOptions.thisBlock.removeProp('background');
									
									blockOptions.thisBlock
											.attr("navli",JSON.stringify(options.navli))
											.attr("navlia",JSON.stringify(options.navlia))
											.attr("navli_hover",JSON.stringify(options.navli_hover))
											.attr("navlia_hover",JSON.stringify(options.navli_hover));
									
									var imgForBlock = blockOptions.popup.find("#imagePreView").children("img");
									if(imgForBlock.length != 0){
										options.menuHelperLogo.children("img").remove();
										options.menuHelperLogo.append($('<img alt="" class="b-logo b-logo_index png">').attr("src", imgForBlock.attr("src")));
										options.menuHelper.append(options.menuHelperLogo);
									} else options.menuHelperLogo.children("img").remove();
									
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
		blockOptions.popup.find('#setBold')
							.click(function(){ 
								if($(this).css("background-color")=='rgb(128, 128, 128)'){
									$(this).css('background','none');
								} else $(this).css('background','gray');
							});
		blockOptions.popup.find('#setItalic')
				.click(function(){
					if($(this).css("background-color")=='rgb(128, 128, 128)'){
						$(this).css('background','none');
					} else $(this).css('background','gray');
				});
		blockOptions.popup.find('form').find('input[name="size"]').keypress(function(e){return onlyDigits(e)});
		blockOptions.popup.find('form').find('input[name="marginright"]').keypress(function(e){return onlyDigits(e)});
		var addPopup = $("<div id='addItemBlockPopup' title='Добавить пункт меню'></div>").append($('#addMenuItemForm').clone().removeClass('hidden')),
							menuItemParent = addPopup.find('form').find('select[name="menuItemParent"]');

					addPopup.dialog({
						autoOpen: false,
						width: 500,
						buttons: [
							{
								text: "Ok",
								click: function() {
									var selectedIndex = $(this).find('form').find('select[name="menuItemParent"]').prop('selectedIndex'),
										selected = $(this).find('form').find('select[name="menuItemParent"]').children()[selectedIndex],
										newId=Math.uuidFast();
									var v = {
										id : newId, //??? USE BIG INTEGER LIBRARY
										name : $(this).find('form').find('input[name="name"]').val(),
										link : $(this).find('form').find('input[name="link"]').val() == '' ? "javascript:void(0);" : $(this).find('form').find('input[name="link"]').val(),
										priority : $(this).find('form').find('input[name="priority"]').val().toString().replace(/\D+/g,''),
										toplevel : $(this).find('form').find('input[name="istop"]').is(':checked')
									},
									newItem = options.prepareElement($('<li>'),v);
									options.appendByPriority( v.toplevel ? blockOptions.thisBlock : blockOptions.thisBlock.find("#"+$(selected).attr('value')), newItem, v.toplevel);
									$( this ).dialog( "close" );
									$(this).find('form').find('input').val("");
								}
							},
							{
								text: "Выход",
								click: function() {
									$( this ).dialog( "close" );
								}
							}
						]			
					});
		//$(this).blockPlugin(blockOptions);
		blockOptions = $.extend(blockOptions, options);
		$(this).imagePlugin(blockOptions);
		
		var menu = this,
			//level = 0;
            preparedElement,
            menuElements,
			getChar = function(event) {
				if (event.which == null) {
				  if (event.keyCode < 32) return null;
				  return String.fromCharCode(event.keyCode) // IE
				}

				if (event.which != 0 && event.charCode != 0) {
				  if (event.which < 32) return null;
				  return String.fromCharCode(event.which) // остальные
				}

				return null;
			  },
            buildMenu = function(menuElements, level){
                var notAdded = [];
                $.each(menuElements, function(k,v){
					preparedElement = options.prepareElement($('<li>'),v);
					preparedElement.attr("conected-slider-child", v.dependSliderBlock);
                    if(v.toplevel){
                        menu.children('ul').css(options.isVertical ? options.vertnav : options.nav).append(preparedElement);
						//if(parseInt(preparedElement.css("width").toString().replace(/\D+/g,''))>options.topMaxWidth) options.topMaxWidth=parseInt(preparedElement.css("width").toString().replace(/\D+/g,''));
                    } else if(typeof(v.parentItem) == 'object'){
						preparedElement.css("margin-left",'-30px');
						var parentElement = $('#'+v.parentItem.id );
                        if(parentElement.length !== 0){
                            if(parentElement.children('ul').length === 0){
								var newNavUl = options.navul;
								if (!parentElement.parent().hasClass('align_center')){
									newNavUl = {
										'list-style' : 'none',
										//'position' : 'absolute',
										'top' : '5px',
										'opacity':'1',
										'display' : 'none'
									};
								}
								parentElement.append($("<ul></ul>").css(newNavUl));
								
							}
                            parentElement.children('ul').append(preparedElement).append($('<br/>'));
                        } else {
                            notAdded.push(v);
                        }
                    }
                });
                
                if(notAdded.length && level<5) buildMenu(notAdded, ++level);
                return false;
            };
		options.menuItemsContainer.css("margin-top","10px");
		//Смотреть наличие logo в болке и добавлять <img alt="" class="b-logo b-logo_index png" src="" height="30" style="padding-bottom: 3px;"/>
		if(typeof(options.logo) != "undefined") options.menuHelperLogo.append($('<img alt="" class="b-logo b-logo_index png">').attr("src", options.logo));
		
		if(options.showContextmenu) options.thisBlock.prepend(options.menuHelper.append(options.menuHelperButton).append(options.menuHelperLogo));
		if(options.showContextmenu) blockOptions.thisBlock
			.attr("navli",JSON.stringify(options.navli))
			.attr("navlia",JSON.stringify(options.navlia))
			.attr("navli_hover",JSON.stringify(options.navli_hover))
			.attr("navlia_hover",JSON.stringify(options.navli_hover));
		if(menu.children('ul').children().length != 0){
			//приготовить стили
			$.each(menu.children('ul').children('li'), function(k,v){ options.activateElements($(v)) });
		} else { 
			options.thisBlock.append(options.menuItemsContainer);
			if(options.eagerItems != undefined){
				setTimeout(function () {
					buildMenu(options.eagerItems,0);
					$.each(menu.children('ul').children('li'), function(k,v){options.setNormalMargin($(v))});
				}, 2000);
			} else $.ajax({
                crossDomain: true,
                type: "GET",
                url: options.url+menu.attr("id"),//+options.thisBlock.attr("id"),
                dataType: "json",
                success: function( data ) {
						buildMenu(data,0);
						$.each(menu.children('ul').children('li'), function(k,v){options.setNormalMargin($(v))});
                }
			});
		}
		return options;
    }
})(jQuery)