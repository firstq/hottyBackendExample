'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
    'ngRoute',
    'ngAnimate',
    'ngMaterial',
    'ngCookies',
    'ngScrollbar',
    'myApp.helloPage',
    'myApp.queryList',
    'myApp.queryDetails',
    'myApp.userOptions',
    'myApp.newProject',
    'myApp.mainEditor',
    'myApp.toast',
    'myApp.version'
]).config(['$locationProvider', '$routeProvider', '$mdIconProvider', '$mdThemingProvider', '$mdDateLocaleProvider', function ($locationProvider,$routeProvider,$mdIconProvider,$mdThemingProvider,$mdDateLocaleProvider) {
    $locationProvider.hashPrefix('!');

    //$routeProvider.otherwise({redirectTo: '/hello'});
    $routeProvider.otherwise({redirectTo: '/newProject/new'});
    $mdIconProvider
        .defaultIconSet("./svg/avatars.svg", 128)
        .icon("menu", "./svg/menu.svg", 24)
        .icon("share", "./svg/share.svg", 24)
        .icon("google_plus", "./svg/google_plus.svg", 24)
        .icon("hangouts", "./svg/hangouts.svg", 24)
        .icon("twitter", "./svg/twitter.svg", 24)
        .icon("phone", "./svg/phone.svg", 24)
        .icon("settings","./svg/ic_settings_black_36px.svg")
        .icon("newpage","./svg/ic_note_add_black_36px.svg");

    $mdThemingProvider.theme('default')
        .primaryPalette('deep-purple')
        .accentPalette('red');

    $mdDateLocaleProvider.formatDate = function(date) {

        var day = date.getDate()<10 ? "0"+date.getDate() : date.getDate();
        var month = (date.getMonth() + 1);
        var monthIndex = month<10 ? "0"+month : month;
        var year = date.getFullYear();

        return day + '.' + monthIndex + '.' + year;
    }
}]).service('hottyService', function ($http, $mdPanel, $mdToast, $location) {

    var _relocationToConstructor = function (uuid) {
        //$location.path('/mainEditor/'+uuid);
        window.location.href = window.location.origin + "/hotty/app/index.html#!/mainEditor/"+uuid;
    };
    this.relocationToConstructor = _relocationToConstructor;

    var _getPageById = function (pageId) {
        for (var i=0;i<this.currentSiteJson.pages.length;i++){
            if (this.currentSiteJson.pages[i].id == pageId) return this.currentSiteJson.pages[i];
        }
        return null;
    };
    this.getPageById = _getPageById;

    var _queryType = "";
    this.queryType = _queryType;

    var _openPopup = function (elemId) {
        $('#'+elemId).get(0).options.items[0].data.click();
    }
    this.openPopup = _openPopup;

    var _createDate = function (dateVal,isShowTime) {
        if (dateVal == null) return "Не запланирован";
        var date = new Date(dateVal);
        var result = (date.getDate().toString().length == 1 ? '0' + date.getDate() : date.getDate()) + "."
            + ((date.getMonth() + 1).toString().length == 1 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + "."
            + date.getFullYear();

        if(isShowTime) result += " " + date.getHours() + ":" + (date.getMinutes()>9 ? date.getMinutes() : "0"+date.getMinutes());
        return result;
    };
    this.createDate = _createDate;

    var _currentModel = {};
    this.currentModel = _currentModel;

    var _languages = [];
    this.languages = _languages;

    var _getLangPosition = function(languageId){
        var langArray;
        switch (this.currentModel.modelType){
            case "news": langArray = this.currentModel.newsLanguages; break;
            case "solution": langArray = this.currentModel.solutionLanguages; break;
            case "service": langArray = this.currentModel.serviceLanguages; break;
        }


        for (var i = 0, len = langArray.length; i < len; i++)
            if (langArray[i].languageId == languageId) return i;
    };
    this.getLangPosition = _getLangPosition;


    var _currentMenu = "";
    this.currentMenu = _currentMenu;

    var _appCtrlScope = {};
    this.appCtrlScope = _appCtrlScope;

    var _curMessage = "";
    this.curMessage = _curMessage;
    var _showMessage = function (str) {
        _curMessage = str;
        this.curMessage = _curMessage;
        $mdToast.show({
            hideDelay   : 5000,
            position    : 'top right',
            controller  : 'ToastCtrl',
            templateUrl : 'toast/ToastTemplate.html'
        });
    };
    this.showMessage = _showMessage;

    var _urlRusLat = function(str) {
        str = str.toLowerCase(); // все в нижний регистр
        var cyr2latChars = new Array(
            ['а', 'a'], ['б', 'b'], ['в', 'v'], ['г', 'g'],
            ['д', 'd'],  ['е', 'e'], ['ё', 'yo'], ['ж', 'zh'], ['з', 'z'],
            ['и', 'i'], ['й', 'y'], ['к', 'k'], ['л', 'l'],
            ['м', 'm'],  ['н', 'n'], ['о', 'o'], ['п', 'p'],  ['р', 'r'],
            ['с', 's'], ['т', 't'], ['у', 'u'], ['ф', 'f'],
            ['х', 'h'],  ['ц', 'c'], ['ч', 'ch'],['ш', 'sh'], ['щ', 'shch'],
            ['ъ', ''],  ['ы', 'y'], ['ь', ''],  ['э', 'e'], ['ю', 'yu'], ['я', 'ya'],

            ['А', 'A'], ['Б', 'B'],  ['В', 'V'], ['Г', 'G'],
            ['Д', 'D'], ['Е', 'E'], ['Ё', 'YO'],  ['Ж', 'ZH'], ['З', 'Z'],
            ['И', 'I'], ['Й', 'Y'],  ['К', 'K'], ['Л', 'L'],
            ['М', 'M'], ['Н', 'N'], ['О', 'O'],  ['П', 'P'],  ['Р', 'R'],
            ['С', 'S'], ['Т', 'T'],  ['У', 'U'], ['Ф', 'F'],
            ['Х', 'H'], ['Ц', 'C'], ['Ч', 'CH'], ['Ш', 'SH'], ['Щ', 'SHCH'],
            ['Ъ', ''],  ['Ы', 'Y'],
            ['Ь', ''],
            ['Э', 'E'],
            ['Ю', 'YU'],
            ['Я', 'YA'],

            ['a', 'a'], ['b', 'b'], ['c', 'c'], ['d', 'd'], ['e', 'e'],
            ['f', 'f'], ['g', 'g'], ['h', 'h'], ['i', 'i'], ['j', 'j'],
            ['k', 'k'], ['l', 'l'], ['m', 'm'], ['n', 'n'], ['o', 'o'],
            ['p', 'p'], ['q', 'q'], ['r', 'r'], ['s', 's'], ['t', 't'],
            ['u', 'u'], ['v', 'v'], ['w', 'w'], ['x', 'x'], ['y', 'y'],
            ['z', 'z'],

            ['A', 'A'], ['B', 'B'], ['C', 'C'], ['D', 'D'],['E', 'E'],
            ['F', 'F'],['G', 'G'],['H', 'H'],['I', 'I'],['J', 'J'],['K', 'K'],
            ['L', 'L'], ['M', 'M'], ['N', 'N'], ['O', 'O'],['P', 'P'],
            ['Q', 'Q'],['R', 'R'],['S', 'S'],['T', 'T'],['U', 'U'],['V', 'V'],
            ['W', 'W'], ['X', 'X'], ['Y', 'Y'], ['Z', 'Z'],

            [' ', '-'],['0', '0'],['1', '1'],['2', '2'],['3', '3'],
            ['4', '4'],['5', '5'],['6', '6'],['7', '7'],['8', '8'],['9', '9'],
            ['-', '-']

        );

        var newStr = new String();

        for (var i = 0; i < str.length; i++) {

            var ch = str.charAt(i);
            var newCh = '';

            for (var j = 0; j < cyr2latChars.length; j++) {
                if (ch == cyr2latChars[j][0]) {
                    newCh = cyr2latChars[j][1];

                }
            }
            // Если найдено совпадение, то добавляется соответствие, если нет - пустая строка
            newStr += newCh;

        }
        // Удаляем повторяющие знаки - Именно на них заменяются пробелы.
        // Так же удаляем символы перевода строки, но это наверное уже лишнее
        return newStr.replace(/[_]{2,}/gim, '-').replace(/\n/gim, '');
    };
    this.urlRusLat = _urlRusLat;

}).controller('AppController', ['$scope','$http','$mdSidenav', 'hottyService', '$location', function ($scope,$http,$mdSidenav,hottyService,$location) {
    var self = this, rebuildScroll = function(){
        $scope.$broadcast('rebuild:me')
        setTimeout(rebuildScroll, 1500);
    };
    setTimeout(rebuildScroll, 1500);

    self.toggleList = function() {
        $mdSidenav('left').toggle();
    };

    $scope.logout = function () {
        window.location.href = window.location.origin + "/hotty/app/login";
    }

    $scope.fullYear = new Date().getFullYear();
    $scope.currentModelId=0;
    $scope.sidenavName = '';
    $scope.sidenavList = [];
    $scope.mainForSidenavList = [
        {
            thumbnail: "/hotty/app/png/ic_plus_one.png",
            header: "Новый проект",
            mediumText: "",
            smallText: "",
            clickFunction: function () {
                hottyService.currentMenu = "news";
                //$scope.sidenavList = [];
                $mdSidenav('left').close();
                $location.path('/newProject/new');
                $scope.$broadcast('rebuild:me');
            }
        },
        {
            thumbnail: "/hotty/app/png/ic_apps.png",
            header: "Мои проекты",
            mediumText: "",
            smallText: "",
            clickFunction: function () {
                hottyService.currentMenu = "projects";
                $scope.sidenavList = [];
                $http.get(CONTEXT_PATH+"/api/v1/sites").then(function (response) {
                    for (var i = 0, len = response.data.length; i < len; i++) {
                        var value = response.data[i];
                        // TODO: Добавить логотип
                        value.thumbnail = value.thumbnail == undefined ? "/hotty/app/png/hotty_logo.png" : value.thumbnail;
                        //value.header = hottyService.createDate(value.date);
                        value.mediumText = value.name;
                        value.smallText = value.keydomain;
                        value.clickFunction = function () {
                            //TODO: по клику на сайт кажем меню проекта: Добавить станицу, Сохранить изменения, Удалить проект
                            $scope.sidenavList = [];
                            $scope.sidenavList.push({
                                thumbnail: "/hotty/app/svg/ic_note_add_black_36px.svg",
                                header: "Добавить станицу",
                                clickFunction: function () {
                                    //Добавление новой странички
                                    $("#newPageForm").clone().removeClass("hidden").dialog({
                                        autoOpen: true,
                                        width: "450px",
                                        buttons: [
                                            {
                                                text: "Ok",
                                                click: function() {
                                                    var pageName = $( this ).find('input[name="nameHtml"]').val(),
                                                        titleName=$( this ).find('input[name="title"]').val(),
                                                        pageDescription = $( this ).find('input[name="description"]').val();
                                                    hottyService.currentSiteJson.pages.push({
                                                        id: Math.uuidFast(),
                                                        pagePath: pageName,
                                                        pageTitle: titleName,
                                                        pageDescription: pageDescription, //TODO: добавить textarea description
                                                        styles: "",
                                                        frontClasses: "",
                                                        siteId: hottyService.currentSiteJson.id,
                                                        blocks:[]
                                                    });
                                                    $(this).remove();
                                                }
                                            },
                                            {
                                                text: "Отмена",
                                                click: function() {
                                                    $(this).remove();
                                                }
                                            }
                                        ]});
                                }
                            });
                            $scope.sidenavList.push({
                                thumbnail: "/hotty/app/svg/ic_save_black_48px.svg",
                                header: "Сохранить изменения",
                                clickFunction: function () {
                                    //Save
                                    for (var i = 0; i<hottyService.currentSiteJson.pages.length; i++){
                                        var pageJsonForSave = savePage($('#'+hottyService.currentSiteJson.pages[i].id));
                                        $.ajax({
                                            url: '/hotty/api/v1/pages',
                                            type: 'POST',
                                            contentType: 'application/json;charset=utf-8',
                                            data: JSON.stringify(pageJsonForSave) ,
                                            beforeSend: function(){

                                            },
                                            success: function( resp ){
                                                $.each(pageJsonForSave.delBlocks, function(k,v){
                                                    //$.ajax({url: v.restUrl+v.id, type: 'DELETE', success: function(){ $("#"+v.id).remove() }});
                                                    $("#"+v.id).remove();
                                                });
                                            },
                                            error: function( resp ){
                                            }
                                        });
                                    }
                                }
                            });
                            $scope.sidenavList.push({
                                thumbnail: "/hotty/app/svg/ic_delete_forever_black_48px.svg",
                                header: "Удалить проект",
                                clickFunction: function () {
                                    //Жадное удаление
                                }
                            });
                            // и жадно загружаем проект
                            $http.get(CONTEXT_PATH+"/api/v1/sites/eager/"+this.id).then(function (response) {
                                hottyService.currentSiteJson = response.data;
                                $mdSidenav('left').close();
                                $location.path('/mainEditor/'+hottyService.currentSiteJson.id);
                            });
                        };
                        $scope.sidenavList.push(value);
                    }
                    $scope.$broadcast('rebuild:me');
                });
            }
        }
    ];
    $scope.sidenavList = $scope.mainForSidenavList;
    $scope.getMainList = function () {
        hottyService.currentMenu = "";
        $scope.sidenavList = $scope.mainForSidenavList;
        //$location.path('/hello');
        $location.path('/newProject/new');
        $mdSidenav('left').close();
        $scope.$broadcast('rebuild:me');
    };

    $scope.isCreateView = function () {
        if($mdSidenav('left').isLockedOpen()){
            return hottyService.currentMenu != "";
        }
        return hottyService.currentMenu != "" && $mdSidenav('left').isOpen();
    };

    hottyService.appCtrlScope = $scope;
}]).directive('simpleBlock', function(hottyService) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            if(typeof($(element).parent().attr("child_level")) != "undefined"){
                var cl = parseInt($(element).parent().attr('child_level'))+1;
                $(element).attr("child_level", cl);
            }
            switch (attrs.type) {
                case "ImageBlock":
                    $(element).imagePlugin({restUrl: "/hotty/imageblocks/",borderSize: 1,resizable:true,thisBlock:$(element),popup: $("<div id='imageBlock"+attrs.id+"Popup' title='Параметры'></div>").append($('#imageForm').clone().removeClass('hidden'))});
                    break;
                case "VideoBlock":
                    $(element).videoPlugin({restUrl: "/hotty/videoblocks/", videoUrl: JSON.parse(attrs.specialData).videoUrl, borderSize: 1,resizable:true,thisBlock:$(element),popup: $("<div id='videoBlock"+attrs.id+"Popup' title='Параметры'></div>").append($('#videoForm').clone().removeClass('hidden'))});
                    break;
                case "MenuBlock":
                    var specialDataParsed = JSON.parse(attrs.specialData);
                    var eagerItems = specialDataParsed.items;
                    var itemStyles = JSON.parse(specialDataParsed.itemStyles); //get string
                    $(element).menuPlugin({
                        restUrl: "/hotty/menublocks/",
                        logo: specialDataParsed.logo,
                        nav: {
                            //'width' : '50%',
                            'font-weight' : 'normal',
                        },
                        isVertical: specialDataParsed.isVertical,
                        menuHelperLogo: $('<a class="navbar-brand menu-helper-logo" href="/" id="logotype"></a>'),
                        menuItemsContainer: $('<ul class="align_center nav navbar-nav navbar-collapse collapse out"> </ul>'),
                        borderSize: 1,
                        resizable:true,
                        thisBlock:$(element).attr("id",attrs.id),
                        navli: itemStyles.navli,
                        navlia: itemStyles.navlia,
                        navlia_hover: itemStyles.navlia_hover,
                        navli_hover : itemStyles.navli_hover,
                        eagerItems: eagerItems
                    });
                    break;
                case "TextBlock":
                    $(element).textPlugin({restUrl: "/hotty/textblocks/", hidePanel: true,borderSize: 1,resizable:true,thisBlock:$(element),savedText:JSON.parse(attrs.specialData).text,popup: $("<div id='"+attrs.id+"Popup' title='Параметры'></div>").append($('#textForm').clone().removeClass('hidden'))});
                    break;
                case "SliderBlock":
                    //откладываем навес до завершения
                    console.log("SliderBlock");
                    sliders.push(element);
                    break;
                case "SendmailBlock":
                    $(element).sendmailPlugin({
                        restUrl: "/hotty/api/v1/sendmailblocks/",
                        capchaEnable: true,
                        borderSize: 1,
                        resizable:true,
                        thisBlock:$(element),
                        popup: $("<div id='sendmailBlock"+attrs.id+"Popup' title='Параметры'></div>").append($('#sendmailForm').clone().removeClass('hidden')),
//									defaultMailFrom: "no_reply@hotty.com",
//									defaultMailTo: "serega2rikov@me.com"
                    });
                    break;
                case "pageBody":
                    $(element).bodyPlugin({currentPage: hottyService.getPageById(attrs.id), popup: $("<div id='body"+attrs.id+"Popup' title='Параметры'></div>").append($('#pageForm').clone().removeClass('hidden'))});
                    break;
                default:

                    if(attrs.simpleBlock.indexOf("scroll-content") == -1){
                        $(element).blockPlugin({restUrl: "/hotty/blocks/", borderSize: 1/*($(element).parent().hasClass("scroll-pane") ? 0 : 1)*/,resizable:true,popup: $("<div id='"+attrs.id+"Popup' title='Параметры'></div>").append($('#simpleForm').clone().removeClass('hidden').show())});
                    }
            }

        }
    };
}).directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            console.log("sliders.length="+sliders.length);
            if (scope.$last === true) {
                $timeout(function () {
                    //Сначала body !!! body занимает не всю площадь страницы
                    //$(document.body).blockPlugin({restUrl: "/hotty/pages/", borderSize: 0,resizable:false,popup: $("<div id='"+attrs.id+"Popup' title='Параметры'></div>").append($('#simpleBodyForm').clone().removeClass('hidden').show())});
                    //Слайдеры
                    if(sliders.length>0){
                        setTimeout(function () {
                            $.each(sliders, function(){
                                var attrOptions = JSON.parse($(this).attr("special-data"));
                                $(this).sliderPlugin({
                                    isSpecial: attrOptions.special,
                                    restUrl: "/hotty/sliderblocks/",
                                    isSliderFade: attrOptions.isSliderShow,
                                    isArrowsShow: attrOptions.isArrowsShow,
                                    isVertical: attrOptions.isVertical,
                                    defaultItemWrapper: $(this).parent().get(0) == $("body").get(0) ? $('<div data-block="Block"></div>') : "",
                                    borderSize: 2,
                                    resizable:true,
                                    thisBlock:$(this),
                                    scrollContentStyle: typeof($(this).attr("scroll-content-style")) == "undefined" ? "{}" : JSON.parse($(this).attr("scroll-content-style")), //Берется последний
                                    scrollContentItemStyle: typeof($(this).attr("scroll-content-item-style")) == "undefined" ? "{}" : JSON.parse($(this).attr("scroll-content-item-style")), //Берется последний
                                    popup: $("<div id='sliderBlock"+this.attr("id")+"Popup' title='Параметры'></div>").append($('#sliderForm').clone().removeClass('hidden'))});
                                });
                        },2000);
                    }
                });
            }
        }
    }
});
