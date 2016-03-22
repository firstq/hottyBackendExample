(function( $ ){
    $.fn.menuPlugin = function(options){
		
        var defaultOptions = {
			setFontStyles : function(element){
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
					topLevel : menuElement.parent().hasClass('align_center')
				};
				options.prepareElement(menuElement,v);
				$.each(menuElement.children('ul').children('li'), function(k,v){ options.activateElements($(v)) });
			},
			prepareElement : function(elem,v){
				var preparedElement = elem
							.attr('id', v.id.toString().replace(/\D+/g,'')).attr('priority', v.priority)
							.css(options.navli).css("border", "1px dashed #000")
							.blockPlugin({
								colors: "Not used",
								popup: $("<div id='menuItemBlock"+v.id.toString().replace(/\D+/g,'')+"Popup' title='Параметры'></div>").append($('#menuItemForm').clone().removeClass('hidden')),
								popupOptions : {
									autoOpen: false,
									width: '500px',
									buttons: [
										{
											text: "Ok",
											click: function() {
												var currentElement = $('#'+v.id.toString().replace(/\D+/g,'')),
												name = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('input[name="name"]').val(),
												link = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('input[name="link"]').val(),
												priority = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('input[name="priority"]').val().toString().replace(/\D+/g,'');
												
												currentElement.children('a').text(name);
												currentElement.children('a').attr('href',link);
												currentElement.attr("priority", priority);
												//Top
												if($('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('input[name="istop"]').is(':checked')) {
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
													var selectedIndex = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('select[name="menuItemParent"]').prop('selectedIndex');
													var selected = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').find('form').find('select[name="menuItemParent"]').children()[selectedIndex];
													
													//Может не быть ul
													if(menu.find("#"+$(selected).attr('value')).children('ul').length == 0){
														menu.find("#"+$(selected).attr('value')).append($("<ul></ul>").css(options.navul));
													}
													options.appendByPriority(menu.find("#"+$(selected).attr('value')),currentElement, false);
												}
												options.setNormalMargin(currentElement.removeAttr("style").css(options.navli_hover).css("border", "1px solid #27e6ed"));
												
												$('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').dialog( "close" );
											}
										},
										{
											text: "Выход",
											click: function() {
												$('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').dialog( "close" );
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
											var currentDialogBlock = $('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup'),
													priorityInput = currentDialogBlock.find('form').find('input[name="priority"]');
											currentDialogBlock.find('form').find('input[name="name"]').val($('#'+v.id.toString().replace(/\D+/g,'')).children('a').text());
											currentDialogBlock.find('form').find('input[name="link"]').val($('#'+v.id.toString().replace(/\D+/g,'')).children('a').attr('href'));
											priorityInput.val($('#'+v.id.toString().replace(/\D+/g,'')).attr('priority'));
											
											priorityInput.get(0).onkeypress = function(e) {
													e = e || event;

													if (e.ctrlKey || e.altKey || e.metaKey) return;

													var chr = getChar(e);

													// с null надо осторожно в неравенствах,
													// т.к. например null >= '0' => true
													// на всякий случай лучше вынести проверку chr == null отдельно
													if (chr == null) return;

													if (chr < '0' || chr > '9') {
													  return false;
													}
												  }
											//Выбрать если верхний уровень
											currentDialogBlock.find('form').find('input[name="istop"]').attr('checked', 
												$('#'+v.id.toString().replace(/\D+/g,'')).parent().hasClass('align_center'));
											//Заполнить select и выбрать родителя
											var menuItemParent = currentDialogBlock.find('form').find('select[name="menuItemParent"]');
											menuItemParent.html('');
											menu.find('li').each(function (){
												//Искл. самое себя
												if($(this).children('a').text()!=$('#'+v.id.toString().replace(/\D+/g,'')).children('a').text()){
													var newOption = $('<option>'+$(this).children('a').text()+'</option>').attr('value',$(this).attr('id'));
													if($('#'+v.id.toString().replace(/\D+/g,'')).parent().parent().get(0) === $(this).get(0)) newOption.attr('selected',true);
													menuItemParent.append(newOption);
												}
											});
											$('#menuItemBlock'+v.id.toString().replace(/\D+/g,'')+'Popup').dialog( "open" );
										}},
										},
										{
										data:{name: "Удалить",
										click: function(){
											var removedElement = $("#"+v.id.toString().replace(/\D+/g,''));
											if(removedElement.next().length != 0 && removedElement.next()[0].localName == "br") removedElement.next().remove();
											removedElement.remove();
											$(this).parent().hide();
										}}
									}]
							})
							
							.hover(
								function(){ 
									$(this).removeAttr("style").css(options.navli_hover).css("border", "1px solid #27e6ed").children('a').css(options.navlia_hover);
									options.setNormalMargin($(this));
									$(this).children('ul').each(function(){ $(this).show() });
								}, 
								function(){ 
									$(this).removeAttr("style").css(options.navli).css("border", "1px dashed #000").children('a').css(options.navlia);
									options.setNormalMargin($(this));
									$(this).children('ul').each(function(){ $(this).hide() });
								});
								if(preparedElement.children('a').length != 0){
									preparedElement.children('a').attr('href', v.link).text(v.name).css(options.navlia);
								} else preparedElement.append($('<a>').attr('href', v.link).text(v.name).css(options.navlia));
					return preparedElement;
			},
			topMaxWidth : 1,
			isVertical : false,
            url:"menuitems",
			vertnav: {
				'width' : '2%',
				'font-weight' : 'normal',
//				'text-align' : 'center'
			},
			nav: {
				'width' : '97%',
				'font-weight' : 'normal',
//				'text-align' : 'center'
			},
			navli : {
				"display" : 'inline',
				'float' : 'left',
//				'margin-right' : '30px', Сделать настраиваемым
				'position':'relative',
//				'margin-left': '10px'
			},
			navlia : {
				'display' : 'block',
				'padding':'1px',
				'text-decoration' : 'none',
				'font-size' : '15px',
//				'background' : 'pink'
				'color' : '#669999',
			},
			navlia_hover:{
				'display' : 'block',
				'color' : '#454545',
				'font-size' : '15px',
				'padding' : '1px',
				'text-decoration' : 'none',
				
			},
			navli_hover : {
				"display" : 'inline',
				'float' : 'left',
//				'margin-right' : '30px', Сделать настраиваемым
				'position':'relative',
//				'margin-left': '10px',
				'background' : '#333',
				'text-decoration' : 'none'
			},
			navul : {
				'list-style' : 'none',
				'position' : 'absolute',
//				'left' : '-40px',//O
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
						element.parent().css("margin", "0").css("width", maxWidth+"px");//*parseFloat(element.css("font-size"))/2.8
					} else element.parent().css("margin", "auto");

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

								blockOptions.popup.find('form').find('input[name="width"]').val(blockOptions.thisBlock.css('width'));
								blockOptions.popup.find('form').find('input[name="height"]').val(blockOptions.thisBlock.css('height'));
								blockOptions.popup.find('form').find('#showColors').css('background',blockOptions.thisBlock.css('background'));
								blockOptions.popup.find('input[name="isvertical"]').prop('checked', options.isVertical);

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
								blockOptions.popup.find('form').find('input[name="size"]').val(parseInt(blockOptions.thisBlock.children('ul').children('li').children('a').css('font-size').replace(/\D+/g,'') ));
								blockOptions.popup.find('form').find('input[name="marginright"]').val(parseInt(blockOptions.thisBlock.children('ul').children('li').css('margin-right').replace(/\D+/g,'') ));	


								blockOptions.popup.dialog( "open" );
								blockOptions.parentElement.hide();
							}}
						},
						{
							data:{name: "Копировать",
							click: function(){
								//Исключить повторение копий
								var clone = blockOptions.thisBlock.clone();
								blockOptions.i++;
								var idn = clone.attr('id')+'Copy';
								var prepareMenuBlockClone = function(menuBlockClone){
									menuBlockClone.children('div').each(function(){ $(this).remove(); });
									menuBlockClone.children('ul').children('li').each(function(){
										$(this).attr("id",$(this).attr("id")+blockOptions.i);
										prepareMenuBlockClone($(this));
									});
								};
								prepareMenuBlockClone(clone);
								clone.attr('id', idn);
								document.body.context_menu_plugin_buffer={
									clone:clone, 
									clones: typeof(document.body.context_menu_plugin_buffer) != "undefined" ? document.body.context_menu_plugin_buffer.clones : [], 
									type:"menu",
//									blockOptions : blockOptions,
//									menuOptions : options
								};

								blockOptions.parentElement.hide();
							}}
						},
						{
							data:{name: "Добавить пункт",
							click: function(){
								menuItemParent.html('');
								blockOptions.thisBlock.find('li').each(function (){
									var newOption = $('<option>'+$(this).children('a').text()+'</option>').attr('value',$(this).attr('id'));
									menuItemParent.append(newOption);
								});
								addPopup.dialog( "open" );
								blockOptions.parentElement.hide();
							}}
						},
						{
							data:{name: "Удалить",
							click: function(){
								//makeNewTopForLastOwnerChildrens(blockOptions.thisBlock);
								blockOptions.thisBlock.remove();
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
										blockOptions.thisBlock.children('ul').css('width', '1%');
									} else blockOptions.thisBlock.children('ul').css('width', '100%');
									
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
									} else blockOptions.thisBlock.removeProp('background')
									
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
		var addPopup = $("<div id='addItemBlockPopup' title='Добавить пункт меню'></div>").append($('#menuItemForm').clone().removeClass('hidden')),
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
										newId="";
									blockOptions.i++;
									$(this).find('form').find('select[name="menuItemParent"]').children().each(function(){
										if($(this).attr("value") != 'undefined')
											newId += $(this).attr("value");
									});
									newId += blockOptions.i;
									var v = {
										id : newId, //??? USE BIG INTEGER LIBRARY
										name : $(this).find('form').find('input[name="name"]').val(),
										link : $(this).find('form').find('input[name="link"]').val(),
										priority : $(this).find('form').find('input[name="priority"]').val().toString().replace(/\D+/g,''),
										topLevel : $(this).find('form').find('input[name="istop"]').is(':checked')
									},
									newItem = options.prepareElement($('<li>'),v);
									options.appendByPriority( v.topLevel ? blockOptions.thisBlock : blockOptions.thisBlock.find("#"+$(selected).attr('value')), newItem, v.topLevel);
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
		$(this).blockPlugin(blockOptions);
		
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
				//Для вертикальности добавить пустой верхний li ul
					if(options.isVertical){
						//menu.css()
					}
					
                var notAdded = [];
                $.each(menuElements, function(k,v){ 
					//Add elements to select and choose real parent
					preparedElement = options.prepareElement($('<li>'),v);
                    if(v.topLevel){
                        menu.children('ul').css(options.isVertical ? options.vertnav : options.nav).append(preparedElement);
						//if(parseInt(preparedElement.css("width").toString().replace(/\D+/g,''))>options.topMaxWidth) options.topMaxWidth=parseInt(preparedElement.css("width").toString().replace(/\D+/g,''));
                    } else if(typeof(v.parent) == 'object'){
						preparedElement.css("margin-left",'-30px');
						var parentElement = $('#'+v.parent.id.toString().replace(/\D+/g,''));
						
                        if(parentElement.length !== 0){
                            if(parentElement.children('ul').length === 0){
								var newNavUl = options.navul;
								if (!parentElement.parent().hasClass('align_center')){
									newNavUl = {
										'list-style' : 'none',
										'position' : 'absolute',
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
                
                if(notAdded.length) buildMenu(notAdded, ++level);
                return false;
            };
		
		if(menu.children('ul').children().length != 0){
			$.each(menu.children('ul').children('li'), function(k,v){ options.activateElements($(v)) });
		} else $.ajax({
                crossDomain: true,
                type: "GET",
                url: options.url,
                dataType: "json",
                success: function( data ) {
						buildMenu(data,0);
						$.each(menu.children('ul').children('li'), function(k,v){options.setNormalMargin($(v))});
                }
			});
		
		return options;
    }
})(jQuery)