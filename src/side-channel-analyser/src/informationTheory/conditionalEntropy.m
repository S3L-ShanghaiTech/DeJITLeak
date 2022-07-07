function HXY = conditionalEntropy(ptable, dim = 1)

if(dim == 2) ptable = ptable'; end

pX = sum(ptable,1);

t = (ptable.*log2(pX./ptable))(:);

HXY = sum(t( !isnan(t) & !isinf(t) ));

