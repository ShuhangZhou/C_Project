Data=dlmread('spoon.txt','\t'); 

VData = Data(:,1); 
%plot(VData,Time) 
%pause 
SigFFT = fft(VData); 
norfft = abs(SigFFT)/abs(SigFFT(2));


Data1=dlmread('spoon3.txt','\t');
VData1 = Data1(:,1);
SigFFT1 = fft(VData1);
norfft1 = abs(SigFFT1)/abs(SigFFT1(2));

subplot(1, 2, 1);
plot(norfft,'r')
xlabel('frequency bins');
ylabel('Magnitude');
subplot(1, 2, 2);
plot(norfft1)
xlabel('frequency bins');
ylabel('Magnitude');
n = 100;
x = 0;
for i = 2:n
    x = x + power(abs(norfft(i)) - abs(norfft1(i)),2);
end

x = x/n;
x

