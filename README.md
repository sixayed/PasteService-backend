REST API позволяющее загружать куски текста и получать на них ссылку, которую можно переслать. 

При загрузке пользователь указывает: текст, время(в секундах), в течение которого он доступен, и модификатор доступа. По истечению времени текст становится недоступен. 

Модификаторы доступа: public(доступен всем) и private(доступен только по ссылке). 

Для загруженного текста выдается ссылка с уникальным номером. 

Пользователь может получить информацию о последних 10 "живых" и публичных записях. 