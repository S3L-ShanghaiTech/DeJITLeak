function visualizeTimingData(data, lengths)

% ===============================
% Need to correct probabilities according to 
% PC probability

% ===============================

[num_samples len] = size(data);

h1 = figure(1);

subplot(2,2,1);
plot(data);
%colormap('summer');
%waterfall(data');
%cmap = zeros(1,3);
%view(0,0);
title('Raw Times');
ylabel('Running Time (s)');
xlabel('Sample Number');
xlim([1 num_samples]);

subplot(2,2,2);
plot(lengths, mean(data), 'k');
title('Mean Time');
ylabel('Running Time (s)');
xlabel('Password Length');


subplot(2,2,3);
plot(lengths, std(data), 'k');
title('Standard Deviation');
ylabel('Running Time (s)');
xlabel('Password Length');




h2 = figure(2);
clrs = 'rbgmcky';
hold off;
for i = 1:len

  %subplot(floor(len/2),1, floor(i/2) + 1);
  %hist(data(:,i), 50, 'w' , 'edgecolor', clrs( mod(i-1, length(clrs)) + 1 ));
  [f t] = hist(data(:,i), 10);
  p_t = f / sum(f);
  %plot([t(1),t, t(end)], [0, p_t, 0], clrs( mod(i-1, length(clrs)) + 1 ));
  plot([t(1),t, t(end)], [0, p_t, 0], 'color', [mod(i,5)/4, 0.4, mod(i,7)/6]);
  %disp(i)  
  hold on;

  % [f t] = hist(data(:,i+1), 50);
  % p_t = f / sum(f);
  % plot([t(1),t, t(end)], [0, p_t, 0], 'r')
  %xlim( [min(data(:)) max(data(:))]  );
  % hold off;
  %drawnow;
  % pause;
end

hold off;

h2 = figure(3);
clrs = 'rbgmcky';
hold off;
hist_size = 50;
for i = 1:len-1

  [f t] = hist(data(:,i), hist_size);
  p_t = f / sum(f);
  plot([t(1),t, t(end)], [0, p_t, 0], 'r'); %color', [mod(i,5)/4, 0.4, mod(i,7)/6]);
  hold on;

  [f t] = hist(data(:,i+1), hist_size);
  p_t = f / sum(f);
  plot([t(1),t, t(end)], [0, p_t, 0], 'b'); %color', [mod(i+1,5)/4, 0.4, mod(i,7)/6]);
  title(['Prefix ', num2str(i), ' vs ', num2str(i+1)])

  hold off;
  drawnow;
  pause(0.01);

  d1 = data(:,i);
  d2 = data(:,i+1);

  t_max = max([d1;d2]);
  t_min = min([d1;d2]);

  hist_size = 50;
  bin_delta = (t_max - t_min) / hist_size;
  bin_centers = [t_min:bin_delta:t_max];

  [f1 t1] = hist(d1, bin_centers);
  [f2 t2] = hist(d2, bin_centers);

  pdf1 = f1 / sum(f1);
  pdf2 = f2 / sum(f2);

  plot(t1, pdf1, t2, pdf2);

  ptable = [pdf1; pdf2];

  plot(bin_centers,ptable', 'LineWidth', 2);

  MI(i) = mutualInformation(ptable);
  IG(i) = informationGain(ptable);
  CE(i) = conditionalEntropy(ptable);
  drawnow;
  pause(0.01);
  

end

hold off;

disp(MI)

figure(4);
hold off;
plot(CE, 'b');
