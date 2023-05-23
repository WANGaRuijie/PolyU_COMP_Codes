def myMax(number_list):

    """
    The function myMax() is used for looking for the maximum and its location index.
    Other functions will process the input of the user, and then transport related information to myMax().
    Similarly, myMax() will transport its return values to other functions to print the results.
    Please pay attention that myMax() is not responsible for all these procedures above.
    One example is as below:
        Please enter a list of different numbers separated by ',': 1,-3,4.5,5,18,-1,3,-4
        The maximal number is 18 .
        Its location index is 5 .
    """

    maximum = number_list[0]
    maximum_location = 0
    length = len(number_list)
    r = 0

    while r < length:
        if maximum < number_list[r]:
            maximum_location = r
            maximum = number_list[r]
        r += 1

    if maximum % 1 != 0:
        return maximum, maximum_location
    else:
        return int(maximum), maximum_location


def mySort(number_list):

    """
    The function mySort() is used for sorting a list of numbers.
    Other functions will process the input of the user, and then transport useful information to mySort().
    Similarly, mySort() will transport its return value to other functions to print the result.
    Please pay attention that mySort() is not responsible for all these procedures above.
    One example is as below:
        Please enter a list of different numbers separated by ',': 1,-3,4.5,5,18,-1,3,-4
        A list of sorting values in descending order: [18, 5, 4.5, 3, 1, -1, -3, -4] .
    """

    length = len(number_list)
    p, q = 0, 0

    while p < length:
        q = p
        new_list = []

        while q < length:
            new_list.append(number_list[q])
            q += 1

        length1 = len(new_list)

        if length1 > 0:
            maximum1, maximum1_location = myMax(new_list)
            number_list[p + maximum1_location] = number_list[p]
            number_list[p] = maximum1

        p += 1

    return number_list


print(myMax.__doc__)
print(mySort.__doc__)

input_list = input("\tPlease enter a list of different numbers separated by ',': ")
string_list = input_list.split(',')
length2 = len(string_list)
number_list = []
i = 0

while i < length2:
    number_list.append(float(string_list[i]))
    i += 1

x = myMax(number_list)
print(f"\n\tThe maximal number is {x[0]} .\n\tIts location index is {(x[1] + 1)} .")

y = mySort(number_list)
print(f"\tA list of sorting values in descending order: {y} .")



