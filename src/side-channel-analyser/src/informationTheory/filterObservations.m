function filtered = filterObservations(observations)

[num_samples num_classes] = size(observations);

for i = 1:num_classes
  obs = observations(:,i);
  inliers = abs(obs - mean(obs)) < 5 * std(obs);
  obs(!inliers) = mean(obs);
  filtered(:,i) = obs;
end