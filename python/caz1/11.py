import time
import hashlib
from itertools import chain
import os
import re


def ex1(text: str) -> list:
    print(re.split(r'\W+', text))


def ex10(directory: str) -> list:
    files_list = list(chain.from_iterable([[os.path.join(
        root, file) for file in files]for root, directories, files in os.walk(directory)]))

    hash_list = []
    for file in files_list:
        sha = hashlib.sha256()
        sha.update(open(file, 'rb').read())
        hash_list.append(sha.hexdigest())

    return [files_list[index] for index, hash_item in enumerate(hash_list) if hash_list.count(hash_item) > 1]


# print(ex10('.'))
def md5(file, mode):
    m = hashlib.md5() if mode else hashlib.sha256()
    m.update(open(file, 'rb').read())
    return m.hexdigest()


def ex9(directory: str) -> list:
    files_list = list(chain.from_iterable([[os.path.join(
        root, file) for file in files]for root, directories, files in os.walk(directory)]))

    return[
        {
            'file_name': os.path.basename(file),
            'md5': md5(file, True),
            'sha256': md5(file, False),
            'size': os.path.getsize(file),
            'time': time.ctime(os.path.getctime(file)),
            'abs': os.path.abspath(file)

        } for file in files_list
    ]


def ex8(directory,pattern):
    files_list = list(chain.from_iterable([[os.path.join(
        root, file) for file in files]for root, directories, files in os.walk(directory)]))
    for file in files_list:
        if()
print(ex9('.'))
