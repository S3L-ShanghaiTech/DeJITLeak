function visualizeInfoGainPrefix(time_bin_centers, info_gain, expected_info_gain, prefix, plotnum = 326)

subplot(plotnum);

plot(time_bin_centers(1:end-1), info_gain, 'b:');
hold on;
plot(time_bin_centers(1:prefix), info_gain(1:prefix), 'k-');
plot(time_bin_centers(prefix), info_gain(prefix), 'ko');
info_diff = max(info_gain) - min(info_gain);
if(info_diff>0.001)
	ylim([min(info_gain)-0.1*info_diff, max(info_gain)+0.1*info_diff]);
end
xlabel('Observation');
ylabel('Information gain (bits)');
title(['Information gain vs. observation.  ', 'Expected IG(class|obs) = ', num2str(expected_info_gain)])
hold off;