import hashlib
import os
from collections import Counter
import json


def problema1(text: str):
    return Counter(text).most_common()[0][0]


def problema2(dir_path: str):
    return sorted([i for i in os.listdir(dir_path) if os.path.isfile(os.path.join(dir_path, i))])


def problema3(my_path: str):
    data = ''
    with open(my_path, 'r') as file:
        data = file.read()
    obj = json.loads(data)
    while True:
        if 'next' in obj.keys():
            my_path = obj['next']
            with open(obj['next'], 'r') as file:
                data = file.read()
            obj = json.loads(data)
        else:
            break
    return os.path.getsize(my_path)


def problema4(dir_path: str, to_find: str):
    abs_path = []
    for root, directories, files in os.walk(dir_path):
        for file in files:
            if os.path.isfile(os.path.join(root, file)):
                hash_b = hashlib.sha1()
                hash_b.update(open(os.path.join(root, file),'rb').read())
                if to_find == hash_b.hexdigest():
                    abs_path.append(os.path.abspath(os.path.join(root, file)))
    return abs_path
