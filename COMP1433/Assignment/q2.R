#Q2(a)

employees <- read.csv("employees.csv")
employees$Salary_Per_Year <- employees$Monthly_Salary * 12

#Q2(b)

male_average_salary <- mean(employees$Salary_Per_Year[employees$Gender == "Male"])
female_average_salary <- mean(employees$Salary_Per_Year[employees$Gender == "Female"])
cat("The average salary per year for male employees is ", male_average_salary, "\n")
cat("The average salary per year for female employees is ", female_average_salary, "\n")

#Q2(c)

library(ggplot2)

ggplot(data = employees) + geom_bar(mapping = aes(x = Years_of_Experience, y = after_stat(prop), group = 1)) + 
  xlab("Years of Experience") + ylab("Frequency")

#Q2(d)
ggplot(data = employees) +geom_point(mapping = aes(x = Years_of_Experience, y = Monthly_Salary)) +
  xlab("Years of Experience") + ylab("Monthly Salary")


#This is the end ofthe answer for Q2
