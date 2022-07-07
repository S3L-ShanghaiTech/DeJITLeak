function visualizeGroupDistributions(data, plotnum = 322)

input_classes = data(1,:);
observations = data(3:end,:);
subplot(plotnum);
[num_samples num_classes] = size(observations);
min_input_class = min(input_classes);
max_input_class = max(input_classes);

hold on;
subplot(plotnum);
for i = 1:num_classes
  obs = observations(:,i);
  inliers = abs(obs - mean(obs)) < 3 * std(obs);
  obs = obs(inliers);
  num_obs = length(obs);
  input_class_vector_i = input_classes(i)*ones(1,num_obs);
  plot(input_class_vector_i, obs, 'marker', '.', 'color', colorfun(i));
  hold on;
end

plot(input_classes, mean(observations), 'k');
xlim([min_input_class-1, max_input_class+1])
title('Observation Distributions by Input Class');
ylabel('Observation');
xlabel('Input Class');
