Assignment - 5
Maddi Jaya Padma Sri (jxm166230)
Lakshmi Priyanka Parimi(lxp160730)

Language Used - R 3.3.3
IDE - RStudio

Packages Used:

require('rpart')
require('caret')
require('class')
require('randomForest')
require('ROCR')
require('ISLR')
require('neuralnet')
require('stats')
require('adabag')
require('deepnet')
require('cvAUC')
require('gbm')
require('e1071')
require('deepnet')

Steps to execute:

1. Open the R file in RStudio. 
2. Install the packages required. This can be done by running the code with require('package name') statements. The required statements are written in the code.
3. Set path to the downloaded data file(haberman.csv) in read.csv.
4. Run the code.

File Information:

classifiers.R : This file has of all the classifers which are implemented on the haberman dataset.K-fold cross validation technique is used.Accuracy of classifier is calculated in each fold/iteration. The average of all these accuracies is displayed as the accuracy of the classifier.
  
