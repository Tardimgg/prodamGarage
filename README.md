# Проектная работа по Java:
РТУ МИРЭА
Институт информационных технологий
____
Игра «Prodam Garage»
Выполнили: Студенты группы ИКБО-10-21
Таран Д. Громыхалин М. Захаров М. Балахонцев Н.
____
Цель игры
Поначалу ваша цель — это просто иметь достаточно денег на существование.
Наёмная работа отчасти решает эту проблему. Но в будущем необходимо подключать
новые источники дохода, высвобождать под них время.
Высшая цель и логическое завершение игры — достижение выбранной мечты, заработать миллион.

### Сборка
Создание jar файла и docker образа
```bash
mvn package
```
### Запуск
Запустить игру можно несколькими способами:
  - Используя созданный jar файл
```bash
java -jar ./target/prodamGarage-1.0-SNAPSHOT.jar
```
  - Используя созданный docker образ (требуется SPICE-клиент)
```bash
docker run -p 5900:5900 -d --name game prodam-garage
```
Подключимся к серверу при помощи SPICE-клиента
```bash
remote-viewer spice://127.0.0.1:5900
```
  - Используя готовый docker образ (требуется SPICE-клиент)
```bash
docker pull tardimgg/prodam-garage &&
docker run -p 5900:5900 -d --name game prodam-garage &&
remote-viewer spice://127.0.0.1:5900
```

### Скриншоты:
  - Главное меню

![alt](/images/main_menu.png)

  - Игровой стол
    
![alt](/images/gameplay.png)
  - Игровые события
    
![alt](/images/notification_event.png)
![alt](/images/possibility_event.png)
![alt](/images/selection_event.png)
  - Игровая статистика
    
![alt](/images/statistics.png)
![alt](/images/property.png)
