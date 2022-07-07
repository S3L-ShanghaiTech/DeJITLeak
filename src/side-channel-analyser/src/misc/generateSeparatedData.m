load passwordData;

observations = data(3:end,:);

observations = observations + 0.2*[1,2,3,4,5,6,7,8,9,10];
data(3:end, :) = observations;

figure(2);
hold off;
visualizeRawData(data, 111);
save passwordData_separated data;
