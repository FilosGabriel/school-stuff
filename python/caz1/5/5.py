def ex2a(*a, **kargs):
    return sum(kargs.values())


ex2b = lambda *a, **b: sum(b.values())


def ex3a(a):
    return [x for x in a if x in 'AEIOUaeiou']


ex3b = lambda a: list(filter(lambda x: x in 'AEIOUaeiou', a))


def ex4(*params, **kargs):
    get_dict = lambda p: list(filter(lambda i: type(i) == type({}) and len(i.keys()) > 1 and
                                     list(filter(lambda c: type(c) == str and len(c) > 2, i.keys())).__len__() > 0, p))
    return get_dict(params) + get_dict(kargs.values())
