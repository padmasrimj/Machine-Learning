require('rpart');
require('caret');
require('class');
require('randomForest')
require('ROCR')
require('ISLR')
require('stats');
library('stats');
library('class');
library('adabag');
library('rpart');
require('adabag');
require('deepnet');
library('randomForest')
library('ROCR')
library('ISLR')
library('caret')
library('cvAUC')
library('gbm')
library(rpart)
library(caret)
library(neuralnet)
library(e1071)
library(deepnet)

# Set number of folds for cross validation

cross_validation_folds = 10;

#Load the data

dataSet = read.csv('C:/Users/Padma Sri/Desktop/haberman1.csv', header = TRUE, na.strings = c(""));


#Processing data to remove redundant values

dataSet = unique(dataSet);

#scaling the data

maxs = apply(dataSet, MARGIN=2, max);
mins = apply(dataSet, MARGIN=2, min);
dataSet = as.data.frame(scale(dataSet,center = mins,scale = maxs-mins));

# Creating a list for k fold Cross Validation

data_id = sample(1:cross_validation_folds, nrow(dataSet), replace = TRUE);
list = 1:cross_validation_folds;

#Setting the accuracy of classifier's to 0 initially

DecisionTree_Accuracy = 0;
Perceptron_Accuracy = 0;
NeuralNet_Accuracy = 0;
DeepLearning_Accuracy = 0;
SVM_Accuracy=0;
NaiveBayes_Accuracy=0;
Logistic_accuracy = 0;
Bagging_accuracy = 0;
AdaBoost_Accuracy = 0;
RandomForest_Accuracy = 0;
KNearestNeighbor_Accuracy = 0;
GradientBoosting_Accuracy = 0;

#Initializing Precision to 0 for all Classifiers

precision_LR=0;
prec_DT = 0;
prec_PCP = 0;
prec_NN = 0;
prec_DL = 0;
prec_SVM = 0;
prec_NB = 0;
prec_LR = 0;
prec_KNN = 0;
prec_BG = 0;
prec_RF = 0;
prec_AB = 0;
prec_GB = 0;

# Running the for loop for k cross_validation_folds times for each classifier

for(i  in 1:cross_validation_folds){
  
  trainData  = subset(dataSet, data_id %in% list[-i]);
  testData = subset(dataSet, data_id %in% c(i));
  
  
  #decision tree
  
  DecisionTree = rpart(class~.,trainData, method = "class",control=rpart.control(maxdepth=30),parms=list(split="information"))
  predDTM = predict(DecisionTree,testData,type="class")
  table_DT=table(testData[,4],predDTM)
  DecisionTree_Accuracy = DecisionTree_Accuracy + (sum(diag(table_DT)) / sum(table_DT))
  prec_DT = prec_DT + mean(100*diag(table_DT)/colSums(table_DT))
  
  #Perceptron
  
  Perceptron1 = neuralnet(class~age+year+nodesPositive, trainData, hidden=0, threshold=0.5,lifesign = 'minimal', err.fct="sse");
  predNN = neuralnet::compute(Perceptron1, testData[,1:3]);
  val1 = mean(predNN$net.result);
  predNNLevel = ifelse(predNN$net.result>val1,1,0);
  table_PCP = table(predNNLevel, testData$class);
  Perceptron_Accuracy  = Perceptron_Accuracy + sum(diag(table_PCP))/sum(table_PCP);
  prec_PCP = prec_PCP + mean(100*diag(table_PCP)/colSums(table_PCP))
  
  
  #neuralNet 
  
  neuralNet = neuralnet(class~age+year+nodesPositive, trainData, hidden=c(c(4),c(3)),  rep=5, threshold=0.1, learningrate = 0.2, act.fct = "logistic" ,lifesign = 'minimal');
  predNN1 = neuralnet::compute(neuralNet, testData[,1:3]);
  val = mean(predNN1$net.result);
  predNNLevel1 = ifelse(predNN1$net.result>val,1,0);
  table_NN = table(predNNLevel1, testData$class);
  NeuralNet_Accuracy = NeuralNet_Accuracy +sum(diag(table_NN))/sum(table_NN);
  prec_NN = prec_NN + mean(100*diag(table_NN)/colSums(table_NN))
  
  #Deep Learning
  deepNet = neuralnet(class~age+year+nodesPositive, trainData, hidden=c(5,8,12,9,15),  rep=5, threshold=0.1, learningrate = 0.2, act.fct = "logistic" ,lifesign = 'minimal');
  pred_dn = neuralnet::compute(deepNet, testData[,1:3]);
  val = mean(pred_dn$net.result);
  predDn = ifelse(pred_dn$net.result>val,1,0);
  table_DN = table(predDn, testData$class);
  DeepLearning_Accuracy = DeepLearning_Accuracy +sum(diag(table_DN))/sum(table_DN);
  prec_DL = prec_DL + mean(100*diag(table_DN)/colSums(table_DN))
  
  #svm
  attach(trainData);
  x = subset(trainData, select = -class);
  y = class;
  svm1 = svm(x,y,data= trainData, cost=100, gamma=0.5, kernel='linear' , type="C-classification");
  detach(trainData);
  attach(testData);
  #prediction
  x = subset(testData, select=-class);
  y =class
  pred_svm = predict(svm1, x);
  table_SVM=table(pred_svm,testData$class);
  SVM_Accuracy = SVM_Accuracy +sum(diag(table_SVM))/sum(table_SVM);
  prec_SVM = prec_SVM + mean(100*diag(table_SVM)/colSums(table_SVM))
  detach(testData);
  
  
  #Naive Bayes
  
  naive_Bayes = naiveBayes(as.factor(class) ~ age+year+nodesPositive, dataSet , type="raw",laplace=1);
  predict_naive = predict(naive_Bayes, testData);
  table_NB=table(predict_naive,testData$class)
  NaiveBayes_Accuracy=NaiveBayes_Accuracy +sum(diag(table_NB))/sum(table_NB);
  prec_NB = prec_NB + mean(100*diag(table_NB)/colSums(table_NB))
  
  #Logistic regression
  
  logistic = glm(class~., trainData, family = binomial, method = "glm.fit");
  predict_LR = predict(logistic, testData ,type='response');
  table_LR = table(testData$class, predict_LR>0.4);
  Logistic_accuracy = Logistic_accuracy + sum(diag(table_LR))/sum(table_LR);
  prec_LR = prec_LR + mean(100*diag(table_LR)/colSums(table_LR))
  
  
  #Bagging
  trainData$class = as.factor(trainData$class);
  Bagging_model = bagging(class~ age+year+nodesPositive, trainData, mfinal= 3, boos = TRUE,rpart.control(maxdepth=7));
  predict_Bagging = predict.bagging(Bagging_model, testData);
  j = predict_Bagging$confusion;
  Bagging_accuracy = Bagging_accuracy+sum(diag(j))/sum(j);
  prec_BG = prec_BG + mean(100*diag(j)/colSums(j))
  
  
  #	AdaBoost
  Adaboost_model = boosting(class~., trainData, mfinal = 5, boos= FALSE,control=rpart.control(minsplit=3,maxdepth = 10))
  predict_AdaBoost = predict.boosting(Adaboost_model, testData)
  m = predict_AdaBoost$confusion;
  AdaBoost_Accuracy = AdaBoost_Accuracy + sum(diag(m))/sum(m);
  prec_AB = prec_AB + mean(100*diag(j)/colSums(j))
  
  
  #Random Forest
  
  random_Forest_Model = randomForest(class~., trainData, importance = TRUE, ntree=50,maxnodes=20);
  predict_RandomForest = predict(random_Forest_Model, testData);
  RandomForest_table = table(predict_RandomForest, testData$class);
  RandomForest_Accuracy = RandomForest_Accuracy + sum(diag(RandomForest_table))/sum(RandomForest_table);
  prec_RF = prec_RF + mean(100*diag(RandomForest_table)/colSums(RandomForest_table))
  
  
  # KNN
  
  attach(trainData)
  knnfit=knn(trainData,testData,cl=factor(trainData$class),k=10)
  predict_KNN = predict(knnFit,newdata = testData );
  testConfusion = confusionMatrix(predict_KNN, testData$class)
  KNearestNeighbor_Accuracy = KNearestNeighbor_Accuracy + testConfusion$overall['Accuracy']
  table_KNN = testConfusion$table 
  prec_KNN = prec_KNN + mean(100*diag(table_KNN)/colSums(table_KNN))
  
  #Gradient Boosting
  
  testData= na.omit(testData)
  trainData=na.omit(trainData)
  model=xgboost( data = as.matrix(trainData[,1:3]), label=trainData$class, max.depth=3,nrounds = 2)
  pred=predict(model,as.matrix(testData[,1:3]))
  pred=ifelse(pred>1,1,0)
  table_GB = table(pred, testData$class);
  GradientBoosting_Accuracy = GradientBoosting_Accuracy +sum(diag(table_GB))/sum(table_GB);
  prec_GB = prec_GB + mean(100*diag(table_GB)/colSums(table_GB))
  
}
cat("\n")
cat("\n")
cat("*******************Accuracy Summary**************************************")
cat('\nAccuracy of Decision Tree :',100*DecisionTree_Accuracy/cross_validation_folds);
cat('\nAccuracy of Perceptron Testing :',100*Perceptron_Accuracy/cross_validation_folds);
cat('\nAccuracy of Neural Net :',100*NeuralNet_Accuracy/cross_validation_folds);
cat('\nAccuracy of Deep Learning :',100*DeepLearning_Accuracy/cross_validation_folds);
cat('\nAccuracy of SVM :',100*SVM_Accuracy/cross_validation_folds);
cat('\nAccuracy of Naive bayes :',100*NaiveBayes_Accuracy/cross_validation_folds);
cat('\nAccuracy of Logistic Regression :',100*Logistic_accuracy/cross_validation_folds);
cat('\nAccuracy of KNN :',100*KNearestNeighbor_Accuracy/cross_validation_folds);
cat('\nAccuracy of Bagging :',100*Bagging_accuracy/cross_validation_folds);
cat('\nAccuracy of Random FOrest :',100*RandomForest_Accuracy/cross_validation_folds);
cat('\nAccuracy of Adaboost :',100*AdaBoost_Accuracy/cross_validation_folds);
cat('\nAccuracy of Gradient Boosting :',100*GradientBoosting_Accuracy/cross_validation_folds);
cat("\n")
cat("\n")
cat("*************************Precision Summary****************************")
cat('\nPrecision of Decision Tree :',prec_DT/cross_validation_folds)
cat('\nPrecision of Perceptron :',prec_PCP/cross_validation_folds)
cat('\nPrecision of Neural Net :',prec_NN/cross_validation_folds)
cat('\nPrecision of Deep Learning :',prec_DL/cross_validation_folds)
cat('\nPrecision of SVM :',prec_SVM/cross_validation_folds)
cat('\nPrecision of Naive Bayes :',prec_NB/cross_validation_folds)
cat('\nPrecision of Logistic Regression :',prec_LR/cross_validation_folds)
cat('\nPrecision of KNN :',prec_KNN/cross_validation_folds)
cat('\nPrecision of Bagging :',prec_BG/cross_validation_folds)
cat('\nPrecision of Random Forest :',prec_RF/cross_validation_folds)
cat('\nPrecision of Adaboost :',prec_AB/cross_validation_folds)
cat('\nPrecision of Gradient Boosting :',prec_GB/cross_validation_folds)