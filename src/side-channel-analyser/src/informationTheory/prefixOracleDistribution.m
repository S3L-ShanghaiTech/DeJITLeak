function p_dist = prefixOracleDistribution(len, alpha_size)

p_dist = (1/alpha_size).^(0:len-2)*( (alpha_size - 1) / (alpha_size));
p_dist = [p_dist, 1-sum(p_dist)];