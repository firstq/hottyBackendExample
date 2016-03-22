(function( $ ){
    $.fn.bodyPlugin = function(options){
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
					self.css("border", "1px solid #27e6ed");
				} else {
					self.css("border", "1px solid #000");
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
								var tabActive = $('li.active'); 
								options.popup.find('form').find('input[name="nameHtml"]').val(tabActive.text().replace(/⊗+/g,"").replace(".html","").trim()),
								options.popup.find('form').find('input[name="title"]').val(self.attr("title"));
								options.popup.find('form').find('input[name="width"]').val(self.css('width'));
								options.popup.find('form').find('input[name="height"]').val(self.css('height'));
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.dialog( "open" );
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
						}
					],
					dragAble : {
						drag: function() {
							if(inside){
								$(this).css("border", "1px solid #27e6ed");
							} else $(this).css("border", "1px solid #27e6ed");
						},
						start: function() {
							$(this).css("border", "1px solid #27e6ed");
						},
						stop: function() {
							$(this).css("border", "1px solid #000");
						},
						containment: "parent", 
						scope: "block"
					},
					droppAble : {
						drop: function(eventb, uib) {
							//Если элемент уже в другом элементе надо ограничить scope его уровнем
							uib.draggable.css("border", "1px solid #000");
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
							uib.draggable.css("border", "1px solid #000");
							$(this).css("border", "1px solid #000");
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
									var pageName = $( this ).find('input[name="nameHtml"]').val(),
									titleName=$( this ).find('input[name="title"]').val();
									$('li.active').children().remove();
									$('li.active').append($('<a href="#'+self.attr("id")+'" aria-controls="'+self.attr("id")+'" role="tab" data-toggle="tab">'+pageName+'.html &nbsp;</a>')
									.append($('<span class="deletePage">⊗</span>').hover(function(){$(this).css('color','red');},function(){$(this).css('color','green');})));
									self.attr("title",titleName);
									
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
									
									//And image
									self.css('background-image', 'none');
									var imgForBlock = options.popup.find("#imagePreView").children("img");
									if(imgForBlock.length != 0){
										self.children("INPUT").remove();
										self.css('background-image', 'url('+imgForBlock.attr("alt")+'?v='+(new Date()).getTime()+')');
										
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
		options = $.extend(defaultOptions, options);
		var self = $(this), inside=false, files;
		self.blockPlugin(options);
		//Если link не указан, показывать кнопку вызова popup
		//console.log(options.popup.find("#imagePreView").children());
		
		options.popup.find('input[type=file]').change(function(){
			files = this.files;
		});
		
		options.popup.find("#downloadBlockImage").click(function( event ){
			
			if(typeof(files) == "undefined" || files.length == 0){
				alert("Файл не выбран!");
				return false;
			}
			var dataForm = new FormData(options.popup.find('form')[0]);
			dataForm.append('file',files[0]);
			
			$.ajax({
				url: 'imageblocks/uploadimg',
				type: 'POST',
				data: dataForm,
				cache: false,
				dataType: 'json',
				processData: false, 
				contentType: false, 
				success: function( resp ){
					if(resp.result){
						var imagePreView = options.popup.find("#imagePreView");
						imagePreView.children().remove();
						imagePreView.append($('<img src="'+resp.url+'" width="'+297+'px" alt="'+resp.url+'" />'));
						options.popup.find("#loadedImgWidth").text(resp.width);//height="'++'"
						options.popup.find("#loadedImgHeight").text(resp.height);
						//Если размеры блока и картинки не совпадают добавляем <INPUT Type="BUTTON" class='form-control' value="Подогнать под размеры блока"/>
						var width = options.popup.find('input[name="width"]').val(),
							height = options.popup.find('input[name="height"]').val();
						options.popup.find("#forResizeButton").children().remove();
						if(width !=resp.width || height !=resp.height){
							//Если что-то 100% то оставляем как есть
							var newWidth = (width === "100%") ? resp.width : width;
								document.body.newHeight = (height === "100%") ? resp.height : height;
							
							var resizeButton = $('<INPUT Type="BUTTON" class="form-control" value="Под размер блока"/>').click(function(){
									$.ajax({
										url: 'imageblocks/resize',
										type: 'POST',
										dataType: 'json',
										data: { "filename":resp.filename, "widthn":newWidth, "heightn":document.body.newHeight },
										success: function( resizedData ){
											if(!resizedData.result){
												alert("Не удалось загрузить файл! Возможно он не является картинкой.");
												return false;
											}
											console.log(resizedData);
											imagePreView.children().remove();
											imagePreView.append($('<img src="'+resizedData.url+'" width="'+297+'px" alt="'+resizedData.url+'" />'));
											options.popup.find("#loadedImgWidth").text(resizedData.width);//height="'++'"
											options.popup.find("#loadedImgHeight").text(resizedData.height);
											
										},
//										complete: function( resp ){
//											console.log(resp);
//										}
									});
								});
							var originalButton = $('<INPUT Type="BUTTON" class="form-control" value="Оригинал"/>').click(function(){
												imagePreView.children().remove();
												imagePreView.append($('<img src="'+resp.url+'" width="'+297+'px" alt="'+resp.url+'" />'));
												options.popup.find("#loadedImgWidth").text(resp.width);//height="'++'"
												options.popup.find("#loadedImgHeight").text(resp.height);
												//options.popup.find("#forResizeButton").append(button.click(resizeClick(button)));
												//originalButton.hide();
											});
							options.popup.find("#forResizeButton").append(originalButton);
							options.popup.find("#forResizeButton").append(resizeButton);
						}
					} else {
						alert("Не удалось загрузить файл! Возможно он не является картинкой.")
					}
				}
			});
		});
		
		self.resizable({animateDuration: "fast"}).droppable({
			drop: function(event, ui) {
				var left = parseInt(ui.helper.css("left").match(/\d+/))-parseInt(ui.draggable.css("width").match(/\d+/)),
					top = event.pageY,
					blockType='simpleBlock';
				document.body.newHeight = document.body.newHeight + parseInt(ui.helper.css('height').match(/\d+/))+100;
				document.body.global_index++;
				$(this).css('height', document.body.newHeight);
				
			
				if(ui.draggable.attr('id') === 'menuBlockMenu'){
					blockType='menuBlock';
					var newBlock = $('<div><ul class="align_center"> </ul></div>')
							.css('height','45px')
							.addClass('menu')
							.attr('id',blockType+document.body.global_index)
							.css('position','relative')
							.css('left',left);
					
					var menuPluginOptions = {
							index : document.body.global_index,
							navlia : {
								'display' : 'block',
								'padding':'1px',
								'text-decoration' : 'none',
								'font-size' : '15px',
								'background' : 'pink',
								'color' : '#669999',
								fontFamily : 'Tahoma',
								fontStyle: 'italic',
								fontWeight: 'bold',
								fontSize : '15px',
							},
							navlia_hover:{
								'display' : 'block',
								'color' : 'red',
								'font-size' : '15px',
								'padding' : '1px',
								'text-decoration' : 'none',
								'background' : 'black',
								fontFamily : 'Tahoma',
								fontStyle: 'italic',
								fontWeight: 'bold',
								fontSize : '15px',
								
							},
							navli : {
								"display" : 'inline',
								'float' : 'left',
								marginRight : '30px', 
								'position':'relative',
							},
							navli_hover : {
								"display" : 'inline',
								'float' : 'left',
								marginRight : '30px',
								'position':'relative',
								'background' : '#333',
								'text-decoration' : 'none'
							},
							isVertical : false,
					};
					
					$(this).append(newBlock);
					menuPluginOptions = newBlock.menuPlugin(menuPluginOptions);
					
					
				} else if(ui.draggable.attr('id') === 'imageBlockMenu') {
					blockType='imageBlock';
					var newBlock = $('<div></div>')
							.css('height','300px')
							.css('width','300px')
							.css('border', '1px dashed rgb(0, 0, 0)')
							.addClass('social')
							.attr('id',blockType+document.body.global_index)
							.css('position','relative')
							.css('left',left+"px");
					$(this).append(newBlock);
					newBlock.imagePlugin({
						thisBlock : newBlock,
						popup: $("<div id='"+blockType+document.body.global_index+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden'))});
				} else  if(ui.draggable.attr('id') === 'textBlockMenu') {
					blockType='textBlock';
					var newBlock = $('<div></div>')
							.css('height','500px')
							.css('width','820px')
							.css('border', '1px dashed rgb(0, 0, 0)')
							.addClass('social')
							.attr('id',blockType+document.body.global_index)
							.css('position','relative')
							.css('left',left+"px");
					$(this).append(newBlock);
					newBlock.textPlugin({
						thisBlock : newBlock,
						popup: $("<div id='"+blockType+document.body.global_index+"Popup' title='Параметры'></div>").append($('#textForm').clone().removeClass('hidden'))});
				} else  if(ui.draggable.attr('id') === 'videoBlockMenu') {
					blockType='videoBlock';
					var newBlock = $('<div></div>')
							.css('height','300px')
							.css('width','350px')
							.css('border', '1px dashed rgb(0, 0, 0)')
							.addClass('video')
							.attr('id',blockType+document.body.global_index)
							.css('position','relative')
							.css('left',left+"px");
					$(this).append(newBlock);
					newBlock.videoPlugin({
						thisBlock : newBlock,
						popup: $("<div id='videoBlock"+document.body.global_index+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden'))});
				} else  if(ui.draggable.attr('id') === 'socialBlockMenu') {
					blockType='socialBlock';
					var newBlock = $('<div></div>')
							.css('height','50px')
							.css('width','100px')
							.css('border', '1px dashed rgb(0, 0, 0)')
							.addClass('social')
							.attr('id',blockType+document.body.global_index)
							.css('position','relative')
							.css('left',left+"px");
					$(this).append(newBlock);
					newBlock.socialPlugin({
						thisBlock : newBlock,
						popup: $("<div id='socialBlock"+document.body.global_index+"Popup' title='Параметры'></div>").append($('#socialForm').clone().removeClass('hidden'))});
				} else {
					$(this).append($('<div id="simpleBlock'+document.body.global_index+'" style="height: 200px; \n\
												width: 200px; \n\
												border: 1px dashed #000;\n\
												height: 200px; \n\
												width: 200px; \n\
												border: 1px dashed rgb(0, 0, 0); \n\
												z-index: 1; position: \n\
												relative; \n\
												left: '+left+'px;" ><div contenteditable></div></div>')
						.blockPlugin({popup: $("<div id='simpleBlock"+document.body.global_index+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden'))})
						);
				}
				$(this).children().each(function(){
					if(typeof($(this).attr('id')) !== 'undefined' && $(this).css('float') !== 'right'){
						top=top-parseInt($(this).css("height").match(/\d+/));
					}
				});
				$("#"+blockType+document.body.global_index).css('top',top+'px').attr('child_level',0);
				$(this).css("border", "1px solid #000");
			},
			over: function(event, ui) {
				$(this).css("border", "1px solid #27e6ed");
			},
			out: function(event, ui) {v
				ui.helper.css("border", "1px dashed #000");
				$(this).css("border", "1px solid #000");
			},
			tolerance: "fit",
			scope: "main"
		});
		
		return this;
	}
})(jQuery)
	