function [infogain expected_info_gain] = infoGain(P, class_probs)
P = class_probs' .* P;
P_cond = (P./ sum(P));


H_init = log2(size(P,1));


for i = 1:size(P,2) - 1
	H_class_obs(i) = entropy(P_cond(:,i)');
end

infogain = H_init - H_class_obs;
infogain( isnan(infogain) ) = 0;

expected_info_gain = sum(P(:,1:end-1))*infogain';

