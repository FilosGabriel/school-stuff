import os
import requests
from time import time as timer
from multiprocessing.pool import ThreadPool
from statistics import mean
import csv
import time

start = timer()
urls = [(i, 'http://localhost/image.jpg?lat=-22.760611&long=-43.522732') for i in range(10)]


def fetch_url(entry):
    path, uri = entry
    startI = timer()
    r = requests.get(uri, stream=True)
    return (timer() - startI, r.status_code)


finalResult = []
for i in range(2):
    print(i)
    results = ThreadPool(10).imap_unordered(fetch_url, urls)
    temp = [path for path in results]
    finalResult.append(temp)
    print(i)
    time.sleep(2)

with open('data.csv', 'w', newline='') as myfile:
    wr = csv.writer(myfile)
    for setLine in finalResult:
        for line in setLine:
            wr.writerow(line)

# print(mean(results))
print(f"Elapsed Time: {timer() - start}")
