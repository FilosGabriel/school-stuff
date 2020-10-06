from itertools import chain


def ex1(l: list) -> list:
    return sorted(l, key=lambda x: x[1])


def ex2(l: list, s: str) -> bool:
    return s in [i[1] for i in l]


operations = {"+": lambda a, b: a + b,
              "*": lambda a, b: a * b,
              "/": lambda a, b: a / b,
              "%": lambda a, b: a % b
              }


def ex3(op, a, b):
    return operations['op'](a, b)


def ex5(*params):
    d = {}
    for i in params:
        for key, value in i.items():
            if key in d:
                d[key] = [value] + d[key]
            else:
                d[key] = [value]
    return dict(map(lambda k: (k[0], k[1][0]) if len(k[1]) == 1 else k, d.items()))


def ex6(d: dict, sep='-', h=''):
    for key, value in d.items():
        if type(value) != type({}):
            print(f'{h}{key}{sep}{value}')
        else:
            ex6(value, h=f"{h}{key}{sep}")
0

a = {

    'a': 1,

    'b':

    {

        'c': 3,

        'd':

        {

            'e': 5,

            'f': 6

        }

    }

}
ex6(a)
