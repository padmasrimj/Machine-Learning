import csv
import sys
import math
import random
import re
import numpy as np

ta = 0.1
training_expected_error = float(sys.argv[3])
training_error = 1
no_iterations = 0


input_file = sys.argv[1]
reader = csv.reader(open(input_file))
input_data = list()
training_data = list()
testing_data = list()

input_for_training = list()
target_for_training = list()

input_for_testing = list()
target_for_testing = list()

for row in reader:
    if "\n" not in row:
        input_data.append(row)
data_percentage = int(sys.argv[2])/100
data_per = int(data_percentage*len(input_data)+0.5)
random.shuffle(input_data)
training_data = input_data[:data_per]
testing_data = input_data[data_per:]

for row in training_data:
    input_for_training.append(row[:-1])
    target_for_training.append(row[-1])

for row in testing_data:
    input_for_testing.append(row[:-1])
    target_for_testing.append(row[-1])
No_of_layers = int(sys.argv[4])
Neuron_matrix_list = list()
for i in range(No_of_layers):
    Neuron_matrix_list.append(int(sys.argv[i+5]))
Neuron_matrix = Neuron_matrix_list;
weight_vector = list()
input_vector = len(input_for_training[0]) + 1

for i in range(No_of_layers):
    output = Neuron_matrix[i]
    temp = list()
    for j in range(input_vector * output):
        temp.append(random.uniform(0, 1))
    weight_vector.append(temp)
    input_vector = output + 1
temp = list()
output = 1
for j in range(input_vector * output):
    temp.append(random.uniform(0, 1))
weight_vector.append(temp)


def sigmoid(x):
    try:
      sig = 1.0 / (1 + math.exp(-x))
      return sig
    except OverflowError:
       if(x>0):
          return 1
       else:
          return 0

while (training_error > training_expected_error) & (no_iterations < 2500):
    no_iterations += 1
    training_error = 0.0
    for z in range(len(input_for_training)):
        weights_modified = list()
        intermidiate_list = list()
        output = list()
        delta_calculation = list()
        a = 1
        intermidiate_list.append([a] + input_for_training[z])
        output.append(intermidiate_list)
        intermidiate_list = np.array(intermidiate_list)
        for k in range(No_of_layers):
            for i in range(Neuron_matrix[k]):
                weights_modified.append(
                    weight_vector[k][int((i * (len(weight_vector[k]) / Neuron_matrix[k]))):int((i + 1) *
                    (len(weight_vector[k]) / Neuron_matrix[k]))])
            weights_modified = np.array(weights_modified)
            intermidiate_list = intermidiate_list.astype(np.float32)
            intermidiate_list = np.transpose(intermidiate_list)
            result = np.matmul(weights_modified, intermidiate_list)
            result = np.transpose(result)
            result = result.tolist()
            result = result[0]
            net_result = list()
            for i in range(len(result)):
                b = (sigmoid(result[i]))
                net_result += [b]
            weights_modified = list()
            intermidiate_list = list()
            intermidiate_list.append([a] + net_result)
            output.append(intermidiate_list)
            intermidiate_list = np.array(intermidiate_list)
        weights_modified.append(weight_vector[k + 1])
        weights_modified = np.array(weights_modified)
        intermidiate_list = intermidiate_list.astype(np.float16)
        intermidiate_list = np.transpose(intermidiate_list)
        result = np.matmul(weights_modified, intermidiate_list)
        result = np.transpose(result)
        result = result.tolist()
        result = result[0]
        net_result = list()
        for i in range(len(result)):
            b = (sigmoid(result[i]))
            net_result += [b]
        temp_delta = (net_result[0]) * (1 - net_result[0]) * (float(target_for_training[z]) - net_result[0])
        training_error += (
        (float(target_for_training[z]) - net_result[0]) * (float(target_for_training[z]) - net_result[0]) * 0.5 / len(input_for_training))
        delta_calculation.append(temp_delta)
        for j in range(No_of_layers):
            temp1_delta = list()
            updated_weights = list()
            for d in range(Neuron_matrix[len(Neuron_matrix) - j - 1] + 1):
                h = weight_vector[len(weight_vector) - j - 1]
                temp_h = list()
                temp_delta = list()

                for i in range(int(len(h) / len(delta_calculation))):
                    temp_h.append(h[(i * (len(delta_calculation))):(i + 1) * (len(delta_calculation))])
                temp_h = np.array(temp_h)
                temp_h = np.transpose(temp_h)
                temp_delta.append(delta_calculation)
                temp_delta = np.array(temp_delta)
                h1 = np.matmul(temp_delta, temp_h)
                h1 = h1.tolist()
                h1 = h1[0][d]
                temp_delta1 = (output[len(output) - j - 1][0][d]) * (
                1 - (output[len(output) - j - 1][0][d])) * (float(h1))
                temp1_delta.append(temp_delta1)
            for d2 in range(len(delta_calculation)):
                for d1 in range(Neuron_matrix[len(Neuron_matrix) - j - 1] + 1):
                    temp_new_weight = ta * delta_calculation[d2] * (output[len(output) - j - 1][0][d1])
                    updated_weights.append(temp_new_weight)
            for i in range(len(weight_vector[len(weight_vector) - j - 1])):
                weight_vector[len(weight_vector) - j - 1][i] = weight_vector[len(weight_vector) - j - 1][i] + updated_weights[i]
            delta_calculation = temp1_delta[1:]
        for d2 in range(len(delta_calculation)):
            for d1 in range(len(input_for_training[0])):
                temp_new_weight = ta * delta_calculation[d2] * (float(input_for_training[z][d1]))
                updated_weights.append(temp_new_weight)
        for i in range(len(weight_vector[0])):
            weight_vector[0][i] = weight_vector[0][i] + updated_weights[i]


testing_error = 0.0
for val in range(len(input_for_testing)):
    weights_modified=list()
    modified_weights = list()
    output=list()
    delta_calculation = list()
    a=1
    modified_weights.append([a]+input_for_testing[val])
    output.append(modified_weights)
    modified_weights=np.array(modified_weights)
    for k in range(No_of_layers):
        for i in range(Neuron_matrix[k]):
            o = (i*int((len(weight_vector[k])/Neuron_matrix[k])))
            n = (i+1)*int((len(weight_vector[k])/Neuron_matrix[k]))
            weights_modified.append(weight_vector[k][o:n])
        weights_modified=np.array(weights_modified)
        modified_weights=modified_weights.astype(np.float16)
        modified_weights=np.transpose(modified_weights)
        result=np.matmul(weights_modified,modified_weights)
        result=np.transpose(result)
        result=result.tolist()
        result=result[0]
        net_result=list()
        for i in range(len(result)):
            b=(sigmoid(result[i]))
            net_result=net_result+[b]
        weights_modified=list()
        modified_weights = list()
        modified_weights.append([a]+net_result)
        output.append(modified_weights)
        modified_weights=np.array(modified_weights)
    weights_modified.append(weight_vector[k+1])
    weights_modified=np.array(weights_modified)
    modified_weights=modified_weights.astype(np.float16)
    modified_weights=np.transpose(modified_weights)
    result=np.matmul(weights_modified,modified_weights)
    result=np.transpose(result)
    result=result.tolist()
    result=result[0]
    net_result=list()
    for i in range(len(result)):
        b=(sigmoid(result[i]))
        net_result=net_result+[b]
    temp_delta=(net_result[0])*(1-net_result[0])*(float(target_for_training[val])-net_result[0])
    testing_error+=((float(target_for_testing[val])-net_result[0])*(float(target_for_testing[val])-net_result[0])*0.5/len(input_for_testing))
    delta_calculation.append(temp_delta)

for m in range(No_of_layers):
    print("")
    print("Hidden Layer #" + str(m + 1) + ":")
    print('\t'),
    for n in range(Neuron_matrix[m]):
        print("Neuron #" + str(n + 1) + ":"),
        k = n * int(len(weight_vector[m]) / Neuron_matrix[m])
        r = (n + 1) * int(len(weight_vector[m]) / Neuron_matrix[m])
        print('Weight Vector:',weight_vector[m][k:r])
        print("\t"),
i += 1
print("")
print('---------------------------------------------------------------------------------------------------------------')
print('\n')
print("Output Layer" + ":")
print('\t'),

print("Neuron #1" + ":"),
print('Weight Vector:',weight_vector[i])
print('\n')
print('***************************************************************************************************************')

print("Training Error:"),
print(training_error)

print('\n')

print("Testing Error:"),
print(testing_error)