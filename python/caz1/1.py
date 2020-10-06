import re


def ex1(a, b):
    b, a = max([a, b]), min(a, b)
    return max([i for i in range(1,b+1) if a%i==0 and b%i==0])


def ex2(s: str):
    return sum([s.count(i) for i in 'aeiouAEIOU'])


def ex3(a, b):
    print(b.count(a))


def ex4(a: str):
    return ''.join([i.lower()+"_" for i in re.findall('[A-Z][^A-Z]*', a)])[:-1]

