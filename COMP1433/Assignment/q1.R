iris_data <- data.frame(Petal.Length = iris[[3]], Petal.Width = iris[[4]])
centorids <- data.frame(Petal.Length = c(1.4, 1.3, 1.7), Petal.Width = c(0.1, 0.2, 0.1))
#initialize two data.frames to store the data of iris and the initial centorids

dis <- function(x, y) {
  sqrt((x[1] - y[1]) ^ 2 + (x[2] - y[2]) ^ 2)
}
#dis function is for calculating the distance between two points
#an independent variable (x or y) for this function is a row of a data.frame
#an independent variable serves a set of all coordinates of a point

cluster <- numeric(nrow(iris_data)) 
#records the cluster that each point belongs to

centorid_distance_sum <- data.frame(matrix(0, nrow = 100, ncol = nrow(centorids)))
#records the sum of the distances between the points and their centorids in 100 times of iterations

centorid_distance_mean <- data.frame(matrix(0, nrow = 100, ncol = nrow(centorids)))
#records the mean of the distances between the points and their centorids in 100 times of iterations

for (i in 1:100) {

  for (j in 1:nrow(iris_data)) {
  
    distances <- numeric(nrow(centorids)) 
    #distances records distances for a point to the 3 centorids
    
    for (k in 1:nrow(centorids)) {
      distances[k] <- dis(iris_data[j,], centorids[k,])
    }
    
    cluster[j] <- which.min(distances)
    #find the index of the minimum of the distances
    
    centorid_distance_sum[i, cluster[j]] <- centorid_distance_sum[i, cluster[j]] + min(unlist(distances))
    #calculate and store the sum of the minimum distances (the distance between each point to its centorid) in an iteration
  }
  
  for (k in 1:nrow(centorids)){
    
    cluster_points <- iris_data[cluster == k,] 
    #cluster_points extracts Petal.Length and Petal.Width of points belonging to a certain cluster
    
    centorids[k, 1] <- mean(cluster_points[[1]])
    centorids[k, 2] <- mean(cluster_points[[2]]) 
    #calculate the new centorids for the next iteration
    
    centorid_distance_mean[i, k] <- centorid_distance_sum[i, k] / nrow(iris_data[cluster == k, ])
    #calculate and store the mean value of each value in centorid_distance_sum
    
  }
  
}

library(ggplot2)

ggplot(data = iris_data) + geom_point(mapping = aes(x = Petal.Length, y = Petal.Width, color = factor(cluster))) +
  scale_color_manual(values = c("red", "green", "blue"))
#draw the scatter plot for all the data samples (x-axis corresponds to the petal length while y-axis 
#corresponds to the petal width), and color each sample in red, green, and blue, indicating the cluster it has been assigned to

ggplot(data = centorid_distance_mean, aes(x = 1:100)) +
  geom_line(mapping = aes(y = X1, color = "Centorid 1")) +
  geom_line(mapping = aes(y = X2, color = "Centorid 2")) +
  geom_line(mapping = aes(y = X3, color = "Centorid 3")) +
  labs(x = "Iteration", y = "Average Distance to Centorid", color = "Legend") +
  scale_color_manual(values = c("red", "green", "blue"))
#draw the line plot with x-axis corresponds to the training iteration, and y-axis corresponds
#to the mean distance to the cluster centroids at that iteration


#This is the end of the answer for Q1.