function h = entropy(y)

y = y(y!=0);
p = y./sum(y);
h = -p * log2(p');


