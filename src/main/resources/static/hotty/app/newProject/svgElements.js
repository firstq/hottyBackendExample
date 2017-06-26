var generateTopMenu = function (elementQuery) {
    var topMenu = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);
    //ширина

    topMenu //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    topMenu //высота
        .append('rect')
        .attr("x", 45) //Начало по х
        .attr("y", 40) //Начало по у
        .attr("width", 200) //ширина
        .attr("height", 30) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "1");//ширина обводки
    topMenu
        .append('text')
        .text('МЕНЮ')
        .attr('x', 100)
        .attr('y', 65)
        .style("fill","firebrick")
        .style("font-size","25px")
        .style("font-weight","bold")
        .style("stroke","black");

    topMenu
        .append("line")
        .attr("x1", 50)
        .attr("y1", 80)
        .attr("x2", 240)
        .attr("y2", 80)
        .style("stroke", "gray") //обводка
        .style("stroke-width", "5");//ширина обводки
    topMenu
        .append("line")
        .attr("x1", 50)
        .attr("y1", 90)
        .attr("x2", 240)
        .attr("y2", 90)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 50)
        .attr("y1", 100)
        .attr("x2", 240)
        .attr("y2", 100)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu //высота
        .append('rect')
        .attr("x", 50) //Начало по х
        .attr("y", 110) //Начало по у
        .attr("width", 50) //ширина
        .attr("height", 50) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "azure") //Заливка
        .style("stroke", "blue") //обводка
        .style("stroke-width", "1");//ширина обводки
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 110)
        .attr("x2", 240)
        .attr("y2", 110)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 120)
        .attr("x2", 240)
        .attr("y2", 120)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 130)
        .attr("x2", 240)
        .attr("y2", 130)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 140)
        .attr("x2", 240)
        .attr("y2", 140)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 150)
        .attr("x2", 240)
        .attr("y2", 150)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 105)
        .attr("y1", 160)
        .attr("x2", 240)
        .attr("y2", 160)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    topMenu
        .append("line")
        .attr("x1", 50)
        .attr("y1", 170)
        .attr("x2", 240)
        .attr("y2", 170)
        .style("stroke", "gray")
        .style("stroke-width", "5");
}

var generateLeftMenu = function (elementQuery) {
    var leftMenu = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);
    //ширина

    leftMenu //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    leftMenu //высота
        .append('rect')
        .attr("x", 35) //Начало по х
        .attr("y", 30) //Начало по у
        .attr("width", 30) //ширина
        .attr("height", 150) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "1");//ширина обводки
    leftMenu
        .append('text')
        .text('МЕНЮ')
        .attr('x', 65)
        .attr('y', -40)
        .attr("transform", "rotate(90)")
        .style("fill","firebrick")
        .style("font-size","25px")
        .style("font-weight","bold")
        .style("stroke","black");

    leftMenu
        .append("line")
        .attr("x1", 75)
        .attr("y1", 60)
        .attr("x2", 265)
        .attr("y2", 60)
        .style("stroke", "gray") //обводка
        .style("stroke-width", "5");//ширина обводки
    leftMenu
        .append("line")
        .attr("x1", 75)
        .attr("y1", 70)
        .attr("x2", 265)
        .attr("y2", 70)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 75)
        .attr("y1", 80)
        .attr("x2", 265)
        .attr("y2", 80)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu //высота
        .append('rect')
        .attr("x", 75) //Начало по х
        .attr("y", 90) //Начало по у
        .attr("width", 50) //ширина
        .attr("height", 50) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "azure") //Заливка
        .style("stroke", "blue") //обводка
        .style("stroke-width", "1");//ширина обводки
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 90)
        .attr("x2", 265)
        .attr("y2", 90)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 100)
        .attr("x2", 265)
        .attr("y2", 100)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 110)
        .attr("x2", 265)
        .attr("y2", 110)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 120)
        .attr("x2", 265)
        .attr("y2", 120)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 130)
        .attr("x2", 265)
        .attr("y2", 130)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 130)
        .attr("y1", 140)
        .attr("x2", 265)
        .attr("y2", 140)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    leftMenu
        .append("line")
        .attr("x1", 75)
        .attr("y1", 150)
        .attr("x2", 265)
        .attr("y2", 150)
        .style("stroke", "gray")
        .style("stroke-width", "5");
}


var generateRightMenu = function (elementQuery) {
    var rightMenu = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);
    //ширина

    rightMenu //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

//    rightMenu
//        .append('text')
//        .text('ШАПКА') // изменение текста в элементе text
//        .attr('x', 95) // задание координат элемента text
//        .attr('y', 25)
//        .style("fill","firebrick")
//        .style("font-size","25px") // заливка текста цветом
//        .style("font-weight","bold")
//        .style("stroke","black");

    rightMenu //высота
        .append('rect')
        .attr("x", 235) //Начало по х
        .attr("y", 30) //Начало по у
        .attr("width", 30) //ширина
        .attr("height", 150) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "1");//ширина обводки
    rightMenu
        .append('text')
        .text('МЕНЮ')
        .attr('x', 65)
        .attr('y', -240)
        .attr("transform", "rotate(90)")
        .style("fill","firebrick")
        .style("font-size","25px")
        .style("font-weight","bold")
        .style("stroke","black");

    rightMenu
        .append("line")
        .attr("x1", 35)
        .attr("y1", 60)
        .attr("x2", 225)
        .attr("y2", 60)
        .style("stroke", "gray") //обводка
        .style("stroke-width", "5");//ширина обводки
    rightMenu
        .append("line")
        .attr("x1", 35)
        .attr("y1", 70)
        .attr("x2", 225)
        .attr("y2", 70)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 35)
        .attr("y1", 80)
        .attr("x2", 225)
        .attr("y2", 80)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu //высота
        .append('rect')
        .attr("x", 35) //Начало по х
        .attr("y", 90) //Начало по у
        .attr("width", 50) //ширина
        .attr("height", 50) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "azure") //Заливка
        .style("stroke", "blue") //обводка
        .style("stroke-width", "1");//ширина обводки
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 90)
        .attr("x2", 225)
        .attr("y2", 90)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 100)
        .attr("x2", 225)
        .attr("y2", 100)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 110)
        .attr("x2", 225)
        .attr("y2", 110)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 120)
        .attr("x2", 225)
        .attr("y2", 120)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 130)
        .attr("x2", 225)
        .attr("y2", 130)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 90)
        .attr("y1", 140)
        .attr("x2", 225)
        .attr("y2", 140)
        .style("stroke", "gray")
        .style("stroke-width", "5");
    rightMenu
        .append("line")
        .attr("x1", 35)
        .attr("y1", 150)
        .attr("x2", 225)
        .attr("y2", 150)
        .style("stroke", "gray")
        .style("stroke-width", "5");
}

var generateHorizontalSlider = function (elementQuery) {
    var horizontalSlider = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);

    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider.append("marker")
        .attr("id","arrow")
            .attr("viewBox","0 -5 10 10")
            .attr("refX",5)
            .attr("refY",0)
            .attr("markerWidth",20)
            .attr("markerHeight",20)
            .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append("marker")
        .attr("id","barrow")
        .attr("viewBox","0 -5 10 10")
        .attr("refX",5)
        .attr("refY",0)
        .attr("markerWidth",20)
        .attr("markerHeight",20)
        .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append('line')
        .attr("x1", 15)
        .attr("y1", 90)
        .attr("x2", 285)
        .attr("y2", 90)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");
    horizontalSlider.append('line')
        .attr("x1", 285)
        .attr("y1", 90)
        .attr("x2", 15)
        .attr("y2", 90)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");


    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 40) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider
        .append('text')
        .text('МЕНЮ') // изменение текста в элементе text
        .attr('x', 95) // задание координат элемента text
        .attr('y', 25)
        .style("fill","firebrick")
        .style("font-size","25px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

    horizontalSlider
        .append('text')
        .text('горизонтальный слайдер') // изменение текста в элементе text
        .attr('x', 35) // задание координат элемента text
        .attr('y', 110)
        .style("fill","firebrick")
        .style("font-size","18px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

}


var generateHorizontalSlider = function (elementQuery) {
    var horizontalSlider = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);

    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider.append("marker")
        .attr("id","arrow")
        .attr("viewBox","0 -5 10 10")
        .attr("refX",5)
        .attr("refY",0)
        .attr("markerWidth",20)
        .attr("markerHeight",20)
        .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append("marker")
        .attr("id","barrow")
        .attr("viewBox","0 -5 10 10")
        .attr("refX",5)
        .attr("refY",0)
        .attr("markerWidth",20)
        .attr("markerHeight",20)
        .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append('line')
        .attr("x1", 15)
        .attr("y1", 60)
        .attr("x2", 285)
        .attr("y2", 60)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");
    horizontalSlider.append('line')
        .attr("x1", 285)
        .attr("y1", 180)
        .attr("x2", 15)
        .attr("y2", 180)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");


    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 40) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider
        .append('text')
        .text('МЕНЮ') // изменение текста в элементе text
        .attr('x', 95) // задание координат элемента text
        .attr('y', 25)
        .style("fill","firebrick")
        .style("font-size","25px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

    horizontalSlider
        .append('text')
        .text('горизонтальный слайдер') // изменение текста в элементе text
        .attr('x', 35) // задание координат элемента text
        .attr('y', 110)
        .style("fill","firebrick")
        .style("font-size","18px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

}

var generateVerticalSlider = function (elementQuery) {
    var horizontalSlider = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);

    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider.append("marker")
        .attr("id","arrow")
        .attr("viewBox","0 -5 10 10")
        .attr("refX",5)
        .attr("refY",0)
        .attr("markerWidth",20)
        .attr("markerHeight",20)
        .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append("marker")
        .attr("id","barrow")
        .attr("viewBox","0 -5 10 10")
        .attr("refX",5)
        .attr("refY",0)
        .attr("markerWidth",20)
        .attr("markerHeight",20)
        .attr("orient","auto")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")
        .attr("class","arrowHead");

    horizontalSlider.append('line')
        .attr("x1", 20)
        .attr("y1", 60)
        .attr("x2", 20)
        .attr("y2", 180)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");
    horizontalSlider.append('line')
        .attr("x1", 280)
        .attr("y1", 180)
        .attr("x2", 280)
        .attr("y2", 60)
        .attr("class","slider")
        .style("stroke", "black")
        .style("stroke-width", "1")
        .attr("marker-end","url(#arrow)");


    horizontalSlider //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 40) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки

    horizontalSlider
        .append('text')
        .text('МЕНЮ') // изменение текста в элементе text
        .attr('x', 95) // задание координат элемента text
        .attr('y', 25)
        .style("fill","firebrick")
        .style("font-size","25px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

    horizontalSlider
        .append('text')
        .text('вертикальный слайдер') // изменение текста в элементе text
        .attr('x', 50) // задание координат элемента text
        .attr('y', 110)
        .style("fill","firebrick")
        .style("font-size","18px") // заливка текста цветом
        .style("font-weight","bold")
        .style("stroke","black");

}

var generateEmpty = function (elementQuery) {
    var emptyPage = d3.select(elementQuery)
        .append('svg')
        .attr("width", 300)
        .attr("height", 200);

    emptyPage //высота
        .append('rect')
        .attr("x", 0) //Начало по х
        .attr("y", 0) //Начало по у
        .attr("width", 300) //ширина
        .attr("height", 200) //высота
        .attr("rx", 5) //Закругление углов
        .style("fill", "none") //Заливка
        .style("stroke", "black") //обводка
        .style("stroke-width", "2");//ширина обводки
    emptyPage
        .append('text')
        .text('свободная планировка')
        .attr('x', 45)
        .attr('y', 110)
        .style("fill","firebrick")
        .style("font-size","18px")
        .style("font-weight","bold")
        .style("stroke","black");
}