from math import sqrt
import calendar
from random import randint
import datetime
import time
import locale
from dateutil.relativedelta import relativedelta


def ex7(a, b):
    now = datetime.datetime.now()
    while True:
        x = randint(a, b)
        time.sleep(x)
        c = datetime.datetime.now() - now
        print(c.total_seconds() / 60)


def ex5(x):
    locale.setlocale(locale.LC_ALL, 'ro_RO')
    for i in range(x):
        print(calendar.day_name[datetime.datetime(2020 - i, 12, 31).weekday()])


def ex61(nr: int) -> bool:
    """
    fast
    """
    return nr in [2, 3] or nr % 2 != 0 and [i for i in range(3, int(sqrt(nr)), 2) if nr % i == 0].__len__() == 0


def ex62(nr: int) -> bool:
    """
    slow
    """
    if nr in [2, 3]:
        return True
    if nr % 2 == 0:
        return False
    else:
        i = 3
        while i < int(sqrt(nr)):
            if nr % i == 0:
                return False
            i += 2
        return True


# t1 = time.time()
# print(ex62(67280421310721))
# print(time.time() - t1)
# t1 = time.time()
# print(ex61(67280421310721))
# print(time.time() - t1)


def ex4():
    santa = datetime.datetime(2020, 12, 25, 3, 33, 33)
    while True:
        time.sleep(1)
        now=datetime.datetime.now()
        a=santa-now
        print(int(a.total_seconds()))


