(function( $ ){
    $.fn.textPlugin = function(options){
		var isURL = function (url) {
			var strRegex = "^s?https?:\/\/[-_.!~*'()a-zA-Z0-9;\/?:\@&=+\$,%#]+$";
			 var re=new RegExp(strRegex);
			 return re.test(url);
		 },
		defaultOptions = {
			restUrl: "/textblocks/",
			hidePanel: true,
			thisBlock : $(this),
			savedText : "",
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
								options.popup.find('form').find('input[name="height"]').val(self.css('height'));
								options.popup.find('form').find('input[type="button"]').css('background',self.css('background'));
								options.popup.find('form').find('textarea[name="classes"]').text(self.attr('class'));
								
								options.popup.find('select[name="textFontFamilySelector"]').val(self.css('font-family').replace(/'+/g,""));
								options.popup.find('form').find('input[name="size"]').val(parseInt(self.css('font-size').replace(/\D+/g,'')));
								
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						null,
						{
							data:{name: "Копировать",
							click: function(){
								//Здесь необходимо сохранять содержимое contentiable и textarea но не копировать их
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
									
									//Цвет и шрифт
									self.css('font-family',options.popup.find('select[name="textFontFamilySelector"]').val());
									self.css('font-size',parseInt(options.popup.find('form').find('input[name="size"]').val().replace(/\D+/g,''))+'px');
									
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
		var self = $(this), 
				inside=false, 
				stikPanelOptions={width: "100%", position: "relative", left: "0px", top:"0px"},
				textareaPanelOptions={width: "100%", height: "100%", /*position: "absolute", */left: "0px", top:"0px"},
				editablePanelOptions={width: "100%", height: "100%", /*position: "absolute",*/ left: "0px", top:"0px"};
		self.blockPlugin(options);
		self.css('font-size',"15px");
		var panel = $("#textBlockPanel").clone().removeClass("hidden").css(stikPanelOptions),
				editableDiv = $("<div></div>").attr("contenteditable",true).css(editablePanelOptions),
				textArea = $("<textarea></textarea>").css(textareaPanelOptions), selStart=0, selEnd=0;
		if(options.savedText != ""){
			editableDiv.html(options.savedText);
			textArea.val(options.savedText);
		}
		self.click(function(e) {
			if(editableDiv.is(":visible")){ 
				var myDiv = editableDiv.get(0);
				myDiv.focus();
				if (window.getSelection && document.createRange) {
					// IE 9 and non-IE
					var sel = window.getSelection(),range = document.createRange();
					range.setStart(myDiv, 0);
					range.collapse(true);// совмещаем конец и начало в стартовой позиции 
					sel.removeAllRanges();
					sel.addRange(range);
				} else if (document.body.createTextRange) {
					// IE < 9
					var textRange = document.body.createTextRange();
					textRange.moveToElementText(myDiv);
					textRange.collapse(true);
					textRange.select();
				}
			}
		});
		
		self.prepend(panel);
		self.append(editableDiv);
		self.append(textArea);
		textArea.hide();
		textArea.select(function(e) {
			selStart=textArea[0].selectionStart;
			selEnd=textArea[0].selectionEnd;
		});

		if(options.hidePanel){
			panel.hide();
			self.hover(
				function() {
					panel.fadeIn();
				}, 
				function() {
					panel.fadeOut();
				});
		}
		//Control buttons on panel
		var wrapTo = function(tag, attributes, selection){
			if(editableDiv.is(":visible")){
				selection = typeof(selection)!="object" ? $.selection().get() : selection;
				if(selection.html != ""){
					if(selection.rang.commonAncestorContainer.parentElement === editableDiv.get(0) 
					|| editableDiv.find(selection.rang.commonAncestorContainer.parentElement).length===1){
						if(editableDiv.find(selection.rang.commonAncestorContainer.parentElement).is(tag)) {
							var text = editableDiv.find(selection.rang.commonAncestorContainer.parentElement).text();
							editableDiv.find(selection.rang.commonAncestorContainer.parentElement).remove();
							$.selection().set(text);
						} else $.selection().set("<"+tag+attributes+">"+selection.html+"</"+tag+">");
					}
				}
			} else {
				var selectedText = textArea.val().substr(selStart, selEnd-selStart),
				firstPart = textArea.val().substr(0,selStart),
				secondPart = textArea.val().substr(selEnd,textArea.val().length);
		
				if(tag == "iframe" || selectedText != "") textArea.val(firstPart+"<"+tag+attributes+">"+selectedText+"</"+tag+">"+secondPart);
				if(tag == "img") textArea.val(firstPart+"<"+tag+attributes+"/>"+secondPart);
			}
			selStart=0, selEnd=0;
		}
		panel.find("#bold").click(function(e){
			e.preventDefault();
			wrapTo("b","");
			e.stopPropagation();
		});
		panel.find("#italics").click(function(e){
			e.preventDefault();
			wrapTo("i","");
			e.stopPropagation();
		});
		panel.find("#underline").click(function(e){
			e.preventDefault();
			wrapTo("span",' style="text-decoration: underline;"');
			e.stopPropagation();
		});
		
		var linkParentElement, targetText, linkSelectedText, linkFirstPart, linkSecondPart;
			
		panel.find("#textBlockLink").click(function(e){
			e.preventDefault();
			
			if(editableDiv.is(":visible")){
				linkParentElement = $($.selection().get().rang.commonAncestorContainer.parentElement),
				targetText=linkParentElement.html();
				linkSelectedText = targetText.substr($.selection().get().rang.startOffset,$.selection().get().rang.endOffset-$.selection().get().rang.startOffset),
				linkFirstPart = targetText.substr(0,$.selection().get().rang.startOffset),
				linkSecondPart = targetText.substr($.selection().get().rang.endOffset,targetText.length);
			}
			$("<form title='Вставьте URL'><input name='link' type='text' class='form-control'/></form>").appendTo($(document.body)).dialog({
				autoOpen: false,
						width: "300px",
						buttons: [
							{
								text: "Ok",
								click: function() {
									var linkParams = ' href="'+$( this ).find("input[name='link']").val()+'" target="blank"';
									if(editableDiv.is(":visible")){
										linkParentElement.html(linkFirstPart+"<a"+linkParams+">"+linkSelectedText+"</a>"+linkSecondPart);
									} else wrapTo("a",linkParams);
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
				}).dialog('open');
			e.stopPropagation();
		});
		panel.find("#floatLeft").click(function(e){
			e.preventDefault();
			wrapTo("span",'  style="float: left;"');
			e.stopPropagation();
		});
		panel.find("#floatRight").click(function(e){
			e.preventDefault();
			wrapTo("span",'  style="float: right;"');
			e.stopPropagation();
		});
		panel.find("#floatCenter").click(function(e){
			e.preventDefault();
			wrapTo("center","");
			e.stopPropagation();
		});
		editableDiv.keypress(function( event ) {
			textArea.val(editableDiv.html());
			options.savedText = textArea.val();
		});
		textArea.keypress(function( event ) {
			editableDiv.html(textArea.val());
			options.savedText = textArea.val();
		});
		panel.find("#htmlTextBoxText").click(function(e){
			e.preventDefault();
			selStart=0, selEnd=0;
			if(editableDiv.is(":visible")){ 
				textArea.val(editableDiv.html());
			} else editableDiv.html(textArea.val());
			editableDiv.toggle();
			textArea.toggle();
			e.stopPropagation();
		});
		panel.find("#colourFone").click(function(e){
			e.preventDefault();
			if(editableDiv.is(":visible")){
				linkParentElement = $($.selection().get().rang.commonAncestorContainer.parentElement),
				targetText=linkParentElement.html();
				linkSelectedText = targetText.substr($.selection().get().rang.startOffset,$.selection().get().rang.endOffset-$.selection().get().rang.startOffset),
				linkFirstPart = targetText.substr(0,$.selection().get().rang.startOffset),
				linkSecondPart = targetText.substr($.selection().get().rang.endOffset,targetText.length);
			}
			var colors = options.colors.clone().removeClass('hidden').dialog({autoOpen: false,width: 500,height: 400,
				buttons: [
						{
							text: "Отмена",
							click: function() {
								$( this ).remove();
							}
						}
					]
				});
				colors.dialog("open");
				colors.find('td').click(function(){
					//options.popup.find('#'+options.colors.attr('checkerid')).css('background',$(this).attr('bgcolor'));
					var textParams = ' style="background-color: '+$(this).attr('bgcolor')+';"';
					if(editableDiv.is(":visible")){
						linkParentElement.html(linkFirstPart+"<span"+textParams+">"+linkSelectedText+"</span>"+linkSecondPart);
					} else wrapTo("span",textParams);
					colors.remove();
				});
			e.stopPropagation();
		});
		panel.find("#colourLeters").click(function(e){
			e.preventDefault();
			if(editableDiv.is(":visible")){
				linkParentElement = $($.selection().get().rang.commonAncestorContainer.parentElement),
				targetText=linkParentElement.html();
				linkSelectedText = targetText.substr($.selection().get().rang.startOffset,$.selection().get().rang.endOffset-$.selection().get().rang.startOffset),
				linkFirstPart = targetText.substr(0,$.selection().get().rang.startOffset),
				linkSecondPart = targetText.substr($.selection().get().rang.endOffset,targetText.length);
			}
			var colors = options.colors.clone().removeClass('hidden').dialog({autoOpen: false,width: 500,height: 400,
				buttons: [
						{
							text: "Отмена",
							click: function() {
								$( this ).remove();
							}
						}
					]
				});
				colors.dialog("open");
				colors.find('td').click(function(){
					var textParams = ' style="color: '+$(this).attr('bgcolor')+';"';
					if(editableDiv.is(":visible")){
						linkParentElement.html(linkFirstPart+"<span"+textParams+">"+linkSelectedText+"</span>"+linkSecondPart);
					} else wrapTo("span",textParams);
					colors.remove();
				});
			e.stopPropagation();
		});
		panel.find("#videoForText").click(function(e){
			e.preventDefault();
			if(editableDiv.is(":visible")){
				linkParentElement = $($.selection().get().rang.commonAncestorContainer.parentElement),
				targetText=linkParentElement.html();
				linkSelectedText = targetText.substr($.selection().get().rang.startOffset,$.selection().get().rang.endOffset-$.selection().get().rang.startOffset),
				linkFirstPart = targetText.substr(0,$.selection().get().rang.startOffset),
				linkSecondPart = targetText.substr($.selection().get().rang.endOffset,targetText.length);
			}
			//Просто фрейм с видео
			var videoForm = $("<div id='videoForTextPopup' title='Параметры'></div>").append($('#videoForTextForm').clone().removeClass('hidden')).dialog({
				autoOpen: false,
						width: "450px",
						buttons: [
							{
								text: "Ok",
								click: function() {
									var videoLink = $( this ).find("#youtubeLink").val(),
											errorMessage = "";
									if(videoLink != ""){
										if(isURL(videoLink)){
											var width = $( this ).find('form').find('input[name="width"]').val();
											if(width == '100%' || (width.indexOf('%') > -1 && parseInt(width.match(/\d+/)) > 100) 
													|| $( this ).find('form').find('input[name="stretch"]').is(':checked')){
												$( this ).find('form').find('input[name="width"]').val("100%");
												width = "100%"
											}
											var height = $( this ).find('form').find('input[name="height"]').val();
											if(height == '100%' || (height.indexOf('%') > -1 && parseInt(height.match(/\d+/)) > 100)){
												height = '100%';
											} 
											var embedCode = "";
											if((videoLink.includes("youtube.com") 
													|| videoLink.includes("youtu.be"))
													&& videoLink.includes("watch?v=")){
												embedCode = videoLink.substring(videoLink.indexOf("=")+1,videoLink.indexOf("=")+12);
											}
											
											if(videoLink.includes("youtu.be")){
												embedCode = videoLink.substring(videoLink.lastIndexOf("/")+1,videoLink.length);
												
											}
											var videoframe;
											if(embedCode != ""){
												videoframe = $('<iframe width="'+width+'" height="'+height+'" src="http://www.youtube.com/embed/'+embedCode+'" frameborder="0"></iframe>');
												//videoframe.attr("src", videoframe.attr("src"));
											} else {
												errorMessage = "<center>"+videoLink + "не является ссылкой на видео youtube!</center><br>"
											}
										} else {
											errorMessage = "<center>"+videoLink + "вообще не является ссылкой!</center>";
											
										}
										if(errorMessage != ""){ 
											var alertMessage = $(errorMessage).dialog({
												autoOpen: false,
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
											alertMessage.dialog("open");
										} else {
											if(editableDiv.is(":visible")){
												if(linkFirstPart == ""){
													editableDiv.append(videoframe);
												} else linkParentElement.html(linkFirstPart+videoframe.wrap('<div/>').parent().html()+linkSecondPart);
											} else { 
												wrapTo("iframe",' width="'+width+'" height="'+height+'" src="http://www.youtube.com/embed/'+embedCode+'" frameborder="0"');
											}
											$( this ).remove();
										}
									} else $("<center>Не указана ссылка на видео!</center>").dialog({
												autoOpen: false,
												width: "250px",
												buttons: [
													{
														text: "Ok",
														click: function() {
															$( this ).remove();
														}
													}
												]
											}).dialog("open");
									
									
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
			videoForm.dialog('open');
			
			e.stopPropagation();
		});
		panel.find("#imageForText").click(function(e){
			e.preventDefault();
			if(editableDiv.is(":visible")){
				linkParentElement = $($.selection().get().rang.commonAncestorContainer.parentElement),
				targetText=linkParentElement.html();
				linkSelectedText = targetText.substr($.selection().get().rang.startOffset,$.selection().get().rang.endOffset-$.selection().get().rang.startOffset),
				linkFirstPart = targetText.substr(0,$.selection().get().rang.startOffset),
				linkSecondPart = targetText.substr($.selection().get().rang.endOffset,targetText.length);
			}
			var imageForm = $("<div id='videoForTextPopup' title='Параметры'></div>").append($('#imageForTextForm').clone().removeClass('hidden')).dialog({
				autoOpen: false,
						width: "550px",
						buttons: [
							{
								text: "Ok",
								click: function() {
									if($( this ).find("#imagePreView").children("img").length != 0){
										var imgForBlock = $( this ).find("#imagePreView").children("img").clone();
										var width = $( this ).find('form').find('input[name="width"]').val();
										if(width == '100%' || (width.indexOf('%') > -1 && parseInt(width.match(/\d+/)) > 100) 
												|| $( this ).find('form').find('input[name="stretch"]').is(':checked')){
											$( this ).find('form').find('input[name="width"]').val("100%");
											width = "100%"
										}
										var height = $( this ).find('form').find('input[name="height"]').val();
										if(height == '100%' || (height.indexOf('%') > -1 && parseInt(height.match(/\d+/)) > 100)){
											height = '100%';
										}
										
										imgForBlock.attr('width', width);
										imgForBlock.attr('height', height);
										if(editableDiv.is(":visible")){
											if(linkFirstPart == ""){
												editableDiv.append(imgForBlock);
											} else linkParentElement.html(linkFirstPart+imgForBlock.wrap('<div/>').parent().html()+linkSecondPart);
										} else { 
											wrapTo("img",' width="'+width+'" height="'+height+'" src="'+imgForBlock.attr("alt")+'" frameborder="0"');
										}
									}
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
		var files;	
		imageForm.find('input[type=file]').change(function(){
			files = this.files;
		});
		
		imageForm.find("#downloadBlockImage").click(function( event ){
			
			if(typeof(files) == "undefined" || files.length == 0){
				alert("Файл не выбран!");
				return false;
			}
			var dataForm = new FormData(imageForm.find('form')[0]);
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
							var imagePreView = imageForm.find("#imagePreView");
							imagePreView.children().remove();
							imagePreView.append($('<img src="'+resp.url+'" width="'+297+'px" alt="'+resp.url+'" />'));
							imageForm.find("#loadedImgWidth").text(resp.width);//height="'++'"
							imageForm.find("#loadedImgHeight").text(resp.height);
							if(imageForm.find('input[name="width"]').val() == ""){
								imageForm.find('input[name="width"]').val(resp.width);
							}
							if(imageForm.find('input[name="height"]').val() == ""){
								imageForm.find('input[name="height"]').val(resp.height);
							}
							var width = imageForm.find('input[name="width"]').val(),
								height = imageForm.find('input[name="height"]').val();
							imageForm.find("#forResizeButton").children().remove();
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
												imageForm.find("#loadedImgWidth").text(resizedData.width);//height="'++'"
												imageForm.find("#loadedImgHeight").text(resizedData.height);

											}
										});
									});
								var originalButton = $('<INPUT Type="BUTTON" class="form-control" value="Оригинал"/>').click(function(){
													imagePreView.children().remove();
													imagePreView.append($('<img src="'+resp.url+'" width="'+297+'px" alt="'+resp.url+'" />'));
													imageForm.find("#loadedImgWidth").text(resp.width);
													imageForm.find("#loadedImgHeight").text(resp.height);
												});
								imageForm.find("#forResizeButton").append(originalButton);
								imageForm.find("#forResizeButton").append(resizeButton);
							}
						} else {
							alert("Не удалось загрузить файл! Возможно он не является картинкой.")
						}
					}
				});
			});	
			imageForm.dialog('open');
			
			e.stopPropagation();
		});
		
		panel.find("#header").change(function(e) {
			wrapTo("h"+$(this).val().replace(/\D+/g,''),"");
		});
		
		panel.find("#header").click(function(e) {
			e.stopPropagation();
		});
    }
})(jQuery)