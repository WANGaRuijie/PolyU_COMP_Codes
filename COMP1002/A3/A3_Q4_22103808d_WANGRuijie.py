# Wang Ruijie 22103808D

def myIntersection(A, B):
    x = list(set(A))
    y = list(set(B))
    z = []

    for i in x:
        if i in y:
            z.append(i)
    return z


def main():

    # A logic for testing

    A = [1, 2, 'a', 'b', 3, 'Hi', 2, 'A', 1]
    B = (3, 2, 'a', 'h', 1, 1, 'abc', 'a')
    print(myIntersection(A, B))


main()