from sys import argv
import time
import datetime
ops = {
    '+': lambda x, y: x + y,
    '-': lambda x, y: x - y,
    '/': lambda x, y: x / y,
    '%': lambda x, y: x % y,
}

a, b = int(argv[1]), int(argv[3])
res=ops[argv[2]](a,b)

# print(time.strftime('%Y', datetime.datetime.now()))
tt=datetime.datetime.now().strftime('%Y-%M-%d %H:%M:%S')
with open(argv[4],'w') as file:
    file.write(f'{tt} {res}')
# print(ops[argv[2]](a,b))