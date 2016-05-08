# After a significant amount of reading and attempted fixes; I cannot understand why the statistics returned to me by
# SIPp nearly have a 100% call failure rate. This is likely due to the fact that I have designed the scenario incorrectly
# however I have no idea what I am doing wrong. The tests showed the correct messages being passed with minimal "dropped"
# or incorrect messages despite a heavy call rate.
# 
# Unfortunately, due to other course and project commitments, I cannot justify continued work on this.
#
# Greg Linklater, g12l4025

registration:
	# sipp [OPTIONS] destination_ip[:destination_port | 5060 (default)]
	# -sf <scenario file>
	# -inf <user db.csv> each line in format UserX;[authentication username=UserX password=PasswordX]
	# ^ First line of user DB file should be ( RANDOM | SEQUENTIAL ); this tells SIPp the method of choosing users.
	# -p <port> that the local service should run on
	# -r <cps> the initial call rate
	# -trace_stat output statistics from the benchmark to file.
	# -m <total calls> Execute a specific number of calls.
	sipp -sf scenarios/register_uac.xml -inf userdb.csv -p 5067 -r 5000 -trace_stat -m 200000 127.0.0.1

callflow_uac:
	# Parameters as above
	sipp -sf scenarios/callflow_uac.xml -p 5067 -r 50 -trace_stat -m 200000 127.0.0.1

callhandler:
	# Parameters as above
	sipp -sf scenarios/callflow_callhandler.xml -p 34567 127.0.0.1
