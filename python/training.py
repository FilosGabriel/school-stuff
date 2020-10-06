from xml.dom import minidom
import xml.etree.ElementTree as et
from collections import Counter
import sqlite3
import zipfile
from datetime import datetime
import sys
import urllib.request
import urllib.parse
import re
from itertools import chain
import os
import hashlib


regex = re.compile(
    r'^(?:http|ftp)s?://'  # http:// or https://
    # domain...
    r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|'
    r'localhost|'  # localhost...
    r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})'  # ...or ip
    r'(?::\d+)?'  # optional port
    r'(?:/?|[/?]\S+)$', re.IGNORECASE)


def problema2(s, url):
    if not re.match(regex, url) is not None:
        return False
    f = urllib.request.urlopen(url)
    return s in f.read().decode('utf-8')


def problema1(s: str):
    return sorted(re.findall(r'[a-zA-Z0-9_]+', s))


def problema4():
    path = sys.argv[1]
    return sorted(list(set([
        os.path.getsize(os.path.join(path, i))
        for i in os.listdir(path)
        if os.path.isfile(os.path.join(path, i))
    ])))


def problema3(path: str) -> list:

    # files_list = list(chain.from_iterable([[os.path.join(
    #     root, file) for file in files]for root, directories, files in os.walk(path)]))
    result = []
    for root, dirs, files in os.walk(path):
        result += files
        # for file in files:
        #     md = hashlib.md5()
        #     md.update(open(os.path.join(root, file), 'rb').read())
        #     result.append(md.hexdigest())
    return result


# print(problema3('caz1'))


def problema5(cod: str):
    x = 1

    instr = {
        'egal': lambda x: x,
        'plus': lambda y: x + y,
        'minus': lambda y: x - y,
        'impartit': lambda y: x // y,
        'inmultit': lambda y: x * y
    }
    ops = re.split('\n', cod)
    for i in ops:
        temp = re.split(' ', i)
        x = instr[temp[1]](int(temp[2]))

    return x


def problema7():
    a = []
    dates = []
    for i in sys.argv[1:]:
        date = datetime.strptime(i, '%m/%d/%Y_%H.%M.%S')
        dates.append(date)
        a.append(date.strftime('%Y-%m-%d %H:%M:%S'))
    return (sorted(a, reverse=True), int(max([(i - j).total_seconds() for i in dates for j in dates])))


def problema8(path: str, low: int, high: int) -> int:

    zip_path = []
    for (root, directories, files) in os.walk(path):
        for fileName in files:
            if fileName.split('.')[-1] == 'zip':
                zip_path = os.path.join(root, fileName)
                break

    with zipfile.ZipFile(zip_path, 'r') as file:
        sql = [i for i in file.namelist() if 'sample.sqlite' in i][0]
        with file.open(sql) as filesql:
            with open('temp.db', 'wb') as db:
                db.write(filesql.read())
    conn = sqlite3.connect('temp.db')
    c = conn.cursor()
    c.execute(
        f"Select albums.Title,tracks.Name,genres.Name from albums join tracks on albums.AlbumId=tracks.AlbumId join genres on genres.GenreId=tracks.GenreId where tracks.Milliseconds between ? and ?", (low, high))
    return(sorted(c.fetchall(), key=lambda x: x[0]))


def problema9(path: str):
    pattern = re.compile(r"\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}")
    files_list = []
    if os.path.isfile(path):
        files_list = [path]
    else:
        for dirpah, dirs, files in os.walk(path):
            for filename in files:
                if filename == 'access.log':
                    files_list.append(os.path.join(dirpah, filename))

    result = []
    for file in files_list:
        with open(file, 'r') as log_file:
            result += pattern.findall(log_file.read(), re.MULTILINE)
    return [i[0] for i in Counter(result).most_common(7)]


def problema10(path):
    if os.path.isfile(path):
        xml_doc=minidom.parse(path)
        content=xml_doc.getElementsByTagName('CONTENT')