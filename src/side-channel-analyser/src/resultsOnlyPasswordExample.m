#! /usr/bin/octave -qf
addpath('./informationTheory');
addpath('./visualization');
addpath('./utils');
clear all;

visualize = false; 
animate = false;
stops = [];

experiment1 = load('../data/passwordData_length_1_separated');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment1.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [];
experiment2 = load('../data/passwordData_length_1_overlap');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment2.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [];
experiment3 = load('../data/passwordData_length_3_separated');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment3.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;

stops = [];
experiment4 = load('../data/passwordData_length_3_overlap');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment4.data, visualize, animate, stops);
disp('Analysis complete. Press ENTER to quit.')
pause;

