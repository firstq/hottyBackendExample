(function( $ ){
    $.fn.videoPlugin = function(options){
		var isURL = function (url) {
			var strRegex = "^s?https?:\/\/[-_.!~*'()a-zA-Z0-9;\/?:\@&=+\$,%#]+$";
			 var re=new RegExp(strRegex);
			 return re.test(url);
		 },
		defaultOptions = {
			restUrl: "/videoblocks",
			videoUrl: "",
			appendVideo: function(videoLink,width,height){
				var errorMessage = "";
				if(videoLink != ""){
					//Разбор линка и вставка iframe
					if(isURL(videoLink)){
						var embedCode = "";
						if((videoLink.includes("youtube.com") 
								|| videoLink.includes("youtu.be"))
								&& videoLink.includes("watch?v=")){
							embedCode = videoLink.substring(videoLink.indexOf("=")+1,videoLink.indexOf("=")+12);
						}
						//Another link https://youtu.be/SxbZiQ760Xk
						if(videoLink.includes("youtu.be")){
							embedCode = videoLink.substring(videoLink.lastIndexOf("/")+1,videoLink.length);

						}
						if(videoLink.includes("embed")){
							embedCode = videoLink.substring(videoLink.lastIndexOf("/")+1,videoLink.length);

						}
						if(embedCode != ""){
							self.children('input').remove();
							self.children('iframe').remove();
							//width="'+width+'" height="'+height+'"
							var videoframe = $('<iframe width="'+width+'" height="'+height+'" src="http://www.youtube.com/embed/'+embedCode+'" frameborder="0" style="position: relative;margin: auto;top: 5%;right: 0;bottom: 0;left: 5%;"></iframe>');
							self.append(videoframe);
							videoframe.attr("src", videoframe.attr("src"));
						} else {
							errorMessage = "<center>"+videoLink + "не является ссылкой на видео youtube!</center><br>"
						}
					} else {
						errorMessage = "<center>"+videoLink + "вообще не является ссылкой!</center>";

					}
					if(errorMessage != ""){ var alertMessage = $(errorMessage).dialog({
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
					}
				} 
			},
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
								options.popup.dialog( "open" );
								options.parentElement.hide();
							}}
						},
						null,
						{
							data:{name: "Копировать",
							click: function(){
								var clone = self.clone().attr('id', '');
								document.body.context_menu_plugin_buffer={
									clone:clone, 
									options: options
								};
								clone.children("div").remove();
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

									//And video
									
									var videoLink = options.popup.find("#youtubeLink").val();
									options.appendVideo(videoLink,"90%","90%");

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
		if(options.videoUrl != "") options.appendVideo(options.videoUrl,"90%","90%");
    }
})(jQuery)