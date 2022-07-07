function visalizeConditionalProbabilities(data, P, input_classes, input_probs, num_samples, time_bin_centers, info_gain, expected_info_gain, stops = [], plotnum1 = 324, plotnum2 = 322, plotnum3 = 325, plotnum4 = 323, plotnum5 = 321)

probs = input_probs.*P';
probs = log2(probs(probs>0));
min_log_p = min(probs);

max_p = max(max(P));

min_input_class = min(input_classes);
max_input_class = max(input_classes);

firstRawplot = true;

for i = 1:length(P)-1
  p_i = P(:,i);

  p_i = input_probs' .* p_i;
  p_i = p_i / sum(p_i);

  subplot(plotnum1);
  %plot(input_classes, p_i, 'color', colorfun2(i)); 
  bar(input_classes, p_i, 'facecolor', colorfun2(i));
  % disp('input_classes');
  % disp(input_classes);
  % disp('p_i');
  % disp(p_i');
  % disp('-------------');
  fflush(stdout);
  % pause;
  ylim([0 1]);
  title(['P(input class | obs = ', num2str((time_bin_centers(i))), ')']);
  hold off;


  subplot(plotnum2);
  plot([min_input_class-1, max_input_class+1],[time_bin_centers(i),time_bin_centers(i)], 'k');

  subplot(plotnum5);
  hold on;
  if(firstRawplot)
    visualizeRawData(data);
    firstRawplot = false;
  end
  plot3([0,0], [0, num_samples], [time_bin_centers(i),time_bin_centers(i)], 'k');
  hold off;

  subplot(plotnum3);
  plot([time_bin_centers(i),time_bin_centers(i)], [0,min_log_p], 'k');
  subplot(plotnum4);
  plot([time_bin_centers(i),time_bin_centers(i)], [0,max_p], 'k');
  drawnow;

  if(ismember(i, stops))
    disp('Animation paused. Press ENTER to continue.')
    pause;
  end

  subplot(plotnum3);
  plot([time_bin_centers(i),time_bin_centers(i)], [0,min_log_p], 'w');
  subplot(plotnum5);
  hold on;
  plot3([0,0], [0, num_samples], [time_bin_centers(i),time_bin_centers(i)], 'w');
  hold off;
  subplot(plotnum4);
  plot([time_bin_centers(i),time_bin_centers(i)], [0,max_p], 'w');
  subplot(plotnum2);
  plot([min_input_class-1, max_input_class+1],[time_bin_centers(i),time_bin_centers(i)], 'w');

  visualizeInfoGainPrefix(time_bin_centers, info_gain, expected_info_gain, i);


end

% for i = 1:length(P)-1
%   p_i = P(:,i);

%   p_i = input_probs' .* p_i;
%   p_i = p_i / sum(p_i);

%   subplot(plotnum1);
%   plot(p_i, 'color', colorfun2(i)); 
%   ylim([0 1]);
%   title(['P(input class | obs = ', num2str((time_bin_centers(i))), ')']);
%   hold on;

% end


% P = input_probs' .* P;
% P_cond = (P./ sum(P))';

% subplot(3,2,4);
% for i = 1:length(time_bin_centers)-1
%   p_i = P(:,i);
%   plot(p_i, 'color', [mod(i,5)/4, 0.6, mod(i+1,7)/6]);
%   hold on;
% end


drawnow;

