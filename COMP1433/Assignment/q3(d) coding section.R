set.seed(42)

data1 <- numeric(500000)
data1 <- rnbinom(n = 500000, size = 1, prob = 0.05)
#simulation of the first situation (only 1 life left)

data2 <- numeric(500000)
data2 <- rnbinom(n = 500000, size = 9, prob = 0.05)
#simulation of the second situation (9 lives left)

x1 <- sum(data1 == 103)
x2 <- sum(data2 == 95)

p1 <- x1/500000
p2 <- x2/500000
#p1 is the simulation probability of the first situation, and p2 is the other
#p1 equals to 0.000242, and p2 equals to 0.003614,
#which are rather approximate to the theoretical probabilities: 0.0002538 and 0.003553


#This is the end of the answer for Q3(d) coding section