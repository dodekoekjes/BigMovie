# Title     : Genres over time
# Objective : Makes a graph of popularity of genres over time
# Created by: Benjamin van Veldhuisen
# Created on: 24-1-2018

install.packages("RMySQL", repos='https://cran.uni-muenster.de/')
library(RMySQL)

genrelimit = 8

invisible(png("Chatbot/src/main/resources/img/chart-genres.png"))
con <- dbConnect(MySQL(), host = "server.kevinswebsite.nl", dbname="moviedatabase", user="root", password="fietsbel")
genres <- dbGetQuery(con, paste("SELECT genre, COUNT(*) as c
                     FROM genres
                     GROUP BY genre
                     ORDER BY c DESC
                     LIMIT ", genrelimit, sep=""))$genre

plot(0, xlab="Year", ylab="Occurences", type="n", xlim=c(1875,2018), ylim=c(0,30000))
color = 1
for (genre in genres) {
    v <- dbGetQuery(con, paste("SELECT m.endyear as year, COUNT(*) as count FROM movies AS m, genres AS g WHERE g.genre = '", genre,"' AND m.id = g.movie_id GROUP BY year ORDER BY year ASC", sep=""))
    lines(v$year, v$count, type="l", col=color)
    color = color + 1
}
legend(1880, 30000, legend=genres, col=1:length(genres), lty = 1, lwd=1)
invisible(dev.off())
