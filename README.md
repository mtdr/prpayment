# Payments sharder
It's an education project oriented on sharding technologies based on Spring Framework

Сервис обработки платежей для демонстрации механизма ручного шардирования. 

Предметная основа - платежи и их участники.
Участники хранятся в едином состоянии на каждом шарде для целостности данных. 
Платежи распределяются по шардам алгоритмом, в основе которого определение остатка от деления значения хэш-функции на количество шардов (сейчас - 3..

Установка:
1. Создать 3 базы данных в postgres - prpayment1, prpayment2, prpayment3 из приложенного дампа `prpayments-dump.sql`, или 
    - запустить проект со значением параметра spring.jpa.hibernate.ddl-auto=update (или create);
    - остановить проект, снять дамп с prpayment1, восстановить prpayment2 и prpayment3 из него;
4. Проект готов к работе, запустить

Использование:
1. Доступные REST API описаны для использования со Swagger UI на http://localhost:8080/swagger-ui.html
2. Для быстрого ознакомления рекомендуется следующий сценарий:
    - генерация двух участников ("a", "b") и платежей между ними (N = `application.properties:generate.payments.count`) - пустой POST запрос на (/api/payments/generate/list/save). В ответном сообщении будет список сохраненных платежей.
    - вызов контроллера для подсчета суммы платежей по участнику (/api/payments/sum) с телом вида `{"data": "a"}`, где "a" - `ParticipantEntity#name`. В ответном сообщении будет указана сумма платежей, отправленных участником во всей системе.
    - получение суммы платежей участника, хранимых на конкретном шарде. Получение суммы доступно по POST-запросу на (/api/payments/sum/shard) с телом вида `{"data": ["a", 2]}`, где "a" - `ParticipantEntity#name`, 2 - номер шарда (от 1 до 3)
    
В корне проекта конфиг для postman `PrPayments.postman_collection.json` и эталонный дамп `prpayments-dump.sql`