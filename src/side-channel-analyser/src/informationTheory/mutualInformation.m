function I = mutualInformation(ptable)

I = entropy(sum(ptable)) - conditionalEntropy(ptable,1);