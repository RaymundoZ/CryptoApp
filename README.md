# Приложение для проведения торгов криптовалютами

## Обзор

Сервис является RESTful API приложением для проведения торгов криптовалютами. Согласно ТЗ
реализованы минимальные требования и часть дополнительных заданий.

### Cервис реализует (минимальные требования):

* регистрация нового пользователя;

* просмотр баланса своего кошелька;

* пополнение кошелька;

* вывод денег с биржи;

* просмотр актуальных курсов валют;

* обмен валют по установленному курсу;

* изменить курс валют;

* посмотреть общую сумму на всех пользовательских счетах для указанной валюты;

* посмотреть количество операций, которые были проведены за указанный период;

### Cервис реализует (дополнительные требования):

* подключить базу данных PostgreSQL для хранения данных о балансе пользовательских кошельков и истории операций;

* подключить Spring Security для разграничения ролей (admin/user);

### Использованные технологии

* Java 17

* Spring Boot

* Spring Web

* Spring Security

* JWT token

* Maven

## Запуск

Склонировать репозиторий, выполнив команду: `git clone https://github.com/RaymundoZ/CryptoApp.git`

Перейдя в корневую папку проекта, прописать команду: `docker-compose build`

Затем `docker-compose up`

#### Либо

В корневой папке проекта прописать команду: `mvn spring-boot:run`

## Endpoints

### Регистрация нового пользователя

` Post /registration` на порте 5000, запрос будет выглядеть так: **localhost:5000/registration**

#### Параметры:

* `"username": "example"`,
* `"email": "example@mail.ru"`

#### Ответ:

`"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`

---

### Просмотр баланса своего кошелька

` Get /balance `

#### Параметры:

`"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`

#### Ответ:

* `"TON": "254.87"`,
* `"BTC": "3.0031589"`,
* `"RUB": "53000.0"`

---

### Пополнение кошелька

` Post /deposit `

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"RUB": "12000"`

#### Ответ:

В случае успешного запроса — в ответ придёт обновленный баланс кошелька:

`"RUB": "65000.0"`

---

### Вывод денег с биржи

` Post /withdraw `

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"currency": "RUB"`,
* `"count": "1500"`,
* `"credit_card": "1234 5678 9012 3456"`

ИЛИ

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"currency": "TON"`,
* `"count": "15"`,
* `"wallet": "AsS5A2SASd2as3q5sd2asd53a1s5"`

#### Ответ:

Выполняется проверка, что на счёте достаточно денег.

В случае успешного запроса — в ответ придёт обновленный баланс кошелька (из которого происходил вывод денег).

`"RUB": "63500.0"`

---

### Просмотр актуальных курсов валют

` Get /available_exchanges `

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"currency": "TON"`

#### Ответ:

Для валюты TON получаем в ответ,
что 1 TON можно приобрести за 180 руб. или 0.00009564 BTC

* `"BTC": "0.00009564"`,
* `"RUB": "180"`

---

### Обмен валют по установленному курсу

` Post /exchange `

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"currency_from": "RUB"`,
* `"currency_to": "TON"`,
* `"amount": "10000"`

#### Ответ:

Перед проведением обмена должна выполниться проверка,
что у пользователя достаточно денег на счету.
В случае успеха, сообщить сколько денег списано и сколько зачислено.

* `"currency_from": "RUB"`,
* `"currency_to": "TON"`,
* `"amount_from": "10000"`,
* `"amount_to": "55.55"`

---

### Изменить курс валют

` Post /change_exchange`

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"base_currency": "TON"`,
* `"BTC": "0.0021"`,
* `"RUB": "5700"`

#### Ответ:

В ответ возвращаются актуальные курс валют относительно базовой валюты.

* `"BTC": "0.0021"`,
* `"RUB": "5700"`

---

### Посмотреть общую сумму на всех пользовательских счетах для указанной валюты

` Get /currency_values`

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"currency": "RUB"`

#### Ответ:

* `"RUB": "145000.0"`

---

### Посмотреть количество операций, которые были проведены за указанный период

` Get /operations`

#### Параметры:

* `"secret_key": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODgyNzI5NH0.atKxdGlkX4uooKUDGeTbjird-6RSshag7YFNaH1WCNprta-KO9URys7QBbhzqQh4ILFfztynhtJoO35NRs0eew"`,
* `"date_from": "28.02.2023"`,
* `"date_to": "01.03.2023"`

#### Ответ:

* `"transaction_count": "32"`
