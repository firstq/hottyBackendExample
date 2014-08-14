INSERT INTO menuitem (id, link, name, priority, toplevel, version, parent) VALUES
(1, '', 'head', 0, 0, 0, NULL),
(2, '/news/ru', 'RuNews', 0, 1, 0, 1),
(3, '/news/en', 'EnNews', 0, 1, 0, 1),
(4, '/pages/ru/rupage.html', 'Page', 0, 1, 0, 1),
(5, '/pages/ru/rupage.html', 'Subpage', 0, 0, 0, 4),
(6, '/pages/ru/rupage.html', '2Subpage', 0, 0, 0, 5),
(7, '/pages/en/page1.html', 'Page2', 1, 1, 1, 1),
(8, '/pages/ru/rupage.html', 'Subpage2', 0, 0, 0, 7);
