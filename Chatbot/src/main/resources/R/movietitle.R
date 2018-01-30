# Title     : Count Numbers in movietitles
# Objective : DONE
# Created by: Tristan Kruijshaar
# Created on: 24-1-18

#hoe vaak komen er cijfers in titels voor?

install.packages("RMySQL", repos='https://cran.uni-muenster.de/')
library(RMySQL)

con <- dbConnect(MySQL(), host = "server.kevinswebsite.nl", dbname="moviedatabase", user="root", password="fietsbel")
nul <- dbGetQuery(con, "SELECT COUNT(movietitle) AS nul FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%0%' OR movietitle LIKE '%zero%');")
een <- dbGetQuery(con, "SELECT COUNT(movietitle) AS een FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%1%' OR movietitle LIKE '%one%' OR movietitle LIKE '%first%' OR movietitle LIKE '%1st%');")
twee <- dbGetQuery(con, "SELECT COUNT(movietitle) AS twee FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%2%' OR movietitle LIKE '%two%' OR movietitle LIKE '%second%' OR movietitle LIKE '%2nd%');")
drie <- dbGetQuery(con, "SELECT COUNT(movietitle) AS drie FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%3%' OR movietitle LIKE '%three%' OR movietitle LIKE '%third%' OR movietitle LIKE '%3rd%');")
vier <- dbGetQuery(con,"SELECT COUNT(movietitle) AS vier FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%4%' OR movietitle LIKE '%four%' OR movietitle LIKE '%fourth%' OR movietitle LIKE '%4th%');")
vijf <- dbGetQuery(con,"SELECT COUNT(movietitle) AS vijf FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%5%' OR movietitle LIKE '%five%' OR movietitle LIKE '%fifth%' OR movietitle LIKE '%5th%');")
zes <- dbGetQuery(con,"SELECT COUNT(movietitle) AS zes FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%6%' OR movietitle LIKE '%six%' OR movietitle LIKE '%sixth%' OR movietitle LIKE '%6th%');")
zeven <- dbGetQuery(con,"SELECT COUNT(movietitle) AS zeven FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%7%' OR movietitle LIKE '%seven%' OR movietitle LIKE '%seventh%' OR movietitle LIKE '%7th%');")
acht <- dbGetQuery(con,"SELECT COUNT(movietitle) AS acht FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%8%' OR movietitle LIKE '%eight%' OR movietitle LIKE '%eighth%' OR movietitle LIKE '%8th%');")
negen <- dbGetQuery(con,"SELECT COUNT(movietitle) AS negen FROM movies WHERE movietitle IN (SELECT movietitle FROM movies WHERE movietitle LIKE '%9%' OR movietitle LIKE '%nine%' OR movietitle LIKE '%ninth%' OR movietitle LIKE '%9th%');")
# 0:2.5, 1:6.5, 2:4.5, 3:2.5, 4:2, 5:1.8, 6:1.5, 6:1.5, 8:1.5, 9:1.8

index <- c(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
numbers <- c(nul$nul, een$een, twee$twee, drie$drie, vier$vier, vijf$vijf, zes$zes, zeven$zeven, acht$acht, negen$negen)
df <-data.frame(index, numbers)

invisible(png('Chatbot/src/main/resources/img/numbersinmovietitle.png'))
plot(df, type = "h", col = "green")
dev.off()