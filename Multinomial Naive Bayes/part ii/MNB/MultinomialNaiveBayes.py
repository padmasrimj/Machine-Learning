import os
import collections
import re
import math
import sys


def calculate_probability_for_class(Words,total_training_count,Actual_Class):
    probability_of_class = [1,1,1,1,1]
    for b in range(len(total_training_count)):
        for words in Words:
            probability_of_class[b] += math.log2(float((total_training_count[b][words]+1)/(m+sum(total_training_count[b].values()))))
    maximum = 0
    for r in range(len(probability_of_class)):
        if probability_of_class[r] > probability_of_class[maximum]:
            maximum = r
    if Actual_Class == maximum:
        return 1
    else:
        return 0

i = 0

root = sys.argv[1]
training_set = []
total_training_count = []
stop_file = open('stopwords.txt', 'r')
stop_word_list = []
for line in stop_file:
    line = line.strip()
    list1 = line.split()
    stop_word_list.extend(list1)
distinct_words = collections.Counter()

for path,dirs,files in os.walk(root):
    count = 0
    if len(dirs) > 0:
        global Classes
        Classes = dirs


for path, dirs, files in os.walk(root):
    no_files = len(files)
    file_data = []
    for name in files:
        file = open(os.path.join(path, name))
        checker = 0
        for lines in file:
            if checker != 0:
                temp = re.sub('[^a-zA-Z]', ' ', lines)
                temp_data = temp.split()
                file_data += temp_data
            if 'Lines:' in lines:
                checker += 1
        training_set += [file_data]
    word_count = collections.Counter()

    if len(files) > 1:
        for k in file_data:
            if len(k) > 1:
                data = k.lower().strip()
                if data not in stop_word_list:
                    word_count[data]+=1
        distinct_words += word_count
        total_training_count += [word_count]

global m
m = len(distinct_words)

Class_No = {}
correctly_classified = 0
misclassified = 0

for i, word in enumerate(Classes):
    Class_No[word] = i

print('---------------------------- Classification Results ------------------------------------------------------------')
test_root = sys.argv[2]
for path, dirs, files in os.walk(test_root):
    if len(files) > 1:
        Actual_Class = -1
        for i in Classes:
            if str(path).find(str(i)) != -1:
                Actual_Class = Class_No[i]
                break

        for name in files:
            file = open(os.path.join(path, name))
            checker = 0
            file_data = []
            for lines in file:
                if checker != 0:
                    temp = re.sub('[^a-zA-Z]', ' ', lines)
                    temp_data = temp.split()
                    file_data += temp_data
                if 'Lines:' in lines:
                    checker += 1

            Words = []
            for k in file_data:
                data = k.lower().strip()
                if len(data) > 1 and data not in stop_word_list:
                    Words += [data]

            cl = calculate_probability_for_class(Words, total_training_count, Actual_Class)
            if cl == 1:
                print(os.path.join(path, name),'Correctly classified')
                correctly_classified += 1
            else:
                print(os.path.join(path, name),'Misclassified')
                misclassified += 1
accuracy = (correctly_classified/(correctly_classified+misclassified))
print(' ')
print(' ')
print('************************* Results Summary *********************************************************************')
print('No of files that are correctly classified = ',correctly_classified)
print('No of files that are misclassified = ',misclassified)
print('accuracy','%.2f'%(accuracy*100),'%')
