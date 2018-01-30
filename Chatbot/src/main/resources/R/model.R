# Title     : model
# Objective : done
# Created by: Tristan Kruijshaar
# Created on: 25-1-18

install.packages('RMySQL', repos='http://cran.uni-muenster.de')
install.packages('gridExtra', repos='http://cran.uni-muenster.de')
library(RMySQL)
library(gridExtra)

con <- dbConnect(MySQL(), host = "server.kevinswebsite.nl", dbname="moviedatabase", user="root", password="fietsbel")
v <- dbGetQuery(con, "SELECT * FROM
(SELECT count(a.id) as TotalActionHorrorFrance
FROM movies a
WHERE a.isSerie = 0
      AND a.id IN (SELECT movie_id as ID
                   FROM genres
                   WHERE Genre LIKE 'action' OR Genre LIKE 'horror')
      AND a.id IN (SELECT movie_id as ID
                   FROM locations
                   WHERE country LIKE '%france%')) as TotalActionHorrorFrance
CROSS JOIN
(SELECT count(a.id) as TotalActionHorrorUSA
FROM movies a
WHERE a.isSerie = 0
      AND a.id IN (SELECT movie_id as ID
                   FROM genres
                   WHERE Genre LIKE 'action' OR Genre LIKE 'horror')
      AND a.id IN (SELECT movie_id as ID
                   FROM locations
                   WHERE country LIKE '%USA%')) as TotalActionHorrorUSA;")

invisible(png('Chatbot/src/main/resources/img/model.png'))
grid.table(v)
invisible(dev.off())