def myMedian(X):

    y = sorted(X)
    length = len(X)

    if length % 2 == 0:
        if y[int(0.5 * length - 1)] != y[int(0.5 * length)]:
            return format(y[int(0.5 * length - 1)], '.3f'), format(y[int(0.5 * length)], '.3f')
        if y[int(0.5 * length - 1)] == y[int(0.5 * length)]:
            return format(y[int(0.5 * length)], '.3f')
    if length % 2 == 1:
        return format(y[int(0.5 * length - 1)], '.3f')


def main():

    y = []
    z = []

    versicolor = []
    setosa = []
    virginica = []

    with open('iris2021.csv', 'r') as f:
        reader = f.readlines()

    del reader[0:1]
    length = len(reader)

    for i in reader:
        x = i.split(',')
        y.append(x[0])
        z.append(x[1])

    for j in range(1, length):
        if z[j-1] == 'versicolor\n':
            versicolor.append(y[j-1])
        if z[j-1] == 'setosa\n':
            setosa.append(y[j-1])
        if z[j-1] == 'virginica\n':
            virginica.append(y[j-1])
        j += 1

    versicolor = list(map(eval, versicolor))
    setosa = list(map(eval, setosa))
    virginica = list(map(eval, virginica))

    print(f"The median of Petal.Width for virginica is {myMedian(virginica)}.")
    print(f"The median of Petal.Width for versicolor is {myMedian(versicolor)}.")
    print(f"The median of Petal.Width for setosa is {myMedian(setosa)}.")


main()







