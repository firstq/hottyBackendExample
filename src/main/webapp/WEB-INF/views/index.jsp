
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app='myAppName'>
    <head>
        <title>Example</title>
        <meta charset="UTF-8">
        
        <link href="http://localhost:8383/hottyBlocks/css/menu.css" type="text/css" rel="stylesheet">
		<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.9/angular.min.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
		<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
        <script src="http://localhost:8084/hottyBackendExample/resources/js/lib/jquery.selection.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/blockPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/imagePlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/textPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/videoPlugin.js" type="text/javascript"></script>
		<script src="http://localhost:8084/hottyBackendExample/resources/js/menuPlugin.js" type="text/javascript"></script>
		<link rel="stylesheet" href="http://localhost:8084/hottyBackendExample/resources/styles/bootstrap.min.css">
		<link rel="stylesheet" href="http://localhost:8084/hottyBackendExample/resources/styles/fix.css">
        <script> 
			var App = angular.module('myAppName', []);
			App.directive('simpleBlock', function() {
				return {
					restrict: 'A',
					link: function(scope, element, attrs) {
						switch (attrs.type) {
							case "class su.hotty.example.domain.ImageBlock":
								$(element).imagePlugin({thisBlock:$(element),popup: $("<div id='imageBlock"+attrs.id+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden'))});
							break
							case "class su.hotty.example.domain.VideoBlock":
								$(element).videoPlugin({thisBlock:$(element),popup: $("<div id='videoBlock"+attrs.id+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden'))});
							break
							case "class su.hotty.example.domain.MenuBlock":
								$(element).menuPlugin({thisBlock:$(element).attr("id",attrs.id)});
							  break
							case "class su.hotty.example.domain.TextBlock":
								$(element).textPlugin({thisBlock:$(element),popup: $("<div id='"+attrs.id+"Popup' title='Параметры'></div>").append($('#textForm').clone().removeClass('hidden'))});
							  break
							default:
								$(element).blockPlugin({popup: $("<div id='"+attrs.id+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden').show())});
						}

					}
				};
			});
			
			App.controller('RepeatController', function($scope,$http) {
					$http.get("http://localhost:8084/hottyBackendExample/rest/service/blocks").success(function(data,status,headers,config){
						$scope.blocks = data;
					 });
				});
			
        </script>
    </head>
    <body>
		<div style="height: 2000px" ng-controller='RepeatController'>
			<div id="{{ block.id }}" style="{{ block.styles }}" ng-repeat="block in blocks" simple-block="{}" type="{{ block.blockType }}"> {{ block.blockType }} </div>
		</div>
		
		<!-- HELP ELEMENTS -->
		<div style="display: none;">
			
			<form id="textForm" class='navbar-form hidden'>
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Цвет: &nbsp;</td> <td><INPUT Type="BUTTON" href="" id='showColors' class='form-control block_colors' value="Выбрать цвет"/><span id='activeColor'>&nbsp;</span></td></tr>
			<tr><td>Вид пунктов меню: &nbsp;</td> <td>
					<table class="menu_settings_text">
						<tr><td colspan="2">&nbsp;Шрифт:&nbsp;<select name="textFontFamilySelector" class='form-control' >
								<option></option>
								<option>Arial</option>
								<option>Arial Black</option>
								<option>Comic Sans MS</option>
								<option>Courier New</option>
								<option>Helvetica</option>
								<option>Garamond</option>
								<option>Georgia</option>
								<option>Impact</option>
								<option>Lucida Console</option>
								<option>Lucida Sans Unicode</option>
								<option>Microsoft Sans Serif</option>
								<option>Tahoma</option>
								<option>Times New Roman</option>
								<option>Trebuchet MS</option>
								<option>Verdana</option>
								<option>Webdings</option>
								<option>western</option>
								<option>wingdings</option>
								<option>zapfellipt bt</option>
							  </select>&nbsp;|&nbsp;
							  <input name='size' class='form-control' type='text' size="2"/>&nbsp;px
							</td>
						</tr>
					</table>
				</td></tr>
		</table>
	</form>
	<div id="textBlockPanel" class='hidden' style="float: top;width: 100%">
		<form class='navbar-form'>
			
				
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/bold.svg" id='bold' class='form-control' title="Жирный"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/italic.svg" id='italics' class="form-control" title="Курсив"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/underline.svg" id='underline' class='form-control' title="Подчеркнутый"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/link.svg" id='textBlockLink' class='form-control' title="Ссылка"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/paragraph-left.svg" id='floatLeft' class='form-control' title="Выровнять по левому краю"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/paragraph-right.svg" id='floatRight' class='form-control' title="Выровнять по правому краю"/>
			<INPUT Type="IMAGE" href="javascript:void(0);" src="/viewHotty/resources/images/paragraph-center.svg" id='floatCenter' class='form-control' title="Выровнять по центру"/>
			
			<fieldset>
				<INPUT Type="BUTTON" id='htmlTextBoxText' class='form-control' value='&lt;/&gt;' title="HTML код"/>
				<INPUT Type="BUTTON" id='colourFone' class='form-control' value="Цвет фона" title="Цвет фона"/>
				<INPUT Type="BUTTON" id='colourLeters' class='form-control' value="Цвет текста" title="Цвет текста"/>
				<INPUT Type="BUTTON" id='videoForText' class='form-control' value="▶ Видео" title="Видео с youtube"/>
				<INPUT Type="BUTTON" id='imageForText' class='form-control' value="☀ Картинка" title="Картинка"/>
				
				<select id="header" class='form-control' title="Заголовок" >
								<option></option>
								<option>Заголовок 1</option>
								<option>Заголовок 2</option>
								<option>Заголовок 3</option>
								<option>Заголовок 4</option>
				</select>
				
				
			
				
			</fieldset>
			
		</form>
	</div>
	
	<form id="imageForTextForm" class='navbar-form hidden' enctype="multipart/form-data">
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			
			<tr><td>Изображение: &nbsp;</td> <td> 
					<table><tr align="center"><td id="loadedImgWidth"></td><td></td></tr>
						<tr><td><div id="imagePreView" style="border: 1px dashed rgb(0, 0, 0); margin: 5px; width: 297px"></div></td><td id="loadedImgHeight"> </td></tr>
								<tr align="center"><td id="forResizeButton">
						
							</td><td></td></tr>
					</table><br/>
				</td></tr>
			<tr><td><INPUT Type="BUTTON" href="" id='downloadBlockImage' class='form-control' value="Загрузить"/> &nbsp;</td> <td><INPUT name="imageFile" type="file" accept="image/*" class='form-control'/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	
	<form id="imageForm" class='navbar-form hidden' enctype="multipart/form-data">
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Изображение: &nbsp;</td> <td> 
					<table><tr align="center"><td id="loadedImgWidth"></td><td></td></tr>
						<tr><td><div id="imagePreView" style="border: 1px dashed rgb(0, 0, 0); margin: 5px; width: 297px"></div></td><td id="loadedImgHeight"> </td></tr>
								<tr align="center"><td id="forResizeButton">
						
							</td><td></td></tr>
					</table><br/>
				</td></tr>
			<tr><td><INPUT Type="BUTTON" href="" id='downloadBlockImage' class='form-control' value="Загрузить"/> &nbsp;</td> <td><INPUT name="imageFile" type="file" accept="image/*" class='form-control'/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	
	<form id="socialForm" class='navbar-form hidden'>
		<table>
			
			<tr><td>Отступ:</td> <td><input name='margin' type='text' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Отображать иконки: &nbsp;</td> <td> 
					<div id="socialNetTypesContainer" style="border: 1px dashed rgb(0, 0, 0); margin: 5px">
						<input type="checkbox" name="stick" value="top"/> <span>vk</span><Br/>
									<input type="checkbox" name="stick" value="left"/> <span>twitter</span><Br/>
									<input type="checkbox" name="stick" value="right"/> <span>facebook</span><Br/>
									<input type="checkbox" name="stick" value="center"/> <span>ok</span><Br/>
									</div>
				</td></tr>
			<tr><td>&nbsp;</td> <td><INPUT Type="BUTTON" href="" id='downloadOwnImages' class='form-control block_colors' value="Загрузить собственные иконки для соц сетей"/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	
	<form id="videoForTextForm" class='navbar-form hidden'>
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			
			<tr><td>Видео youtube: &nbsp;</td> <td><INPUT Type="text" href="" id='youtubeLink' class='form-control'/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	
	<form id="videoForm" class='navbar-form hidden'>
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Видео youtube: &nbsp;</td> <td><INPUT Type="text" href="" id='youtubeLink' class='form-control'/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	
	<form id="menuItemForm" class='navbar-form hidden'>
		<table>
			<tr><td>Название:</td> <td><input name='name' class='form-control' type='text'/></td></tr>
			<tr><td>Ссылка:</td> <td><input name='link' type='text' class='form-control'/></td></tr>
			<tr><td>Приоритет:</td> <td><input name='priority' type='text' class='form-control'/></td></tr>
			<tr><td>Верхний уровень:</td> <td><input name='istop' type='checkbox' class='form-control'/></td></tr>
						<tr><td>Родитель:</td> <td><select name="menuItemParent" class='form-control' >
								<!--	<option>Option 1</option>
									<option>Option 2 **Добавить вспомогательный элемент для "приоритет"</option> -->
					</select></td></tr>
		</table>
	</form>
	
	<form id="menuForm" class='navbar-form hidden'>
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Вертикальное:</td> <td><input name='isvertical' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Цвет: &nbsp;</td> <td><INPUT Type="BUTTON" href="" id='showColors' class='form-control block_colors' value="Выбрать цвет"/><span id='activeColor'>&nbsp;</span></td></tr>
			<tr><td>Вид пунктов меню: &nbsp;</td> <td>
					<table class="menu_settings_text">
						<tr><td>&nbsp;<a href="#" id="menuTextColor" class="block_colors">Цвет текста</a>&nbsp;</td><td>&nbsp;<a href="#" class="block_colors" id="menuBackgroundColor">Цвет фона</a>&nbsp;</td></tr>
						<tr><td>&nbsp;<a href="#" id="menuHoverTextColor" class="block_colors">Цвет выделенного текста</a>&nbsp;</td><td>&nbsp;<a href="#" class="block_colors" id="menuHoverBackgroundColor">Цвет выделенного фона</a>&nbsp;</td></tr>
						<tr><td>&nbsp;Тень текста&nbsp;</td><td>&nbsp;<input name='shadow' type='checkbox' class='form-control'/>&nbsp;</td></tr>
						<tr><td colspan="2">&nbsp;Шрифт:&nbsp;<select name="menuFontFamilySelector" class='form-control' >
								<option>Arial</option>
								<option>Arial Black</option>
								<option>Comic Sans MS</option>
								<option>Courier New</option>
								<option>Helvetica</option>
								<option>Garamond</option>
								<option>Georgia</option>
								<option>Impact</option>
								<option>Lucida Console</option>
								<option>Lucida Sans Unicode</option>
								<option>Microsoft Sans Serif</option>
								<option>Tahoma</option>
								<option>Times New Roman</option>
								<option>Trebuchet MS</option>
								<option>Verdana</option>
								<option>Webdings</option>
								<option>western</option>
								<option>wingdings</option>
								<option>zapfellipt bt</option>
							  </select>&nbsp;|&nbsp;
							  <INPUT Type="BUTTON" href="" id='setBold' class='form-control' value="B"/>&nbsp;
							  <INPUT Type="BUTTON" href="" id='setItalic' class='form-control' value="I"/>&nbsp;
							</td>
						</tr>
						<tr><td>Размер текста</td><td><input name='size' class='form-control' type='text'/>&nbsp;px</td></tr>
						<tr><td>Отступ</td><td><input name='marginright' class='form-control' type='text'/>&nbsp;px</td></tr>
					</table>
				</td></tr>
		</table>
	</form>
	<form id="simpleForm" class='navbar-form hidden'>
		<table>
			<tr><td>Ширина:</td> <td><input name='width' class='form-control' type='text'/></td></tr>
			<tr><td>Высота:</td> <td><input name='height' type='text' class='form-control'/></td></tr>
			<tr><td>Растянуть:</td> <td><input name='stretch' type='checkbox' class='form-control'/></td></tr>
			<tr><td>Приклеить: &nbsp;</td> <td>
									<input type="radio" name="stick" value="top"/> Вверх<Br/>
									<input type="radio" name="stick" value="left"/> Слева<Br/>
									<input type="radio" name="stick" value="right"/> Справа<Br/>
									<input type="radio" name="stick" value="center"/> По центру<Br/></td></tr>
			<tr><td>Цвет: &nbsp;</td> <td><INPUT Type="BUTTON" href="" id='showColors' class='form-control block_colors' value="Выбрать цвет"/><span id='activeColor'>&nbsp;</span></td></tr>
		</table>
	</form>
	<div id="simpleColors" title='Выбрать цвет'>
	<table style='width: 100%; height: 100%'>
	<tbody><tr><td bgcolor="#FBEFEF"></td><td bgcolor="#FBF2EF"></td><td bgcolor="#FBF5EF"></td><td bgcolor="#FBF8EF"></td><td bgcolor="#FBFBEF"></td><td bgcolor="#F8FBEF"></td><td bgcolor="#F5FBEF"></td><td bgcolor="#F2FBEF"></td><td bgcolor="#EFFBEF"></td><td bgcolor="#EFFBF2"></td><td bgcolor="#EFFBF5"></td><td bgcolor="#EFFBF8"></td><td bgcolor="#EFFBFB"></td><td bgcolor="#EFF8FB"></td><td bgcolor="#EFF5FB"></td><td bgcolor="#EFF2FB"></td><td bgcolor="#EFEFFB"></td><td bgcolor="#F2EFFB"></td><td bgcolor="#F5EFFB"></td><td bgcolor="#F8EFFB"></td><td bgcolor="#FBEFFB"></td><td bgcolor="#FBEFF8"></td><td bgcolor="#FBEFF5"></td><td bgcolor="#FBEFF2"></td><td bgcolor="#FFFFFF"></td></tr>
	<tr><td bgcolor="#F8E0E0"></td><td bgcolor="#F8E6E0"></td><td bgcolor="#F8ECE0"></td><td bgcolor="#F7F2E0"></td><td bgcolor="#F7F8E0"></td><td bgcolor="#F1F8E0"></td><td bgcolor="#ECF8E0"></td><td bgcolor="#E6F8E0"></td><td bgcolor="#E0F8E0"></td><td bgcolor="#E0F8E6"></td><td bgcolor="#E0F8EC"></td><td bgcolor="#E0F8F1"></td><td bgcolor="#E0F8F7"></td><td bgcolor="#E0F2F7"></td><td bgcolor="#E0ECF8"></td><td bgcolor="#E0E6F8"></td><td bgcolor="#E0E0F8"></td><td bgcolor="#E6E0F8"></td><td bgcolor="#ECE0F8"></td><td bgcolor="#F2E0F7"></td><td bgcolor="#F8E0F7"></td><td bgcolor="#F8E0F1"></td><td bgcolor="#F8E0EC"></td><td bgcolor="#F8E0E6"></td><td bgcolor="#FAFAFA"></td></tr>
	<tr><td bgcolor="#F6CECE"></td><td bgcolor="#F6D8CE"></td><td bgcolor="#F6E3CE"></td><td bgcolor="#F5ECCE"></td><td bgcolor="#F5F6CE"></td><td bgcolor="#ECF6CE"></td><td bgcolor="#E3F6CE"></td><td bgcolor="#D8F6CE"></td><td bgcolor="#CEF6CE"></td><td bgcolor="#CEF6D8"></td><td bgcolor="#CEF6E3"></td><td bgcolor="#CEF6EC"></td><td bgcolor="#CEF6F5"></td><td bgcolor="#CEECF5"></td><td bgcolor="#CEE3F6"></td><td bgcolor="#CED8F6"></td><td bgcolor="#CECEF6"></td><td bgcolor="#D8CEF6"></td><td bgcolor="#E3CEF6"></td><td bgcolor="#ECCEF5"></td><td bgcolor="#F6CEF5"></td><td bgcolor="#F6CEEC"></td><td bgcolor="#F6CEE3"></td><td bgcolor="#F6CED8"></td><td bgcolor="#F2F2F2"></td></tr>
	<tr><td bgcolor="#F5A9A9"></td><td bgcolor="#F5BCA9"></td><td bgcolor="#F5D0A9"></td><td bgcolor="#F3E2A9"></td><td bgcolor="#F2F5A9"></td><td bgcolor="#E1F5A9"></td><td bgcolor="#D0F5A9"></td><td bgcolor="#BCF5A9"></td><td bgcolor="#A9F5A9"></td><td bgcolor="#A9F5BC"></td><td bgcolor="#A9F5D0"></td><td bgcolor="#A9F5E1"></td><td bgcolor="#A9F5F2"></td><td bgcolor="#A9E2F3"></td><td bgcolor="#A9D0F5"></td><td bgcolor="#A9BCF5"></td><td bgcolor="#A9A9F5"></td><td bgcolor="#BCA9F5"></td><td bgcolor="#D0A9F5"></td><td bgcolor="#E2A9F3"></td><td bgcolor="#F5A9F2"></td><td bgcolor="#F5A9E1"></td><td bgcolor="#F5A9D0"></td><td bgcolor="#F5A9BC"></td><td bgcolor="#E6E6E6"></td></tr>
	<tr><td bgcolor="#F78181"></td><td bgcolor="#F79F81"></td><td bgcolor="#F7BE81"></td><td bgcolor="#F5DA81"></td><td bgcolor="#F3F781"></td><td bgcolor="#D8F781"></td><td bgcolor="#BEF781"></td><td bgcolor="#9FF781"></td><td bgcolor="#81F781"></td><td bgcolor="#81F79F"></td><td bgcolor="#81F7BE"></td><td bgcolor="#81F7D8"></td><td bgcolor="#81F7F3"></td><td bgcolor="#81DAF5"></td><td bgcolor="#81BEF7"></td><td bgcolor="#819FF7"></td><td bgcolor="#8181F7"></td><td bgcolor="#9F81F7"></td><td bgcolor="#BE81F7"></td><td bgcolor="#DA81F5"></td><td bgcolor="#F781F3"></td><td bgcolor="#F781D8"></td><td bgcolor="#F781BE"></td><td bgcolor="#F7819F"></td><td bgcolor="#D8D8D8"></td></tr>
	<tr><td bgcolor="#FA5858"></td><td bgcolor="#FA8258"></td><td bgcolor="#FAAC58"></td><td bgcolor="#F7D358"></td><td bgcolor="#F4FA58"></td><td bgcolor="#D0FA58"></td><td bgcolor="#ACFA58"></td><td bgcolor="#82FA58"></td><td bgcolor="#58FA58"></td><td bgcolor="#58FA82"></td><td bgcolor="#58FAAC"></td><td bgcolor="#58FAD0"></td><td bgcolor="#58FAF4"></td><td bgcolor="#58D3F7"></td><td bgcolor="#58ACFA"></td><td bgcolor="#5882FA"></td><td bgcolor="#5858FA"></td><td bgcolor="#8258FA"></td><td bgcolor="#AC58FA"></td><td bgcolor="#D358F7"></td><td bgcolor="#FA58F4"></td><td bgcolor="#FA58D0"></td><td bgcolor="#FA58AC"></td><td bgcolor="#FA5882"></td><td bgcolor="#BDBDBD"></td></tr>
	<tr><td bgcolor="#FE2E2E"></td><td bgcolor="#FE642E"></td><td bgcolor="#FE9A2E"></td><td bgcolor="#FACC2E"></td><td bgcolor="#F7FE2E"></td><td bgcolor="#C8FE2E"></td><td bgcolor="#9AFE2E"></td><td bgcolor="#64FE2E"></td><td bgcolor="#2EFE2E"></td><td bgcolor="#2EFE64"></td><td bgcolor="#2EFE9A"></td><td bgcolor="#2EFEC8"></td><td bgcolor="#2EFEF7"></td><td bgcolor="#2ECCFA"></td><td bgcolor="#2E9AFE"></td><td bgcolor="#2E64FE"></td><td bgcolor="#2E2EFE"></td><td bgcolor="#642EFE"></td><td bgcolor="#9A2EFE"></td><td bgcolor="#CC2EFA"></td><td bgcolor="#FE2EF7"></td><td bgcolor="#FE2EC8"></td><td bgcolor="#FE2E9A"></td><td bgcolor="#FE2E64"></td><td bgcolor="#A4A4A4"></td></tr>
	<tr><td bgcolor="#FF0000"></td><td bgcolor="#FF4000"></td><td bgcolor="#FF8000"></td><td bgcolor="#FFBF00"></td><td bgcolor="#FFFF00"></td><td bgcolor="#BFFF00"></td><td bgcolor="#80FF00"></td><td bgcolor="#40FF00"></td><td bgcolor="#00FF00"></td><td bgcolor="#00FF40"></td><td bgcolor="#00FF80"></td><td bgcolor="#00FFBF"></td><td bgcolor="#00FFFF"></td><td bgcolor="#00BFFF"></td><td bgcolor="#0080FF"></td><td bgcolor="#0040FF"></td><td bgcolor="#0000FF"></td><td bgcolor="#4000FF"></td><td bgcolor="#8000FF"></td><td bgcolor="#BF00FF"></td><td bgcolor="#FF00FF"></td><td bgcolor="#FF00BF"></td><td bgcolor="#FF0080"></td><td bgcolor="#FF0040"></td><td bgcolor="#848484"></td></tr>
	<tr><td bgcolor="#DF0101"></td><td bgcolor="#DF3A01"></td><td bgcolor="#DF7401"></td><td bgcolor="#DBA901"></td><td bgcolor="#D7DF01"></td><td bgcolor="#A5DF00"></td><td bgcolor="#74DF00"></td><td bgcolor="#3ADF00"></td><td bgcolor="#01DF01"></td><td bgcolor="#01DF3A"></td><td bgcolor="#01DF74"></td><td bgcolor="#01DFA5"></td><td bgcolor="#01DFD7"></td><td bgcolor="#01A9DB"></td><td bgcolor="#0174DF"></td><td bgcolor="#013ADF"></td><td bgcolor="#0101DF"></td><td bgcolor="#3A01DF"></td><td bgcolor="#7401DF"></td><td bgcolor="#A901DB"></td><td bgcolor="#DF01D7"></td><td bgcolor="#DF01A5"></td><td bgcolor="#DF0174"></td><td bgcolor="#DF013A"></td><td bgcolor="#6E6E6E"></td></tr>
	<tr><td bgcolor="#B40404"></td><td bgcolor="#B43104"></td><td bgcolor="#B45F04"></td><td bgcolor="#B18904"></td><td bgcolor="#AEB404"></td><td bgcolor="#86B404"></td><td bgcolor="#5FB404"></td><td bgcolor="#31B404"></td><td bgcolor="#04B404"></td><td bgcolor="#04B431"></td><td bgcolor="#04B45F"></td><td bgcolor="#04B486"></td><td bgcolor="#04B4AE"></td><td bgcolor="#0489B1"></td><td bgcolor="#045FB4"></td><td bgcolor="#0431B4"></td><td bgcolor="#0404B4"></td><td bgcolor="#3104B4"></td><td bgcolor="#5F04B4"></td><td bgcolor="#8904B1"></td><td bgcolor="#B404AE"></td><td bgcolor="#B40486"></td><td bgcolor="#B4045F"></td><td bgcolor="#B40431"></td><td bgcolor="#585858"></td></tr>
	<tr><td bgcolor="#8A0808"></td><td bgcolor="#8A2908"></td><td bgcolor="#8A4B08"></td><td bgcolor="#886A08"></td><td bgcolor="#868A08"></td><td bgcolor="#688A08"></td><td bgcolor="#4B8A08"></td><td bgcolor="#298A08"></td><td bgcolor="#088A08"></td><td bgcolor="#088A29"></td><td bgcolor="#088A4B"></td><td bgcolor="#088A68"></td><td bgcolor="#088A85"></td><td bgcolor="#086A87"></td><td bgcolor="#084B8A"></td><td bgcolor="#08298A"></td><td bgcolor="#08088A"></td><td bgcolor="#29088A"></td><td bgcolor="#4B088A"></td><td bgcolor="#6A0888"></td><td bgcolor="#8A0886"></td><td bgcolor="#8A0868"></td><td bgcolor="#8A084B"></td><td bgcolor="#8A0829"></td><td bgcolor="#424242"></td></tr>
	<tr><td bgcolor="#610B0B"></td><td bgcolor="#61210B"></td><td bgcolor="#61380B"></td><td bgcolor="#5F4C0B"></td><td bgcolor="#5E610B"></td><td bgcolor="#4B610B"></td><td bgcolor="#38610B"></td><td bgcolor="#21610B"></td><td bgcolor="#0B610B"></td><td bgcolor="#0B6121"></td><td bgcolor="#0B6138"></td><td bgcolor="#0B614B"></td><td bgcolor="#0B615E"></td><td bgcolor="#0B4C5F"></td><td bgcolor="#0B3861"></td><td bgcolor="#0B2161"></td><td bgcolor="#0B0B61"></td><td bgcolor="#210B61"></td><td bgcolor="#380B61"></td><td bgcolor="#4C0B5F"></td><td bgcolor="#610B5E"></td><td bgcolor="#610B4B"></td><td bgcolor="#610B38"></td><td bgcolor="#610B21"></td><td bgcolor="#2E2E2E"></td></tr>
	<tr><td bgcolor="#3B0B0B"></td><td bgcolor="#3B170B"></td><td bgcolor="#3B240B"></td><td bgcolor="#3A2F0B"></td><td bgcolor="#393B0B"></td><td bgcolor="#2E3B0B"></td><td bgcolor="#243B0B"></td><td bgcolor="#173B0B"></td><td bgcolor="#0B3B0B"></td><td bgcolor="#0B3B17"></td><td bgcolor="#0B3B24"></td><td bgcolor="#0B3B2E"></td><td bgcolor="#0B3B39"></td><td bgcolor="#0B2F3A"></td><td bgcolor="#0B243B"></td><td bgcolor="#0B173B"></td><td bgcolor="#0B0B3B"></td><td bgcolor="#170B3B"></td><td bgcolor="#240B3B"></td><td bgcolor="#2F0B3A"></td><td bgcolor="#3B0B39"></td><td bgcolor="#3B0B2E"></td><td bgcolor="#3B0B24"></td><td bgcolor="#3B0B17"></td><td bgcolor="#1C1C1C"></td></tr>
	<tr><td bgcolor="#2A0A0A"></td><td bgcolor="#2A120A"></td><td bgcolor="#2A1B0A"></td><td bgcolor="#29220A"></td><td bgcolor="#292A0A"></td><td bgcolor="#222A0A"></td><td bgcolor="#1B2A0A"></td><td bgcolor="#122A0A"></td><td bgcolor="#0A2A0A"></td><td bgcolor="#0A2A12"></td><td bgcolor="#0A2A1B"></td><td bgcolor="#0A2A22"></td><td bgcolor="#0A2A29"></td><td bgcolor="#0A2229"></td><td bgcolor="#0A1B2A"></td><td bgcolor="#0A122A"></td><td bgcolor="#0A0A2A"></td><td bgcolor="#120A2A"></td><td bgcolor="#1B0A2A"></td><td bgcolor="#220A29"></td><td bgcolor="#2A0A29"></td><td bgcolor="#2A0A22"></td><td bgcolor="#2A0A1B"></td><td bgcolor="#2A0A12"></td><td bgcolor="#151515"></td></tr>
	<tr><td bgcolor="#190707"></td><td bgcolor="#190B07"></td><td bgcolor="#191007"></td><td bgcolor="#181407"></td><td bgcolor="#181907"></td><td bgcolor="#141907"></td><td bgcolor="#101907"></td><td bgcolor="#0B1907"></td><td bgcolor="#071907"></td><td bgcolor="#07190B"></td><td bgcolor="#071910"></td><td bgcolor="#071914"></td><td bgcolor="#071918"></td><td bgcolor="#071418"></td><td bgcolor="#071019"></td><td bgcolor="#070B19"></td><td bgcolor="#070719"></td><td bgcolor="#0B0719"></td><td bgcolor="#100719"></td><td bgcolor="#140718"></td><td bgcolor="#190718"></td><td bgcolor="#190714"></td><td bgcolor="#190710"></td><td bgcolor="#19070B"></td><td bgcolor="#000000"></td></tr>
	</tbody></table></div>
			
		</div>
    </body>
</html>
