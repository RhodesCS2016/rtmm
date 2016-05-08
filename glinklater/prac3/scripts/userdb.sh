#/bin/bash

outfile_csv="userdb.csv"
outfile_db="userdb"
if [ $1 ]; then
  outfile_csv=`printf "%s.csv" "$1"`
  outfile_db="$1"
fi

rm $outfile_csv
rm $outfile_db

for ((i=0;i<1000;i++)); do
  user="User$i"
  password=`head -c 16 /dev/urandom | md5sum - | head -c 12`
  printf "%s;%s\n" "$user" "$password" >> $outfile_db
  printf "%s;[authentication username=%s password=%s]\n" "$user" "$user" "$password" >> $outfile_csv
done
