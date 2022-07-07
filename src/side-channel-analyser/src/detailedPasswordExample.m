#! /usr/bin/octave -qf
addpath('./informationTheory');
addpath('./visualization');
addpath('./utils');
clear all;

visualize = true; 
animate = true;
stops = [1,5,25, 50, 85];
%stops = [];

experiment1 = load('../data/passwordData_length_1_separated');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment1.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [25, 50, 85];
experiment2 = load('../data/passwordData_length_1_overlap');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment2.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [20, 47];
experiment3 = load('../data/passwordData_length_3_separated');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment3.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [20, 47, 80];
experiment4 = load('../data/passwordData_length_3_overlap');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment4.data, visualize, animate, stops);
disp('Analysis complete. Press ENTER to quit.')
pause;

