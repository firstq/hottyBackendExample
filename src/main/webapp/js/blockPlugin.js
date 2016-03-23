(function( $ ){
    $.fn.blockPlugin = function(options){
		//TO DO ВЫНЕСТИ В ОТДЕЛЬНЫЙ ФАЙЛ!!!
		var makeNewTopForCurent = function(top, receiver){
			receiver.children().each(function(){
									if(typeof($(this).attr('id')) !== 'undefined' && $(this).css('float') !== 'right'){
										top=top-parseInt($(this).css("height").match(/\d+/));
									}
								});
			return top;
		};
		//TO DO ВЫНЕСТИ В ОТДЕЛЬНЫЙ ФАЙЛ!!!
		var makeNewTopForLastOwnerChildrens = function(movable){
			var isAfterMovable = false, newTop = parseInt(movable.css('height').match(/\d+/));
			movable.parent().children().each(function(){
				//Изменять только те, которые идут после целевого
				if(movable.get(0) === $(this).get(0)) isAfterMovable = true;
				if(typeof($(this).attr('id')) !== 'undefined' && isAfterMovable){
					$(this).css('top', '+='+newTop+"px");
				}
			});
			return top;
		};
		
		var clones = [];
		var self=$(this),
				inside = false;
        var defaultOptions = {
			contextmenu: function(e){
				$(document.body).click();
				e.preventDefault();
				var offset = $(this).offset();
				var x = e.pageX;
				var y = e.pageY;
				options.parentElement.css("top",y+"px").css("left", x+"px").css("z-index","1").toggle();
				if(options.parentElement.is(':visible')){
					self.css("border", "1px dashed #27e6ed");
				} else {
					self.css("border", "1px dashed #000");
				}
				e.stopPropagation();
			},
			resizable : true,
			popupWidth: 600,
			parentElement:$('<ul style="width:100px;position: absolute;"></ul>'),
            popup: $("<div id='"+self.attr('id')+"Popup' title='Параметры'></div>"),
			colors: $('#simpleColors'),
			items: [
						{
							data:{name: "Параметры",
							click: function(){
								options.popup.find('form').find('input[name="width"]').val(self.css('width'));
								options.popup.find('form').find('input[name="height"]').val(self.css('height'));
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Копировать",
							click: function(){
								//Исключить повторение копий
								var clone = self.clone();
								var idn = clone.attr('id')+'Copy';
								clone.children().each(function(){
									$(this).remove();
								});
								clone.attr('id', idn);
								document.body.context_menu_plugin_buffer={clone:clone, clones:clones};
								
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Вставить",
							click: function(){
								if(typeof(document.body.context_menu_plugin_buffer) != 'undefined'){
									
									var child_level = parseInt(self.attr('child_level'));
									var pastedElement = document.body.context_menu_plugin_buffer.clone.clone();
									document.body.context_menu_plugin_buffer.clones.push(pastedElement);
									var idn = pastedElement.attr('id')+document.body.context_menu_plugin_buffer.clones.length, top=makeNewTopForCurent(0,self);
										pastedElement.attr('id', idn)
										.draggable(options.dragAble)
										.resizable({animateDuration: "fast"})
										.droppable(options.droppAble)
										.css("top",top+"px")
										.css("left","0px").attr("parent-id",self.attr("id")).attr("child_level",++child_level).appendTo(self);
									if(typeof(document.body.context_menu_plugin_buffer.type) != undefined && document.body.context_menu_plugin_buffer.type == "image" ){
										//перенести настройки popup
										pastedElement.imagePlugin({
											thisBlock : pastedElement,
											popup: $("<div id='imageBlock"+idn+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden'))});
									} else if(typeof(document.body.context_menu_plugin_buffer.type) != undefined && document.body.context_menu_plugin_buffer.type == "menu" ){
										//перенести настройки popup
										pastedElement.menuPlugin({});
									} else if(typeof(document.body.context_menu_plugin_buffer.type) != undefined && document.body.context_menu_plugin_buffer.type == "video" ){
										pastedElement.videoPlugin({
											thisBlock : pastedElement,
											popup: $("<div id='videoBlock"+idn+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden'))});
									} else pastedElement.blockPlugin({popup: $("<div id='"+idn+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden'))});
										
								}
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Удалить",
							click: function(){
								makeNewTopForLastOwnerChildrens(self);
								self.remove();
								options.parentElement.remove();
								options.popup.remove();
							}}
						},
					],
					dragAble : {
						drag: function() {
							if(inside){
								$(this).css("border", "1px solid #27e6ed");
							} else $(this).css("border", "1px dashed #27e6ed");
						},
						start: function() {
							$(this).css("border", "1px dashed #27e6ed");
						},
						stop: function() {
							$(this).css("border", "1px dashed #000");
						},
						containment: "parent", 
						scope: "block"
					},
					droppAble : {
						drop: function(eventb, uib) {
							//Если элемент уже в другом элементе надо ограничить scope его уровнем
							uib.draggable.css("border", "1px dashed #000");
							var child_level = parseInt(uib.draggable.attr('child_level'));
							if($(this).attr("child_level") >= child_level && $(this).attr("id") !== uib.draggable.attr('parent-id')) {
								child_level++;
								var newDraggable = uib.draggable.attr("parent-id",$(this).attr("id")), top=makeNewTopForCurent(0,$(this));
								makeNewTopForLastOwnerChildrens(newDraggable);
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

		options = $.extend(defaultOptions, options);
		//!!!if dragAble or droppAble isEmpty then nothing
		
		self.draggable(options.dragAble).droppable(options.droppAble);
		
		options.popup.appendTo($(document.body)).dialog(options.popupOptions).find('.block_colors').click(function(e){
				options.colors.attr('checkerid', $(this).attr('id')).dialog('open');
				
			});
			//options.popup.css("top","0px").css("left","0px").css("z-index",100);
		$.each(options.items, function(k,v){
			options.parentElement.append($('<li>'+v.data.name+'</li>').click(v.data.click));
		});
		options.parentElement.menu();
		options.parentElement.appendTo($(document.body)).hide()
		//contextmenu function in options
		this.bind("contextmenu",options.contextmenu);
		$(document.body).mousedown(function(event){
			if ($(event.target).closest(options.parentElement).length === 0) {
				options.parentElement.hide();
				self.css("border", "1px dashed #000");
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
		
		if(options.resizable) self.resizable({animateDuration: "fast"});
		
		return this;
	}
})(jQuery)
	