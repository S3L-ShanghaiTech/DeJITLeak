function [info_gain expected_info_gain time_bin_centers] = analyzeNoisySideChannel(data, visualize = false, animate = false, stops = [])
warning off;

if(visualize)
  disp('Analyzing Data. Press ENTER to start.');
  pause;
end

data(3:end,:) = filterObservations(data(3:end,:));


class_probs = data(2,:);
num_bins = 20;

input_classes = data(1,:);
observations = data(3:end,:);


[num_samples num_classes] = size(observations);

[P O time_bin_centers] = computeProbabilityTable(data, num_bins);


[info_gain expected_info_gain] = infoGain(P, class_probs);


if(visualize)
  figure(10);
  clf;
  visualizeRawData(data);
  visualizeGroupDistributions(data);
  disp('Visualization paused. Press ENTER to continue.')
  pause;
  visualizeProbabilityDistributions(P, O, class_probs);
  disp('Visualization paused. Press ENTER to continue.')
  pause;
  
  if(animate)
  	visalizeConditionalProbabilities(data, P, input_classes, class_probs, num_samples, time_bin_centers, info_gain, expected_info_gain, stops);
  end
  visualizeInfoGain(time_bin_centers, info_gain, expected_info_gain);
% else
%   visualizeInfoGain(time_bin_centers, info_gain, expected_info_gain, 111);
end