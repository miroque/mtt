# Мульти поточный сервер

## Журнал

### 12.05.2017 (21:42 - 22:26)

Начинаю исследование по многопоточности.

Первое это идет запрос в yandex.ru [java multithread socket server]

Первая ссылка http://www.mysamplecode.com/2011/12/java-multithreaded-socket-server.html

C нее и начинаю копировать и исследовать.

Чтож, хорошие реализации сервера и клиента.
Время ушло, на создание двух мавен проектов в Еклипсе.
Настройка упаковщика, что он правильно главные классы указал, и сам зпуск сервера и клиентов.

Следующий, шаг, это реализация наших требований, в рамках этих алгоритмов..

### 13.05.2017 (21:02 - 21:28 … 21:45 - 22:30)

Начинаем сегодня с идеи словаря…

Так, освежаем как работать из консоли, читать ввод пользователя.
Да, и сделать чтоб клиент долго работал.
До тех пор пока спец. слово не напишем.
Ага наш друг мистер mkyong как всегда помогает своими записками. И мы можем читать с консоли удобно.

Ага поигрался с Мапой которая словарь, волатайл. Для всех потоков это одинакова переменная.
В целом, руками пощупать различие волатайл не получилось, потому-что потоки я руками "шатал".
А это не дает почти одновременных обращений, ну и все те случаи про которые пишут в интернетах.
То есть, это надо автотестом гнать и на многих потоках чтоб были случаи "коллизий", тогда это и проявиться скорее всего.
А на сейчас, вокабуляр, вроде корректно сделанный, но нет механизма по работе с ним, а именно
удалить изменить добавить.
Вроде немножко, но конечно это надо еще тестами обложить.

### 16.05.2017 (22:30 - 23:00)

попробовал выделить в синхронный метод, обрабтку добавления определения слова в словарь.
Руками тестировать бесполезно, необходимы тесты, которые бы проверили на клэш ресурсов, и побольше клиентов бы.

### 22.05.2017 (00:30 - 00:50)

реализация методов получения значения слова и удаления из словаря значения или слова..
и тестирование.. как выяснилось, серверы я не умею писать. приходиться копаться в базовых моментах..

### 08.06.2017 (21:30 - 21:55, 22:55-23:03)
После прочтения в интернете, о тестировании клиента и сервера как junit, короче, нет тестов которые к junit  бы относились. 
И это или интеграционное тестирование, или тестирование ручаками. В целом, интернет меня не утешил. или же я как-то не так его спросил.
Пока другого варианта нет. продолжу с написания оставшегося функционала.

Выяснилось, что сервер в принципе работает корретно, некоретно отображает клиент.
Да, на стороне клиента, что-то неправильное, ответы от сервера, не правильно обрабатываются.
Словно кеш, где то сидит, и не сбрасывается.

К сожалению голова отказывается больше рабоать.. :(

### 11.06.2017 (10:00 - 10:30, 10:30 - 11:08)

Нашел наконец, что же мне мешало правильно передавать ответы от сервера на клиент. Теперь взаимодействите между клиентом и сервером, хоть нормально читаются.

вторым этапом, сделал удаление определения слова.
И нашел ошибку, что если клиент пошлет на сервер ни чего, то.. Взаимодействие с сервером, прекращается, ни чего не происходит. следовательно, ошибка, яркая такая, заметная.

Да, починилось поведение, добавлением выбора по умолчанию, т.е. если что-то не понятное пришло, мы клиенту отправляем, извини, команды нет