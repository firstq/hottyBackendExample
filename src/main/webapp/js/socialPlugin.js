(function( $ ){
    $.fn.socialPlugin = function(options){
		var defaultOptions = {
			thisBlock : $(this),
			url : "socialnettypes",
			contextmenu: function(e){
				$(document.body).click();
				e.preventDefault();
				var offset = $(this).offset();
				var x = e.pageX;
				var y = e.pageY;
				options.parentElement.css("top",y+"px").css("left", x+"px").toggle();
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
            popup: $("<div id='"+options.thisBlock.attr('id')+"Popup' title='Параметры'></div>"),
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
								document.body.context_menu_plugin_buffer={clone:clone,
									clones: typeof(document.body.context_menu_plugin_buffer) != "undefined" ? document.body.context_menu_plugin_buffer.clones : [], 
									type:"video",};
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
		var self = $(this), inside=false,
				prepareSocialBlock = function(socialTypes){
					$.each(socialTypes, function(k,v){
						console.log(v);
						//Из полученных добавить иконки с сылками в self
						self.append($("<a>"+v.name+"</a>").attr("id", v.id).attr("href",v.name));
						//+ добавить все в popup блок socialNetTypesContainer
						console.log(options.popup.find("#socialNetTypesContainer"));
						options.popup.find("#socialNetTypesContainer")
								.append(
									$('<input type="checkbox">')
										.attr("name",v.name)
										.attr("value",v.name)
										)
								.append(
												$('<span>'+v.name+'</span>')
								)
								.append($('<br/>'));
					});
				};
		self.blockPlugin(options);
		//вытаскивать из базы соцсети и в popup предлагать выбрать какие выводить (по умолчанию выводить все)
		//var 
		$.ajax({
                crossDomain: true,
                type: "GET",
                url: options.url,
                dataType: "json",
                success: function( data ) {
						prepareSocialBlock(data);
                }
			});
    }
})(jQuery)