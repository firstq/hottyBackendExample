(function( $ ){
    $.fn.sendmailPlugin = function(options){
		var isURL = function (url) {
			var strRegex = "^s?https?:\/\/[-_.!~*'()a-zA-Z0-9;\/?:\@&=+\$,%#]+$";
			 var re=new RegExp(strRegex);
			 return re.test(url);
		 },
		defaultOptions = {
			restUrl: "/api/v1/sendmailblocks/",
			defaultMailFrom: "no_reply@hotty.com",
			defaultMailTo: "serega2rikov@me.com",
			mailButton: $('<input name="sendButton" type="button" class="form-control btn btn-success" value="Отправить" />'),
			mailFiles: $('<input type="file" class="form-control"  accept="image/*,text/html,text/plain,application/msword,application/pdf,application/rtf,application/zip"  value="Файл..." />'),
			captchaImage: $('<img src="/hotty/api/v1/captcha/captcha.html" style="height: 34px"/>'),
			captchaInput: $('<input placeholder="введите символы с картинки" style="margin: 1px; width: 300px" type="text" name="j_captcha_response" class="form-control" />'),
			categoriesSelect: $('<select name="messageTheme" class="form-control" style="margin: 0px; width: 99%;" ><option>Категория обращения</option></select>'),
			mailForm: $('<div class="row">'+
					    '<div class="col-md-12">'+
						'<form class="navbar-form" enctype="multipart/form-data">'+
						   '<p id="email" style="font-size: small;color:red"></p><input name="responseEmail" type="text" class="form-control" placeholder="Ваш email для обратной связи" style="margin: 0px; width: 99%;" /><br/><br/>'+
						   '<textarea name="targetMessage" class="form-control" style="margin: 0px; width: 99%;; height: 40%"></textarea><br/><br/>'+
						'</form></div></div>'),
			thisBlock : $(this),
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
								
								//письма адреса
								var to = options.popup.find('form').find('input[name="to"]'), from = options.popup.find('form').find('input[name="from"]');
								if(to.val() == "") to.val(options.defaultMailTo);
								if(from.val() == "") from.val(options.defaultMailFrom);
								
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						null,
						{
							data:{name: "Копировать",
							click: function(){
								var clone = self.clone().attr('id', '').children().remove().end();
								document.body.context_menu_plugin_buffer={
									clone:clone, 
									options: options
								};
								options.parentElement.hide();
							}}
						},
						null

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
									
									//письма адреса
									var to = options.popup.find('form').find('input[name="to"]'), from = options.popup.find('form').find('input[name="from"]');
									//серверу на валидацию
									var dataForm = new FormData(options.popup.find('form')[0]),
										activePopup = $( this );
									dataForm.append('blockId',self.attr("id"));
									$.ajax({
										url: '/hotty/api/v1/sendmailblocks/validate',
										type: 'POST',
										data: dataForm,
										cache: false,
										dataType: 'json',
										processData: false, 
										contentType: false, 
										beforeSend: function(){
											//TO DO: Вставлять полосу загрузки
											options.popup.find('form').find('p').text("").next().css("border", "0px");
										},
										success: function( resp ){
											if(typeof(resp.errors) != "undefined" && resp.hasErrors){
												//В случае непрохождения пишем ошибки и подсвечиваем красным
												$.each(resp.errors, function(k,v){
													options.popup.find('form').find('p#'+k).text(v).next().css("border", "1px dashed red");
												});
											} else{
												//Если всё ок сохраняем
												options.defaultMailTo = to.val();
												options.defaultMailFrom = from.val();
												activePopup.dialog( "close" );
											}
										}
									});
								}
							},
							{
								text: "Выход",
								click: function() {
									var to = options.popup.find('form').find('input[name="to"]'), from = options.popup.find('form').find('input[name="from"]');
									to.val(options.defaultMailTo);
									from.val(options.defaultMailFrom);
									options.popup.find('form').find('p').text("").next().css("border", "0px");
									$( this ).dialog( "close" );
								}
							}
						]
					}
        };
		options = $.extend(defaultOptions, options);
		var self = $(this), inside=false;
		options = self.blockPlugin(options);
		//Вставляем форму
		options.categoriesSelect
			.append($('<option>Предложение о сотрудничестве</option>').attr('value',1))
			.append($('<option>Проблемы с работой сервиса</option>').attr('value',2))
			.append($('<option>Другое</option>').attr('value',3));
		options.mailForm.find('form').prepend("<br/><br/>").prepend(options.categoriesSelect);
		self.addClass("jumbotron").append(options.mailForm);
		//if(typeof(options.capchaEnable)!= 'undefined' && options.capchaEnable) 
		options.mailForm.find('form').append('<p id="captcha" style="font-size: small;color:red"></p>').append(options.captchaInput).append(options.captchaImage);
		options.captchaImage.click(function(){
			if($(this).css("height")=="100px") $(this).css("height","34px");
			else $(this).css("height","100px");
		});
		options.mailForm.find('form').append("<br/><br/>").append('<p id="uploadFail" style="font-size: small;color:red"></p>').append(options.mailButton).append(options.mailFiles).append('<ul id="successUploaded"></ul>');
		
		options.mailFiles.change(function(){
			var dataForm = new FormData(options.mailForm.find('form')[0]);
			dataForm.append('file',this.files[0]);
			$.ajax({
				url: '/hotty/api/v1/sendmailblocks/uploadfile',
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
					$('#uploadFail').text("").next().next().css("border", "0px");
					if(resp.result){
						var newFile = $('<li></li>').css("font-size","smaller").text(resp.filename).append($('<a>Удалить</a>').css("margin-left", "5px").click(function(){ newFile.remove() }));
						options.mailForm.find('#successUploaded').append(newFile);
					} else {
						options.mailForm.find('#uploadFail').text(resp.message).next().next().css("border", "1px dashed red");
					}
				}
			});
		});
		
		options.mailForm.find('input[name="sendButton"]').click(function(){
			var dataForm = new FormData($(this).parent()[0]);
			dataForm.append('parentBlock',self.attr('id'));
			var selectedIndex = options.categoriesSelect.prop('selectedIndex');
			var selected = options.categoriesSelect.children()[selectedIndex];
			dataForm.append('subject',$(selected).text());
			//Add filenames to form
			var files = "";
			options.mailForm.find('#successUploaded').children().each(function(){
				
				files += $(this).html().replace('<a style="margin-left: 5px;">Удалить</a>',',>');
			});
			dataForm.append('files',files);
			//В каком-то случае брать sendTO из options.popup
			dataForm.append('targetEmail',options.popup.find('form').find('input[name="to"]').val());
			
			$.ajax({
				url: '/hotty/api/v1/sendmailblocks/send',
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
					options.mailForm.find('form').find('p').text("").next().css("border", "0px");
					if(typeof(resp.errors) != "undefined" && resp.hasErrors){
						$.each(resp.errors, function(k,v){
							options.mailForm.find('form').find('p#'+k).text(v).next().css("border", "1px dashed red");
						});
					} else {
						$("<center>Сообщение успешно отправлено </center>").dialog({
												autoOpen: true,
												width: "200px",
												buttons: [
													{
														text: "Ok",
														click: function() {
															$(this).dialog("close");
														}
													}
												]
											});
						//TO DO: Очистка inputs and textarea
						options.mailForm.find('input[type="text"]').val("").end().find("textarea").val("");
					}
					options.captchaImage.fadeOut('fast').attr("src", "/hotty/api/v1/captcha/captcha.html?"+new Date().getTime()).fadeIn('fast');
				},
				error: function( resp ){
					$("<center> Неизвестная системная ошибка! Сообщение не отправлено! </center>").dialog({
												autoOpen: true,
												width: "200px",
												buttons: [
													{
														text: "Ok",
														click: function() {
															$(this).dialog("close");
														}
													}
												]
											});
				}
			});
		});
    }
})(jQuery)