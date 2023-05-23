print("The program is to calculate the root(s) of the quadratic equation, ax^2+bx+c=0.")
a = eval(input("Please enter the value a."))
b = eval(input("Please enter the value b."))
c = eval(input("Please enter the value c."))
d = b ** 2 - (4 * a * c)
x1 = (-b + (d ** 0.5))/(2 * a)
x2 = (-b - (d ** 0.5))/(2 * a)
if d < 0:
    print("Invalid input.")
if d == 0:
    print("There is only one root: ", x1)
if d > 0:
    print("There are two roots:", (x1,x2))
input("Enter to close this program.")