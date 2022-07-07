function visualizeRawData(data, plotnum = 321)

observations = data(3:end,:);
subplot(plotnum);
[num_samples num_classes] = size(observations);
for i = 1:num_classes
  plot3(i*ones(1,num_samples), 1:num_samples, (observations(:,i)), 'color', colorfun(i));
  hold on;
  view(90,0);
  %pause;
  drawnow;
end


title('Raw Observations');
zlabel('Observation');
ylabel('Sample Number');
