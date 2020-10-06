from random import randint
from sys import argv
if len(argv) != 3:
    print("3 arg")
    exit()


if not 10 <= int(argv[1]) <= 49:
    print('amx_nr')

nr=[randint() for i in range(argv[2])]
