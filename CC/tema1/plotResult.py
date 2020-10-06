import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn
import json

seaborn.set()
data = pd.read_csv('./data.csv')

times = []
for line in data.values:
    times.append(line[0])

splitted=[]
for i in range(7):
    splitted.append(times[i*30:(i+1)*30])


# print(times[1:2])
# for line in splitted:
plt.plot(times, '.-')

# plt.yticks(list(range(0.5,17,0.5)))
plt.ylabel('Time')
plt.xlabel('Runs')
plt.show()

fetch_weather = []
fetch_image = []
fetch_edit = []
with open('logging.json', 'r') as f:
    data = json.load(f)
    for api in data['log']:
        if api['url'][8:11] == 'res':
            fetch_edit.append(api['latency'])
        elif api['url'][8:11] == 'api':
            fetch_weather.append(api['latency'])
        else:
            fetch_image.append(api['latency'])


labels = ["weather ", "image", "edit"]
x=range(len(fetch_edit))
fig, ax = plt.subplots()
ax.stackplot(x, fetch_weather,fetch_image, fetch_edit, labels=labels)
ax.legend(loc='upper left')
plt.show()