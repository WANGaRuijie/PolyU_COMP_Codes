set.seed(42)

data <- data.frame(matrix(0, nrow = 10000, ncol = 3))
colnames(data) <- c("failure.times", "simulation.probability", "theoretical.probability")
# data is a data.frame used for recording the number of failures and their corresponding probability

data$failure.times <- rnbinom(n = 10000, size = 20, prob = 0.5)
#randomly generalize the number of failures for 10000 times

simulation_PMF <- function(n) {
  return(nrow(subset(data, failure.times == n)) / 10000)
}
#the simulation probability of having n failures is equal to N(times of having n failures) / N(total number)

theoretical_PMF <- function(n, r, p) {
  return(choose(n + r - 1, r - 1) * p ^ r * (1 - p) ^ n)
}
#equals to dnbinom(n, size, prob)

for (i in 1:10000) {
  data$simulation.probability[i] <- simulation_PMF(n = data$failure.times[i])
}

data$theoretical.probability <- theoretical_PMF(n = data$failure.times, r = 20, p = 0.5)
#fill in the blanks of data

library(ggplot2)

ggplot(data, aes(x = failure.times)) + 
  geom_point(mapping = aes(y = simulation.probability, color = "Simulation")) +
  geom_point(mapping = aes(y = theoretical.probability, color = "Theoretical")) + 
  labs(x = "Times of Failures", y = "Probability") + 
  xlim(0, 100) +
  scale_color_manual(values = c("Simulation" = "red", "Theoretical" = "blue"))
#scatter plots with different colors


#This is the end of the answer for Q3(b)

 
  


