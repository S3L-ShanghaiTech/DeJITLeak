#! /usr/bin/octave -qf
addpath('./informationTheory');
addpath('./visualization');
addpath('./utils');
clear all;

visualize = true; 
animate = true;
stops = [1,5,25, 50, 85];
%stops = [];

stops = [20, 47, 80];
experiment4 = load('../data/passwordData_length_3_overlap');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment4.data, visualize, animate, stops);
disp('Analysis complete. Press ENTER to quit.')
pause;

