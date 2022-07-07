#! /usr/bin/octave -qf
addpath('./informationTheory');
addpath('./visualization');
addpath('./utils');
clear all;

visualize = true; 
animate = false;
% stops = [1,5,25, 50, 85];
stops = [];

experiment1 = load('../data/bidpal2data');
[infogain avg_infogain observables] = analyzeNoisySideChannel(experiment1.data, visualize, animate, stops);
disp('Paused. Press ENTER to analyze next experiment.')
pause;
