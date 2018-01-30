# Title     : Ratings plot
# Objective : Makes a plot of the movie ratings
# Created by: Benjamin
# Created on: 24-1-2018

install.packages("RMySQL")
library(RMySQL)

con <- dbConnect(MySQL(), host = "server.kevinswebsite.nl", dbname="moviedatabase", user="root", password="fietsbel")
v <- dbGetQuery(con, "SELECT rating, COUNT(*) AS count FROM ratings GROUP BY rating ORDER BY rating")
l <- list(breaks=v$rating, counts=v$count, density=v$count/diff(v$rating), xname="Rating")
class(l) <- "histogram"
invisible(png('Chatbot/src/main/resources/img/ratingsplot.png'))
plot(l)
invisible(dev.off())