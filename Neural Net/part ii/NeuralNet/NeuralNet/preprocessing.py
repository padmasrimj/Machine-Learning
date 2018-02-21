import numpy as np
import csv
import re
import sys
file_object = open(sys.argv[1], 'r')
input_data = []
lines = file_object.readlines()
for row in lines:
    x = re.split(r'[ ,|;"]+', row)
    if x[0] == '':
        input_data.append(x[1:])
    else:
        input_data.append(x)
cleaned_data = list()
for row in input_data:
    nullCount = 0
    for col in row:
        if(not col)or(col == '?'):
            nullCount += 1
        else:
            nullCount += 0
    if nullCount == 0:
        cleaned_data.append(row)
colCount = 0
for row in cleaned_data:
    for col in row:
        colCount+=1
    if colCount != 0:
        break
for r in range(colCount):
    count = 0
    sum = 0.0
    temp = list()
    for row in range(len(cleaned_data)-1):
        try:
            no_lines = float(cleaned_data[row][r])
            count += 1
            sum += no_lines
            temp.append(no_lines)
        except ValueError:
            count += 0
            pass
    if count == len(cleaned_data)-1:
        temp = np.array(temp)
        mean = np.mean(temp)
        stddev = np.std(temp)
        for row in range(len(cleaned_data)-1):
            cleaned_data[row][r] = (float(cleaned_data[row][r]) - mean)/stddev
    else:
        for row in range(len(cleaned_data)-1):
            temp.append(cleaned_data[row][r])
        temp = np.array(temp)
        uni = np.unique(temp)
        uni = uni.tolist()
        for row in range(len(cleaned_data)-1):
            cleaned_data[row][r] = uni.index(cleaned_data[row][r])
output_path = sys.argv[2]
with open(output_path,'w') as output:
    writer = csv.writer(output, lineterminator='\n')
    writer.writerows(cleaned_data)
