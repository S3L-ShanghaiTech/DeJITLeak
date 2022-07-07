load passwordData2;
i = 1;

d1 = data(:,i);
d2 = data(:,i+1);

t_max = max([t1,t2]);
t_min = min([t1,t2]);

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

MI = mutualInformation(ptable)
IG = informationGain(ptable)
