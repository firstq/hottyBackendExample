(function( $ ){
    $.fn.imagePlugin = function(options){
		var isURL = function (url) {
			var strRegex = "^s?https?:\/\/[-_.!~*'()a-zA-Z0-9;\/?:\@&=+\$,%#]+$";
			 var re=new RegExp(strRegex);
			 return re.test(url);
		 },
				 //TO DO ВЫНЕСТИ В ОТДЕЛЬНЫЙ ФАЙЛ!!!
		 makeNewTopForCurent = function(top, receiver){
			receiver.children().each(function(){
									if(typeof($(this).attr('id')) !== 'undefined' && $(this).css('float') !== 'right'){
										top=top-parseInt($(this).css("height").match(/\d+/));
									}
								});
			return top;
		},
		//TO DO ВЫНЕСТИ В ОТДЕЛЬНЫЙ ФАЙЛ!!!
		makeNewTopForLastOwnerChildrens = function(movable){
			var isAfterMovable = false, newTop = parseInt(movable.css('height').match(/\d+/));
			movable.parent().children().each(function(){
				//Изменять только те, которые идут после целевого
				if(movable.get(0) === $(this).get(0)) isAfterMovable = true;
				if(typeof($(this).attr('id')) !== 'undefined' && isAfterMovable){
					$(this).css('top', '+='+newTop+"px");
				}
			});
			return top;
		},
		defaultOptions = {
			thisBlock : $(this),
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
			//popupWidth: 600,
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
								clone.children('div').each(function(){
									$(this).remove();
								});
								clone.attr('id', idn);
								document.body.context_menu_plugin_buffer={clone:clone,
									clones: typeof(document.body.context_menu_plugin_buffer) != "undefined" ? document.body.context_menu_plugin_buffer.clones : [], 
									type:"image",};
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
								//makeNewTopForLastOwnerChildrens(self);
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
		
		if(options.popup.find("#imagePreView").children().length == 0) 
			self
				.append($("<INPUT Type='button' href='' value='Загрузить картинку'/>").css('height','40px')
							.css('width','270px')
							.addClass('align_center')
							.click(function(){
								options.popup.find('form').find('input[name="width"]').val(self.css('width'));
								options.popup.find('form').find('input[name="height"]').val(self.css('height'));
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.dialog( "open" );
							}));
		
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
							var newWidth = (width === "100%") ? resp.width : width,
								newHeight = (height === "100%") ? resp.height : height;
							
							var resizeButton = $('<INPUT Type="BUTTON" class="form-control" value="Под размер блока"/>').click(function(){
									$.ajax({
										url: 'imageblocks/resize',
										type: 'POST',
										dataType: 'json',
										data: { "filename":resp.filename, "widthn":newWidth, "heightn":newHeight },
										success: function( resizedData ){
											if(!resizedData.result){
												alert("Не удалось загрузить файл! Возможно он не является картинкой.");
												return false;
											}
											
											imagePreView.children().remove();
											imagePreView.append($('<img src="'+resizedData.url+'" width="'+297+'px" alt="'+resizedData.url+'" />'));
											options.popup.find("#loadedImgWidth").text(resizedData.width);//height="'++'"
											options.popup.find("#loadedImgHeight").text(resizedData.height);
											
										},
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
		
	}})(jQuery)