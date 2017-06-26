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
			resizable : false,
			popupWidth: 600,
			parentElement:$('<ul style="width:100px;position: absolute;"></ul>'),
            popup: $("<div id='"+self.attr('id')+"Popup' title='Параметры'></div>"),
			colors: $('#simpleColors'),
			items: [
						{
							data:{name: "Параметры",
							click: function(){
								var tabActive = $('li.active'); 
								options.popup.find('form').find('input[name="nameHtml"]').val(options.currentPage.pagePath.trim());
								options.popup.find('form').find('input[name="title"]').val(options.currentPage.pageTitle.trim());
								options.popup.find('form').find('input[name="description"]').val(options.currentPage.pageDescription.trim());
								if(self.css("background-image")!= "none" && typeof(self.css("background-image")) != "undefined" && self.css("background-image") != ""){
									var imagePreView = options.popup.find("#imagePreView"), url=self.css("background-image").replace("url(","").replace(")","");
									imagePreView.append($('<img src="'+url+'" width="'+297+'px" alt="'+url+'" />'));
								}
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						undefined,null,undefined,null

					],

				popupOptions : {
					autoOpen: false,
					width: options.popupWidth+"px",
					buttons: [
						{
							text: "Ok",
							click: function() {

								options.currentPage.pagePath = $( this ).find('input[name="nameHtml"]').val().trim();
								options.currentPage.pageTitle = $( this ).find('input[name="title"]').val().trim();
								options.currentPage.pageDescription = $( this ).find('input[name="description"]').val().trim();
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
				url: '/hotty/app/uploadimg',
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
							var clearButton = $('<INPUT Type="BUTTON" class="form-control" value="Очистить"/>').click(function(){
								options.popup.find("#loadedImgWidth").text("");//height="'++'"
								options.popup.find("#loadedImgHeight").text("");
								imagePreView.children().remove();
								options.popup.find("#forResizeButton").children().remove();
							});
							options.popup.find("#forResizeButton").append(clearButton);
							//options.popup.find("#forResizeButton").append(originalButton);
							//options.popup.find("#forResizeButton").append(resizeButton);
						}
					} else {
						alert("Не удалось загрузить файл! Возможно он не является картинкой.")
					}
				}
			});
		});

		return this;
	}
})(jQuery)
	