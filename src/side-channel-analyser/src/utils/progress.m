function progress(p, msg ='')
printf(['  ', msg, ' %6i percent complete.\r'], floor(100*p));
if(p==1)
  printf(['  ', msg, ' %6i percent complete.\n'], floor(100*p));
end
fflush(stdout);