(function( $ ){
    $.fn.accordeonPlugin = function(options){
		var isURL = function (url) {
			var strRegex = "^s?https?:\/\/[-_.!~*'()a-zA-Z0-9;\/?:\@&=+\$,%#]+$";
			 var re=new RegExp(strRegex);
			 return re.test(url);
		 },
		defaultOptions = {
			restUrl: "/accordeonblocks/",
			thisBlock : $(this),
			resizable : true,
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
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Добавить вкладку",
							click: function(){
								/*
								* <div class="accordeon-text" data-block="text"><h3>НАЗВАНИЕ ВКЛАДКИ(сделать настраиваемой текстовую панель)</h3></div> 
										   <div class="accordeon-text" data-block="text">ТЕКСТ ВКЛАДКИ</div>
								*/
							   var localPopup = $("#accordeonItemAddForm").clone().removeClass("hidden");
							   //Values for select
								self.children('div').each(function(k,v){
									if((k+1)% 2 != 0){
										var newOption = $('<option>'+$(v).attr("id")+'-'+$(v).find('div[contenteditable="true"]').text().substring(0,25)+"..."+'</option>')
															.attr('value',$(v).attr("id"));
										localPopup.find('select[name="push"]').append(newOption);
									}
								});
							   $("<div id='"+self.attr("id")+"AddItem' title='Добавить вкладку'></div>").append(localPopup).dialog({
									autoOpen: true,
									width: options.popupWidth+"px",
									buttons: [
										{
											text: "Ok",
											click: function() {
												var header = $( this ).find('form').find("input[name='name']").val(),
													push = $( this ).find('form').find('select[name="push"]').prop('selectedIndex'),
													selectedId = $($( this ).find('form').find('select[name="push"]').children()[push]).attr("value");
												var newHeaderBlock = $("<div></div>").attr("type","TextBlock"),
													newDescriptionBlock = $("<div></div>").attr("type","TextBlock"); 
												options.pasteElement(newHeaderBlock, self, {options:{pushAfter:selectedId == "none" ? null : $("#"+selectedId).next(), savedText: "<h3>"+header+"</h3>", dependBlock: newDescriptionBlock}});
												options.pasteElement(newDescriptionBlock, self, {options:{pushAfter:newHeaderBlock, savedText: "Содержание вкладки", dependBlock: newHeaderBlock}});
//												
												$( this ).dialog( "close" );
												$( this ).remove();
											}
										},
										{
											text: "Отмена",
											click: function() {
												$( this ).dialog( "close" );
												$( this ).remove();
											}
										}
									]
							   });
								options.parentElement.hide();
							}}
						},
						{
							data:{name: "Копировать",
							click: function(){
								var clone = self.clone().attr('id', '');
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
		var self = $(this), inside=false;
		options = self.blockPlugin(options);
		
		
    }
})(jQuery)