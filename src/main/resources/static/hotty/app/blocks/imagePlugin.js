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
			restUrl: "/imageblocks/",
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
								options.popup.find('form').find('input[name="width"]').val(self.get(0).style.width);
								options.popup.find('form').find('input[name="height"]').val(self.get(0).style.height);
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								
								options.popup.find('form').find('textarea[name="classes"]').text(self.attr('class'));
								if(self.css("background-image")!= "none" && typeof(self.css("background-image")) != "undefined" && self.css("background-image") != ""){
									var imagePreView = options.popup.find("#imagePreView"), url=self.css("background-image").replace("url(","").replace(")","");
									imagePreView.append($('<img src="'+url+'" width="'+297+'px" alt="'+url+'" />'));
								}
								
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						null,
//						{
//							data:{name: "Копировать",
//							click: function(){
//								var clone = self.clone().attr('id', '');
//								document.body.context_menu_plugin_buffer={
//									clone:clone, 
//									options: options,
//									childrens: options.prepareChildrensForCopy(clone)
//								}; 
//								options.parentElement.hide();
//							}}
//						}
					],
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
										self.css("background-size", "100%");
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
				url: '/hotty/app/uploadimg',
				type: 'POST',
				data: dataForm,
				cache: false,
				dataType: 'json',
				processData: false, 
				contentType: false, 
				beforeSend: function(){
					//TO DO: Вставлять полосу загрузки
				},
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
							//TO DO Изменить логику расчёта размера. Что если указано в процентах?
							
							var resizeButton = $('<INPUT Type="BUTTON" class="form-control" value="Изменить размер"/>').click(function(){
									var resizePopup = $("<div title='Указать размеры'></div>").append($("#resizeForm").clone().removeClass("hidden"));
									resizePopup.dialog({
										autoOpen: true,
										width: "400px",
										buttons: [
											{
												text: "Ok",
												click:function(){
													var newWidth = resizePopup.find("input[name='width']").val(), //width, (width === "100%") ? resp.width : self.width()
														newHeight =  resizePopup.find("input[name='height']").val();//height; (height === "100%") ? resp.height : self.height()

													$.ajax({
														url: 'resize',
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
							var clearButton = $('<INPUT Type="BUTTON" class="form-control" value="Очистить"/>').click(function(){
								options.popup.find("#loadedImgWidth").text("");//height="'++'"
								options.popup.find("#loadedImgHeight").text("");
								imagePreView.children().remove();
								options.popup.find("#forResizeButton").children().remove();
							});
							options.popup.find("#forResizeButton").append(clearButton);
						//options.popup.find("#forResizeButton").append(originalButton);
						//	options.popup.find("#forResizeButton").append(resizeButton);
						}
					} else {
						alert("Не удалось загрузить файл! Возможно он не является картинкой.");
					}
				},
				error: function( resp ){
				}
		});
		});
		
	}})(jQuery)