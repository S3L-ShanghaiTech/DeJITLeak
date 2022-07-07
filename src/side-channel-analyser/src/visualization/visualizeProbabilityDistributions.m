function visualizeProbabilityDistributions(P, O, input_probs, plotnum1 = 323, plotnum2 = 325)

probs = input_probs.*P';
probs = log2(probs(probs>0));
min_log_p = min(probs);

for i = 1:size(P,1)
  subplot(plotnum1);
  plot([O(i,1),O(i,:), O(i,end)], [0, P(i,:), 0], 'color', colorfun(i));
  xlabel('Observation');
  ylabel('Probability');
  title(['Uncorrected P(Observation | input = class ', num2str(i),')']);
  hold on;
  subplot(plotnum2)
  log2p = log2(  [0, input_probs(i)*P(i,:), 0 ] );
  log2p(log2p == -Inf) = min_log_p ;
  plot( [O(i,1), O(i,:), O(i,end)], log2p, 'color', colorfun(i));
  xlabel('Observation');
  ylabel('Log2 ( Probability )');
  title(['Corrected Log2( P(Observation | input = class ', num2str(i),'))']);
  hold on;
  % drawnow;
  %pause;
end

subplot(plotnum1);
title(['Uncorrected P(Observation | input)']);
subplot(plotnum2);
title(['Corrected Log2( P(Observation | input))'])