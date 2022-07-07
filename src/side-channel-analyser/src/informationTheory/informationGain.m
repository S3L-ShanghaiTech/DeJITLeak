function IG = informationGain(ptable)

n_obs = size(ptable,1);

IG = log2(n_obs) - mutualInformation(ptable);