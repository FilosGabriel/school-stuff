def ex1(a: list, b: list):
    a, b = set(a), set(b)
    return set([frozenset(a & b), frozenset(a | b), frozenset(a - b), frozenset(b - a)])


def ex2(s: str) -> dict:
    return {i: s.count(i) for i in set(s)}


def ex3(d1: dict, d2: dict) -> list:
    l = []
    for i in d1:
        if not i in d2.values():
            l.append(i)
    return l


def ex4(tag, content, **params):
    return f"<{tag} " + "".join([f'{key}=\"{value}\"' for key, value in params.items()]) + f">{content} <\\{tag}>"


def ex6(l: list):
    return (len(set(l)), len(l) - len(set(l)))


print(ex6([1, 1, 1, 1]))
