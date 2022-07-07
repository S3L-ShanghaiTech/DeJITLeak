function [P O time_bin_centers] = computeProbabilityTable(data, num_bins) 

observations = data(3:end,:);
%observations = filterObservations(observations);
[num_samples num_classes] = size(observations);

min_observation = min(min(observations));
max_observation = max(max(observations));
delta_time_bin = (max_observation - min_observation) / num_bins;
time_bin_centers = min_observation:delta_time_bin:max_observation;

for i = 1:num_classes
  obs = observations(:,i);
  inliers = abs(obs - mean(obs)) < 5 * std(obs);
  obs = obs(inliers);
  [f o] = hist(obs,time_bin_centers);
  p_o = f/sum(f);
  P(i,:) = p_o;
  O(i,:) = o;
end