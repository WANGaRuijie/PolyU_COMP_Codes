a = eval(input("Please enter a decimal number (an integer in base 10): "))
remainder_set = []
while a > 0:
    b = a % 8
    c = a // 8
    print("quotient: ",c)
    print("remainder: ",b)
    a = c
    remainder_set.insert(0,b)
remainders = ''
for remainder in remainder_set:
    remainders = remainders + str(remainder)
print("The octal number is ", remainders)
input("Enter to close this program")
