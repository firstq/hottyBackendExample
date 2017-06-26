var createTopMenuExampleJson = function () {

};

var createLeftMenuExampleJson = function () {

};

var createRightMenuExampleJson = function () {

};

var createHorizontalSliderExampleJson = function () {

};

var createVerticalSliderExampleJson = function () {

};

var createEmptyExampleJson = function (newprojectPopup) {
    var siteId = Math.uuidFast();
    var siteExample = {
        id: siteId,
        name: newprojectPopup.find('form').find("input[name='name']").val(),
        keydomain: newprojectPopup.find('form').find("input[name='keydomain']").val(),
        scripts: "",
        clientId: 1,
        isExample: false,
        isPublished: false,
        pages:[]
    };
    var indexPageId = Math.uuidFast(), notFoundPageId = Math.uuidFast();

    var indexPage = {
        id: indexPageId,
        pagePath: "index.html",
        pageTitle: newprojectPopup.find('form').find("input[name='name']").val()+" Главная страница",
        pageDescription: newprojectPopup.find('form').find("input[name='name']").val(),
        styles: "",
        frontClasses: "",
        siteId: siteId,
            blocks:[
            {
                id: Math.uuidFast(),
                name: "block1",
                styles: "width: 100%;height: 50%;top 0px;",
                frontClasses: "header",
                isTop: true,
                isStatic: true,
                tagType: "div",
                blockType: "Block",
                pageId: indexPageId,
                containedPage: null,
                children:[]
            },
           {
                id: Math.uuidFast(),
                name: "block2",
                styles: "width: 100%;height: 50%;bottom 0px;background-color: blue;",
                frontClasses: "footer",
                isTop: true,
                isStatic: true,
                tagType: "div",
                blockType: "Block",
                pageId: indexPageId,
                containedPage: null,
                children:[{
                        id: Math.uuidFast(),
                        name: "block1",
                        styles: "width: 50%;height: 50%;top 0px;background-color: red;",
                        frontClasses: "header",
                        isTop: true,
                        isStatic: true,
                        tagType: "div",
                        blockType: "Block",
                        pageId: indexPageId,
                        containedPage: null,
                        children:[]
                    },
                    {
                        id: Math.uuidFast(),
                        name: "block2",
                        styles: "width: 50%;height: 50%;bottom 0px;background-color: yellow;",
                        frontClasses: "footer",
                        isTop: true,
                        isStatic: true,
                        tagType: "div",
                        blockType: "Block",
                        pageId: indexPageId,
                        containedPage: null,
                        children:[]
                    }]
            }
        ]
    },
    notFoundPage = {
        id: notFoundPageId,
        pagePath: "404.html",
        pageTitle: "404 Страница не найдена",
        pageDescription: "404 Страница не найдена",
        styles: "",
        frontClasses: "",
        siteId: siteId,
        blocks:[
            {
                id: Math.uuidFast(),
                name: "block1",
                styles: "width: 100%;height: 50%;top 0;background-color: yelow;position: absolute;",
                frontClasses: "header",
                isTop: true,
                isStatic: true,
                tagType: "div",
                blockType: "Block",
                pageId: notFoundPageId,
                containedPage: null,
                children:[]
            },
            {
                id: Math.uuidFast(),
                name: "block1",
                styles: "width: 100%;height: 50%;top 50px;position: absolute;",
                frontClasses: "content",
                isTop: true,
                isStatic: false,
                tagType: "div",
                blockType: "TextBlock",
                pageId: notFoundPageId,
                specialData:{
                    text: "<font style='color: red'>404 Страница не найдена</font>"
                },
                containedPage: null,
                children:[]
            },
            {
                id: Math.uuidFast(),
                name: "block2",
                styles: "width: 100%;bottom 0;position: absolute;",
                frontClasses: "footer",
                isTop: true,
                isStatic: true,
                tagType: "div",
                blockType: "Block",
                pageId: notFoundPageId,
                containedPage: null,
                children:[]
            }
        ]
    };
    siteExample.pages.push(indexPage);
    siteExample.pages.push(notFoundPage);
    return siteExample;
};

