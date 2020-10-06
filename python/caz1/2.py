from itertools import zip_longest
from itertools import chain
from itertools import permutations
from itertools import repeat
import test
import os


def ex1(n):
    a = [0, 1]
    for i in range(3, n):
        a.append(a[-1] + a[-2])
    return a[-1]


def ex2(l):
    is_prime = lambda a: a == 1 or a == 2 or a % 2 != 0 and [i for i in range(3, int(a / 2), 2) if a % i == 0].__len__() == 0
    return[i for i in l if is_prime(i)]


def ex4(a, b):
    a, b = set(a), set(b)
    return (a & b, a | b, a - b, b - a)


def ex5(x: list, k):
    return list(permutations(x, k))


def ex6(x, *param):
    d = {i: 0 for i in set(list(chain.from_iterable(param)))}
    for i in list(chain.from_iterable(param)):
        d[i] += 1
    return list(map(lambda y: y[0], list(filter(lambda k: k[1] == x, d.items()))))


def ex7(*param, x=1, flag=True):
    te = lambda op: [[i for i in car if op(ord(i) % x)] for car in param]
    return te(lambda x: x == 0) if flag else te(lambda x: x != 0)


def ex8(*params):
    # m = max([len(i) for i in params])
    # return list(zip(map(lambda x: x if len(x) == m else x + list(repeat(None, m - len(x))), params)))
    return list(zip_longest(*params))


def ex9(l: list):
    return sorted(l, key=lambda x: x[1][2])


